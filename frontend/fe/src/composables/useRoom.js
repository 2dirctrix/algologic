/**
 * 방(Room) 관련 API 관리 컴포저블
 * 방 리스트 조회, 필터링, 페이지네이션 기능 제공
 */

import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { apiClient } from '@/api/utils/index.js';
import { API_ENDPOINTS } from '@/api/constants/endpoints.js';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';

export const useRoom = () => {
  const router = useRouter();
  const rooms = ref([]);
  const isLoading = ref(false);
  const totalPages = ref(0);
  const currentPage = ref(0);

  // 페이지별 데이터 캐시
  const pageCache = ref(new Map());
  const cacheKey = ref('');

  // 필터링 및 검색 조건
  const filters = reactive({
    roomName: '',
    hostNickname: '',
    programmingLanguage: null,
    gameType: null,
    offset: 0, // 이제 페이지 번호 (0부터 시작)
    size: 8,
  });

  /**
   * 캐시 키 생성 (필터 조건을 기반으로)
   */
  const generateCacheKey = (params = {}) => {
    const queryParams = { ...filters, ...params };
    const keyParams = { ...queryParams };
    delete keyParams.offset; // offset은 캐시 키에서 제외
    return JSON.stringify(keyParams);
  };

  /**
   * 방 리스트 조회
   * @param {Object} params - 검색 및 필터링 파라미터
   * @param {boolean} forceRefresh - 강제 새로고침 여부
   * @returns {Promise<Object>} 방 리스트 응답 데이터
   */
  const fetchRoomList = async (params = {}, forceRefresh = false) => {
    try {
      isLoading.value = true;

      // 필터 값과 전달받은 파라미터를 합쳐서 쿼리 파라미터 생성
      const queryParams = {
        ...filters,
        ...params,
      };

      const newCacheKey = generateCacheKey(params);
      const pageNumber = queryParams.offset; // offset은 이제 페이지 번호

      // 캐시 확인 (새로고침이 아닌 경우)
      if (!forceRefresh && cacheKey.value === newCacheKey && pageCache.value.has(pageNumber)) {
        const cachedData = pageCache.value.get(pageNumber);
        rooms.value = cachedData.roomList;
        totalPages.value = cachedData.totalPages;
        currentPage.value = pageNumber;
        return cachedData;
      }

      // 빈 값 제거 (null은 필터 초기화 의도이므로 유지)
      const cleanParams = Object.entries(queryParams).reduce((acc, [key, value]) => {
        if (value !== undefined && value !== '') {
          acc[key] = value;
        }
        return acc;
      }, {});

      const response = await apiClient.get(API_ENDPOINTS.ROOM.LIST, {
        params: cleanParams,
      });

      const data = response.data.data;
      const roomList = data.roomList || [];
      const totalCount = data.totalCount || 0;
      const calculatedTotalPages = Math.ceil(totalCount / filters.size);

      // 응답 데이터 설정
      rooms.value = roomList;
      totalPages.value = calculatedTotalPages;
      currentPage.value = pageNumber;

      // 캐시 업데이트
      cacheKey.value = newCacheKey;
      pageCache.value.set(pageNumber, {
        roomList,
        totalCount,
        totalPages: calculatedTotalPages,
      });

      return { roomList, totalCount, totalPages: calculatedTotalPages };
    } catch (error) {
      rooms.value = [];
      totalPages.value = 0;
      currentPage.value = 0;
      return { roomList: [], totalCount: 0, totalPages: 0 };
    } finally {
      isLoading.value = false;
    }
  };

  /**
   * 필터 조건 업데이트
   * @param {Object} newFilters - 새로운 필터 조건
   */
  const updateFilters = newFilters => {
    Object.assign(filters, newFilters);
    // 필터 변경 시 첫 페이지로 이동하고 캐시 초기화
    filters.offset = 0;
    pageCache.value.clear();
    cacheKey.value = '';
  };

  /**
   * 페이지 변경
   * @param {number} page - 이동할 페이지 (0부터 시작)
   */
  const changePage = async page => {
    if (page >= 0 && page < totalPages.value) {
      filters.offset = page; // offset을 페이지 번호로 설정
      await fetchRoomList();
    }
  };

  /**
   * 새로고침 (캐시 초기화 후 첫 페이지부터 다시 조회)
   */
  const refreshRoomList = async () => {
    pageCache.value.clear();
    cacheKey.value = '';
    filters.offset = 0; // 첫 페이지로 초기화
    await fetchRoomList({}, true); // 강제 새로고침
  };

  /**
   * 검색 실행
   * @param {Object} searchParams - 검색 조건
   */
  const searchRooms = async searchParams => {
    updateFilters(searchParams);
    await fetchRoomList();
  };

  /**
   * 새로운 방 생성 API POST 요청
   * 방 구독 및 join 시도
   */
  const createRoom = async roomData => {
    try {
      const response = await apiClient.post(API_ENDPOINTS.ROOM.CREATE, roomData);
      const roomId = response.data.data.roomId;

      if (response.data && response.data.status === 200) {
        await router.push({
          name: 'waitingRoom',
          query: { roomId },
        });
      }
    } catch (error) {
      throw new Error(error.message || '방 접속에 실패했습니다.');
    }
  };

  /**
   * 방 입장 요청
   * 200ok 이면 waitingRoom 라우팅
   * 400error 이면 status에 따라 정의되어있는 에러 처리
   * */
  const joinRoom = async roomId => {
    try {
      const response = await apiClient.post(API_ENDPOINTS.ROOM.JOIN(roomId));

      if (response.data && response.status === 200) {
        await router.push({
          name: 'waitingRoom',
          query: { roomId },
        });
      }
    } catch (error) {
      throw error;
    }
  };

  const reConnectRoom = async roomId => {
    const inGameStore = useInGameStore();

    try {
      const response = await apiClient.post(API_ENDPOINTS.ROOM.JOIN(roomId));

      if (response.data && response.data.status === 200) {
        await router.push({
          name: 'inGame',
          query: { roomId },
        });
      }
    } catch (error) {
      // 재접속 실패 시 모든 경우에 스토어 초기화
      inGameStore.reset();

      // HTTP 상태 코드별 에러 처리
      if (error.response) {
        const statusCode = error.response.status;
        const errorData = error.response.data;

        if (statusCode === 409) {
          return { success: false, error: 'ROOM_CONFLICT', message: errorData?.data || '방에 접속할 수 없습니다.' };
        } else if (statusCode === 500) {
          return { success: false, error: 'SERVER_ERROR', message: errorData?.data || '서버 오류가 발생했습니다.' };
        } else {
          return { success: false, error: 'UNKNOWN_ERROR', message: '방 재접속에 실패했습니다.' };
        }
      } else {
        return { success: false, error: 'NETWORK_ERROR', message: '네트워크 오류가 발생했습니다.' };
      }
    }

    // 성공 시
    return { success: true };
  };

  /**
   * 게임 시작 (게임 입장 API 호출 후 inGame 페이지로 라우팅)
   * @param {string} roomId - 방 ID
   * @returns {Promise<Object>} 게임 입장 응답 데이터
   */
  const startGame = async roomId => {
    try {
      const response = await apiClient.post(API_ENDPOINTS.ROOM.ENTER_GAME(roomId));

      if (response.data.status === 200) {
        return response.data;
      } else {
        throw new Error(response.data.message || '게임 시작에 실패했습니다.');
      }
    } catch (error) {
      throw new Error(error.message || '게임 시작에 실패했습니다.');
    }
  };

  return {
    rooms,
    isLoading,
    totalPages,
    currentPage,
    filters,
    fetchRoomList,
    updateFilters,
    changePage,
    refreshRoomList,
    searchRooms,
    createRoom,
    joinRoom,
    reConnectRoom,
    startGame,
  };
};
