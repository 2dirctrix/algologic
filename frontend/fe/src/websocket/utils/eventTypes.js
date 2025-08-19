// ===================================
// WebSocket 이벤트 타입 상수
// ===================================

export const EVENT_TYPES = {
  // 방 상태 변경 이벤트
  JOINED: 'JOINED', // 플레이어 입장
  LEFT: 'LEFT', // 플레이어 퇴장
  READY_CHANGED: 'READY_CHANGED', // 준비 상태 변경
  REFRESHED: 'REFRESHED', // 방 정보 전체 갱신
};

export default EVENT_TYPES;
