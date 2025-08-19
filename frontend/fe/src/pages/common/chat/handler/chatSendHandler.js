import websocketService from '@/websocket/services/WebSocketService.js';
import { useAuthStore } from '@/stores/auth/auth.js';

/**
 * 채팅 메시지 전송 핸들러
 */

/**
 * 공용 채팅 메시지 전송
 * @param {string} content - 전송할 메시지 내용
 * @param {string} endpoint - 전송할 웹소켓 엔드포인트
 * @returns {Promise<void>}
 */
export async function sendChatMessage(content, endpoint) {
  try {
    const authStore = useAuthStore();

    if (!authStore.currentUser?.id) {
      console.error('사용자 정보가 없습니다.');
      return;
    }

    if (!endpoint) {
      console.error('전송 엔드포인트가 지정되지 않았습니다.');
      return;
    }

    const messagePayload = {
      memberId: authStore.currentUser.id,
      content: content,
    };

    await websocketService.sendMessage(endpoint, messagePayload);
  } catch (error) {
    console.error('채팅 메시지 전송 중 오류:', error);
    throw error;
  }
}
