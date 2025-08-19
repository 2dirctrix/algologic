// ===================================
// WebSocket 메시지 타입 상수
// ===================================

export const MESSAGE_TYPES = {
  // 방 관련 액션 - /v1/rooms/{roomId}/* 경로와 매핑
  JOIN: 'join',
  READY: 'ready',
  LEAVE: 'leave',
};

export default MESSAGE_TYPES;
