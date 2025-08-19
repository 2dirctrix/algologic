<template>
  <div class="statistics-card">
    <div class="rank-number" :style="{ color: color }">{{ rank }}</div>
    <div class="algorithm-name">{{ algorithmName }}</div>
    <div class="percentage-text">{{ percentage }}%</div>
  </div>
</template>

<script setup>
defineProps({
  rank: {
    type: Number,
    required: true,
  },
  algorithmName: {
    type: String,
    required: true,
  },
  percentage: {
    type: Number,
    required: true,
  },
  color: {
    type: String,
    required: true,
  },
});
</script>

<style lang="scss" scoped>
.statistics-card {
  display: flex;
  align-items: center;
  gap: calc(1vh);
  padding: calc(1vh) calc(1.5vh);
  border-radius: $radius-button;
  transition: all $transition-base;
  height: 100%;
  min-height: 0;
  opacity: 0;
  transform: translateY(2vh);

  &.slide-up {
    animation: slideUp 0.8s cubic-bezier(0.25, 0.46, 0.45, 0.94) forwards;
  }

  &:hover {
    border-color: rgba($electric-blue, $opacity-text-secondary);
    background: rgba($card-bg, 0.9);

    @include neon-glow($electric-blue);
  }
}

.rank-number {
  font-family: $font-accent;
  font-size: calc(2vh);
  color: inherit;
  text-align: center;
  min-width: 1.5em;
  opacity: 0.8;
  flex-shrink: 0;
}

.algorithm-name {
  font-family: $font-primary;
  font-size: 1.8vh;
  font-weight: $font-weight-medium;
  color: $text-primary;
  flex: 1;
  text-align: left;
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.percentage-text {
  font-family: $font-accent;
  font-size: 1.8vh;
  color: $text-primary;
  flex-shrink: 0;
  margin-left: auto;
}

// 슬라이드 업 애니메이션
@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(2vh);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
