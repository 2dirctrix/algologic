// 게임 방 참가 관련 에러 메시지
export const ROOM_JOIN_ERROR_MESSAGES = {
  // ROOM_NOT_FOUND (41001)
  41001: {
    title: '방을 찾을 수 없습니다',
    message: '해당 방이 존재하지 않거나 삭제되었습니다.',
    type: 'error'
  },
  
  // DUPLICATE_SESSION (41004)
  41004: {
    title: '중복 접속 감지',
    message: '이미 다른 세션에서 연결중입니다. 기존 연결을 종료하고 다시 시도해주세요.',
    type: 'warning'
  },
  
  // ROOM_CLOSED (41006)
  41006: {
    title: '방이 종료되었습니다',
    message: '이미 종료된 방입니다. 다른 방을 선택해주세요.',
    type: 'error'
  },
  
  // ROOM_NOT_OPEN (41007)
  41007: {
    title: '방이 아직 열리지 않았습니다',
    message: '방이 아직 준비되지 않았습니다. 잠시 후 다시 시도해주세요.',
    type: 'warning'
  },
  
  // ROOM_ALREADY_STARTED (41008)
  41008: {
    title: '게임이 이미 시작되었습니다',
    message: '이미 게임이 진행중인 방입니다. 다른 방을 선택해주세요.',
    type: 'info'
  },
  
  // ROOM_FULL (41009)
  41009: {
    title: '방이 가득 찼습니다',
    message: '참가 가능한 인원을 초과했습니다. 다른 방을 선택하거나 잠시 후 다시 시도해주세요.',
    type: 'warning'
  },
  
  // ROOM_KICKED (41014)
  41014: {
    title: '입장이 제한되었습니다',
    message: '강퇴당한 방에는 입장할 수 없습니다.',
    type: 'error'
  },
  
  // 기본 에러 (예상하지 못한 에러 코드)
  default: {
    title: '방 입장 실패',
    message: '방 입장 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.',
    type: 'error'
  }
};

// 일반적인 네트워크/연결 에러 메시지
export const NETWORK_ERROR_MESSAGES = {
  CONNECTION_FAILED: {
    title: '연결 실패',
    message: '서버에 연결할 수 없습니다. 인터넷 연결을 확인해주세요.',
    type: 'error'
  },
  
  TIMEOUT: {
    title: '요청 시간 초과',
    message: '요청 시간이 초과되었습니다. 다시 시도해주세요.',
    type: 'warning'
  },
  
  WEBSOCKET_ERROR: {
    title: '실시간 연결 오류',
    message: '실시간 연결에 문제가 발생했습니다. 페이지를 새로고침하거나 다시 시도해주세요.',
    type: 'error'
  }
};

// 에러 코드로 메시지 가져오기
export const getRoomJoinErrorMessage = (errorCode) => {
  return ROOM_JOIN_ERROR_MESSAGES[errorCode] || ROOM_JOIN_ERROR_MESSAGES.default;
};

// 에러 객체에서 적절한 메시지 추출
export const getErrorMessage = (error) => {
  // WebSocket 메시지 에러
  if (error.code && ROOM_JOIN_ERROR_MESSAGES[error.code]) {
    return ROOM_JOIN_ERROR_MESSAGES[error.code];
  }
  
  // HTTP 에러
  if (error.response?.data?.code && ROOM_JOIN_ERROR_MESSAGES[error.response.data.code]) {
    return ROOM_JOIN_ERROR_MESSAGES[error.response.data.code];
  }
  
  // 네트워크 에러
  if (error.name === 'NetworkError' || error.code === 'ERR_NETWORK') {
    return NETWORK_ERROR_MESSAGES.CONNECTION_FAILED;
  }
  
  // 타임아웃 에러
  if (error.name === 'TimeoutError' || error.code === 'ECONNABORTED') {
    return NETWORK_ERROR_MESSAGES.TIMEOUT;
  }
  
  // WebSocket 에러
  if (error.type === 'websocket' || error.name === 'WebSocketError') {
    return NETWORK_ERROR_MESSAGES.WEBSOCKET_ERROR;
  }
  
  // 기본 에러
  return ROOM_JOIN_ERROR_MESSAGES.default;
};