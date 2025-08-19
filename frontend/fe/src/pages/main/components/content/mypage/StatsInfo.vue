<template>
  <div class="stats-info">
    <!-- 알고리즘 차트 -->
    <div class="algorithm-section">
      <AlgorithmChart :data="algorithmStats" :size="300" :is-loading="isLoadingAlgorithm" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useAuth } from '@/composables/useAuth';
import AlgorithmChart from './AlgorithmChart.vue';

const { fetchAlgorithmStats } = useAuth();

// 알고리즘 통계 데이터
const algorithmStats = ref([]);
const isLoadingAlgorithm = ref(false);

// 알고리즘 통계 조회
const loadAlgorithmStats = async () => {
  try {
    isLoadingAlgorithm.value = true;
    const stats = await fetchAlgorithmStats();
    algorithmStats.value = stats || [];
  } catch (error) {
    console.error('알고리즘 통계 조회 실패:', error);
    algorithmStats.value = [];
  } finally {
    isLoadingAlgorithm.value = false;
  }
};

// 컴포넌트 마운트 시 알고리즘 통계 조회
onMounted(() => {
  loadAlgorithmStats();
});
</script>

<style lang="scss" scoped>
.stats-info {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
  //padding: calc(2vh);

  .algorithm-section {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
  }
}
</style>
