<template>
  <div
    class="player-slot"
    :class="{
      'player-slot--empty': !player,
      'player-slot--completed': isCompleted,
    }"
  >
    <!-- 플레이어가 있는 경우 -->
    <template v-if="player">
      <div class="player-content">
        <div class="player-info">
          <div class="player-name-section">
            <div class="tier-icon" v-if="tierIconSrc">
              <img :src="tierIconSrc" :alt="tierDisplay" @error="handleImageError" />
            </div>
            <div class="player-nickname">{{ player.nickname }}</div>
          </div>
          <div class="tier-rating-group">
            <div class="tier-name">{{ tierDisplay }}</div>
            <div class="rating-badge">
              {{ player.rating }}
            </div>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script>
import { getScoreToTier } from '@/constants/tiers.js';
import { ref } from 'vue';

export default {
  name: 'PlayerSlot',
  props: {
    player: {
      type: Object,
      default: null,
    },
    slotIndex: {
      type: Number,
      required: true,
    },
    isCompleted: {
      type: Boolean,
      default: false,
    },
  },
  setup() {
    const imageError = ref(false);

    const handleImageError = () => {
      imageError.value = true;
    };

    return {
      imageError,
      handleImageError,
    };
  },
  computed: {
    tierDisplay() {
      if (!this.player || !this.player.rating) {
        return 'UNRANKED';
      }

      const tierInfo = getScoreToTier(this.player.rating);

      if (tierInfo.division) {
        return `${tierInfo.name} ${tierInfo.division}`;
      }

      return tierInfo.name;
    },
    tierIconSrc() {
      if (!this.player || !this.player.rating || this.imageError) {
        return null;
      }

      const tierInfo = getScoreToTier(this.player.rating);
      const tierName = tierInfo.name;

      // UNRANKED는 아이콘이 없음
      if (tierName === 'UNRANKED') {
        return null;
      }

      try {
        // 파일명 규칙에 맞게 변환
        const fileName = tierName.charAt(0).toUpperCase() + tierName.slice(1).toLowerCase();
        return new URL(`/src/assets/tier-icons/${fileName}.png`, import.meta.url).href;
      } catch (error) {
        return null;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
@use 'sass:color';
.player-slot {
  width: 100%;
  height: 100%;
  min-height: calc(10vh);
  background: linear-gradient(135deg, rgba($card-bg, 0.9) 0%, rgba($card-bg, 0.7) 50%, rgba($primary-bg, 0.8) 100%);
  border: calc(0.2vh) solid rgba($electric-blue, 0.4);
  border-radius: 1vh;
  position: relative;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  display: flex;
  align-items: center;
  padding: calc(1.5vh);

  // 미묘한 패턴 효과
  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: repeating-linear-gradient(
      45deg,
      transparent,
      transparent 2px,
      rgba($electric-blue, 0.02) 2px,
      rgba($electric-blue, 0.02) 4px
    );
    border-radius: 0.8vh;
    pointer-events: none;
  }


  // 플레이어가 있는 슬롯
  &:not(.player-slot--empty) {
    &:hover {
      background: linear-gradient(
        135deg,
        rgba($electric-blue, 0.08) 0%,
        rgba($card-bg, 0.9) 30%,
        rgba($card-bg, 0.7) 70%,
        rgba($electric-blue, 0.05) 100%
      );
      border-color: rgba($electric-blue, 0.6);
      transform: translateY(-0.1vh) scale(1.01);
      @include neon-glow($electric-blue);

      &::before {
        background: repeating-linear-gradient(
          45deg,
          transparent,
          transparent 2px,
          rgba($electric-blue, 0.05) 2px,
          rgba($electric-blue, 0.05) 4px
        );
      }

    }
  }

  // 선택 완료 상태
  &.player-slot--completed {
    background: linear-gradient(
      135deg,
      rgba($neon-green, 0.15) 0%,
      rgba($card-bg, 0.9) 30%,
      rgba($card-bg, 0.8) 70%,
      rgba($neon-green, 0.1) 100%
    ) !important;
    border: calc(0.6vh) solid rgba($neon-green, 0.7) !important;
    transform: scale(1.02);
    @include neon-glow-strong($neon-green);

    &::before {
      background: repeating-linear-gradient(45deg, transparent, transparent 2px, rgba($neon-green, 0.08) 2px, rgba($neon-green, 0.08) 4px);
    }


    .player-nickname {
      color: color.scale($neon-green, $lightness: 40%) !important;
      text-shadow: 0 0 0.8vh rgba($neon-green, 0.6) !important;
    }

    .rating-badge {
      border-color: $neon-green !important;
      color: $neon-green !important;
      text-shadow: 0 0 4px rgba($neon-green, 0.8) !important;
      box-shadow:
        0 0 8px rgba($neon-green, 0.5) !important,
        inset 0 0 8px rgba($neon-green, 0.2) !important;
    }
  }
}

.player-content {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
}

.player-info {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: calc(1vh);
  flex-wrap: nowrap;
  min-width: 0;
}

.player-name-section {
  display: flex;
  align-items: center;
  gap: calc(0.8vh);
  flex: 1 1 auto;
  min-width: 0;
}

.tier-icon {
  flex-shrink: 0;
  width: calc(5vh);
  height: calc(5vh);
  display: flex;
  align-items: center;
  justify-content: center;

  img {
    width: 100%;
    height: 100%;
    object-fit: contain;
    filter: drop-shadow(0 0 3px rgba(255, 255, 255, 0.3));
  }
}

.player-nickname {
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: calc(2.2vh);
  line-height: 1;
  color: $electric-blue;
  margin: 0;
  flex: 1 1 auto;
  min-width: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: left;
}

.tier-rating-group {
  display: flex;
  align-items: center;
  gap: calc(1vh);
  flex: 0 0 auto;
  flex-shrink: 0;
}

.tier-name {
  font-family: $font-primary;
  font-weight: $font-weight-medium;
  font-size: calc(1.4vh);
  line-height: 1;
  color: rgba($text-primary, $opacity-text-secondary);
  white-space: nowrap;
  text-align: right;
  min-width: calc(6vh);
}

.rating-badge {
  background: rgba($primary-bg, 0.8);
  border: calc(0.3vh) solid $cyberpunk-yellow;
  color: $cyberpunk-yellow;
  padding: calc(0.5vh) calc(1vh);
  border-radius: $radius-button;
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: calc(1.05vh);
  line-height: 1.3;
  min-width: calc(3vh);
  text-align: center;
  transition: all $transition-base;
  flex-shrink: 0;
  white-space: nowrap;
  text-shadow: 0 0 4px rgba($cyberpunk-yellow, 0.8);
  letter-spacing: 0.5px;

  box-shadow:
    0 0 5px rgba($cyberpunk-yellow, 0.3),
    inset 0 0 5px rgba($cyberpunk-yellow, 0.1);

  &:hover {
    box-shadow:
      0 0 8px rgba($cyberpunk-yellow, 0.5),
      inset 0 0 8px rgba($cyberpunk-yellow, 0.2);
  }
}
</style>
