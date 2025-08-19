<template>
  <div v-if="status !== 'available'" class="algorithm-status-overlay" :class="`algorithm-status-overlay--${status}`">
    <div class="algorithm-status-overlay__background"></div>
    <div class="algorithm-status-overlay__frame"></div>
    <div class="algorithm-status-overlay__content">
      <div class="algorithm-status-overlay__text">{{ statusText }}</div>
      <div class="algorithm-status-overlay__glow"></div>
    </div>
  </div>
</template>

<script>
import { ALGORITHM_STATUS } from '../../constants/phase-status.js';

export default {
  name: 'AlgorithmStatusOverlay',
  props: {
    status: {
      type: String,
      required: true,
      validator: value => Object.values(ALGORITHM_STATUS).includes(value),
    },
  },
  computed: {
    statusText() {
      const textMap = {
        [ALGORITHM_STATUS.BANNED]: 'BANNED',
        [ALGORITHM_STATUS.PICKED]: 'PICKED',
      };
      return textMap[this.status] || '';
    },
  },
};
</script>

<style lang="scss" scoped>
@use 'sass:color';
.algorithm-status-overlay {
  position: absolute;
  inset: 0;
  border-radius: 1vh;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 15;
  animation: overlay-appear 0.3s ease-out;

  &__background {
    position: absolute;
    inset: 0;
    border-radius: 1vh;
    backdrop-filter: blur(0.8vh);
    -webkit-backdrop-filter: blur(0.8vh);
  }

  &__frame {
    position: absolute;
    inset: 0.2vh;
    border-radius: 0.8vh;
    border: 0.2vh solid transparent;
    z-index: 1;

    // 모서리 장식
    &::before {
      content: '';
      position: absolute;
      top: -0.2vh;
      left: -0.2vh;
      width: 2vh;
      height: 2vh;
      border-top: 0.3vh solid currentColor;
      border-left: 0.3vh solid currentColor;
      border-radius: 0.3vh 0 0 0;
      opacity: 0.8;
    }

    &::after {
      content: '';
      position: absolute;
      bottom: -0.2vh;
      right: -0.2vh;
      width: 2vh;
      height: 2vh;
      border-bottom: 0.3vh solid currentColor;
      border-right: 0.3vh solid currentColor;
      border-radius: 0 0 0.3vh 0;
      opacity: 0.8;
    }
  }

  &__content {
    position: relative;
    z-index: 3;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
  }

  &__text {
    font-family: $font-primary;
    font-size: 2.4vh;
    font-weight: 900;
    line-height: 1;
    letter-spacing: 0.25em;
    text-transform: uppercase;
    text-align: center;
    color: #ffffff;
    text-shadow:
      0 0 0.3vh rgba(0, 0, 0, 0.9),
      0 2px 4px rgba(0, 0, 0, 0.8);
  }

  &__glow {
    position: absolute;
    inset: -1vh;
    border-radius: 2vh;
    opacity: 0.3;
    animation: glow-pulse 2.5s ease-in-out infinite;
    pointer-events: none;
  }

  // BANNED 상태 - 강렬한 빨간색
  &--banned {
    .algorithm-status-overlay__background {
      background: linear-gradient(135deg, rgba(255, 71, 87, 0.85) 0%, rgba(170, 17, 17, 0.9) 50%, rgba(139, 0, 0, 0.95) 100%);
    }

    .algorithm-status-overlay__frame {
      border-color: rgba(#ff4757, 0.6);
      color: #ff4757;
      animation: frame-warning 1.5s ease-in-out infinite;

      &::before,
      &::after {
        border-color: color.adjust(#ff4757, $lightness: 20%);
        animation: corner-flash 2s ease-in-out infinite;
      }
    }

    .algorithm-status-overlay__text {
      color: #ffffff;
      text-shadow:
        0 0 0.8vh #ff4757,
        0 0 1.5vh #ff4757,
        0 2px 6px rgba(0, 0, 0, 0.9);
    }

    .algorithm-status-overlay__glow {
      background: radial-gradient(circle, rgba(#ff4757, 0.4) 0%, rgba(#ff4757, 0.2) 50%, transparent 100%);
      animation: glow-pulse 1.8s ease-in-out infinite;
    }
  }

  // PICKED 상태 - 우아한 금색
  &--picked {
    .algorithm-status-overlay__background {
      background: linear-gradient(135deg, rgba($text-gold, 0.85) 0%, rgba(166, 124, 42, 0.9) 50%, rgba(139, 105, 20, 0.95) 100%);
    }

    .algorithm-status-overlay__frame {
      border-color: rgba($text-gold, 0.6);
      color: $text-gold;

      &::before,
      &::after {
        border-color: color.adjust($text-gold, $lightness: 15%);
      }
    }

    .algorithm-status-overlay__text {
      color: #ffffff;
      text-shadow:
        0 0 0.8vh $text-gold,
        0 0 1.2vh $text-gold,
        0 2px 6px rgba(0, 0, 0, 0.9);
    }

    .algorithm-status-overlay__glow {
      background: radial-gradient(circle, rgba($text-gold, 0.3) 0%, rgba($text-gold, 0.15) 50%, transparent 100%);
    }
  }
}

// 애니메이션 키프레임
@keyframes overlay-appear {
  0% {
    opacity: 0;
    transform: scale(0.8);
  }
  100% {
    opacity: 1;
    transform: scale(1);
  }
}

@keyframes glow-pulse {
  0%,
  100% {
    opacity: 0.2;
    transform: scale(1);
  }
  50% {
    opacity: 0.4;
    transform: scale(1.05);
  }
}

@keyframes frame-warning {
  0%,
  100% {
    border-color: rgba(#ff4757, 0.6);
  }
  50% {
    border-color: rgba(#ff4757, 0.9);
  }
}

@keyframes corner-flash {
  0%,
  70%,
  100% {
    opacity: 0.8;
  }
  35% {
    opacity: 1;
    transform: scale(1.1);
  }
}
</style>
