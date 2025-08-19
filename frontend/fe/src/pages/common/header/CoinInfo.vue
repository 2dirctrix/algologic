<template>
  <div class="coin-info">
    <div class="coin-icon">
      <span class="coin-symbol">₳</span>
    </div>
    <span class="coin-amount">{{ formattedCoinAmount }}</span>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useAuthStore } from '@/stores/auth/auth.js';

const authStore = useAuthStore();

// 코인 금액을 천단위 콤마로 포맷팅
const formattedCoinAmount = computed(() => {
  return authStore.coin.toLocaleString();
});
</script>

<style lang="scss" scoped>
.coin-info {
  background: rgba($text-primary, $opacity-inactive);
  border: 0.2vh solid rgba($electric-blue, $opacity-text-secondary);
  border-radius: $radius-nested;
  padding: calc(1vh) calc(2vh);
  display: flex;
  align-items: center;
  gap: calc(1vh);
  height: calc(3.5vh);
  white-space: nowrap;

  @include neon-glow($electric-blue);

  &:hover {
    background: rgba($text-primary, 0.4);
  }
}

.coin-icon {
  background: $electric-blue;
  color: $primary-bg;
  width: calc(2.5vh);
  height: calc(2vh);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;

  .coin-symbol {
    font-family: $font-primary;
    font-weight: $font-weight-bold;
    font-size: calc(1.4vh);
  }
}

.coin-amount {
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: calc(1.6vh);
  color: $text-primary;
}
</style>
