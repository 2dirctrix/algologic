/**
 * WebSocket 기본 연결 관리 스토어
 * 전역 WebSocket 연결 상태만 관리
 */

import { defineStore } from 'pinia';
import webSocketService from '@/websocket/services/WebSocketService.js';

export const useWebSocketStore = defineStore('websocket', {
  state: () => ({
    isConnected: false,
    isConnecting: false,
    connectionError: null,
  }),

  getters: {
    connectionStatus: state => {
      if (state.isConnecting) return 'connecting';
      if (state.isConnected) return 'connected';
      if (state.connectionError) return 'error';
      return 'disconnected';
    },
  },

  actions: {
    /**
     * WebSocket 연결 초기화
     * 환경변수 VITE_SERVER_DOMAIN 사용
     */
    async connect() {
      if (this.isConnected || this.isConnecting) {
        return;
      }

      try {
        this.isConnecting = true;
        this.connectionError = null;

        // 환경변수에서 서버 도메인 가져오기
        const baseUrl = import.meta.env.VITE_API_BASE_URL;

        await webSocketService.connect(baseUrl);

        this.isConnected = true;
      } catch (error) {
        this.connectionError = error.message;
        console.error('WebSocket 연결 실패:', error);
        throw error;
      } finally {
        this.isConnecting = false;
      }
    },

    /**
     * WebSocket 연결 해제
     */
    disconnect() {
      try {
        webSocketService.disconnect();
        this.isConnected = false;
        this.connectionError = null;
      } catch (error) {
        console.error('WebSocket 연결 해제 실패:', error);
      }
    },

    /**
     * 연결 상태 확인 및 갱신
     */
    checkConnection() {
      this.isConnected = webSocketService.isConnected();
      return this.isConnected;
    },

    /**
     * WebSocket 서비스 인스턴스 반환
     */
    getService() {
      return webSocketService;
    },

    /**
     * 연결 재시도
     * @param {number} maxRetries - 최대 재시도 횟수
     */
    async reconnect(maxRetries = 3) {
      for (let i = 0; i < maxRetries; i++) {
        try {
          await this.connect();
          return;
        } catch (error) {
          if (i === maxRetries - 1) {
            throw error;
          }
          // 재시도 간격 (1초, 2초, 3초)
          await new Promise(resolve => setTimeout(resolve, (i + 1) * 1000));
        }
      }
    },
  },
});
