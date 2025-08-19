/**
 * 인증 관리 컴포저블 (Pinia Store 래핑)
 * 기존 인터페이스 유지하면서 내부 구현을 스토어 기반으로 변경
 */

import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth/auth.js';
import { apiClient } from '@/api/utils/index.js';
import { API_ENDPOINTS } from '@/api/constants/endpoints.js';

export const useAuth = () => {
  const router = useRouter();
  const authStore = useAuthStore();

  /**
   * 카카오 인가코드로 로그인 시도
   * @param {string} authCode
   */
  const loginWithCode = async authCode => {
    try {
      authStore.setLoading(true);

      const response = await apiClient.post(API_ENDPOINTS.AUTH.KAKAO_LOGIN, {
        code: authCode,
      });

      const { data } = response.data;

      if (data.needSignup) {
        authStore.setNeedSignup(data.email);
      } else {
        authStore.login(data);
        // authStore.login();
        redirectToMain();
      }

      return data;
    } catch (error) {
      throw new Error('로그인에 실패했습니다.');
    } finally {
      authStore.setLoading(false);
    }
  };

  /**
   * 회원가입 처리
   * @param {Object} userInfo
   * @param {string} userInfo.nickname
   * @param {string} userInfo.language
   */
  const signupUser = async userInfo => {
    try {
      authStore.setLoading(true);

      const signupData = {
        email: authStore.userEmail,
        nickname: userInfo.nickname,
        programmingLanguage: userInfo.language,
      };

      const response = await apiClient.post(API_ENDPOINTS.AUTH.SIGNUP, signupData);

      authStore.login(response.data.data);

      redirectToMain();

      return response.data;
    } catch (error) {
      console.error('회원가입 실패: ', error);
      throw new Error('회원가입에 실패했습니다.');
    } finally {
      authStore.setLoading(false);
    }
  };

  /**
   * 로그아웃 처리
   */
  const logout = async () => {
    try {
      authStore.setLoading(true);

      // 서버에 로그아웃 요청하여 httpOnly 쿠키 만료
      await apiClient.post(API_ENDPOINTS.AUTH.LOGOUT);

      // 클라이언트 상태 초기화
      authStore.logout();

      // 로그인 페이지로 이동
      router.push('/auth');
    } catch (error) {
      console.error('로그아웃 실패: ', error);
      // 서버 요청 실패해도 클라이언트 상태는 초기화
      authStore.logout();
      router.push('/auth');
    } finally {
      authStore.setLoading(false);
    }
  };

  /**
   * 사용자 정보 조회
   * */
  const fetchUserInfo = async () => {
    try {
      authStore.setLoading(true);

      const response = await apiClient.get(API_ENDPOINTS.USER.INFO);
      const userData = response.data.data;

      authStore.setUserData(userData);

      return userData;
    } catch (error) {
      console.error('사용자 정보 조회 실패: ', error);
      throw new Error('사용자 정보를 불러올 수 없습니다.');
    } finally {
      authStore.setLoading(false);
    }
  };

  /**
   * 사용자 상세 정보 조회
   * */
  const fetchUserDetail = async () => {
    try {
      authStore.setLoading(true);

      const response = await apiClient.get(API_ENDPOINTS.USER.DETAIL);
      const detailData = response.data.data;

      authStore.setUserDetail(detailData);

      return detailData;
    } catch (error) {
      console.error('사용자 상세 정보 조회 실패: ', error);
      throw new Error('사용자 상세 정보를 불러올 수 없습니다.');
    } finally {
      authStore.setLoading(false);
    }
  };

  /**
   * 사용자 정보 수정 (통합)
   * @param {Object} updateData - 수정할 데이터
   * @param {string} [updateData.nickname] - 닉네임 (선택)
   * @param {string} [updateData.programmingLanguage] - 주언어 (선택)
   * @param {File} [updateData.profileImage] - 프로필 이미지 파일 (선택)
   */
  const updateUserInfo = async updateData => {
    try {
      authStore.setLoading(true);

      // 현재 사용자 정보와 비교하여 변경된 데이터만 추출
      const currentUser = authStore.user;
      const requestData = {};
      let hasChanges = false;

      // 닉네임 변경 확인
      if (updateData.nickname !== undefined && updateData.nickname !== currentUser?.nickname) {
        requestData.nickname = updateData.nickname;
        hasChanges = true;
      }

      // 주언어 변경 확인
      if (updateData.programmingLanguage !== undefined && updateData.programmingLanguage !== currentUser?.programmingLanguage) {
        requestData.programmingLanguage = updateData.programmingLanguage;
        hasChanges = true;
      }

      // 프로필 이미지가 있는지 확인
      const hasProfileImage = updateData.profileImage instanceof File;

      // 변경사항이 없고 프로필 이미지도 없으면 요청하지 않음
      if (!hasChanges && !hasProfileImage) {
        return currentUser;
      }

      const formData = new FormData();

      // 프로필 이미지가 있으면 추가
      if (hasProfileImage) {
        formData.append('profileImage', updateData.profileImage);
      }

      // request 데이터 추가 (변경사항이 있거나 프로필 이미지만 업데이트하는 경우)
      formData.append('request', new Blob([JSON.stringify(requestData)], { type: 'application/json' }));

      const response = await apiClient.put(API_ENDPOINTS.USER.UPDATE, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });

      // 성공 시 사용자 정보 및 상세 정보 재조회
      await fetchUserInfo();
      await fetchUserDetail();

      return response.data.data;
    } catch (error) {
      console.error('사용자 정보 수정 실패: ', error);
      throw new Error('사용자 정보 수정에 실패했습니다.');
    } finally {
      authStore.setLoading(false);
    }
  };

  /**
   * 메인 페이지로 리다이렉트
   */
  const redirectToMain = () => {
    setTimeout(() => {
      router.replace('/main');
    }, 500);
  };

  /**
   * 알고리즘 통계 조회
   */
  const fetchAlgorithmStats = async () => {
    try {
      authStore.setLoading(true);

      const response = await apiClient.get(API_ENDPOINTS.USER.ALGORITHM_STATS);
      const statsData = response.data.data;

      return statsData;
    } catch (error) {
      console.error('알고리즘 통계 조회 실패: ', error);
      throw new Error('알고리즘 통계를 불러올 수 없습니다.');
    } finally {
      authStore.setLoading(false);
    }
  };

  /**
   * 일일 게임 통계 조회
   */
  const fetchDailyGames = async () => {
    try {
      authStore.setLoading(true);

      const response = await apiClient.get(API_ENDPOINTS.USER.DAILY_GAMES);
      const dailyData = response.data.data;

      return dailyData;
    } catch (error) {
      console.error('일일 게임 통계 조회 실패: ', error);
      throw new Error('일일 게임 통계를 불러올 수 없습니다.');
    } finally {
      authStore.setLoading(false);
    }
  };

  /**
   * 게임 결과 목록 조회
   */
  const fetchGameResults = async () => {
    try {
      authStore.setLoading(true);

      const response = await apiClient.get(API_ENDPOINTS.USER.GAME_RESULTS);
      const { data } = response.data;

      return data;
    } catch (error) {
      console.error('게임 결과 조회 실패:', error);
      throw new Error('게임 결과를 불러올 수 없습니다.');
    } finally {
      authStore.setLoading(false);
    }
  };

  /**
   * 게임 결과 상세 조회 (기존 API 활용)
   */
  const fetchGameResultDetail = async (gameResultId) => {
    try {
      const response = await apiClient.get(API_ENDPOINTS.BATTLE.GAME_RESULT_PREFIX(gameResultId));
      const { data } = response.data;

      return data;
    } catch (error) {
      console.error('게임 결과 상세 조회 실패:', error);
      throw new Error('게임 결과 상세 정보를 불러올 수 없습니다.');
    }
  };

  /**
   * 회원가입 모달 닫기
   */
  const closeSignupModal = () => {
    authStore.closeSignupModal();
  };

  return {
    user: computed(() => authStore.user),
    isAuthenticated: computed(() => authStore.isAuthenticated),
    isLoggedIn: computed(() => authStore.isLoggedIn),
    isLoading: computed(() => authStore.isLoading),
    needSignup: computed(() => authStore.needSignup),

    loginWithCode,
    signupUser,
    logout,
    fetchUserInfo,
    fetchUserDetail,
    updateUserInfo,
    fetchAlgorithmStats,
    fetchDailyGames,
    fetchGameResults,
    fetchGameResultDetail,
    closeSignupModal,
  };
};
