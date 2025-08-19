<template>
  <div class="algorithm-info">
    <div class="algorithm-section">
      <h3 class="section-title">BANNED ALGORITHM</h3>
      <div class="algorithm-tags">
        <div v-for="algorithm in uniqueBannedAlgorithms" :key="algorithm" class="algorithm-tag banned">
          {{ algorithm }}
        </div>
      </div>
    </div>

    <div class="algorithm-section">
      <h3 class="section-title">PICKED ALGORITHM</h3>
      <div class="algorithm-tags">
        <div v-for="algorithm in uniquePickedAlgorithms" :key="algorithm" class="algorithm-tag picked">
          {{ algorithm }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  players: {
    type: Array,
    required: true,
  },
});

const uniqueBannedAlgorithms = computed(() => {
  const banned = props.players.map(player => player.bannedProblemCategoryName).filter(algorithm => algorithm);
  return [...new Set(banned)];
});

const uniquePickedAlgorithms = computed(() => {
  const picked = props.players.map(player => player.pickedProblemCategoryName).filter(algorithm => algorithm);
  return [...new Set(picked)];
});
</script>

<style lang="scss" scoped>
.algorithm-info {
  width: 100%;
  display: flex;
  justify-content: space-between;
  gap: 4vw;
  margin-top: 2vh;
}

.algorithm-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2vh;
  background: linear-gradient(to bottom, rgba($primary-bg, 0.1) 0%, rgba($primary-bg, 0.2) 100%);
  border: 0.1vh solid rgba($text-primary, 0.1);
  border-radius: $radius-small;
  backdrop-filter: blur(1vh);
  padding: 2vh;
  position: relative;
  
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: linear-gradient(135deg, 
      rgba($electric-blue, 0.02) 0%, 
      rgba($cyberpunk-pink, 0.02) 100%);
    border-radius: $radius-small;
    z-index: -1;
  }
}

.section-title {
  font-family: $font-accent;
  font-size: 2.8vh;
  font-weight: 700;
  color: $text-gold;
  margin: 0;
  text-shadow: 0 0 0.5vh rgba($text-gold, 0.4);
  text-align: center;
}

.algorithm-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 1vh;
  justify-content: center;
}

.algorithm-tag {
  padding: 1vh 2vw;
  border-radius: $radius-button;
  font-family: $font-primary;
  font-size: 1.6vh;
  font-weight: $font-weight-semibold;
  text-align: center;
  backdrop-filter: blur(0.5vh);
  position: relative;
  overflow: hidden;
  transition: all $transition-base;
  cursor: default;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
    transition: left 0.6s ease;
  }

  &:hover::before {
    left: 100%;
  }

  &.banned {
    background: rgba($cyberpunk-pink, 0.15);
    border: 0.1vh solid $cyberpunk-pink;
    color: $cyberpunk-pink;

    &:hover {
      @include neon-glow($cyberpunk-pink);
      transform: translateY(-0.1vh);
    }
  }

  &.picked {
    background: rgba($electric-blue, 0.15);
    border: 0.1vh solid $electric-blue;
    color: $electric-blue;

    &:hover {
      @include neon-glow($electric-blue);
      transform: translateY(-0.1vh);
    }
  }
}
</style>
