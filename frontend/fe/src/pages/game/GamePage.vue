<template>
  <div class="game-page">
    <WaitingRoomView v-if="gameStatus === 'waiting'" />
    <BanPickBuyPage v-else-if="gameStatus === 'banPickBuy'" />
    <InGamePage v-else-if="gameStatus === 'play'" />
    <GameResultPage v-else-if="gameStatus === 'result'" />
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted } from 'vue';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';
import { useBattle } from '@/composables/useBattle.js';
import WaitingRoomView from './components/waiting-room/WaitingRoomView.vue';
import BanPickBuyPage from './components/ban-pick-buy/BanPickBuyPage.vue';
import InGamePage from './components/in-game/InGamePage.vue';
import GameResultPage from './components/result/GameResultPage.vue';

const inGameStore = useInGameStore();
const { subscribeToBattleEvents, unsubscribeFromBattleEvents, subscribeToBattleEndEvents, unsubscribeFromBattleEndEvents } = useBattle();

const gameStatus = computed(() => inGameStore.gameStatus);

// 컴포넌트 마운트 시 배틀 이벤트 구독 시작
onMounted(async () => {
  try {
    await subscribeToBattleEvents();
    await subscribeToBattleEndEvents();
  } catch (error) {
    console.error('[GamePage] 배틀 이벤트 구독 실패:', error);
  }
});

// 컴포넌트 언마운트 시 배틀 이벤트 구독 해제
onUnmounted(() => {
  unsubscribeFromBattleEvents();
  unsubscribeFromBattleEndEvents();
});
</script>

<style lang="scss" scoped>
.game-page {
  width: 100%;
  height: 100vh;
  background: url('./assets/waiting-room-wallpaper.jpg') no-repeat fixed center center;
  background-size: cover;
  position: relative;

  // 어두운 오버레이로 가독성 확보
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.7);
    z-index: 1;
  }

  // 컨텐츠가 오버레이 위에 표시되도록
  > * {
    position: relative;
    z-index: 2;
  }
}
</style>
