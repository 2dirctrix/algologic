import { formatTimeToHM } from '@/websocket/utils/timeUtils.js';

/**
 * 채팅 메시지 핸들러
 * 소켓으로 받은 채팅 메시지를 처리하여 Vue 컴포넌트에서 사용할 수 있는 형태로 변환
 */

/**
 * 채팅 메시지 핸들러
 * @param {Object} message - 소켓에서 받은 메시지 객체
 * @param {string} message.nickname - 발신자 닉네임
 * @param {string} message.content - 메시지 내용
 * @param {string} message.isoTime - ISO 형식 시간 문자열
 * @returns {Object} - 개별 값으로 분리된 메시지 객체
 */
export function handleChatMessage(message) {
  if (!message || typeof message !== 'object') {
    console.error('유효하지 않은 채팅 메시지:', message);
    return null;
  }

  const { nickname, content, isoTime } = message;

  if (!nickname || !content || !isoTime) {
    console.error('필수 메시지 필드가 누락되었습니다:', message);
    return null;
  }

  return {
    id: `${nickname}-${isoTime}-${Date.now()}`, // 고유 ID 생성
    nickname: nickname,
    content: content,
    time: formatTimeToHM(isoTime)
  };
}

/**
 * 인게임 채팅 메시지 핸들러 (나중에 구현할 예정)
 * @param {Object} message - 소켓에서 받은 메시지 객체
 * @returns {Object} - 개별 값으로 분리된 메시지 객체
 */
export function handleRoomChatMessage(message) {
  // 현재는 전체 채팅과 동일한 로직 사용
  return handleChatMessage(message);
}
