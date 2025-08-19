<template>
  <div class="progress-display">
    <div class="progress-circle">
      <svg class="circle-svg" viewBox="0 0 100 100">
        <!-- 배경 원 -->
        <circle cx="50" cy="50" r="45" fill="none" stroke="rgba(255, 255, 255, 0.1)" stroke-width="8" />
        <!-- 진행률 원 -->
        <circle
          cx="50"
          cy="50"
          r="45"
          fill="none"
          :stroke="color"
          stroke-width="8"
          stroke-linecap="round"
          :stroke-dasharray="circumference"
          :stroke-dashoffset="strokeDashoffset"
          transform="rotate(-90 50 50)"
          class="progress-arc"
          :style="{ '--target-offset': strokeDashoffset }"
        />
      </svg>
      <div class="progress-text">
        <span class="progress-percentage">{{ displayPercentage }}%</span>
      </div>
    </div>
    <div class="progress-label">{{ label }}</div>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  percentage: {
    type: Number,
    required: true,
    validator: value => value >= 0 && value <= 100,
  },
  label: {
    type: String,
    required: true,
  },
  color: {
    type: String,
    default: '#00ff88',
  },
  decimals: {
    type: Number,
    default: 0,
  },
});

// 표시할 퍼센트 (소수점 처리)
const displayPercentage = computed(() => {
  if (props.decimals === 0) {
    return Math.round(props.percentage);
  }
  return props.percentage.toFixed(props.decimals);
});

// SVG 원의 둘레
const circumference = computed(() => 2 * Math.PI * 45);

// stroke-dashoffset 계산
const strokeDashoffset = computed(() => {
  return circumference.value - (circumference.value * props.percentage) / 100;
});
</script>

<style lang="scss" scoped>
.progress-display {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: calc(1.5vh);

  .progress-circle {
    position: relative;
    width: calc(12vh);
    height: calc(12vh);
    cursor: pointer;
    transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);

    &:hover {
      transform: scale(1.1);
    }

    .circle-svg {
      width: 100%;
      height: 100%;
      transform: rotate(0deg);
    }

    .progress-arc {
      transition: stroke-dashoffset 1s ease-in-out;
    }

    // 호버 시 테두리 애니메이션
    &:hover .progress-arc {
      animation: borderFill 1.5s ease-in-out;
      animation-fill-mode: forwards;
    }

    .progress-text {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      text-align: center;

      .progress-percentage {
        font-family: $font-accent;
        font-size: calc(2.2vh);
        font-weight: $font-weight-bold;
        color: $text-primary;
        text-shadow: 0 0 calc(0.3vh) rgba($text-primary, 0.3);
      }
    }
  }

  .progress-label {
    font-family: $font-primary;
    font-size: calc(1.4vh);
    font-weight: $font-weight-medium;
    color: rgba($text-primary, 0.8);
    text-transform: uppercase;
    letter-spacing: calc(0.1vh);
  }
}

// 테두리 차오르는 애니메이션
@keyframes borderFill {
  0% {
    stroke-dashoffset: 283; // 전체 둘레 (2 * π * 45 ≈ 283) - 빈 상태
  }
  100% {
    stroke-dashoffset: var(--target-offset); // 현재 진행률까지만 차오름
  }
}
</style>
