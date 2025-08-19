/**
 * ISO 8601 형식의 시간 문자열을 시:분 (HH:mm) 형식으로 변환하는 함수
 * @param {string} isoTime - ISO 8601 형식의 시간 문자열 (예: "2025-08-05T04:13:28.633803400Z")
 * @returns {string} - 시:분 형식 문자열 (예: "04:13")
 */
export function formatTimeToHM(isoTime) {
  if (!isoTime) return '';

  try {
    const date = new Date(isoTime);
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${hours}:${minutes}`;
  } catch (error) {
    console.error('시간 변환 오류:', error);
    return '';
  }
}

/**
 * 두 Unix timestamp 간의 차이를 초로 반환
 * @param {number} startUnix - 시작 시간 (Unix timestamp)
 * @param {number} endUnix - 종료 시간 (Unix timestamp)
 * @returns {number} - 차이 (초 단위)
 */
export function getTimeDifferenceInSeconds(startUnix, endUnix) {
  return Math.floor((endUnix - startUnix) / 1000);
}

/**
 * 현재 시간부터 목표 시간까지 남은 시간을 초로 반환
 * @param {number} currentUnix - 현재 서버 시간 (Unix timestamp)
 * @param {number} deadlineUnix - 종료 시간 (Unix timestamp)
 * @returns {number} - 남은 시간 (초 단위)
 */
export function getRemainingTimeInSeconds(currentUnix, deadlineUnix) {
  const remaining = Math.floor((deadlineUnix - currentUnix) / 1000);
  return Math.max(0, remaining); // 음수 방지
}

/**
 * 페이즈 총 지속시간을 초로 반환
 * @param {number} startUnix - 페이즈 시작 시간
 * @param {number} deadlineUnix - 페이즈 종료 시간
 * @returns {number} - 총 지속시간 (초 단위)
 */
export function getPhaseDurationInSeconds(startUnix, deadlineUnix) {
  return Math.floor((deadlineUnix - startUnix) / 1000);
}

/**
 * 시간을 분:초 형식으로 포맷팅
 * @param {number} totalSeconds - 총 초
 * @returns {string} - "mm:ss" 형식 문자열
 */
export function formatSecondsToMMSS(totalSeconds) {
  const minutes = Math.floor(totalSeconds / 60);
  const seconds = totalSeconds % 60;
  return `${minutes}:${seconds.toString().padStart(2, '0')}`;
}

/**
 * 게임 동기화 데이터로부터 시간 정보 추출
 * @returns {Object} - 처리된 시간 정보
 */
export function processGameSyncTime(currentUnix, startUnix, deadlineUnix) {
  const totalDuration = getPhaseDurationInSeconds(startUnix, deadlineUnix);
  const remainingTime = getRemainingTimeInSeconds(currentUnix, deadlineUnix);

  return { totalDuration, remainingTime };
}
