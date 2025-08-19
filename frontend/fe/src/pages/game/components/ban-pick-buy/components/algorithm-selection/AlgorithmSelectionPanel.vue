<template>
  <div class="algorithm-selection-panel">
    <div class="algorithm-selection-panel__background"></div>

    <!-- 타이머 및 안내 -->
    <div class="algorithm-selection-panel__header">
      <PhaseTimer :remaining-time="remainingTime" :message="timerMessage" />
    </div>

    <!-- 알고리즘 그리드 -->
    <div class="algorithm-selection-panel__grid">
      <AlgorithmCard
        v-for="algorithm in algorithmCategories"
        :key="algorithm.categoryId"
        :algorithm="algorithm"
        :status="getAlgorithmStatus(algorithm.categoryId)"
        :selected="selectedAlgorithm && selectedAlgorithm.categoryId === algorithm.categoryId"
        @select="handleAlgorithmSelect"
        class="algorithm-selection-panel__card"
      />
    </div>

    <!-- 선택 버튼 영역 -->
    <div class="algorithm-selection-panel__actions">
      <button
        class="algorithm-selection-panel__select-btn"
        :disabled="!selectedAlgorithm || isSelectionComplete || isInteractionDisabled"
        @click="handleSelectConfirm"
      >
        {{ isSelectionComplete ? '선택 완료' : '선택' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';
import PhaseTimer from '../header/PhaseTimer.vue';
import AlgorithmCard from './AlgorithmCard.vue';
import { ALGORITHM_STATUS } from '../../constants/phase-status.js';

// Props
const props = defineProps({
  bannedAlgorithms: {
    type: Array,
    default: () => [],
  },
  pickedAlgorithms: {
    type: Array,
    default: () => [],
  },
  remainingTime: {
    type: Number,
    default: 0,
  },
  currentPhase: {
    type: String,
    required: true,
  },
  isSelectionComplete: {
    type: Boolean,
    default: false,
  },
});

// Emits
const emit = defineEmits(['algorithm-select']);

// Store
const inGameStore = useInGameStore();

// Reactive data
const selectedAlgorithm = ref(null);

// Computed
const algorithmCategories = computed(() => {
  return inGameStore.algorithmCategories;
});

const timerMessage = computed(() => {
  if (props.currentPhase === 'ban') {
    return '알고리즘을 선택하세요';
  } else if (props.currentPhase === 'pick') {
    return '알고리즘을 선택하세요';
  }
  return '아이템/스펠을 구매하세요';
});

const isInteractionDisabled = computed(() => {
  return inGameStore.timerState.isCountdownActive;
});

// Methods
const playButtonSound = () => {
  try {
    const audio = new Audio('/audio/interaction/chosen-sound.wav');
    audio.volume = 0.6;
    audio.play().catch(error => {
      console.warn('Audio play failed:', error);
    });
  } catch (error) {
    console.warn('Audio creation failed:', error);
  }
};

const getAlgorithmStatus = algorithmId => {
  if (props.bannedAlgorithms.includes(algorithmId)) {
    return ALGORITHM_STATUS.BANNED;
  }
  if (props.pickedAlgorithms.includes(algorithmId)) {
    return ALGORITHM_STATUS.PICKED;
  }
  return ALGORITHM_STATUS.AVAILABLE;
};

const handleAlgorithmSelect = algorithm => {
  if (isInteractionDisabled.value) return;
  if (!props.isSelectionComplete && getAlgorithmStatus(algorithm.categoryId) === ALGORITHM_STATUS.AVAILABLE) {
    selectedAlgorithm.value = algorithm;
  }
};

const handleSelectConfirm = () => {
  if (isInteractionDisabled.value) return;
  if (selectedAlgorithm.value && !props.isSelectionComplete) {
    playButtonSound();
    emit('algorithm-select', selectedAlgorithm.value);
    selectedAlgorithm.value = null;
  }
};
</script>

<style lang="scss" scoped>
@use 'sass:color';

.algorithm-selection-panel {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;

  &__background {
    position: absolute;
    inset: 0;
    background: rgba($card-bg, 0.4);
    border: 0.2vh solid $neon-magenta;
    border-radius: 2vh;
    box-shadow:
      0vh 0.8vh 3.2vh 0vh rgba(0, 0, 0, 0.4),
      0vh 0.1vh 0vh 0vh rgba(255, 255, 255, 0.1);
  }

  &__header {
    position: relative;
    z-index: 2;
    padding: 2vh;
    flex-shrink: 0;
  }

  &__grid {
    position: relative;
    z-index: 2;
    flex: 1;
    padding: 1vh 2vh;
    margin: 0 3vh;
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    grid-auto-rows: 12vh; // 고정 높이
    gap: 1.5vh;
    min-height: 0;
    max-height: 100%;
    overflow-y: auto;
    overflow-x: hidden;
    align-content: start;

    // 스크롤바 스타일링
    &::-webkit-scrollbar {
      width: 0.6vh;
    }

    &::-webkit-scrollbar-track {
      background: rgba($primary-bg, 0.3);
      border-radius: 0.3vh;
    }

    &::-webkit-scrollbar-thumb {
      background: $neon-magenta;
      border-radius: 0.3vh;

      &:hover {
        background: lighten($neon-magenta, 10%);
      }
    }

    // Firefox 스크롤바
    scrollbar-width: thin;
    scrollbar-color: $neon-magenta rgba($primary-bg, 0.3);
  }

  &__actions {
    position: relative;
    z-index: 2;
    padding: 1vh 2vh 2vh 2vh;
    flex-shrink: 0;
    min-height: 8vh;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  &__select-btn {
    background: linear-gradient(135deg, rgba($cyberpunk-yellow, 0.2) 0%, rgba($cyberpunk-yellow, 0.1) 50%, rgba($electric-blue, 0.1) 100%);
    border: 0.3vh solid $cyberpunk-yellow;
    border-radius: 1.5vh;
    color: $cyberpunk-yellow;
    font-family: $font-primary;
    font-size: 2.2vh;
    font-weight: 700;
    padding: 1.8vh 4vh;
    cursor: pointer;
    transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
    min-width: 16vh;
    min-height: 6vh;
    position: relative;
    overflow: hidden;
    text-transform: uppercase;
    letter-spacing: 0.1em;

    // 내부 글로우 효과
    &::before {
      content: '';
      position: absolute;
      inset: 0.2vh;
      background: linear-gradient(135deg, rgba($cyberpunk-yellow, 0.05) 0%, transparent 50%, rgba($electric-blue, 0.05) 100%);
      border-radius: 1.2vh;
      z-index: -1;
      transition: all 0.4s ease;
    }

    &:hover:not(:disabled) {
      @include neon-glow-strong($cyberpunk-yellow);
      transform: translateY(-0.3vh) scale(1.02);
      border-color: lighten($cyberpunk-yellow, 15%);
      color: lighten($cyberpunk-yellow, 20%);
      background: linear-gradient(
        135deg,
        rgba($cyberpunk-yellow, 0.3) 0%,
        rgba($cyberpunk-yellow, 0.2) 50%,
        rgba($electric-blue, 0.15) 100%
      );

      &::before {
        background: linear-gradient(
          135deg,
          rgba($cyberpunk-yellow, 0.1) 0%,
          rgba($electric-blue, 0.05) 50%,
          rgba($cyberpunk-yellow, 0.1) 100%
        );
      }
    }

    &:active:not(:disabled) {
      transform: translateY(-0.1vh) scale(1.01);
      transition: all 0.15s ease;
    }

    &:disabled {
      background: rgba($cyberpunk-yellow, 0.1);
      border-color: rgba($cyberpunk-yellow, 0.3);
      color: rgba($cyberpunk-yellow, 0.4);
      cursor: not-allowed;
      transform: none;

      &::before {
        background: rgba($cyberpunk-yellow, 0.02);
      }
    }
  }
}
</style>
