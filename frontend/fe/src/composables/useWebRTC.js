import { ref } from 'vue';
import { apiClient } from '@/api/utils/index.js';
import { API_ENDPOINTS } from '@/api/constants/endpoints';
import { useWebRTCStore } from '@/stores/webrtc/webrtc';
import { useInGameStore } from '@/pages/game/store/useInGameStore';
import { useAuthStore } from '@/stores/auth/auth';
import { useWebSocketStore } from '@/stores/websocket/websocket';
import { WS_ENDPOINTS } from '@/websocket/constants/endpoints';

export function useWebRTC() {
  const webrtcStore = useWebRTCStore();
  const inGameStore = useInGameStore();
  const authStore = useAuthStore();
  const webSocketStore = useWebSocketStore();

  // 토큰 상태(필요하면 컴포저블 내부에서도 관리 가능)
  const token = ref(null);
  const isInitialized = ref(false);
  const screenShareReady = ref(false);

  // 채점 결과 구독 관련 상태
  // 채점 결과 메시지를 저장할 반응형 변수
  const judgeResultMessage = ref(null);
  const judgeResultsSubscriptionId = ref(null);
  const judgeResultsHandler = ref(null);

  /**
   * LiveKit 토큰 발급 요청
   * @param {string} roomId
   * @param {string} memberId
   */
  const getToken = async (roomId, memberId) => {
    try {
      const res = await apiClient.post(API_ENDPOINTS.WEBRTC.DISPLAY, {
        roomName: roomId,
        participantName: memberId,
      });
      token.value = res.data.data.token;
      webrtcStore.token = token.value; // Pinia 스토어 저장
      return token.value;
    } catch (err) {
      console.error('[useWebRTC] 토큰 발급 실패:', err);
      throw err;
    }
  };

  async function connectWebRTC() {
    try {
      const roomId = inGameStore.currentRoomId;
      const memberId = authStore.currentUser.id;

      if (!roomId) {
        throw new Error('방 ID를 찾을 수 없습니다.');
      }

      if (!memberId) {
        throw new Error('사용자 정보를 찾을 수 없습니다.');
      }

      // 1. LiveKit 토큰 발급
      await getToken(roomId, memberId);

      // 2. LiveKit 연결
      await webrtcStore.joinRoom();

      isInitialized.value = true;
      screenShareReady.value = webrtcStore.screenShareReady;
      return true;
    } catch (err) {
      console.error('[useWebRTC] 게임용 WebRTC 초기화 실패:', err);
      isInitialized.value = false;
      screenShareReady.value = false;
      throw err;
    }
  }

  /**
   * 채점 결과 핸들러 설정
   * @param {function} handler - 채점 결과를 처리할 핸들러 함수
   */
  const setJudgeResultsHandler = handler => {
    judgeResultsHandler.value = handler;
  };

  /**
   * 채점 결과 WebSocket 구독
   */
  const subscribeToJudgeResults = async () => {
    try {
      // WebSocket 연결이 없으면 연결
      if (!webSocketStore.isConnected) {
        await webSocketStore.connect();
      }

      const webSocketService = webSocketStore.getService();
      const endpoint = WS_ENDPOINTS.PROBLEM.JUDGE_RESULTS;

      judgeResultsSubscriptionId.value = webSocketService.subscribe(endpoint, message => {
        // 인게임 스토어에 채점 결과 저장
        inGameStore.updateJudgeResult(message);

        // 기존 반응형 변수도 유지 (호환성 위해)
        judgeResultMessage.value = message;
      });
    } catch (err) {
      console.error('[useWebRTC] 채점 결과 구독 실패:', err);
      throw err;
    }
  };

  /**
   * 채점 결과 구독 해제
   */
  const unsubscribeFromJudgeResults = () => {
    if (judgeResultsSubscriptionId.value) {
      const webSocketService = webSocketStore.getService();
      webSocketService.unsubscribe(judgeResultsSubscriptionId.value);
      judgeResultsSubscriptionId.value = null;
    }
  };

  /**
   * 게임용 WebRTC 초기화 (InGamePage 전용)
   */
  const initializeForGame = async () => {
    await connectWebRTC();
    await subscribeToJudgeResults();
  };

  /**
   * 게임용 WebRTC 정리 (InGamePage 전용)
   */
  const cleanupForGame = async () => {
    try {
      // 채점 결과 구독 해제
      unsubscribeFromJudgeResults();
      await webrtcStore.leaveRoom();
      isInitialized.value = false;
      screenShareReady.value = false;
    } catch (err) {
      console.error('[useWebRTC] InGamePage WebRTC 정리 실패:', err);
    }
  };

  return {
    token,
    isInitialized,
    screenShareReady,
    judgeResultMessage,
    getToken,
    initializeForGame,
    cleanupForGame,
    setJudgeResultsHandler,
    subscribeToJudgeResults,
    unsubscribeFromJudgeResults,
  };
}
