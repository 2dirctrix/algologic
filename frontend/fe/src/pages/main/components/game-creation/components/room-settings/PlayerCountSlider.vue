<template>
  <div class="player-count-slider">
    <label class="slider-label">참가 인원</label>

    <!-- 버튼 컨트롤 -->
    <div class="button-controls">
      <button
        v-for="count in availableCounts"
        :key="count"
        class="count-button"
        :class="{ 'count-button--active': playerCount === count }"
        @click="updatePlayerCount(count)"
      >
        {{ count }}명
      </button>
    </div>

    <div class="slider-container">
      <!-- 슬라이더 트랙 -->
      <div class="slider-track" @click="handleSliderClick" @mousedown="startDragging">
        <div class="slider-fill" :style="{ width: fillWidth }"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onUnmounted } from 'vue';

const props = defineProps({
  modelValue: {
    type: Number,
    default: 4,
  },
  min: {
    type: Number,
    default: 2,
  },
  max: {
    type: Number,
    default: 6,
  },
});

const emit = defineEmits(['update:modelValue']);

const playerCount = ref(props.modelValue);
const isDragging = ref(false);

const fillWidth = computed(() => {
  const percentage = ((playerCount.value - props.min) / (props.max - props.min)) * 100;
  return `${percentage}%`;
});

const availableCounts = computed(() => {
  const counts = [];
  for (let i = props.min; i <= props.max; i++) {
    counts.push(i);
  }
  return counts;
});

// 메서드
const updatePlayerCount = newCount => {
  const clampedCount = Math.max(props.min, Math.min(props.max, Math.round(newCount)));
  if (clampedCount !== playerCount.value) {
    playerCount.value = clampedCount;
    emit('update:modelValue', clampedCount);
  }
};

const handleSliderClick = event => {
  if (isDragging.value) return;

  const rect = event.currentTarget.getBoundingClientRect();
  const clickX = event.clientX - rect.left;
  const percentage = clickX / rect.width;
  const newCount = props.min + percentage * (props.max - props.min);
  updatePlayerCount(newCount);
};

const startDragging = event => {
  isDragging.value = true;
  document.addEventListener('mousemove', handleMouseMove);
  document.addEventListener('mouseup', stopDragging);
  event.preventDefault();
};

const handleMouseMove = event => {
  if (!isDragging.value) return;

  const sliderRect = document.querySelector('.slider-track')?.getBoundingClientRect();
  if (!sliderRect) return;

  const mouseX = event.clientX - sliderRect.left;
  const percentage = Math.max(0, Math.min(1, mouseX / sliderRect.width));
  const newCount = props.min + percentage * (props.max - props.min);
  updatePlayerCount(newCount);
};

const stopDragging = () => {
  isDragging.value = false;
  document.removeEventListener('mousemove', handleMouseMove);
  document.removeEventListener('mouseup', stopDragging);
};

onUnmounted(() => {
  document.removeEventListener('mousemove', handleMouseMove);
  document.removeEventListener('mouseup', stopDragging);
});
</script>

<style scoped lang="scss">
.player-count-slider {
  margin-bottom: calc(2vh);
}

.slider-label {
  display: block;
  font-family: $font-primary;
  font-weight: $font-weight-semibold;
  font-size: calc(1.7vh);
  color: $text-primary;
  margin-bottom: calc(2vh);
}

.button-controls {
  display: flex;
  gap: calc(1vh);
  margin-bottom: calc(2vh);
  justify-content: center;
}

.count-button {
  background: linear-gradient(135deg, rgba($electric-blue, 0.1) 0%, rgba($card-bg, 0.7) 50%, rgba($electric-blue, 0.05) 100%);
  border: calc(0.2vh) solid rgba($electric-blue, 0.4);
  border-radius: calc(0.8vh);
  color: $electric-blue;
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: calc(1.4vh);
  padding: calc(0.8vh) calc(1.5vh);
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  position: relative;
  overflow: hidden;
  min-width: calc(4vh);
  text-align: center;

  // 내부 글로우 효과
  &::before {
    content: '';
    position: absolute;
    inset: 0.1vh;
    background: linear-gradient(135deg, rgba($electric-blue, 0.03) 0%, transparent 50%, rgba($electric-blue, 0.03) 100%);
    border-radius: calc(0.6vh);
    transition: all 0.4s ease;
    z-index: -1;
  }

  &:hover:not(.count-button--active) {
    background: linear-gradient(135deg, rgba($electric-blue, 0.2) 0%, rgba($card-bg, 0.8) 50%, rgba($electric-blue, 0.15) 100%);
    border-color: rgba($electric-blue, 0.6);
    color: lighten($electric-blue, 15%);
    transform: translateY(-0.1vh) scale(1.05);
    @include neon-glow($electric-blue);

    &::before {
      background: linear-gradient(135deg, rgba($electric-blue, 0.08) 0%, rgba($electric-blue, 0.03) 50%, rgba($electric-blue, 0.08) 100%);
    }
  }

  &:active:not(.count-button--active) {
    transform: translateY(0) scale(1.02);
    transition: all 0.15s ease;
  }

  &--active {
    background: linear-gradient(135deg, rgba($cyberpunk-yellow, 0.3) 0%, rgba($cyberpunk-yellow, 0.2) 50%, rgba($electric-blue, 0.15) 100%);
    border: calc(0.3vh) solid $cyberpunk-yellow;
    color: $cyberpunk-yellow;
    transform: scale(1.1);
    @include neon-glow-strong($cyberpunk-yellow);
    box-shadow:
      0 0 8px rgba($cyberpunk-yellow, 0.4),
      inset 0 0 8px rgba($cyberpunk-yellow, 0.1);

    &::before {
      background: linear-gradient(135deg, rgba($cyberpunk-yellow, 0.1) 0%, transparent 50%, rgba($electric-blue, 0.05) 100%);
    }

    &:hover {
      transform: scale(1.1);
      @include neon-glow-strong($cyberpunk-yellow);
    }
  }
}

.slider-container {
  position: relative;
  height: calc(4vh);
  width: 100%;
}

.slider-track {
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: calc(3vh);
  background: linear-gradient(135deg, rgba($electric-blue, 0.2) 0%, rgba($card-bg, 0.6) 50%, rgba($electric-blue, 0.15) 100%);
  border: calc(0.1vh) solid rgba($electric-blue, 0.4);
  border-radius: calc(1vh);
  transform: translateY(-50%);
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;

  &:hover {
    background: linear-gradient(135deg, rgba($electric-blue, 0.3) 0%, rgba($card-bg, 0.7) 50%, rgba($electric-blue, 0.25) 100%);
    border-color: rgba($electric-blue, 0.6);
    @include neon-glow($electric-blue);
  }

  &:active {
    background: linear-gradient(135deg, rgba($cyberpunk-yellow, 0.2) 0%, rgba($card-bg, 0.8) 50%, rgba($cyberpunk-yellow, 0.15) 100%);
    border-color: rgba($cyberpunk-yellow, 0.6);
    @include neon-glow($cyberpunk-yellow);
  }
}

.slider-fill {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  background: linear-gradient(135deg, rgba($cyberpunk-yellow, 0.4) 0%, rgba($cyberpunk-yellow, 0.3) 50%, rgba($electric-blue, 0.2) 100%);
  border-radius: calc(1vh);
  transition: all 0.3s ease;
  z-index: 1;

  .slider-track:hover & {
    background: linear-gradient(135deg, rgba($cyberpunk-yellow, 0.5) 0%, rgba($cyberpunk-yellow, 0.4) 50%, rgba($electric-blue, 0.3) 100%);
  }
}

.slider-value-indicator {
  position: relative;
  z-index: 2;
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: calc(2.3vh);
  color: $cyberpunk-yellow;
  text-shadow: 0 0 4px rgba($cyberpunk-yellow, 0.8);
  pointer-events: none;
}
</style>
