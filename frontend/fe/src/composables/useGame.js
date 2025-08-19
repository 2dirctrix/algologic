import { ref } from 'vue';
import { useWebSocketStore } from '@/stores/websocket/websocket.js';
import { WS_ENDPOINTS } from '@/websocket/constants/endpoints.js';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';

export const useGame = () => {
  const webSocketStore = useWebSocketStore();
  const inGameStore = useInGameStore();

  const isSubscribed = ref(false);
  const subscriptionId = ref(null);

  const subscribeInGameEvents = async roomId => {
    const webSocketService = webSocketStore.getService();

    // 1. 싱크 이벤트 구독
    const subscribeEndpoint = WS_ENDPOINTS.GAME.SYNC_SUBSCRIBE(roomId);
    subscriptionId.value = webSocketService.subscribe(subscribeEndpoint, message => {
      handleSyncMessage(message, roomId);
    });

    // 2. 단계 이벤트 구독
    const banPickBuyPhaseEvent = WS_ENDPOINTS.GAME.BAN_PICK_BUY_SUBSCRIBE(roomId);
    webSocketService.subscribe(banPickBuyPhaseEvent, message => {
      handleBanPickBuyMessage(message, roomId);
    });

    // 3. 단계 종료 이벤트 구독
    const banPickBuyPhaseEndEvent = WS_ENDPOINTS.GAME.BAN_PICK_BUY_END_SUBSCRIBE(roomId);
    webSocketService.subscribe(banPickBuyPhaseEndEvent, message => {
      handleBanPickBuyEndMessage(message, roomId);
    });
  };

  /**
   * 게임 동기화 요청 - 구독 후 동기화 요청
   * @param {string|number} roomId - 방 ID
   */
  const manualSync = async roomId => {
    try {
      // WebSocket 서비스 가져오기
      const webSocketService = webSocketStore.getService();

      // 1. 구독이 안되어 있으면 구독
      if (!isSubscribed.value) {
        const subscribeEndpoint = WS_ENDPOINTS.GAME.SYNC_SUBSCRIBE(roomId);

        // 구독 등록 - SYNC 응답 수신
        subscriptionId.value = webSocketService.subscribe(subscribeEndpoint, message => {
          handleSyncMessage(message, roomId);
        });

        isSubscribed.value = true;
      }

      // 2. 동기화 요청 전송
      const requestEndpoint = WS_ENDPOINTS.GAME.SYNC_REQUEST(roomId);

      webSocketService.sendMessage(requestEndpoint, {});
    } catch (error) {
      console.error('[USE GAME] 게임 동기화 실패:', error);
      throw error;
    }
  };

  /**
   * SYNC 응답 처리
   * @param {Object} message - 수신된 메시지
   * @param {string|number} roomId - 방 ID
   */
  const handleSyncMessage = (message, roomId) => {
    // message.data.payload.phase === 'LOADING' 인 경우 핸들러가 아무 동작하지않고 넘어가도록 로직 추가
    try {
      if (
        (message.type === 'RESPONSE' || message.type === 'SYNC') &&
        message.code === 200 &&
        message.data?.event === 'SYNC' &&
        message.data?.payload
      ) {
        // LOADING 상태면 처리 스킵
        if (message.data.payload.phase === 'LOADING') {
          return;
        }
        // InGameStore로 동기화 데이터 전달
        inGameStore.updateTimeSync(message.data.payload);
      } else {
        console.warn('[USE GAME] 유효하지 않은 SYNC 응답:', message);
      }
    } catch (error) {
      console.error('[USE GAME] SYNC 응답 처리 중 오류:', error);
    }
  };

  const handleBanPickBuyEndMessage = (message, roomId) => {
    try {
      if (message.type === 'EVENT' && message.code === 200) {
        const event = message.data.event;

        switch (event) {
          case 'BAN_FINISHED':
            inGameStore.updateBanResult(message.data.payload);
            break;
          case 'PICK_FINISHED':
            inGameStore.updatePickResult(message.data.payload);
            break;
          case 'PURCHASE_FINISHED':
            inGameStore.startBanPickBuyExitAnimation();
            break;
        }
      }
    } catch (error) {
      console.error('종료 에러: ', error);
    }
  };

  const handleBanPickBuyMessage = (message, roomId) => {
    //  && message.data.event === 'PHASE_STARTED'
    try {
      if (message.type === 'EVENT' && message.code === 200) {
        const event = message.data.payload.phase;

        switch (event) {
          case 'BAN_CHOICE':
            inGameStore.updateTimeSync(message.data.payload);
            inGameStore.updateBanPickStatus(message.data.payload);
            break;
          case 'PICK_CHOICE':
            inGameStore.updateTimeSync(message.data.payload);
            inGameStore.updateBanPickStatus(message.data.payload);
            break;
          case 'PURCHASE':
            inGameStore.updateTimeSync(message.data.payload);
            inGameStore.updateBuyStatus(message.data.payload);
            break;
        }
        // } else if (message.type === 'EVENT' && message.code === 200) {
        //   const event = message.data.payload.phase;
        switch (event) {
          case 'BAN_CHOSEN':
            inGameStore.updateBanPickStatus(message.data.payload);
            break;
          case 'PICK_CHOSEN':
            inGameStore.updateBanPickStatus(message.data.payload);
            break;
        }
      }
    } catch (error) {
      console.error('종료 에러: ', error);
    }
  };

  /**
   * 구독 해제
   * @param {string|number} roomId - 방 ID
   */
  const unsubscribeSync = async roomId => {
    if (isSubscribed.value && subscriptionId.value) {
      try {
        const webSocketService = webSocketStore.getService();
        webSocketService.unsubscribe(subscriptionId.value);

        isSubscribed.value = false;
        subscriptionId.value = null;
      } catch (error) {
        console.error('[USE GAME] 구독 해제 중 오류:', error);
      }
    }
  };

  return {
    subscribeInGameEvents,
    manualSync,
    unsubscribeSync,
    isSubscribed,
  };
};
