// Prefix 정의
const WS_PREFIX = '/ws/v1';
const WS_BASE_URL = `/topic`;
const WS_ROOMS_PREFIX = `${WS_PREFIX}/rooms`;

// 채팅
const WS_CHAT_PREFIX = `${WS_PREFIX}/chat`; // 채팅 전송
const WS_TOPIC_CHAT_PREFIX = `/topic/chat`; // 채팅 수신

// 대기실
const WS_TOPIC_ROOMS_PREFIX = `/topic/rooms`; // 특정 대기실 구독, 방 리스트 새로고침

// 인게임 관련
const WS_TOPIC_INGAME_PREFIX = `/topic/ingame`;

// 서버 시간 및 게임 상태 동기화 SYNC
const WS_SERVER_SYNC_PREFIX = `${WS_PREFIX}/ingame`; // 서버에 동기화 요청
const WS_TOPIC_SERVER_SYNC_PREFIX = `/user/queue/ingame`; // 소켓 개인 싱크용 응답
const WS_JUDGE_RESULT_PREFIX = `/user/queue/judge-result`;

export const WS_ENDPOINTS = {
  MAIN: {
    RANKERS: `${WS_BASE_URL}/rankers`,
  },
  // 방 관련 WebSocket 엔드포인트
  ROOM: {
    // 방 액션 메시지 전송 (join, ready, leave)
    ACTION: (roomId, action) => `${WS_ROOMS_PREFIX}/${roomId}/${action}`, // /ws/v1/rooms/{roomId}/{action}

    // 강퇴 기능
    KICK: roomId => `${WS_ROOMS_PREFIX}/${roomId}/kick`, // /ws/v1/rooms/{roomId}/kick

    // 방 토픽 구독
    TOPIC: roomId => `${WS_TOPIC_ROOMS_PREFIX}/${roomId}`, // /topic/rooms/{roomId}

    // 개인 소켓 구독 포인트 (강퇴 알림 등)
    PERSONAL_QUEUE: roomId => `/user/queue/rooms/${roomId}`, // /user/queue/rooms/{roomId}

    // 방 목록 갱신 알림 토픽
    REFRESH_TOPIC: `${WS_TOPIC_ROOMS_PREFIX}/refresh`, // /topic/rooms/refresh
  },

  // 채팅 관련 WebSocket 엔드포인트
  CHAT: {
    // 전체 채팅 전송
    MAIN_SEND: `${WS_CHAT_PREFIX}/main`, // /ws/v1/chat/main

    // 전체 채팅 구독
    MAIN_SUBSCRIBE: `${WS_TOPIC_CHAT_PREFIX}/main`, // /topic/chat/main

    // 인게임 채팅 전송
    ROOM_SEND: roomId => `${WS_CHAT_PREFIX}/rooms/${roomId}`, // /ws/v1/chat/rooms/{roomId}

    // 인게임 채팅 구독
    ROOM_SUBSCRIBE: roomId => `${WS_TOPIC_CHAT_PREFIX}/rooms/${roomId}`, // /topic/chat/rooms/{roomId}
  },

  GAME: {
    // 게임 동기화 요청
    SYNC_REQUEST: roomId => `${WS_SERVER_SYNC_PREFIX}/${roomId}/sync`,

    // 게임 동기화 구독 (개인용)
    SYNC_SUBSCRIBE: roomId => `${WS_TOPIC_SERVER_SYNC_PREFIX}/${roomId}/sync`,

    // 밴 픽 구매 개인 이벤트
    BAN_PICK_BUY_SUBSCRIBE: roomId => `${WS_TOPIC_SERVER_SYNC_PREFIX}/${roomId}/choice`,

    // 밴 픽 구매 종료 이벤트
    BAN_PICK_BUY_END_SUBSCRIBE: roomId => `${WS_TOPIC_INGAME_PREFIX}/${roomId}/choice`,

    // 배틀 단계 이벤트
    BATTLE_SUBSCRIBE: roomId => `${WS_TOPIC_SERVER_SYNC_PREFIX}/${roomId}/battle`,

    // 배틀 종료 이벤트
    BATTLE_END_SUBSCRIBE: roomId => `${WS_TOPIC_INGAME_PREFIX}/${roomId}/battle`,

    // 아이템 사용 요청
    ITEM_SENT_PUB: roomId => `${WS_SERVER_SYNC_PREFIX}/${roomId}/item-use`,

    // 스펠 사용 요청
    SPELL_USE_PUB: roomId => `${WS_SERVER_SYNC_PREFIX}/${roomId}/spell-use`,

    // 서렌 요청
    SURRENDER_REQUEST_PUB: roomId => `${WS_SERVER_SYNC_PREFIX}/${roomId}/surrender`,
  },

  PROBLEM: {
    // 제출한 코드의 채점 결과 수신을 위한 구독
    // JUDGE_RESULTS: memberId => `${WS_JUDGE_RESULT_PREFIX}/${memberId}`,
    JUDGE_RESULTS: `${WS_JUDGE_RESULT_PREFIX}`,

    ALGO_RATE_SUB: `/topic/problems/category/rank`,
  },
};
