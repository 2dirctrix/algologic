<template>
  <div
    class="phase-indicator"
    :style="animationStyles"
    :class="[`phase-indicator--${phase}`, `phase-indicator--${status}`, `phase-indicator--${position}`]"
  >
    <svg class="phase-indicator__svg" viewBox="0 0 100 40" preserveAspectRatio="none">
      <defs>
        <linearGradient :id="`gradient-${phase}`" x1="0%" y1="0%" x2="100%" y2="0%">
          <stop offset="0%" :stop-color="gradientStart" :stop-opacity="gradientOpacity" />
          <stop offset="100%" :stop-color="gradientEnd" :stop-opacity="gradientOpacity" />
        </linearGradient>
        <clipPath :id="`clip-progress-${phase}`">
          <rect class="phase-indicator__progress-clipper" :x="clipStartX" y="0" width="120" height="40" />
        </clipPath>
        <clipPath :id="`clip-${phase}`">
          <path :d="svgPath" />
        </clipPath>
      </defs>
      <g :clip-path="`url(#clip-${phase})`">
        <path :d="svgPath" :fill="backgroundColor" />
        <path :d="svgPath" :fill="`url(#gradient-${phase})`" :clip-path="`url(#clip-progress-${phase})`" />
        <path :d="svgPath" fill="none" :stroke="borderColor" :stroke-width="borderWidth" class="phase-indicator__border-path" />
      </g>
    </svg>

    <div class="phase-indicator__content">
      <span class="phase-indicator__text">{{ phaseText }}</span>
    </div>
  </div>
</template>

<script>
import { PHASE_STATES, INDICATOR_STATUS } from '../../constants/phase-status.js';

export default {
  name: 'PhaseIndicator',
  props: {
    phase: { type: String, required: true, validator: value => Object.values(PHASE_STATES).includes(value) },
    status: { type: String, required: true, validator: value => Object.values(INDICATOR_STATUS).includes(value) },
    totalDuration: { type: Number, default: 0 },
    remainingTime: { type: Number, default: 0 },
    position: { type: String, default: 'middle', validator: value => ['first', 'middle', 'last'].includes(value) },
    bannedAlgorithmName: { type: String, default: null },
  },
  data() {
    return {
      slope: 10,
    };
  },
  computed: {
    progressRatio() {
      if (this.status === INDICATOR_STATUS.COMPLETED) return 1;
      if (this.status === INDICATOR_STATUS.WAITING) return 0;

      // ACTIVE 상태일 때: totalDuration과 remainingTime으로 실시간 계산
      if (this.totalDuration <= 0) return 0;
      const elapsed = this.totalDuration - this.remainingTime;
      return Math.max(0, Math.min(1, elapsed / this.totalDuration));
    },
    animationStyles() {
      const baseStyles = {
        '--transform-origin': this.transformOrigin,
        '--progress': this.progressRatio,
      };

      return baseStyles;
    },
    transformOrigin() {
      if (this.position === 'first') return 'left';
      return `${-this.slope}px 50%`; // '-10px 50%'
    },
    clipStartX() {
      if (this.position === 'first') return 0;
      return -this.slope; // -10
    },
    phaseText() {
      if (this.phase === PHASE_STATES.BAN && this.status === INDICATOR_STATUS.COMPLETED && this.bannedAlgorithmName) {
        return `${this.bannedAlgorithmName} 밴`;
      }

      const textMap = {
        [PHASE_STATES.BAN]: '알고리즘 밴',
        [PHASE_STATES.PICK]: '알고리즘 픽',
        [PHASE_STATES.BUY]: '아이템 / 스펠 구매',
      };
      return textMap[this.phase];
    },
    svgPath() {
      const radius = 10; // 곡률 반지름

      if (this.position === 'first') {
        // 좌측 상단과 하단에 곡률 추가
        return `M ${radius} 0 Q 0 0 0 ${radius} L 0 ${40 - radius} Q 0 40 ${radius} 40 L ${100 + this.slope} 40 L ${100 - this.slope} 0 Z`;
      }

      if (this.position === 'last') {
        // 우측 상단과 하단에 곡률 추가
        return `M ${-this.slope} 0 L ${100 - radius} 0 Q 100 0 100 ${radius} L 100 ${40 - radius} Q 100 40 ${100 - radius} 40 L ${this.slope} 40 L ${-this.slope} 0 Z`;
      }

      return `M ${-this.slope} 0 L ${100 - this.slope} 0 L ${100 + this.slope} 40 L ${this.slope} 40 L ${-this.slope} 0 Z`;
    },
    gradientStart() {
      const colors = {
        [PHASE_STATES.BAN]: '#8F00FF', // $synthwave-purple
        [PHASE_STATES.PICK]: '#FFCC00', // $cyberpunk-yellow
        [PHASE_STATES.BUY]: '#FF3D94', // $cyberpunk-pink
      };
      return colors[this.phase] || '#6B7280';
    },
    gradientEnd() {
      const colors = {
        [PHASE_STATES.BAN]: '#CC00FF', // $electric-violet
        [PHASE_STATES.PICK]: '#C89B3C', // $text-gold
        [PHASE_STATES.BUY]: '#FF0066', // $hot-pink
      };
      return colors[this.phase] || '#4B5563';
    },
    gradientOpacity() {
      if (this.status === INDICATOR_STATUS.ACTIVE) return 0.85;
      if (this.status === INDICATOR_STATUS.COMPLETED) return 0.7;
      return 0.85;
    },
    borderColor() {
      const colors = {
        [PHASE_STATES.BAN]: '#8F00FF', // $synthwave-purple
        [PHASE_STATES.PICK]: '#FFCC00', // $cyberpunk-yellow
        [PHASE_STATES.BUY]: '#FF3D94', // $cyberpunk-pink
      };
      return colors[this.phase] || '#6B7280';
    },
    backgroundColor() {
      if (this.status === INDICATOR_STATUS.ACTIVE) {
        // 자기 단계일 때 낮은 투명도 (더 진하게)
        const colors = {
          [PHASE_STATES.BAN]: 'rgba(143, 0, 255, 0.3)', // $synthwave-purple
          [PHASE_STATES.PICK]: 'rgba(255, 204, 0, 0.3)', // $cyberpunk-yellow
          [PHASE_STATES.BUY]: 'rgba(255, 61, 148, 0.3)', // $cyberpunk-pink
        };
        return colors[this.phase];
      }
      // 자기 단계가 아닐 때 높은 투명도 (더 연하게)
      const colors = {
        [PHASE_STATES.BAN]: 'rgba(143, 0, 255, 0.08)', // $synthwave-purple
        [PHASE_STATES.PICK]: 'rgba(255, 204, 0, 0.08)', // $cyberpunk-yellow
        [PHASE_STATES.BUY]: 'rgba(255, 61, 148, 0.08)', // $cyberpunk-pink
      };
      return colors[this.phase] || 'rgba(107, 114, 128, 0.1)';
    },
    borderWidth() {
      return this.status === INDICATOR_STATUS.ACTIVE ? '5' : '2';
    },
  },
};
</script>

<style lang="scss" scoped>
@use 'sass:color';

$font-primary: 'Noto Sans KR', sans-serif;
$text-primary: #ffffff;
$synthwave-purple: #8f00ff;
$electric-violet: #cc00ff;
$cyberpunk-yellow: #ffcc00;
$text-gold: #c89b3c;
$cyberpunk-pink: #ff3d94;
$hot-pink: #ff0066;

@keyframes fillAnimation {
  from {
    transform: scaleX(0);
  }
  to {
    transform: scaleX(1);
  }
}

.phase-indicator {
  position: relative;
  height: 100%;
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;

  &:first-child {
    flex: 1;
    z-index: 3;
  }
  &--middle {
    flex: 1;
    z-index: 2;
  }
  &:last-child {
    flex: 1;
    z-index: 1;
  }

  &__svg {
    position: absolute;
    inset: 0;
    width: 100%;
    height: 100%;
    overflow: visible;
  }

  &__progress-clipper {
    transform-origin: var(--transform-origin, left);
    transform: scaleX(var(--progress, 0));
    transition: transform 1s linear;
  }

  &__border-path {
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    filter: drop-shadow(0 0 8px currentColor);
  }

  &__content {
    position: relative;
    z-index: 2;
    padding: 1vh 1.5vh;
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
  }

  &__text {
    font-family: $font-primary;
    font-size: 2vh;
    font-weight: 600;
    line-height: 1.2;
    color: $text-primary;
    white-space: nowrap;
    text-align: center;
    text-shadow: 0 0.2vh 0.4vh rgba(0, 0, 0, 0.3);
    letter-spacing: 0.05vh;
  }

  &--waiting .phase-indicator__text {
    color: rgba($text-primary, 0.7);
  }

  &--completed {
    .phase-indicator__border-path {
      filter: none;
    }
    .phase-indicator__text {
      color: rgba($text-primary, 0.8);
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
    }
  }

  &--ban.phase-indicator--active {
    .phase-indicator__border-path {
      filter: drop-shadow(0 0 15px $synthwave-purple);
    }
    .phase-indicator__text {
      color: color.adjust($synthwave-purple, $lightness: 25%);
      text-shadow: 0 0 10px rgba($synthwave-purple, 0.7);
      font-weight: 700;
    }
  }

  &--pick.phase-indicator--active {
    .phase-indicator__border-path {
      filter: drop-shadow(0 0 15px $cyberpunk-yellow);
    }
    .phase-indicator__text {
      color: color.adjust($cyberpunk-yellow, $lightness: 15%);
      text-shadow: 0 0 10px rgba($cyberpunk-yellow, 0.7);
      font-weight: 700;
    }
  }

  &--buy.phase-indicator--active {
    .phase-indicator__border-path {
      filter: drop-shadow(0 0 15px $hot-pink);
    }
    .phase-indicator__text {
      color: color.adjust($hot-pink, $lightness: 15%);
      text-shadow: 0 0 10px rgba($hot-pink, 0.7);
      font-weight: 700;
    }
  }
}
</style>
