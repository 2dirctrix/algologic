<template>
  <div class="statistics-section">
    <!-- Tab Indicators -->
    <div class="tab-indicators">
      <div class="tab-indicator" :class="{ active: currentSectionType === 0 }" @click="switchTab(0)">Most Banned</div>
      <div class="tab-indicator" :class="{ active: currentSectionType === 1 }" @click="switchTab(1)">Most Picked</div>
    </div>

    <!-- Statistics Cards -->
    <div class="statistics-list">
      <StatisticsCard
        v-for="(item, index) in currentPageData"
        :key="`${currentSectionType}-${currentPage}-${index}`"
        :rank="getItemRank(index)"
        :algorithmName="item.name"
        :percentage="item.percentage"
        :color="item.color"
        :class="{ 'slide-up': showAnimation }"
        :style="{ animationDelay: `${index * 0.4}s` }"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch } from 'vue';
import StatisticsCard from './StatisticsCard.vue';
import { useWebSocketStore } from '@/stores/websocket/websocket.js';
import { WS_ENDPOINTS } from '@/websocket/constants/endpoints.js';
import { API_ENDPOINTS } from '@/api/constants/endpoints.js';
import { apiClient } from '@/api/utils/index.js';
const webSocketStore = useWebSocketStore();

// 상수 정의
const ITEMS_PER_PAGE = 5;
const AUTO_SWITCH_INTERVAL = 3000; // 3초

// 상태 관리
const currentPage = ref(0);
const currentSectionType = ref(0); // 0: banned, 1: picked
const showAnimation = ref(false);
let intervalId = null;
let algorithmSubscriptionId = null;

// 알고리즘 랭킹 데이터
const topPickedData = ref([]);
const topBannedData = ref([]);

// 데이터 가공 함수
const processRankingData = (rawData, type) => {
  if (!Array.isArray(rawData)) return [];
  return rawData.map(item => ({
    name: item.name,
    percentage: type === 'picked' ? item.pickedRate : item.bannedRate,
    color: '#00FFFF', // 단일 색상 (시안)
  }));
};

// 현재 표시할 데이터
const pickedData = computed(() => processRankingData(topPickedData.value, 'picked'));
const bannedData = computed(() => processRankingData(topBannedData.value, 'banned'));

const currentPageData = computed(() => {
  const data = currentSectionType.value === 0 ? bannedData.value : pickedData.value;
  const startIndex = currentPage.value * ITEMS_PER_PAGE;
  return data.slice(startIndex, startIndex + ITEMS_PER_PAGE);
});

const getItemRank = index => {
  return currentPage.value * ITEMS_PER_PAGE + index + 1;
};

// 탭 전환 함수
const switchTab = tabIndex => {
  if (currentSectionType.value !== tabIndex) {
    showAnimation.value = false;
    currentSectionType.value = tabIndex;
    currentPage.value = 0;
    restartInterval();
    // 탭 전환 후 애니메이션 트리거
    setTimeout(() => {
      showAnimation.value = true;
    }, 50);
  }
};

// 페이지 자동 전환
const nextPage = () => {
  showAnimation.value = false;
  setTimeout(() => {
    currentPage.value = currentPage.value === 1 ? 0 : 1;
    // 페이지 전환 후 애니메이션 트리거
    setTimeout(() => {
      showAnimation.value = true;
    }, 50);
  }, 100);
};

// 인터벌 관리
const startInterval = () => {
  intervalId = setInterval(nextPage, AUTO_SWITCH_INTERVAL);
};

const restartInterval = () => {
  if (intervalId) {
    clearInterval(intervalId);
  }
  startInterval();
};

// 초기 랭킹 데이터 가져오기
const fetchInitialRanking = async () => {
  try {
    const response = await apiClient.get(API_ENDPOINTS.PROBLEM.RANK);

    if (response.data?.status === 200 && response.data?.data) {
      const data = response.data.data;
      topPickedData.value = data.topPicked || [];
      topBannedData.value = data.topBanned || [];
    }
  } catch (error) {
    console.error('[ALGORITHM RANKING] 초기 랭킹 데이터 로드 실패:', error);
  }
};

// WebSocket 메시지 핸들러
const handleAlgorithmRankingMessage = message => {
  try {
    if (message.data?.event === 'PROBLEM_CATEGORY_RANK_UPDATED') {
      const payload = message.data.payload;

      // 데이터 업데이트
      topPickedData.value = payload.topPicked || [];
      topBannedData.value = payload.topBanned || [];
    }
  } catch (error) {
    console.error('[ALGORITHM RANKING] 메시지 처리 실패:', error);
  }
};

onMounted(() => {
  startInterval();
  // 초기 애니메이션 트리거
  setTimeout(() => {
    showAnimation.value = true;
  }, 100);
});

// WebSocket 연결 상태 감시 및 구독
watch(
  () => webSocketStore.isConnected,
  async isConnected => {
    if (isConnected && !algorithmSubscriptionId) {
      try {
        // 먼저 초기 랭킹 데이터를 가져온다
        await fetchInitialRanking();

        // 그 다음 WebSocket 구독을 시작한다
        const webSocketService = webSocketStore.getService();
        algorithmSubscriptionId = webSocketService.subscribe(WS_ENDPOINTS.PROBLEM.ALGO_RATE_SUB, handleAlgorithmRankingMessage);
      } catch (error) {
        console.error('[ALGORITHM RANKING] 알고리즘 랭킹 구독 실패:', error);
      }
    }
  },
  { immediate: true },
);

onUnmounted(() => {
  if (intervalId) {
    clearInterval(intervalId);
  }

  // WebSocket 구독 해제
  if (algorithmSubscriptionId) {
    try {
      const webSocketService = webSocketStore.getService();
      webSocketService.unsubscribe(algorithmSubscriptionId);
    } catch (error) {
      console.error('[ALGORITHM RANKING] 구독 해제 실패:', error);
    }
  }
});
</script>

<style lang="scss" scoped>
.statistics-section {
  display: flex;
  flex-direction: column;
  align-items: stretch;
  width: 100%;
  height: 100%;
  background: rgba($card-bg, 0.6);
  border: calc(0.3vh) solid rgba($synthwave-purple, 0.7);
  border-radius: calc(2vh);
  //box-shadow: inset 0px 0px 15px 0px rgba($electric-blue, 0.3);
  padding: calc(1.5vh);

  @include neon-glow($synthwave-purple);
}

.tab-indicators {
  display: flex;
  gap: calc(0.5vh);
  background: rgba($primary-bg, $opacity-active);
  border-radius: $radius-button;
  padding: calc(0.5vh);
  margin-bottom: 1vh;

  // vh 단위로 고정
  height: calc(3.4vh) !important;
  min-height: calc(3.4vh) !important;
  max-height: calc(3.4vh) !important;

  // flex 확장 완전 방지
  flex: none !important;
  flex-shrink: 0 !important;
  flex-grow: 0 !important;
}

.tab-indicator {
  flex: 1;
  height: calc(3.5vh);
  padding: 0 calc(1vh);
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  font-family: $font-primary;
  font-weight: $font-weight-medium;
  font-size: 1.4vh;
  color: $text-primary;
  opacity: $opacity-text-secondary;
  background: transparent;
  border: 1px solid transparent;
  border-radius: $radius-button;
  cursor: pointer;
  transition: all $transition-base;

  &.active {
    background: rgba($synthwave-purple, $opacity-active);
    border-color: $synthwave-purple;
    opacity: 1;

    @include neon-glow($synthwave-purple);
  }

  &:hover:not(.active) {
    background: rgba($text-primary, 0.05);
    opacity: 1;
  }

  &:focus {
    outline: none;
    box-shadow: 0 0 0 2px rgba($synthwave-purple, 0.5);
  }
}

.statistics-list {
  display: flex;
  flex-direction: column;
  gap: calc(1vh);
  flex: 1;
  min-height: 0;
}
</style>
