/**
 * 인증 상태 관리 스토어
 * localStorage에 로그인 플래그, 사용자 정보 영속 저장
 */

import { defineStore } from 'pinia';
import { watch } from 'vue';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    isLoggedIn: false,
    user: null,
    userDetail: null,
    isLoading: false,
    needSignup: false,
    userEmail: '',
  }),

  getters: {
    isAuthenticated: state => state.isLoggedIn,
    currentUser: state => state.user,
    nickname: state => state.user?.nickname || '',
    email: state => state.user?.email || '',
    coin: state => state.user?.coin || 0,
    score: state => state.user?.score || 0,
    profileImage: state => state.user?.profileImageUrl || '',
    programmingLanguage: state => state.user?.programmingLanguage || '',
  },

  actions: {
    /**
     * 회원가입 필요 상태 설정
     * @param {string} email
     */
    setNeedSignup(email) {
      this.needSignup = true;
      this.userEmail = email;
      this.isLoggedIn = false;
      this.user = null;
    },

    /**
     * 로그인 성공 시 상태 업데이트
     * @param {Object} userData - 백엔드에서 받은 사용자 정보
     */
    login(userData) {
      this.isLoggedIn = true;
      this.user = userData;
      this.needSignup = false;
      this.userEmail = '';
    },

    /**
     * 로그아웃 시 localStorage 데이터 삭제 및 게임 스토어 초기화
     */
    logout() {
      this.isLoggedIn = false;
      this.user = null;
      this.userDetail = null;
      this.needSignup = false;
      this.userEmail = '';

      // 게임 관련 스토어 초기화
      try {
        const inGameStore = useInGameStore();
        inGameStore.reset();
      } catch (error) {
        console.warn('[AUTH STORE] 게임 스토어 초기화 중 오류 (무시됨):', error);
      }
    },

    /**
     * 로딩 상태 설정
     * @param {boolean} loading
     */
    setLoading(loading) {
      this.isLoading = loading;
    },

    /**
     * 사용자 정보 조회 및 저장
     * @param {Object} userData - 백엔드에서 받은 사용자 정보
     */
    setUserData(userData) {
      this.user = userData;
    },

    /**
     * 사용자 상세 정보 저장
     * @param {Object} detailData - 백엔드에서 받은 사용자 상세 정보
     */
    setUserDetail(detailData) {
      this.userDetail = detailData;
    },

    /**
     * 회원가입 모달 닫기
     * @Task 테스트 후 불필요한 경우 삭제 처리 필요
     */
    closeSignupModal() {
      this.needSignup = false;
      this.userEmail = '';
    },
  },

  persist: {
    key: 'auth-store',
    storage: localStorage,
    pick: ['isLoggedIn', 'user'],
  },
});

/**
 * 인증 상태 변화를 감지하여 WebSocket 연결을 자동 관리
 * @param {object} pinia - Pinia 인스턴스
 */
export function watchAuthAndConnect(pinia) {
  const authStore = useAuthStore(pinia);

  // WebSocket 스토어는 동적 import로 순환 참조 방지
  let webSocketStore = null;

  watch(
    () => authStore.isAuthenticated,
    async isAuthenticated => {
      // 필요할 때만 WebSocket 스토어 import
      if (!webSocketStore) {
        const { useWebSocketStore } = await import('@/stores/websocket/websocket.js');
        webSocketStore = useWebSocketStore(pinia);
      }

      if (isAuthenticated && !webSocketStore.isConnected) {
        try {
          await webSocketStore.connect();
        } catch (error) {
          console.error('[AUTH WATCH] WebSocket 연결 실패:', error);
          // 연결 실패 시 재시도
          try {
            await webSocketStore.reconnect();
          } catch (retryError) {
            console.error('[AUTH WATCH] WebSocket 재연결 실패:', retryError);
          }
        }
      } else if (!isAuthenticated && webSocketStore?.isConnected) {
        webSocketStore.disconnect();
      }
    },
    { immediate: true }, // 앱 시작시 즉시 체크
  );
}
