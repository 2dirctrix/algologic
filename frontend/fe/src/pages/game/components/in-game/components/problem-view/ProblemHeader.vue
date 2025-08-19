<!-- src/pages/game/components/in-game/components/problem-view/ProblemHeader.vue -->
<template>
  <div class="problem-header">
    <div class="header-left">
      <h2 class="problem-title">{{ title }}</h2>
      <span class="problem-difficulty" :class="`difficulty-${difficulty.toLowerCase()}`">
        {{ difficulty }}
      </span>
    </div>
    <div class="timer-section">
      <span class="timer-text">{{ formattedTime }}</span>
    </div>
  </div>
</template>

<script>
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';
import { computed } from 'vue';

export default {
  name: 'ProblemHeader',
  props: {
    title: {
      type: String,
      required: true,
    },
    difficulty: {
      type: String,
      default: 'Medium',
    },
  },
  setup() {
    const inGameStore = useInGameStore();

    const formattedTime = computed(() => {
      const timeMs = inGameStore.timerState.phaseRemainingTime;
      if (timeMs <= 0) return '00:00';

      const totalSeconds = Math.ceil(timeMs / 1000);
      const minutes = Math.floor(totalSeconds / 60);
      const seconds = totalSeconds % 60;

      return `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
    });

    return {
      formattedTime,
    };
  },
};
</script>

<style lang="scss" scoped>
.problem-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 2vh;
  border-bottom: 0.2vh solid rgba($text-primary, $opacity-minimal);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 1vh;
}

.problem-title {
  font-family: $font-primary;
  font-weight: $font-weight-semibold;
  font-size: 3.2vh;
  color: $electric-blue;
  margin: 0;
}

.problem-difficulty {
  font-family: $font-primary;
  font-weight: $font-weight-medium;
  font-size: 1.8vh;
  padding: 0.5vh 1.5vh;
  border-radius: $radius-button;

  &.difficulty-easy {
    background: rgba($neon-green, $opacity-active);
    color: $neon-green;
    border: 0.5vh solid $neon-green;
  }

  &.difficulty-medium {
    background: rgba($cyberpunk-yellow, $opacity-active);
    color: $cyberpunk-yellow;
    border: 1px solid $cyberpunk-yellow;
  }

  &.difficulty-hard {
    background: rgba($cyberpunk-pink, $opacity-active);
    color: $cyberpunk-pink;
    border: 1px solid $cyberpunk-pink;
  }
}

.timer-section {
  display: flex;
  align-items: center;
}

.timer-text {
  font-family: $font-primary;
  font-weight: $font-weight-semibold;
  font-size: 3.2vh;
  color: $electric-blue;
}
</style>
