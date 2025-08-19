<template>
  <div class="live-banner">
    <div class="banner-content">
      <!-- Live Indicator -->
      <!--      <LiveIndicator />-->

      <!-- Main Title -->
      <h1 class="banner-title">Global Algorithm Championship</h1>

      <!-- Subtitle -->
      <p class="banner-subtitle">실시간 코딩 대전에 참여하고 글로벌 랭킹을 확인하세요</p>

      <!-- Bottom Section -->
      <div class="banner-bottom">
        <!-- Statistics -->
        <div class="banner-stats">
          <span class="stat-item"> </span>
          <span class="stat-item"> </span>
          <span class="stat-item"> </span>
        </div>

        <!-- Join Button -->
        <button class="join-button" @click="handleJoinClick">Join</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import LiveIndicator from './LiveIndicator.vue';

const emit = defineEmits(['join-live']);

// 더미 데이터 - 추후 API 연동
const stats = ref({
  participants: '1,247',
  timeRemaining: '2h 15m',
  prize: '50,000',
});

const handleJoinClick = () => {
  emit('join-live');
};
</script>

<style lang="scss" scoped>
.live-banner {
  width: 100%;
  height: 100%;
  background: linear-gradient(rgba(0, 0, 0, 0.25), rgba(0, 0, 0, 0.25)), url('@/pages/main/assets/images/live-banner-image.png');
  background-size: cover;
  background-position: center;
  border: calc(0.3vh) solid rgba($synthwave-purple, 0.7);
  border-radius: calc(2vh);
  box-shadow: inset 0px 0px 15px 0px rgba($electric-blue, 0.3);
  padding: calc(2vh);

  @include neon-glow($synthwave-purple);
}

.banner-content {
  display: flex;
  flex-direction: column;
  height: 100%;
  gap: calc(1.6vh);
}

.banner-title {
  font-family: $font-accent;
  font-size: calc(4.8vh);
  font-weight: $font-weight-regular;
  color: $electric-blue;
  line-height: 1.1;
  margin: 0;
  text-shadow: 0px 0px 10px rgba($electric-blue, 0.5);
}

.banner-subtitle {
  font-family: $font-primary;
  font-size: calc(2vh);
  font-weight: $font-weight-bold;
  color: $text-primary;
  line-height: 1.4;
  margin: 0;
}

.banner-bottom {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-top: auto;
}

.banner-stats {
  display: flex;
  flex-direction: column;
  gap: calc(0.8vh);
}

.stat-item {
  font-family: $font-primary;
  font-size: calc(1.6vh);
  font-weight: $font-weight-regular;
  color: $text-primary;
  opacity: $opacity-text-secondary;
  line-height: 1.3;
}

.join-button {
  background: rgba($cyberpunk-yellow, 0.5);
  border: 0.3vh solid $cyberpunk-yellow;
  color: $text-primary;
  border-radius: $radius-main;
  padding: calc(0.8vh) calc(1.6vh);
  font-family: $font-accent;
  font-size: 3vh;
  font-weight: $font-weight-regular;
  line-height: 1.2;
  cursor: pointer;
  min-width: calc(8vh);

  @include neon-glow($cyberpunk-yellow);

  &:hover {
    background: rgba($cyberpunk-yellow, 0.3);
  }
}
</style>
