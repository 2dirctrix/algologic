/**
 * Vue Router 설정
 * 라우터 가드 적용하여 인증 상태에 따른 접근 제어
 */

import { createRouter, createWebHistory } from 'vue-router';
import { requireAuth, setupRouterGuards } from './guards.js';
import AuthPage from '@/pages/auth/AuthPage.vue';
import MainPage from '@/pages/main/MainPage.vue';
import WaitingRoom from '@/pages/game/GamePage.vue';
import GamePage from '@/pages/game/GamePage.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/main',
    },

    {
      path: '/auth',
      name: 'auth',
      component: AuthPage,
    },
    {
      path: '/main',
      name: 'main',
      component: MainPage,
      beforeEnter: requireAuth,
    },
    {
      path: '/room',
      name: 'waitingRoom',
      component: WaitingRoom,
    },
    {
      path: '/game',
      name: 'inGame',
      component: GamePage,
    },
  ],
});

setupRouterGuards(router);

export default router;
