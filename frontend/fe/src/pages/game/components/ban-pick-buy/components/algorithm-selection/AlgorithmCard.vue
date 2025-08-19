<template>
  <div
    class="algorithm-card"
    :class="[`algorithm-card--${status}`, { 'algorithm-card--selected': selected, 'algorithm-card--disabled': isInteractionDisabled }]"
    @click="handleClick"
    @mouseenter="handleMouseEnter"
  >
    <div class="algorithm-card__background"></div>
    <div class="algorithm-card__border-frame"></div>
    <div class="algorithm-card__content">
      <div class="algorithm-card__header">
        <div class="algorithm-card__type-label">ALGORITHM</div>
      </div>
      <div class="algorithm-card__main">
        <span class="algorithm-card__name">{{ algorithm.category }}</span>
      </div>
    </div>
    <AlgorithmStatusOverlay :status="status" />
  </div>
</template>

<script>
import AlgorithmStatusOverlay from './AlgorithmStatusOverlay.vue';
import { ALGORITHM_STATUS } from '../../constants/phase-status.js';
import { computed } from 'vue';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';

export default {
  name: 'AlgorithmCard',
  components: {
    AlgorithmStatusOverlay,
  },
  props: {
    algorithm: {
      type: Object,
      required: true,
    },
    status: {
      type: String,
      default: ALGORITHM_STATUS.AVAILABLE,
      validator: value => Object.values(ALGORITHM_STATUS).includes(value),
    },

    selected: {
      type: Boolean,
      default: false,
    },
  },
  setup() {
    const inGameStore = useInGameStore();

    const isInteractionDisabled = computed(() => {
      return inGameStore.timerState.isCountdownActive;
    });

    return {
      isInteractionDisabled,
    };
  },
  methods: {
    playHoverSound() {
      try {
        const audio = new Audio('/audio/interaction/button-hover-sound.wav');
        audio.volume = 0.3;
        audio.play().catch(error => {
          console.warn('Audio play failed:', error);
        });
      } catch (error) {
        console.warn('Audio creation failed:', error);
      }
    },
    playClickSound() {
      try {
        const audio = new Audio('/audio/interaction/common-click-sound.wav');
        audio.volume = 0.6;
        audio.play().catch(error => {
          console.warn('Audio play failed:', error);
        });
      } catch (error) {
        console.warn('Audio creation failed:', error);
      }
    },
    handleMouseEnter() {
      if (this.isInteractionDisabled) return;
      if (this.status === ALGORITHM_STATUS.AVAILABLE) {
        this.playHoverSound();
      }
    },
    handleClick() {
      if (this.isInteractionDisabled) return;
      if (this.status === ALGORITHM_STATUS.AVAILABLE) {
        this.playClickSound();
        this.$emit('select', this.algorithm);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.algorithm-card {
  position: relative;
  width: 100%;
  height: 100%;
  border-radius: 1vh;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  overflow: hidden;
  box-sizing: border-box;

  // 정사각형 비율 유지 (grid 내에서)
  &::before {
    content: '';
    display: block;
    aspect-ratio: 1;
    width: 100%;
  }

  &__background {
    position: absolute;
    inset: 0;
    border-radius: 1vh;
    background: linear-gradient(135deg, rgba($card-bg, 0.95) 0%, rgba($card-bg, 0.8) 50%, rgba($primary-bg, 0.9) 100%);
    border: 0.2vh solid rgba($electric-blue, 0.4);
    box-shadow:
      0vh 0.6vh 2vh 0vh rgba(0, 0, 0, 0.4),
      inset 0vh 0.1vh 0vh 0vh rgba(255, 255, 255, 0.08);

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
    }
  }

  &__border-frame {
    position: absolute;
    inset: 0.3vh;
    border: 0.1vh solid rgba($electric-blue, 0.2);
    border-radius: 0.7vh;
    z-index: 1;

    // 모서리 강조
    &::before {
      content: '';
      position: absolute;
      top: -0.1vh;
      left: -0.1vh;
      width: 1.5vh;
      height: 1.5vh;
      border-top: 0.2vh solid $electric-blue;
      border-left: 0.2vh solid $electric-blue;
      border-radius: 0.2vh 0 0 0;
    }

    &::after {
      content: '';
      position: absolute;
      bottom: -0.1vh;
      right: -0.1vh;
      width: 1.5vh;
      height: 1.5vh;
      border-bottom: 0.2vh solid $electric-blue;
      border-right: 0.2vh solid $electric-blue;
      border-radius: 0 0 0.2vh 0;
    }
  }

  &__content {
    position: absolute;
    inset: 0;
    z-index: 2;
    display: flex;
    flex-direction: column;
    padding: 1.2vh;
  }

  &__header {
    display: flex;
    justify-content: center;
    margin-bottom: 0.5vh;
  }

  &__type-label {
    font-family: $font-primary;
    font-size: 1vh;
    font-weight: 600;
    color: rgba($electric-blue, 0.7);
    text-transform: uppercase;
    letter-spacing: 0.15em;
    background: rgba($electric-blue, 0.1);
    padding: 0.3vh 0.8vh;
    border-radius: 0.3vh;
    border: 0.1vh solid rgba($electric-blue, 0.3);
  }

  &__main {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    text-align: center;
  }

  &__name {
    font-family: $font-primary;
    font-size: 1.8vh;
    font-weight: 700;
    line-height: 1.1;
    letter-spacing: -0.02em;
    color: $text-primary;
    text-align: center;
    word-break: keep-all;
    text-transform: uppercase;
    text-shadow: 0 0 0.5vh rgba($text-primary, 0.3);
  }

  // 호버 효과 - 강화된 애니메이션
  &:hover:not(&--disabled):not(&--banned):not(&--picked) {
    transform: translateY(-0.3vh) scale(1.03);

    .algorithm-card__background {
      background: linear-gradient(
        135deg,
        rgba($electric-blue, 0.15) 0%,
        rgba($card-bg, 0.9) 30%,
        rgba($card-bg, 0.7) 70%,
        rgba($electric-blue, 0.1) 100%
      );
      border-color: rgba($electric-blue, 0.8);
      @include neon-glow-strong($electric-blue);

      &::before {
        background: repeating-linear-gradient(
          45deg,
          transparent,
          transparent 2px,
          rgba($electric-blue, 0.08) 2px,
          rgba($electric-blue, 0.08) 4px
        );
      }
    }

    .algorithm-card__border-frame {
      border-color: rgba($electric-blue, 0.6);
      animation: pulse-frame 1.5s ease-in-out infinite;

      &::before {
        width: 2.5vh;
        height: 2.5vh;
        border-width: 0.3vh;
        border-color: lighten($electric-blue, 30%);
        animation: corner-glow 1.8s ease-in-out infinite;
      }

      &::after {
        width: 2.5vh;
        height: 2.5vh;
        border-width: 0.3vh;
        border-color: lighten($electric-blue, 30%);
        animation: corner-glow 1.8s ease-in-out infinite 0.2s;
      }
    }

    .algorithm-card__type-label {
      color: lighten($electric-blue, 25%);
      background: rgba($electric-blue, 0.25);
      border-color: rgba($electric-blue, 0.7);
      transform: scale(1.05);
      box-shadow: 0 0 1vh rgba($electric-blue, 0.4);
    }

    .algorithm-card__name {
      color: lighten($text-primary, 15%);
      text-shadow:
        0 0 1vh rgba($electric-blue, 0.6),
        0 0 2vh rgba($electric-blue, 0.3);
      transform: scale(1.02);
    }
  }

  // 선택된 상태
  &--selected {
    transform: translateY(-0.1vh) scale(1.01);

    .algorithm-card__background {
      background: linear-gradient(
        135deg,
        rgba($cyberpunk-yellow, 0.2) 0%,
        rgba($cyberpunk-yellow, 0.1) 50%,
        rgba($electric-blue, 0.05) 100%
      );
      border-color: $cyberpunk-yellow;
      @include neon-glow-strong($cyberpunk-yellow);
    }

    .algorithm-card__border-frame {
      border-color: rgba($cyberpunk-yellow, 0.6);

      &::before,
      &::after {
        border-color: $cyberpunk-yellow;
      }
    }

    .algorithm-card__type-label {
      color: $cyberpunk-yellow;
      background: rgba($cyberpunk-yellow, 0.2);
      border-color: rgba($cyberpunk-yellow, 0.5);
    }

    .algorithm-card__name {
      color: lighten($cyberpunk-yellow, 20%);
      text-shadow: 0 0 1vh rgba($cyberpunk-yellow, 0.6);
    }
  }

  // 밴된 상태
  &--banned {
    cursor: not-allowed;
    filter: blur(0.1vh) grayscale(0.8);
    opacity: 0.5;
    transform: scale(0.95);

    .algorithm-card__background {
      background: linear-gradient(135deg, rgba(170, 17, 17, 0.3) 0%, rgba(204, 51, 51, 0.2) 50%, rgba($primary-bg, 0.9) 100%);
      border-color: #ff4757;
      box-shadow:
        0vh 0.6vh 2vh 0vh rgba(255, 71, 87, 0.3),
        inset 0vh 0.1vh 0vh 0vh rgba(255, 255, 255, 0.05);
    }

    .algorithm-card__border-frame {
      border-color: rgba(#ff4757, 0.4);

      &::before,
      &::after {
        border-color: #ff4757;
      }
    }

    .algorithm-card__type-label {
      color: rgba(#ff4757, 0.8);
      background: rgba(#ff4757, 0.1);
      border-color: rgba(#ff4757, 0.3);
    }

    .algorithm-card__name {
      color: rgba($text-primary, 0.4);
      text-shadow: none;
    }
  }

  // 픽된 상태
  &--picked {
    cursor: not-allowed;
    filter: blur(0.1vh) grayscale(0.8);
    opacity: 0.8;
    transform: scale(0.95);

    .algorithm-card__background {
      background: linear-gradient(135deg, rgba($text-gold, 0.3) 0%, rgba(166, 124, 42, 0.2) 50%, rgba($primary-bg, 0.9) 100%);
      border-color: $text-gold;
      box-shadow:
        0vh 0.6vh 2vh 0vh rgba($text-gold, 0.3),
        inset 0vh 0.1vh 0vh 0vh rgba(255, 255, 255, 0.05);
    }

    .algorithm-card__border-frame {
      border-color: rgba($text-gold, 0.5);

      &::before,
      &::after {
        border-color: $text-gold;
      }
    }

    .algorithm-card__type-label {
      color: $text-gold;
      background: rgba($text-gold, 0.1);
      border-color: rgba($text-gold, 0.3);
    }

    .algorithm-card__name {
      color: lighten($text-gold, 10%);
      text-shadow: 0 0 0.5vh rgba($text-gold, 0.4);
    }
  }

  // 비활성화된 상태 (카운트다운 중)
  &--disabled {
    cursor: not-allowed;
    pointer-events: none;
    opacity: 0.3;
    filter: grayscale(0.5);
    transform: scale(0.95);

    &:hover {
      transform: scale(0.95);

      .algorithm-card__background {
        box-shadow:
          0vh 0.6vh 2vh 0vh rgba(0, 0, 0, 0.4),
          inset 0vh 0.1vh 0vh 0vh rgba(255, 255, 255, 0.08);
      }
    }
  }
}

// 애니메이션 키프레임
@keyframes pulse-frame {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.7;
  }
}

@keyframes corner-glow {
  0%,
  100% {
    opacity: 0.6;
    transform: scale(1);
  }
  50% {
    opacity: 1;
    transform: scale(1.1);
  }
}
</style>
