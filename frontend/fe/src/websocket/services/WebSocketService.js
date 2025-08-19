// ===================================
// WebSocket STOMP 클라이언트 서비스
// Spring Boot STOMP 엔드포인트 연동
// 통신 메서드 정의
// ===================================

import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { WS_ENDPOINTS } from '../constants/endpoints.js';

class WebSocketService {
  constructor() {
    this.client = null;
    this.connected = false;
    this.subscriptions = new Map();
    this.baseUrl = null;
  }

  /**
   * WebSocket 연결 초기화
   * @param {string} baseUrl - 백엔드 서버 기본 URL
   */
  connect(baseUrl) {
    this.baseUrl = baseUrl;

    return new Promise((resolve, reject) => {
      this.client = new Client({
        // SockJS를 통한 WebSocket 연결 (쿠키 인증 포함)
        webSocketFactory: () =>
          new SockJS(`${baseUrl}/ws`, null, {
            withCredentials: true, // httpOnly 쿠키 포함
          }),

        // 연결 성공 콜백
        onConnect: frame => {
          this.connected = true;
          resolve(frame);
        },

        // 연결 실패 콜백
        onStompError: frame => {
          this.connected = false;
          console.error('WebSocket STOMP Error:', frame.headers.message);
          reject(new Error(frame.headers.message));
        },

        // 연결 해제 콜백
        onDisconnect: () => {
          this.connected = false;
        },
      });

      this.client.activate();
    });
  }

  /**
   * WebSocket 연결 해제
   */
  disconnect() {
    if (this.client) {
      // 모든 구독 해제
      this.subscriptions.forEach(subscription => {
        subscription.unsubscribe();
      });
      this.subscriptions.clear();

      // 클라이언트 비활성화
      this.client.deactivate();
      this.connected = false;
    }
  }

  /**
   * 연결 상태 확인
   * @returns {boolean}
   */
  isConnected() {
    return this.connected && this.client?.connected;
  }

  /**
   * 메시지 전송
   * @param {string} destination - 메시지 전송 경로 (예: /app/v1/rooms/123/join)
   * @param {object} message - 전송할 메시지 객체
   */
  sendMessage(destination, message = {}) {
    if (!this.isConnected()) {
      throw new Error('WebSocket is not connected');
    }

    try {
      this.client.publish({
        destination,
        body: JSON.stringify(message),
        headers: {
          'content-type': 'application/json',
        },
      });
    } catch (error) {
      console.error('Failed to send message:', error);
      throw error;
    }
  }

  /**
   * 토픽 구독
   * @param {string} destination - 구독할 토픽 경로 (예: /topic/rooms/123)
   * @param {function} handler - 메시지 수신 핸들러
   * @returns {string} subscriptionId - 구독 ID
   */
  subscribe(destination, handler) {
    if (!this.isConnected()) {
      throw new Error('WebSocket is not connected');
    }

    try {
      const subscription = this.client.subscribe(destination, message => {
        try {
          const parsedMessage = JSON.parse(message.body);

          handler(parsedMessage);
        } catch (error) {
          console.error('Failed to parse received message:', error);
        }
      });

      const subscriptionId = subscription.id;
      this.subscriptions.set(subscriptionId, subscription);

      return subscriptionId;
    } catch (error) {
      console.error('Failed to subscribe:', error);
      throw error;
    }
  }

  /**
   * 구독 해제
   * @param {string} subscriptionId - 구독 ID
   */
  unsubscribe(subscriptionId) {
    const subscription = this.subscriptions.get(subscriptionId);
    if (subscription) {
      subscription.unsubscribe();
      this.subscriptions.delete(subscriptionId);
    }
  }

  /**
   * 방 관련 메시지 전송을 위한 헬퍼 메서드
   * @param {string} roomId - 방 ID
   * @param {string} action - 액션 타입 (join, ready, leave)
   * @param {object} payload - 메시지 페이로드
   */
  sendRoomMessage(roomId, action, payload = {}) {
    const destination = WS_ENDPOINTS.ROOM.ACTION(roomId, action);
    this.sendMessage(destination, payload);
  }

  /**
   * 방 토픽 구독을 위한 헬퍼 메서드
   * @param {string} roomId - 방 ID
   * @param {function} handler - 메시지 수신 핸들러
   * @returns {string} subscriptionId
   */
  subscribeToRoom(roomId, handler) {
    return this.subscribe(WS_ENDPOINTS.ROOM.TOPIC(roomId), handler);
  }
}

const webSocketService = new WebSocketService();
export default webSocketService;
