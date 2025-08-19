import { ref } from 'vue';
import { apiClient } from '@/api/utils/index.js';
import { API_ENDPOINTS } from '@/api/constants/endpoints.js';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';

/**
 * 문제 관련 API 및 상태 관리를 위한 Composable
 */
export function useProblem() {
  // 반응형 상태
  const problem = ref(null);
  const loading = ref(false);
  const error = ref(null);

  /**
   * 알고리즘 카테고리 전체 조회
   * @returns {Promise<Array>} 카테고리 목록
   */
  const fetchAlgorithmCategories = async () => {
    const inGameStore = useInGameStore();
    loading.value = true;
    error.value = null;

    try {
      const response = await apiClient.get(API_ENDPOINTS.PROBLEM.CATEGORY);

      if (response.data && response.data.status === 200) {
        const categories = response.data.data.category;
        // useInGameStore에 저장
        inGameStore.setAlgorithmCategories(categories);

        return categories;
      } else {
        throw new Error(response.data?.message || '카테고리 조회 실패');
      }
    } catch (err) {
      console.error('카테고리 조회 에러:', err);
      error.value = err.response?.data?.message || err.message || '카테고리 조회에 실패했습니다.';
      return [];
    } finally {
      loading.value = false;
    }
  };

  /**
   * 문제 조회
   * @param {number} problemId - 문제 ID
   * @returns {Promise<Object>} 문제 데이터
   */
  const fetchProblem = async problemId => {
    if (!problemId) {
      error.value = '문제 ID가 필요합니다.';
      return null;
    }

    loading.value = true;
    error.value = null;

    try {
      const response = await apiClient.get(API_ENDPOINTS.PROBLEM.GET(problemId));

      if (response.data && response.data.status === 200) {
        problem.value = response.data.data;
        return problem.value;
      } else {
        throw new Error(response.data?.message || '문제 조회 실패');
      }
    } catch (err) {
      console.error('문제 조회 에러:', err);
      error.value = err.response?.data?.message || err.message || '문제 조회에 실패했습니다.';
      problem.value = null;
      return null;
    } finally {
      loading.value = false;
    }
  };

  /**
   * 문제 데이터 초기화
   */
  const resetProblem = () => {
    problem.value = null;
    error.value = null;
    loading.value = false;
  };

  /**
   * 문제의 예제 입출력 포맷팅
   * @param {Array} examples - 예제 배열
   * @returns {Array} 포맷된 예제 배열
   */
  const formatExamples = (examples = []) => {
    return examples.map((example, index) => ({
      id: index + 1,
      input: example.input || '',
      output: example.output || '',
    }));
  };

  /**
   * 문제 난이도 레벨을 한글로 변환
   * @param {string} level - 영문 레벨
   * @returns {string} 한글 레벨
   */
  const getLevelLabel = level => {
    const levelMap = {
      BRONZE: '브론즈',
      SILVER: '실버',
      GOLD: '골드',
      EMERALD: '에메랄드',
      DIAMOND: '다이아몬드',
      RUBY: '루비',
    };
    return levelMap[level?.toUpperCase()] || level || '알 수 없음';
  };

  /**
   * 문제 카테고리 포맷팅
   * @param {Array} categories - 카테고리 배열
   * @returns {string} 포맷된 카테고리 문자열
   */
  const formatCategories = (categories = []) => {
    return categories.length > 0 ? categories.join(', ') : '미분류';
  };

  /**
   * 알고리즘 밴 요청
   * @param {number} problemCategoryId - 밴할 문제 카테고리 ID
   * @returns {Promise<boolean>} 성공 여부
   */
  const banAlgorithm = async problemCategoryId => {
    loading.value = true;
    error.value = null;

    try {
      const response = await apiClient.post(API_ENDPOINTS.ROOM.BAN, {
        problemCategoryId,
      });

      if (response.data && response.data.status === 200) {
        return true;
      } else {
        throw new Error(response.data?.message || '알고리즘 밴 실패');
      }
    } catch (err) {
      console.error('알고리즘 밴 에러:', err);
      error.value = err.response?.data?.message || err.message || '알고리즘 밴에 실패했습니다.';
      return false;
    } finally {
      loading.value = false;
    }
  };

  /**
   * 알고리즘 픽 요청
   * @param {number} problemCategoryId - 픽할 문제 카테고리 ID
   * @returns {Promise<boolean>} 성공 여부
   */
  const pickAlgorithm = async problemCategoryId => {
    loading.value = true;
    error.value = null;

    try {
      const response = await apiClient.post(API_ENDPOINTS.ROOM.PICK, {
        problemCategoryId,
      });

      if (response.data && response.data.status === 200) {
        return true;
      } else {
        throw new Error(response.data?.message || '알고리즘 픽 실패');
      }
    } catch (err) {
      console.error('알고리즘 픽 에러:', err);
      error.value = err.response?.data?.message || err.message || '알고리즘 픽에 실패했습니다.';
      return false;
    } finally {
      loading.value = false;
    }
  };

  /**
   * 문제 채점 요청
   * @param {number} problemId - 문제 ID
   * @param {string} language - 프로그래밍 언어 (JAVA, PYTHON 등)
   * @param {string} sourceCode - 소스 코드
   * @returns {Promise<number>} 테스트케이스 개수
   */
  const submitProblem = async sourceCode => {
    const inGameStore = useInGameStore();

    inGameStore.judgeResultMessage = null;

    const problemId = inGameStore.banPickBuyResults.pickedProblem;
    const playerId = inGameStore.currentPlayerId;
    const language = inGameStore.roomData.language.toUpperCase();

    if (!problemId) {
      error.value = '문제 ID가 필요합니다.';
      return null;
    }

    if (!language) {
      error.value = '프로그래밍 언어가 필요합니다.';
      return null;
    }

    loading.value = true;
    error.value = null;

    try {
      const response = await apiClient.post(API_ENDPOINTS.PROBLEM.SUBMIT(problemId), {
        playerId: playerId,
        programmingLanguage: language,
        sourceCode: sourceCode,
      });

      if (response.data && response.data.status === 200) {
        return response.data.data.count;
      } else {
        throw new Error(response.data?.message || '채점 요청 실패');
      }
    } catch (err) {
      console.error('채점 요청 에러:', err);
      error.value = err.response?.data?.message || err.message || '채점 요청에 실패했습니다.';
      return null;
    } finally {
      loading.value = false;
    }
  };

  return {
    // 상태
    problem,
    loading,
    error,

    // 메서드
    fetchAlgorithmCategories,
    banAlgorithm,
    pickAlgorithm,
    fetchProblem,
    resetProblem,
    formatExamples,
    getLevelLabel,
    formatCategories,
    submitProblem,
  };
}
