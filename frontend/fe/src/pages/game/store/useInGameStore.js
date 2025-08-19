// 인게임 내에서 필요한 방 정보만 관리하도록 분리

import { defineStore } from 'pinia';
import { useRoomWebSocketStore } from '@/stores/roomWebsocket.js';
import { useAuthStore } from '@/stores/auth/auth.js';
import { getScoreToTier } from '@/constants/tiers.js';
import { processGameSyncTime } from '@/websocket/utils/timeUtils.js';

export const useInGameStore = defineStore('waitingRoom', {
  state: () => ({
    // persist될 데이터: 현재 접속 중인 방 ID
    currentRoomId: null,
    currentPlayerId: null,
    phase: null,

    // hydration 완료 플래그
    hydrated: false,

    // 게임 상태 관리
    gameStatus: 'waiting', // 'waiting', 'banPickBuy', 'play'

    // 런타임 데이터 (웹소켓 구독으로부터 실시간 갱신)
    roomData: {
      id: null,
      title: '',
      gameType: 'normal',
      maxPlayers: 4,
      language: 'Python',
      timeLimit: 60,
      host: { name: '' },
    },

    // 방 참가자
    players: [],

    // 나 (본인)
    currentUser: {
      isHost: false,
      isReady: false,
    },

    isConnected: false,

    // 밴/픽/구매 페이지 동기화 상태
    gameSync: {
      phase: null, // 'BAN_CHOICE', 'PICK_CHOICE', 'PURCHASE'
      totalDuration: 0, // 페이즈 총 지속시간 (초)
      remainingTime: 0, // 남은 시간 (초)
      players: [], // 플레이어 선택 완료 상태
    },

    // 밴픽구매 페이지 + 배틀 페이지 시간 동기화용
    localTimer: {
      intervalId: null,
    },

    // 카운트다운 + 페이즈 타이머 + offset 적용 타이머
    timerState: {
      countdownBeforeStart: 0, // 게임 시작 전 카운트다운 시간 (ms)
      phaseRemainingTime: 0, // 현재 페이즈 남은 시간 (ms)
      isCountdownActive: false, // 카운트다운 중인지 여부
      isPhaseTimerActive: false, // 페이즈 타이머 활성화 여부
    },

    // 밴픽구매 결과 저장
    banPickBuyResults: {
      bannedAlgorithm: null, // 밴된 알고리즘 ID (하나만)
      pickedAlgorithm: null, // 픽된 알고리즘 ID (하나만)
      pickedProblem: 0,
      purchasedItems: [], // 구매한 아이템 리스트
      purchasedSpells: [], // 구매한 스펠 리스트
      activatedItems: [], // 활성화된 아이템들
      activatedSpells: [], // 활성화된 스펠들
      surrender: false, // 항복 상태
    },

    // 아이템 종류 목록
    itemList: [],

    // 스펠 종류 목록
    spellList: [],

    // 알고리즘 카테고리 목록 (밴픽구매 페이지 렌더링 용)
    algorithmCategories: [],

    // 채점 결과 메시지
    judgeResultMessage: null,

    // 게임 결과 조회 PK
    gameResultId: null,

    // 게임 결과 데이터
    gameResult: null,

    // 코드 제출 상태
    isSubmittingCode: false,

    // 현재 작성 중인 코드
    currentCode: '',

    // 구매 완료 후 퇴장 애니메이션 상태
    shouldStartExitAnimation: false,
  }),

  getters: {
    // TODO : 최종 배포 시 시작 인원 제한 주석 해제 필요
    // 게임 시작 가능 여부
    canStartGame: state => {
      return (
        state.currentUser.isHost &&
        // TASK : 준비완료 상태 검증 로직 임시 비활성화
        // state.players.length > 1 &&
        state.players.every(player => player.readyStatus === 'ready' || player.nickname === state.roomData.host.name) &&
        state.players.every(player => player.connected === true)
      );
    },

    // 채점 결과 정보 getter
    judgeResultInfo: state => {
      if (!state.judgeResultMessage) return null;

      return {
        isSolved: state.judgeResultMessage.isSolved || false,
        maxRunningTime: state.judgeResultMessage.maxRunningTime || null,
        maxMemoryUsage: state.judgeResultMessage.maxMemoryUsage || null,
        message: state.judgeResultMessage.message || '',
      };
    },
  },

  actions: {
    /**
     * 방 초기화 - WebSocket 연결만 처리, 방 정보는 구독 메시지에서 받아옴
     * @param {string} roomId
     */
    async initializeRoom(roomId) {
      this.$patch({ currentRoomId: roomId }); // persist될 방 ID 저장
      this.roomData.id = roomId;

      const roomWebSocketStore = useRoomWebSocketStore();

      try {
        // WebSocket 구독 시작 - join 요청의 응답으로 방 정보를 받아옴
        roomWebSocketStore.connectToRoom(roomId, this.handleWebSocketMessage);
        this.isConnected = true;
      } catch (error) {
        console.error('[WAITING ROOM STORE] 방 초기화 중 오류:', error);
        throw error;
      }
    },

    // 웹소켓 연결 해제 및 상태 초기화
    disconnect() {
      const roomWebSocketStore = useRoomWebSocketStore();
      roomWebSocketStore.disconnectFromRoom();
      this.reset();
    },

    // 상태값 초기화
    reset() {
      // persist된 데이터들도 함께 초기화
      this.$patch({
        currentRoomId: null,
        currentPlayerId: null,
        gameStatus: 'waiting',
        players: [],
        phase: null,
        gameSync: {
          phase: null,
          totalDuration: 0,
          remainingTime: 0,
          players: [],
        },
        timerState: {
          countdownBeforeStart: 0,
          phaseRemainingTime: 0,
          isCountdownActive: false,
          isPhaseTimerActive: false,
        },
        banPickBuyResults: {
          bannedAlgorithm: null,
          pickedAlgorithm: null,
          pickedProblem: 0,
          purchasedItems: [],
          purchasedSpells: [],
          activatedItems: [],
          activatedSpells: [],
          surrender: false,
        },
        algorithmCategories: [],
        roomData: {
          id: null,
          title: '',
          gameType: 'normal',
          maxPlayers: 4,
          language: 'Python',
          timeLimit: 60,
          host: { name: '' },
        },
        currentUser: {
          isHost: false,
          isReady: false,
        },
        isConnected: false,
        currentCode: '',
      });
    },

    /**
     * 서버로부터 오는 웹소켓 메시지 처리
     * @param {object} message
     */
    handleWebSocketMessage(message) {
      if (message.type === 'EVENT' && message.code === 200 && message.data?.payload) {
        const payload = message.data.payload;

        switch (message.data.event) {
          case 'JOINED':
            this.handlePlayerJoined(payload);
            break;
          case 'LEFT':
            this.handlePlayerLeft(payload);
            break;
          case 'READY_CHANGED':
            this.handleReadyChanged(payload);
            break;
          case 'ENTERED':
            this.handleGameEntered(payload);
            break;
          case 'DISCONNECTED':
            this.handlePlayerDisconnected(payload);
            break;
          default:
            if (payload.roomId && payload.participants) {
              this.updatePlayersFromServer(payload);
            }
            break;
        }
      } else {
        // TASK : 서버 측 예외 메세지 전달 필요
        throw new Error('존재하지 않는 방 입니다.');
      }
    },

    handlePlayerJoined(payload) {
      this.updatePlayersFromServer(payload);
    },

    handlePlayerLeft(payload) {
      this.updatePlayersFromServer(payload);
    },

    handleReadyChanged(payload) {
      this.updatePlayersFromServer(payload);
    },
    handlePlayerDisconnected(payload) {
      this.updatePlayersFromServer(payload);
    },

    handleGameEntered() {
      this.gameStatus = 'banPickBuy';
    },

    handleGameStarted() {
      this.gameStatus = 'play';
    },

    /**
     * 구매 완료 후 퇴장 애니메이션 시작 신호
     */
    startBanPickBuyExitAnimation() {
      this.shouldStartExitAnimation = true;
    },

    /**
     * 퇴장 애니메이션 완료 후 실제 게임 시작
     */
    completeBanPickBuyExitAnimation() {
      this.shouldStartExitAnimation = false;
      this.gameStatus = 'play';
    },

    /**
     * 서버 payload 기준으로 방 정보 및 플레이어 배열 업데이트
     * @param {object} payload - 서버에서 받은 방 정보
     */
    updatePlayersFromServer(payload) {
      const authStore = useAuthStore();
      const currentUserNickname = authStore.nickname;

      // 현재 사용자가 방장인지 확인
      this.currentUser.isHost = payload.hostNickname === currentUserNickname;

      // 방 정보 업데이트
      if (payload.roomId) this.roomData.id = payload.roomId;
      if (payload.roomName) this.roomData.title = payload.roomName;
      if (payload.maxSize) this.roomData.maxPlayers = payload.maxSize;
      if (payload.hostNickname) this.roomData.host.name = payload.hostNickname;
      if (payload.programmingLanguage) this.roomData.language = payload.programmingLanguage;
      if (payload.timeLimit) this.roomData.timeLimit = payload.timeLimit;
      if (payload.gameType) {
        this.roomData.gameType = payload.gameType === 'RANKED' ? 'rank' : 'normal';
      }

      // 플레이어 정보 업데이트
      if (payload.participants && Array.isArray(payload.participants)) {
        this.players = payload.participants.map(participant => {
          const tierInfo = getScoreToTier(participant.score || 0);

          return {
            id: participant.memberId,
            nickname: participant.nickname,
            tier: tierInfo.name,
            division: tierInfo.division,
            rating: participant.score || 0,
            readyStatus: participant.ready ? 'ready' : 'waiting',
            connected: participant.connected,
          };
        });

        // 현재 사용자의 레디 상태 업데이트
        const currentPlayer = payload.participants.find(p => p.nickname === currentUserNickname);
        this.currentUser.isReady = currentPlayer ? currentPlayer.ready : false;
      }
    },

    // 레디 상태 토글
    async toggleReady() {
      const roomWebSocketStore = useRoomWebSocketStore();
      const authStore = useAuthStore();

      try {
        const newReadyState = !this.currentUser.isReady;
        this.currentUser.isReady = newReadyState;

        const currentPlayer = this.players.find(p => p.nickname === authStore.nickname);
        if (currentPlayer) {
          currentPlayer.readyStatus = newReadyState ? 'ready' : 'waiting';
        }

        roomWebSocketStore.changeReadyStatus(newReadyState);
      } catch (error) {
        // 실패 시 상태 롤백
        this.currentUser.isReady = !this.currentUser.isReady;
        const currentPlayer = this.players.find(p => p.nickname === authStore.nickname);
        if (currentPlayer) {
          currentPlayer.readyStatus = this.currentUser.isReady ? 'ready' : 'waiting';
        }
        console.error('[WAITING ROOM STORE] 레디 토글 실패:', error);
        throw error;
      }
    },

    /**
     * 로컬스토리지에서 방 상태 확인
     * @returns {boolean} 활성화된 방이 있는지 여부
     */
    hasActiveRoom() {
      return this.currentRoomId !== null;
    },

    /**
     * 저장된 방으로 재접속 시도
     * @returns {Promise<boolean>} 재접속 성공 여부
     */
    async reconnectToSavedRoom() {
      if (!this.hasActiveRoom()) {
        return false;
      }

      const roomId = this.currentRoomId;
      try {
        const roomWebSocketStore = useRoomWebSocketStore();

        roomWebSocketStore.connectToRoom(roomId, this.handleWebSocketMessage);

        // 런타임 데이터 설정
        this.roomData.id = roomId;
        this.isConnected = true;

        return true;
      } catch (error) {
        console.error('[WAITING ROOM STORE] 방 재접속 실패:', error);
        this.reset(); // 재접속 실패 시 상태 초기화
        return false;
      }
    },

    /**
     * 밴픽 단계에서 현재 밴픽 현황 업데이트
     * */
    updateBanPickStatus(payload) {
      this.gameSync = {
        ...this.gameSync,
        phase: payload.phase,
        players: payload.players,
      };
    },

    /**
     * 구매 단계에서 현재 구매 현황 업데이트
     * */
    updateBuyStatus(payload) {
      this.banPickBuyResults.purchasedItems = payload.me.purchasedItems || [];
      this.banPickBuyResults.purchasedSpells = payload.me.purchasedSpells || [];
      this.gameSync = {
        ...this.gameSync,
        phase: payload.phase,
        players: payload.players,
      };
    },

    /**
     * 밴 결과 업데이트
     * */
    updateBanResult(payload) {
      this.banPickBuyResults.bannedAlgorithm = payload.bannedProblemCategoryId;
    },

    /**
     * 밴 결과 업데이트
     * */
    updatePickResult(payload) {
      this.banPickBuyResults.pickedProblem = payload.problemId;
    },

    /**
     * 게임 동기화 데이터 업데이트
     * @param {Object} payload - SYNC 응답의 payload
     */
    updateTimeSync(payload) {
      // timeUtils를 사용해 시간 정보 처리
      const timeInfo = processGameSyncTime(payload.currentUnix, payload.startUnix, payload.deadlineUnix);

      // 1. 서버와 클라이언트 시간의 offset 계산
      const clientCurrentTime = Date.now();
      const offset = payload.currentUnix - clientCurrentTime;

      // 2. offset을 고려한 시간 계산
      const leftBeforeStart = payload.startUnix - clientCurrentTime - offset;
      const phaseTimeLeft = payload.deadlineUnix - clientCurrentTime - offset;

      // 타이머 상태 업데이트
      if (leftBeforeStart > 0) {
        // 게임 시작 전 카운트다운 상태
        this.timerState.countdownBeforeStart = leftBeforeStart;
        // 페이즈 타이머는 카운트다운 시간을 제외한 시간으로 설정
        this.timerState.phaseRemainingTime = Math.max(0, phaseTimeLeft - leftBeforeStart);
        this.timerState.isCountdownActive = true;
        this.timerState.isPhaseTimerActive = false;
      } else {
        // 페이즈 타이머 상태
        this.timerState.countdownBeforeStart = 0;
        this.timerState.phaseRemainingTime = phaseTimeLeft > 0 ? phaseTimeLeft : 0;
        this.timerState.isCountdownActive = false;
        this.timerState.isPhaseTimerActive = phaseTimeLeft > 0;
      }

      this.gameSync = {
        phase: payload.phase,
        totalDuration: timeInfo.totalDuration,
        remainingTime: timeInfo.remainingTime,
        players: payload.players,
      };

      if (payload.me && payload.me.playerId) {
        this.currentPlayerId = payload.me.playerId;
      }

      // 로컬 타이머 시작
      this.startLocalTimer();
    },

    /**
     * 로컬 타이머 시작 - 카운트다운과 페이즈 타이머를 순차적으로 처리
     */
    startLocalTimer() {
      // 기존 타이머 정리
      this.stopLocalTimer();

      this.localTimer.intervalId = setInterval(() => {
        if (this.timerState.isCountdownActive && this.timerState.countdownBeforeStart > 0) {
          this.timerState.countdownBeforeStart -= 1000;

          if (this.timerState.countdownBeforeStart <= 0) {
            this.timerState.isCountdownActive = false;
            this.timerState.isPhaseTimerActive = this.timerState.phaseRemainingTime > 0;
          }
        } else if (this.timerState.isPhaseTimerActive && this.timerState.phaseRemainingTime > 0) {
          this.timerState.phaseRemainingTime -= 1000;

          if (this.timerState.phaseRemainingTime <= 0) {
            this.timerState.isPhaseTimerActive = false;
          }
        }

        if (this.gameSync.remainingTime > 0) {
          this.gameSync.remainingTime -= 1;
        }

        if (!this.timerState.isCountdownActive && !this.timerState.isPhaseTimerActive && this.gameSync.remainingTime <= 0) {
          this.stopLocalTimer();
        }
      }, 1000);
    },

    /**
     * 로컬 타이머 중지
     */
    stopLocalTimer() {
      if (this.localTimer.intervalId) {
        clearInterval(this.localTimer.intervalId);
        this.localTimer.intervalId = null;
      }
    },

    /**
     * 알고리즘 밴 결과 저장
     * @param {string|number} algorithmId - 밴된 알고리즘 ID
     */
    setBannedAlgorithm(algorithmId) {
      this.banPickBuyResults.bannedAlgorithm = algorithmId;
    },

    /**
     * 알고리즘 픽 결과 저장
     * @param {string|number} algorithmId - 픽된 알고리즘 ID
     */
    setPickedAlgorithm(algorithmId) {
      this.banPickBuyResults.pickedAlgorithm = algorithmId;
    },

    /**
     * 아이템 구매 결과 저장
     * @param {Array} items - 구매한 아이템 리스트
     */
    setPurchasedItems(items) {
      this.banPickBuyResults.purchasedItems = [...items];
    },

    /**
     * 스펠 구매 결과 저장
     * @param {Array} spells - 구매한 스펠 리스트
     */
    setPurchasedSpells(spells) {
      this.banPickBuyResults.purchasedSpells = [...spells];
    },

    /**
     * 알고리즘 카테고리 목록 저장
     * @param {Array} categories - 카테고리 목록
     */
    setAlgorithmCategories(categories) {
      this.algorithmCategories = [...categories];
    },

    /**
     * 채점 결과 메시지 업데이트
     * @param {Object} message - 채점 결과 메시지
     */
    updateJudgeResult(message) {
      this.judgeResultMessage = message;
      this.isSubmittingCode = false;
    },

    /**
     * 코드 제출 상태 설정
     * @param {boolean} isSubmitting - 제출 중 여부
     */
    setCodeSubmissionState(isSubmitting) {
      this.isSubmittingCode = isSubmitting;
    },

    /**
     * 현재 코드 업데이트
     * @param {string} code - 작성 중인 코드
     */
    updateCurrentCode(code) {
      this.currentCode = code;
    },

    /**
     * 게임 결과 데이터 업데이트 및 게임 상태를 result로 변경
     * @param {Object} gameResultData - 게임 결과 데이터
     */
    updateGameResult(gameResultData) {
      // 게임 종료 시 모든 아이템/스펠 효과 정리
      this.cleanupAllEffects();

      this.gameResult = gameResultData;
      this.gameStatus = 'result';
    },

    /**
     * 게임 종료 시 모든 아이템/스펠 효과 정리
     */
    cleanupAllEffects() {
      // 아이템 효과 정리
      if (window.cleanupVirtualKeyboard) {
        window.cleanupVirtualKeyboard();
        window.cleanupVirtualKeyboard = null;
      }

      if (window.cleanupInputDelay) {
        window.cleanupInputDelay();
        window.cleanupInputDelay = null;
      }

      // 보호막 효과 정리
      if (window.cleanupShieldEffect) {
        window.cleanupShieldEffect();
        window.cleanupShieldEffect = null;
      }

      // 화면 흔들림 효과 제거
      const body = document.body;
      if (body.classList.contains('shake-effect')) {
        body.classList.remove('shake-effect');
      }

      // 어둡게 효과 오버레이 제거
      const darkenOverlays = document.querySelectorAll('[style*="rgba(0, 0, 0, 0.95)"]');
      darkenOverlays.forEach(overlay => {
        if (document.body.contains(overlay)) {
          document.body.removeChild(overlay);
        }
      });

      // 월식 이미지 오버레이 제거
      const imageOverlays = document.querySelectorAll('[style*="item-4.png"], [style*="pulseBrightness"]');
      imageOverlays.forEach(overlay => {
        if (document.body.contains(overlay)) {
          document.body.removeChild(overlay);
        }
      });

      // 점화 효과의 화염 오버레이 제거
      const fireOverlays = document.querySelectorAll('.fire-overlay-ignition');
      fireOverlays.forEach(overlay => {
        if (document.body.contains(overlay)) {
          document.body.removeChild(overlay);
        }
      });

      // 입력 지연의 녹색 네온 테두리 제거
      const glowBorders = document.querySelectorAll('.input-delay-glow-border');
      glowBorders.forEach(border => {
        if (document.body.contains(border)) {
          document.body.removeChild(border);
        }
      });

      // 보호막의 금색 네온 테두리 제거
      const goldenBorders = document.querySelectorAll('.shield-golden-border');
      goldenBorders.forEach(border => {
        if (document.body.contains(border)) {
          document.body.removeChild(border);
        }
      });

      // 스파이 팝업 제거
      const spyPopups = document.querySelectorAll('.webrtc-spy-popup');
      spyPopups.forEach(popup => {
        if (document.body.contains(popup)) {
          document.body.removeChild(popup);
        }
      });

      // 모든 이펙트 관련 스타일 태그 제거
      const effectStyleTags = document.querySelectorAll(
        'style[id*="effect"], style[id*="glow"], style[id*="golden"], style[id*="popup"], style[id*="shield"]',
      );
      effectStyleTags.forEach(styleTag => {
        if (document.head.contains(styleTag)) {
          document.head.removeChild(styleTag);
        }
      });

      // pulseBrightness 애니메이션 스타일 태그 제거
      const pulseStyleTags = document.querySelectorAll('style');
      pulseStyleTags.forEach(styleTag => {
        if (styleTag.innerHTML && styleTag.innerHTML.includes('pulseBrightness')) {
          if (document.head.contains(styleTag)) {
            document.head.removeChild(styleTag);
          }
        }
      });

      // 전역 상태 변수 초기화
      window.virtualKeyboardActive = false;
      window.physicalKeyboardBlocked = false;
      window.inputDelayActive = false;
      window.shieldActive = false;
      window.shieldItemsBlocked = 0;

      // 정리 함수들 초기화
      window.removeShieldEffects = null;
      window.currentSpyTarget = null;

      // 스토어의 활성화된 아이템/스펠 배열 초기화
      this.banPickBuyResults.activatedItems = [];
      this.banPickBuyResults.activatedSpells = [];
    },

    /**
     * 이전 방 관련 정보 강제 삭제
     * */
    forceCleanLocalStorage() {
      this.$reset();
      localStorage.removeItem('waiting-room-store');
    },

    /**
     * 방 나가기 - API 요청 후 상태 초기화
     */
    async leaveWaitingRoom() {
      if (!this.currentRoomId) {
        console.warn('[IN-GAME STORE] 현재 방 ID가 없습니다.');
        return;
      }

      try {
        // 1. API 엔드포인트로 POST 요청
        const { apiClient } = await import('@/api/utils/index.js');
        const { API_ENDPOINTS } = await import('@/api/constants/endpoints.js');

        await apiClient.post(API_ENDPOINTS.ROOM.LEAVE(this.currentRoomId));

        // 2. 200 OK인 경우 정상적으로 연결 해제 및 나가기 처리
        const roomWebSocketStore = useRoomWebSocketStore();
        roomWebSocketStore.disconnectFromRoom();
        this.stopLocalTimer();
        this.reset();
      } catch (error) {
        console.error('[IN-GAME STORE] 방 나가기 실패:', error);

        // 3. 에러 처리
        if (error.response?.status === 400) {
          const errorData = error.response.data;

          if (errorData === 'ROOM_NOT_FOUND') {
            // roomId에 해당하는 방을 찾을 수 없는 경우 /main 페이지 이동
            console.warn('[IN-GAME STORE] 방을 찾을 수 없습니다. 메인 페이지로 이동합니다.');
            const { default: router } = await import('@/router/index.js');
            await router.push('/main');

            // 로컬 상태 정리
            const roomWebSocketStore = useRoomWebSocketStore();
            roomWebSocketStore.disconnectFromRoom();
            this.stopLocalTimer();
            this.reset();
          } else if (errorData === 'ROOM_ALREADY_STARTED') {
            // 이미 게임이 시작된 경우 아무 동작도 하지 않음
            console.warn('[IN-GAME STORE] 이미 게임이 시작되었습니다.');
            return;
          }
        } else {
          // 기타 에러의 경우 로컬 상태는 정리
          const roomWebSocketStore = useRoomWebSocketStore();
          roomWebSocketStore.disconnectFromRoom();
          this.stopLocalTimer();
          this.reset();
        }

        throw error;
      }
    },
  },

  persist: {
    key: 'waiting-room-store',
    storage: localStorage,
    pick: [
      'currentRoomId',
      'currentPlayerId',
      'gameStatus',
      'players',
      'phase',
      'gameSync',
      'timerState',
      'banPickBuyResults',
      'algorithmCategories',
      'roomData',
      'currentCode',
    ],
    afterHydrate: ({ store }) => {
      store.hydrated = true;
    },
  },
});
