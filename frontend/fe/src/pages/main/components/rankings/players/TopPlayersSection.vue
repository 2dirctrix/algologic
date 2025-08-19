<template>
  <div class="top-players-section">
    <!-- Header -->
    <div class="section-header">
      <h3 class="section-title">Top Players</h3>
    </div>

    <!-- Player List -->
    <div class="players-list">
      <PlayerSlot
        v-for="(player, index) in currentPagePlayers"
        :key="player.id"
        :rank="getPlayerRank(index)"
        :nickname="player.nickname"
        :tier="player.tier"
        :rating="player.rating"
        :class="{ 'slide-up': showAnimation }"
        :style="{ animationDelay: `${index * 0.4}s` }"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch } from 'vue';
import PlayerSlot from './PlayerSlot.vue';
import { useMember } from '@/composables/useMember.js';
import { useWebSocketStore } from '@/stores/websocket/websocket.js';
import { WS_ENDPOINTS } from '@/websocket/constants/endpoints.js';

const { rankers, isLoading, fetchRankers, updateRankers } = useMember();
const webSocketStore = useWebSocketStore();
const onlineCount = ref('1,240');
const currentPage = ref(0);
const showAnimation = ref(false);
let intervalId = null;
let rankersSubscriptionId = null;

// 현재 페이지의 플레이어 5명
const currentPagePlayers = computed(() => {
  const startIndex = currentPage.value * 5;
  return rankers.value.slice(startIndex, startIndex + 5);
});

// 플레이어 순위 계산
const getPlayerRank = index => {
  return currentPage.value * 5 + index + 1;
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
  intervalId = setInterval(nextPage, 4000);
};

// WebSocket 메시지 핸들러
const handleRankersMessage = message => {
  try {
    // 랭커 정보가 있으면 업데이트
    if (message.code === 200 && Array.isArray(message.data.payload)) {
      updateRankers(message.data.payload);
    }
  } catch (error) {
    console.error('[TOP PLAYERS] 랭커 정보 업데이트 실패:', error);
  }
};

// WebSocket 연결 상태 감시 및 구독
watch(
  () => webSocketStore.isConnected,
  isConnected => {
    if (isConnected && !rankersSubscriptionId) {
      try {
        const webSocketService = webSocketStore.getService();
        rankersSubscriptionId = webSocketService.subscribe(WS_ENDPOINTS.MAIN.RANKERS, handleRankersMessage);
      } catch (error) {
        console.error('[TOP PLAYERS] 랭커 정보 구독 실패:', error);
      }
    }
  },
  { immediate: true },
);

onMounted(async () => {
  await fetchRankers();
  startInterval();
  // 초기 애니메이션 트리거
  setTimeout(() => {
    showAnimation.value = true;
  }, 100);
});

onUnmounted(() => {
  // 인터벌 정리
  if (intervalId) {
    clearInterval(intervalId);
  }

  // WebSocket 구독 해제
  if (rankersSubscriptionId && webSocketStore.isConnected) {
    try {
      const webSocketService = webSocketStore.getService();
      webSocketService.unsubscribe(rankersSubscriptionId);
    } catch (error) {
      console.error('[TOP PLAYERS] 랭커 정보 구독 해제 실패:', error);
    }
  }
});
</script>

<style lang="scss" scoped>
.top-players-section {
  width: 100%;
  height: 100%;
  background: rgba($card-bg, 0.5);
  border: calc(0.3vh) solid rgba($synthwave-purple, 0.7);
  border-radius: calc(2vh);
  //box-shadow: inset 0px 0px 15px 0px rgba($electric-blue, 0.3);
  padding: calc(1vh);
  display: flex;
  flex-direction: column;
  min-height: 0;
  font-size: calc(1.2vh);

  @include neon-glow($synthwave-purple);
}

.section-header {
  flex: 0 0 auto;
  height: calc(4vh);
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-bottom: calc(0.5vh);
}

.section-title {
  font-family: $font-accent;
  font-size: calc(1.6vh);
  font-weight: 400;
  color: $neon-green;
  line-height: 1.2;
  margin: 0;
  text-align: center;
}

.players-list {
  display: flex;
  flex-direction: column;
  gap: calc(0.5vh);
  flex: 1;
  min-height: 0;

  & > * {
    flex: 1;
    min-height: 0;
  }
}
</style>
