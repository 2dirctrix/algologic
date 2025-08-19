/**
 * 회원 관련 API 관리 컴포저블
 * 랭커 정보 조회 등 회원 관련 기능 제공
 */

import { ref } from 'vue';
import { apiClient } from '@/api/utils/index.js';
import { API_ENDPOINTS } from '@/api/constants/endpoints.js';
import { getScoreToTier } from '@/constants/tiers.js';

export const useMember = () => {
  const rankers = ref([]);
  const isLoading = ref(false);

  /**
   * 상위 플레이어 10건 정보 조회
   * @returns {Promise<Array>} 랭커 데이터 배열
   */
  const fetchRankers = async () => {
    try {
      isLoading.value = true;

      const response = await apiClient.get(API_ENDPOINTS.USER.RANKERS);
      const rankersData = response.data.data || [];

      // 점수를 티어로 변환하여 저장
      const processedRankers = rankersData.map((player, index) => {
        const tierInfo = getScoreToTier(player.score);
        return {
          id: index + 1,
          nickname: player.nickname,
          tier: tierInfo.division ? `${tierInfo.name} ${tierInfo.division}` : tierInfo.name,
          rating: player.score,
          programmingLanguage: player.programmingLanguage,
        };
      });

      rankers.value = processedRankers;
      return processedRankers;
    } catch (error) {
      console.error('랭커 데이터 조회 실패:', error);
      rankers.value = [];
      throw new Error('랭커 정보를 불러올 수 없습니다.');
    } finally {
      isLoading.value = false;
    }
  };

  /**
   * WebSocket으로 받은 랭커 데이터로 업데이트
   * @param {Array} rankersData - 새로운 랭커 데이터 배열
   */
  const updateRankers = rankersData => {
    try {
      const processedRankers = rankersData.map((player, index) => {
        const tierInfo = getScoreToTier(player.score);
        return {
          id: index + 1,
          nickname: player.nickname,
          tier: tierInfo.division ? `${tierInfo.name} ${tierInfo.division}` : tierInfo.name,
          rating: player.score,
          programmingLanguage: player.programmingLanguage,
        };
      });

      rankers.value = processedRankers;
    } catch (error) {
      console.error('랭커 데이터 업데이트 실패:', error);
    }
  };

  return {
    rankers,
    isLoading,
    fetchRankers,
    updateRankers,
  };
};
