/**
 * 방 관련 WebSocket 관리 스토어 (단순화 버전)
 * 사용자는 한 번에 하나의 방에만 구독 가능
 */

import { defineStore } from 'pinia';
import { useWebSocketStore } from './websocket/websocket.js';
import { WS_ENDPOINTS } from '@/websocket/constants/endpoints.js';
import router from '@/router/index.js';
import { useAlertStore } from '@/stores/alertStore.js';

export const useRoomWebSocketStore = defineStore('roomWebsocket', {
  state: () => ({
    // 현재 구독 중인 방
    currentRoomId: null,
    currentSubscriptionId: null,

    // 개인 소켓 구독
    personalSubscriptionId: null,

    // 현재 방 데이터
    roomData: null,

    // 연결 상태
    isSubscribed: false,
    isPersonalSubscribed: false,
  }),

  getters: {
    // 현재 방에 구독되어 있는지
    isConnectedToRoom: state => {
      return state.isSubscribed && state.currentRoomId && state.currentSubscriptionId;
    },
  },

  actions: {
    // ===================================
    // 방 구독 관리 (단일 방만)
    // ===================================

    /**
     * 방 구독 시작
     * @param {string} roomId - 방 ID
     * @param {function} handler - 메시지 처리 핸들러
     */
    subscribeToRoom(roomId, handler) {
      const webSocketStore = useWebSocketStore();

      if (!webSocketStore.isConnected) {
        throw new Error('WebSocket이 연결되지 않았습니다.');
      }

      // 기존 구독이 있으면 해제
      if (this.isSubscribed) {
        this.unsubscribeFromRoom();
      }

      const webSocketService = webSocketStore.getService();

      // 새로운 구독 시작
      this.currentSubscriptionId = webSocketService.subscribeToRoom(roomId, message => {
        this.handleRoomMessage(message);
        handler(message);
      });

      // 개인 소켓 구독 추가
      this.personalSubscriptionId = webSocketService.subscribe(WS_ENDPOINTS.ROOM.PERSONAL_QUEUE(roomId), message => {
        // 강퇴 메시지 처리
        if (message.type === 'EVENT' && message.data?.event === 'KICKED') {
          // 방 구독 해제
          this.unsubscribeFromRoom();

          // 메인 페이지로 이동 후 알림 표시
          router.push('/main').then(() => {
            const alertStore = useAlertStore();
            alertStore.showAlert('방에서 강제 퇴장되었습니다.', 'error');
          });
        }
      });

      this.currentRoomId = roomId;
      this.isSubscribed = true;
      this.isPersonalSubscribed = true;
    },

    /**
     * 현재 방 구독 해제
     */
    unsubscribeFromRoom() {
      if (!this.isSubscribed || !this.currentSubscriptionId) {
        return;
      }

      try {
        const webSocketStore = useWebSocketStore();
        const webSocketService = webSocketStore.getService();

        webSocketService.unsubscribe(this.currentSubscriptionId);

        // 개인 소켓 구독도 해제
        if (this.personalSubscriptionId) {
          webSocketService.unsubscribe(this.personalSubscriptionId);
        }

        // 상태 초기화
        this.currentRoomId = null;
        this.currentSubscriptionId = null;
        this.personalSubscriptionId = null;
        this.roomData = null;
        this.isSubscribed = false;
        this.isPersonalSubscribed = false;
      } catch (error) {}
    },

    // ===================================
    // 메시지 처리
    // ===================================

    /**
     * 방 메시지 처리 (내부용)
     * @param {object} message - 수신된 메시지
     */
    handleRoomMessage(message) {
      if (message.type === 'EVENT' && message.code === 200 && message.data?.payload) {
        this.updateRoomData(message.data.payload);
      }
    },

    /**
     * 방 데이터 업데이트
     * @param {object} payload - 방 데이터 페이로드
     */
    updateRoomData(payload) {
      if (payload.roomId && payload.participants) {
        this.roomData = {
          ...payload,
          lastUpdated: Date.now(),
        };
      }
    },

    // ===================================
    // 방 액션
    // ===================================

    /**
     * 방에 메시지 전송
     * @param {string} action - 액션 타입 (join, ready, leave, start)
     * @param {object} payload - 메시지 페이로드
     */
    sendMessage(action, payload = {}) {
      if (!this.currentRoomId) {
        throw new Error('현재 구독 중인 방이 없습니다.');
      }

      const webSocketStore = useWebSocketStore();

      if (!webSocketStore.isConnected) {
        throw new Error('WebSocket이 연결되지 않았습니다.');
      }

      try {
        const webSocketService = webSocketStore.getService();
        webSocketService.sendRoomMessage(this.currentRoomId, action, payload);
      } catch (error) {
        console.error(`방 메시지 전송 실패: ${this.currentRoomId}/${action}`, error);
        throw error;
      }
    },

    /**
     * 방 입장 요청
     */
    joinRoom() {
      this.sendMessage('join');
    },

    /**
     * 방 퇴장 요청
     */
    leaveRoom() {
      // 현재 방이 있을 때만 leave 메시지 전송
      if (this.currentRoomId) {
        try {
          this.sendMessage('leave');
        } catch (error) {
          console.error('방 퇴장 메시지 전송 실패:', error);
        }
      }
      this.unsubscribeFromRoom();
    },

    /**
     * 준비 상태 변경
     * @param {boolean} isReady - 준비 상태
     */
    changeReadyStatus(isReady) {
      this.sendMessage('ready', { ready: isReady });
    },

    /**
     * 게임 시작 요청
     */
    startGame() {
      this.sendMessage('start');
    },

    // ===================================
    // 편의 메서드
    // ===================================

    /**
     * 방 구독 + 입장 (일반적인 사용 패턴)
     * @param {string} roomId - 방 ID
     * @param {function} handler - 메시지 핸들러
     */
    connectToRoom(roomId, handler) {
      this.subscribeToRoom(roomId, handler);
      this.joinRoom();
    },

    /**
     * 방 퇴장 + 구독 해제 (일반적인 사용 패턴)
     */
    disconnectFromRoom() {
      // 현재 방이 있을 때만 leave 메시지 전송
      if (this.currentRoomId) {
        try {
          this.sendMessage('leave');
        } catch (error) {
          console.error('방 퇴장 메시지 전송 실패:', error);
        }
      }
      // 구독 해제는 항상 실행
      this.unsubscribeFromRoom();
    },

    /**
     * 현재 방 정보 조회
     * @returns {object|null}
     */
    getCurrentRoomData() {
      return this.roomData;
    },

    /**
     * 상태 초기화 (로그아웃 시 등)
     */
    reset() {
      this.unsubscribeFromRoom();
    },
  },
});
