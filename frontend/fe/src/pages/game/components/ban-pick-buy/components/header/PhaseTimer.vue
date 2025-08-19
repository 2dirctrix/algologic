<template>
  <div class="phase-timer">
    <div class="phase-timer__time" :class="{ 'phase-timer__time--pulsing': shouldHighlight }">
      {{ formattedTime }}
    </div>
    <div class="phase-timer__message">{{ message }}</div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';
import { PHASE_STATES } from '../../constants/phase-status.js';

const inGameStore = useInGameStore();

const currentPhase = computed(() => {
  return inGameStore.gameSync.phase || PHASE_STATES.BAN;
});

// 카운트다운 활성 여부
const isCountdownActive = computed(() => {
  return inGameStore.timerState.isCountdownActive;
});

// 페이즈 타이머에서 5초 이하일 때 강조 스타일 적용 여부
const shouldHighlight = computed(() => {
  if (isCountdownActive.value) {
    return true; // 카운트다운 중일 때는 항상 강조
  } else if (isPhaseTimerActive.value) {
    return displayTime.value <= 5; // 페이즈 타이머에서 5초 이하일 때 강조
  }
  return false;
});

// 페이즈 타이머 활성 여부
const isPhaseTimerActive = computed(() => {
  return inGameStore.timerState.isPhaseTimerActive;
});

// 표시할 시간 값 계산
const displayTime = computed(() => {
  if (isCountdownActive.value) {
    // 카운트다운 중일 때는 카운트다운 시간을 초 단위로 표시
    return Math.ceil(inGameStore.timerState.countdownBeforeStart / 1000);
  } else if (isPhaseTimerActive.value) {
    // 페이즈 타이머 중일 때는 페이즈 잔여시간을 초 단위로 표시
    return Math.ceil(inGameStore.timerState.phaseRemainingTime / 1000);
  } else {
    // 기본값으로 gameSync.remainingTime 사용 (호환성)
    return inGameStore.gameSync.remainingTime || 0;
  }
});

const formattedTime = computed(() => {
  // 카운트다운 및 페이즈타이머 모두 00초 형식으로 표시
  return `${displayTime.value.toString().padStart(2, '0')}`;
});

const message = computed(() => {
  if (isCountdownActive.value) {
    // 카운트다운 중일 때의 준비 메시지
    const countdownMessageMap = {
      [PHASE_STATES.BAN]: '밴을 준비하세요',
      [PHASE_STATES.PICK]: '픽을 준비하세요',
      [PHASE_STATES.BUY]: '구매를 준비하세요',
    };
    return countdownMessageMap[currentPhase.value] || '준비하세요';
  } else {
    // 페이즈 진행 중일 때의 기존 메시지
    const messageMap = {
      [PHASE_STATES.BAN]: '밴할 알고리즘을 선택하세요',
      [PHASE_STATES.PICK]: '픽할 알고리즘을 선택하세요',
      [PHASE_STATES.BUY]: '아이템과 스펠을 구매하세요',
    };
    return messageMap[currentPhase.value] || '알고리즘을 선택하세요';
  }
});
</script>

<style lang="scss" scoped>
.phase-timer {
  text-align: center;
  padding: 1vh 2vh;

  &__time {
    font-family: $font-accent;
    font-size: 8vh;
    line-height: 1.2;
    letter-spacing: 0.05em;
    color: $text-primary;
    margin-bottom: 0.5vh;
    transition: all 0.3s ease;

    // 카운트다운 중 박동 애니메이션
    &--pulsing {
      animation: countdown-pulse 1s ease-in-out infinite;
      color: #ff6b6b; // 긴급감을 주는 빨간색
      text-shadow: 0 0 10px rgba(255, 107, 107, 0.5);
    }
  }

  &__message {
    font-family: $font-primary;
    font-weight: $font-weight-regular;
    font-size: 1.8vh;
    line-height: 1.5;
    letter-spacing: 0.025em;
    color: rgba($text-primary, 0.7);
  }
}

// 카운트다운 박동 애니메이션
@keyframes countdown-pulse {
  0% {
    transform: scale(1.1);
    opacity: 1;
  }
  50% {
    transform: scale(1.3);
    opacity: 0.8;
  }
  100% {
    transform: scale(1.2);
    opacity: 1;
  }
}
</style>
