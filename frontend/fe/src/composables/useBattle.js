import { ref } from 'vue';
import { useInGameStore } from '@/pages/game/store/useInGameStore';
import { useWebSocketStore } from '@/stores/websocket/websocket';
import { WS_ENDPOINTS } from '@/websocket/constants/endpoints';
import { apiClient } from '@/api/utils/index.js';
import { API_ENDPOINTS } from '@/api/constants/endpoints';
import webSocketService from '@/websocket/services/WebSocketService.js';

export function useBattle() {
  const inGameStore = useInGameStore();
  const webSocketStore = useWebSocketStore();

  // 배틀 이벤트 구독 관련 상태
  const battleSubscriptionId = ref(null);
  const battleEndSubscriptionId = ref(null);
  const battleHandler = ref(null);

  /**
   * TODO : 게임 종료 (서렌) 요청
   * */
  const sendSurrender = () => {
    const inGameStore = useInGameStore();
    const roomId = inGameStore.currentRoomId;

    // 이미 항복한 상태라면 중복 요청 방지
    if (inGameStore.banPickBuyResults.surrender) {
      return;
    }

    // 항복 상태를 즉시 설정하여 추가 제출 방지 (확인 후 실행되므로 여기서 설정)
    inGameStore.banPickBuyResults.surrender = true;

    const endPoint = WS_ENDPOINTS.GAME.SURRENDER_REQUEST_PUB(roomId);

    webSocketService.sendMessage(endPoint);
  };

  /**
   * 배틀 이벤트 핸들러 설정
   * @param {function} handler - 배틀 이벤트를 처리할 핸들러 함수
   */
  const setBattleHandler = handler => {
    battleHandler.value = handler;
  };

  /**
   * 게임 결과 API 호출 및 스토어 업데이트
   * @param {string} gameResultId - 게임 결과 ID
   */
  const fetchGameResult = async gameResultId => {
    try {
      const response = await apiClient.get(API_ENDPOINTS.BATTLE.GAME_RESULT_PREFIX(gameResultId));
      const gameResultData = response.data.data;

      // 인게임 스토어에 게임 결과 업데이트 및 상태 변경
      inGameStore.updateGameResult(gameResultData);
    } catch (error) {
      console.error('[useBattle] 게임 결과 조회 실패:', error);
      throw error;
    }
  };

  /**
   * 배틀 이벤트 WebSocket 구독
   */
  const subscribeToBattleEvents = async () => {
    function updateMyBattleData(payload) {
      // me 필드 데이터를 스토어에 갱신
      if (payload.me) {
        const meData = payload.me;

        inGameStore.playerId = meData.playerId;

        // surrender 상태 갱신
        if (meData.surrender !== undefined) {
          inGameStore.surrender = meData.surrender;
        }

        // 구매한 아이템들 갱신
        if (meData.purchasedItems) {
          inGameStore.banPickBuyResults.purchasedItems = meData.purchasedItems;
        }

        // 구매한 스펠들 갱신
        if (meData.purchasedSpells) {
          inGameStore.banPickBuyResults.purchasedSpells = meData.purchasedSpells;
        }

        // 활성화된 아이템들 갱신
        if (meData.activatedItems) {
          inGameStore.activatedItems = meData.activatedItems;
        }

        // 활성화된 스펠들 갱신
        if (meData.activatedSpells) {
          inGameStore.activatedSpells = meData.activatedSpells;
        }
      }
    }

    try {
      const roomId = inGameStore.currentRoomId;
      if (!roomId) {
        throw new Error('방 ID를 찾을 수 없습니다.');
      }

      // WebSocket 연결이 없으면 연결
      if (!webSocketStore.isConnected) {
        await webSocketStore.connect();
      }

      const webSocketService = webSocketStore.getService();
      const endpoint = WS_ENDPOINTS.GAME.BATTLE_SUBSCRIBE(roomId);

      battleSubscriptionId.value = webSocketService.subscribe(endpoint, message => {
        // PHASE_STARTED 이벤트 분기 처리  && message.data?.event === 'PHASE_STARTED'
        if (message.type === 'EVENT') {
          const payload = message.data.payload;
          const phase = payload?.phase;

          switch (phase) {
            case 'BATTLE':
              // 시간 동기화 업데이트
              inGameStore.updateTimeSync(payload);

              // 내 배틀 데이터 (아이템/스펠/서렌) 업데이트
              updateMyBattleData(payload);
              break;

            default:
              break;
          }
        }
        if (message.data?.event === 'ITEM_RECEIVED') {
          const receivedActivatedItems = message.data?.payload?.me?.activatedItems;

          if (receivedActivatedItems && Array.isArray(receivedActivatedItems)) {
            // 기존 activatedItems에서 새로운 아이템만 필터링하여 추가
            const currentActivatedItems = inGameStore.banPickBuyResults.activatedItems || [];

            const newItems = receivedActivatedItems.filter(
              receivedItem => !currentActivatedItems.some(currentItem => currentItem.activationId === receivedItem.activationId),
            );

            if (newItems.length > 0) {
              // 새로운 아이템들을 기존 리스트에 추가
              const updatedItems = [
                ...currentActivatedItems,
                ...newItems.map(item => ({
                  itemId: item.itemId,
                  activationId: item.activationId,
                  duration: item.duration,
                })),
              ];

              // 스토어에 업데이트
              inGameStore.banPickBuyResults.activatedItems = updatedItems;
            }
          }
        }

        if (message.data?.event === 'SPELL_ACTIVATED') {
          const receivedActivatedSpells = message.data?.payload?.me?.activatedSpells;

          if (receivedActivatedSpells && Array.isArray(receivedActivatedSpells)) {
            // 기존 activatedSpells에서 새로운 스펠만 필터링하여 추가
            const currentActivatedSpells = inGameStore.banPickBuyResults.activatedSpells || [];

            const newSpells = receivedActivatedSpells.filter(
              receivedSpell => !currentActivatedSpells.some(currentSpell => currentSpell.activationId === receivedSpell.activationId),
            );

            if (newSpells.length > 0) {
              // 새로운 스펠들을 기존 리스트에 추가
              const updatedSpells = [
                ...currentActivatedSpells,
                ...newSpells.map(spell => ({
                  spellId: spell.spellId,
                  activationId: spell.activationId,
                  duration: spell.duration,
                })),
              ];

              // 스토어에 업데이트
              inGameStore.banPickBuyResults.activatedSpells = updatedSpells;
            }
          }
        }

        // 외부 핸들러가 설정되어 있으면 호출
        if (battleHandler.value) {
          battleHandler.value(message);
        }
      });
    } catch (err) {
      console.error('[useBattle] 배틀 이벤트 구독 실패:', err);
      throw err;
    }
  };

  /**
   * 배틀 종료 이벤트 구독 포인트
   * */
  const subscribeToBattleEndEvents = async () => {
    try {
      const roomId = inGameStore.currentRoomId;
      if (!roomId) {
        throw new Error('방 ID를 찾을 수 없습니다.');
      }

      // WebSocket 연결이 없으면 연결
      if (!webSocketStore.isConnected) {
        await webSocketStore.connect();
      }

      const webSocketService = webSocketStore.getService();
      const endpoint = WS_ENDPOINTS.GAME.BATTLE_END_SUBSCRIBE(roomId);

      battleEndSubscriptionId.value = webSocketService.subscribe(endpoint, async message => {
        if (message.type === 'EVENT' && (message.data?.event === 'BATTLE_FINISHED' || message.data?.event === 'SURRENDERED')) {
          const gameResultId = message.data.payload.gameResultId;
          inGameStore.gameResultId = gameResultId;

          // 게임 결과 API 호출 및 상태 업데이트
          try {
            await fetchGameResult(gameResultId);
          } catch (error) {
            console.error('[useBattle] 게임 결과 처리 실패:', error);
          }
        }

        // 외부 핸들러가 설정되어 있으면 호출
        if (battleHandler.value) {
          battleHandler.value(message);
        }
      });
    } catch (err) {
      console.error('[useBattle] 배틀 이벤트 구독 실패:', err);
      throw err;
    }
  };

  /**
   * 배틀 이벤트 구독 해제
   */
  const unsubscribeFromBattleEvents = () => {
    if (battleSubscriptionId.value) {
      const webSocketService = webSocketStore.getService();
      webSocketService.unsubscribe(battleSubscriptionId.value);
      battleSubscriptionId.value = null;
    }
  };

  /**
   * 배틀 종료 이벤트 구독 해제
   */
  const unsubscribeFromBattleEndEvents = () => {
    if (battleEndSubscriptionId.value) {
      const webSocketService = webSocketStore.getService();
      webSocketService.unsubscribe(battleEndSubscriptionId.value);
      battleEndSubscriptionId.value = null;
    }
  };

  return {
    setBattleHandler,
    subscribeToBattleEvents,
    subscribeToBattleEndEvents,
    unsubscribeFromBattleEvents,
    unsubscribeFromBattleEndEvents,
    sendSurrender,
  };
}
