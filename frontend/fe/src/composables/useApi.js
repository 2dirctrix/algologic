/**
 * API 호출 헬퍼 컴포저블
 * API 호출 시 로딩 상태, 에러 처리를 위한 공통 로직 제공
 */

import { ref } from 'vue';

export const useApi = () => {
  const isLoading = ref(false);
  const error = ref(null);

  /**
   * API 호출 래퍼 함수
   * @param {Function} apiCall - 실행할 API 함수
   * @param {Object} options - 옵션
   * @param {boolean} options.showError - 에러 알림 표시 여부
   */
  const execute = async (apiCall, options = {}) => {
    const { showError = true } = options;

    try {
      isLoading.value = true;
      error.value = null;

      return await apiCall();
    } catch (err) {
      error.value = err.message || '알 수 없는 오류가 발생했습니다.';
      console.error('API 호출 오류:', err);

      if (showError) {
        alert(error.value);
      }

      throw err;
    } finally {
      isLoading.value = false;
    }
  };

  /**
   * 에러 상태 초기화
   */
  const clearError = () => {
    error.value = null;
  };

  return {
    isLoading,
    error,
    execute,
    clearError,
  };
};
