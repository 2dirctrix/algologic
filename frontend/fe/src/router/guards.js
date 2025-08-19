/**
 * 라우터 가드
 */

import { useAuthStore } from '@/stores/auth/auth.js';
import { useAuth } from '@/composables/useAuth.js';

/**
 * 라우터 가드 설정
 * @param {Router} router
 */
export const setupRouterGuards = router => {
  router.beforeEach((to, from, next) => {
    const authStore = useAuthStore();

    // 로그인 페이지 접근 처리
    if (to.path === '/auth') {
      if (authStore.isLoggedIn) {
        // 이미 로그인된 사용자는 메인페이지로 리다이렉트
        next('/main');
      } else {
        next();
      }
      return;
    }

    // 기타 페이지 접근 처리
    if (authStore.isLoggedIn) {
      // 로그인된 사용자는 모든 페이지 접근 가능
      next();
    } else {
      // 로그인하지 않은 사용자는 로그인 페이지로 리다이렉트
      next('/auth');
    }
  });
};

/**
 * 인증 필요 페이지 가드
 * 로그인 확인 + 사용자 정보 로드
 */
export const requireAuth = async (to, from, next) => {
  const authStore = useAuthStore();
  const { fetchUserInfo } = useAuth();

  try {
    await fetchUserInfo();
    next();
  } catch (error) {
    authStore.logout();
    next('/auth');
  }
};
