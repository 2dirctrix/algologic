<template>
  <div class="player-slot">
    <div class="player-rank">{{ rank }}</div>
    <div class="player-info">
      <img v-if="tierIcon" :src="tierIcon" :alt="tierInfo.name" class="tier-icon" />
      <div class="player-nickname">{{ nickname }}</div>
    </div>
    <div class="player-rating">
      <span class="rating-value">{{ rating }}</span>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { getScoreToTier } from '@/constants/tiers.js';

const props = defineProps({
  rank: {
    type: Number,
    required: true,
  },
  nickname: {
    type: String,
    required: true,
  },
  tier: {
    type: String,
    required: true,
  },
  rating: {
    type: Number,
    required: true,
  },
});

const tierInfo = computed(() => {
  return getScoreToTier(props.rating);
});

const tierIcon = computed(() => {
  const tierName = tierInfo.value.name;
  if (tierName === 'UNRANKED') return null;

  try {
    return new URL(`/src/assets/tier-icons/${tierName.charAt(0) + tierName.slice(1).toLowerCase()}.png`, import.meta.url).href;
  } catch {
    console.warn(`Tier icon not found for ${tierName}`);
    return null;
  }
});
</script>

<style lang="scss" scoped>
.player-slot {
  display: flex;
  align-items: center;
  gap: calc(1vh);
  padding: calc(1vh) calc(1.5vh);
  transition: all $transition-base;
  font-size: calc(2vh);
  border-radius: calc(2vh);
  opacity: 0;
  transform: translateY(2vh);

  &.slide-up {
    animation: slideUp 0.8s cubic-bezier(0.25, 0.46, 0.45, 0.94) forwards;
  }

  &:hover {
    @include neon-glow($neon-green);
  }
}

.player-rank {
  font-family: $font-accent;
  font-size: calc(2vh);
  color: rgba($electric-blue, $opacity-text-secondary);
  min-width: calc(3vh);
  text-align: center;
  flex-shrink: 0;
}

.player-info {
  display: flex;
  align-items: center;
  gap: calc(0.8vh);
  flex: 1;
  min-width: 0;
}

.tier-icon {
  width: calc(3.5vh);
  height: calc(3.5vh);
  flex-shrink: 0;
  filter: drop-shadow(0 0 2px rgba(255, 255, 255, 0.3));
}

.player-nickname {
  font-family: $font-primary;
  font-size: 1.8vh;
  font-weight: $font-weight-medium;
  color: $text-primary;
  flex: 1;
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.player-rating {
  background: rgba($primary-bg, 0.8);
  border: calc(0.3vh) solid $cyberpunk-yellow;
  border-radius: $radius-button;
  padding: calc(0.5vh) calc(1vh);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  position: relative;

  box-shadow:
    0 0 5px rgba($cyberpunk-yellow, 0.3),
    inset 0 0 5px rgba($cyberpunk-yellow, 0.1);

  transition: all $transition-base;

  &:hover {
    box-shadow:
      0 0 8px rgba($cyberpunk-yellow, 0.5),
      inset 0 0 8px rgba($cyberpunk-yellow, 0.2);
  }
}

.rating-value {
  font-family: $font-primary;
  font-size: calc(1.05vh);
  font-weight: $font-weight-bold;
  color: $cyberpunk-yellow;
  text-shadow: 0 0 4px rgba($cyberpunk-yellow, 0.8);
  letter-spacing: 0.5px;
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
