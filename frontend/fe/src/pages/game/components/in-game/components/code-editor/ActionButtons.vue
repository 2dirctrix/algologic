<!-- src/pages/game/components/in-game/components/code-editor/ActionButtons.vue -->
<template>
  <div class="action-buttons">
    <button class="btn-action btn-surrender" @click="handleSurrender">조기종료</button>
    <button
      class="btn-action btn-submit"
      :class="{ 'btn-submit--loading': isSubmitting }"
      :disabled="isSubmitDisabled"
      @click="handleSubmit"
    >
      <span v-if="isSurrendered">완료</span>
      <span v-else-if="!isSubmitting">제출</span>
      <span v-else>채점 중</span>
    </button>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useInGameStore } from '@/pages/game/store/useInGameStore';

const props = defineProps({
  isSubmitting: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['submit', 'surrender-requested']);

const inGameStore = useInGameStore();

// 항복 상태 확인
const isSurrendered = computed(() => inGameStore.banPickBuyResults.surrender);

// 제출 버튼 비활성화 조건: 제출 중이거나 항복한 상태
const isSubmitDisabled = computed(() => props.isSubmitting || isSurrendered.value);

const handleSurrender = () => {
  // 이미 항복한 상태라면 이벤트를 발생시키지 않음
  if (isSurrendered.value) {
    return;
  }
  emit('surrender-requested');
};

const handleRun = () => {
  // TODO : 코드 실행 로직 구현
};

const handleSubmit = () => {
  if (!isSubmitDisabled.value) {
    emit('submit');
  }
};
</script>

<style lang="scss" scoped>
.action-buttons {
  display: flex;
  gap: 1vh;
  padding: 0 1.5vh;
  height: 100%;
  align-items: center;
}

.btn-action {
  padding: 0.8vh 2vh;
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: 1.8vh;
  border-radius: $radius-button;
  cursor: pointer;
  transition: all 0.3s ease;
  border-width: 0.2vh;
  border-style: solid;
  height: 4vh;
  min-height: 4vh;
  max-height: 4vh;
  display: flex;
  align-items: center;
  justify-content: center;
  white-space: nowrap;
}

.btn-surrender {
  background: rgba($cyberpunk-pink, $opacity-active);
  border-color: $cyberpunk-pink;
  color: $text-primary;

  &:hover {
    @include neon-glow($cyberpunk-pink);
  }
}

.btn-run {
  background: rgba($neon-green, $opacity-active);
  border-color: $neon-green;
  color: $neon-green;

  &:hover {
    @include neon-glow($neon-green);
  }
}

.btn-submit {
  background: rgba($cyberpunk-yellow, $opacity-active);
  border-color: $cyberpunk-yellow;
  color: $cyberpunk-yellow;

  &:hover:not(:disabled) {
    @include neon-glow($cyberpunk-yellow);
  }

  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
    background: rgba($cyberpunk-yellow, 0.2);
    border-color: rgba($cyberpunk-yellow, 0.4);
    color: rgba($cyberpunk-yellow, 0.6);
  }

  &--loading {
    animation: pulse 1.5s ease-in-out infinite;
  }
}

@keyframes pulse {
  0%,
  100% {
    opacity: 0.6;
  }
  50% {
    opacity: 1;
  }
}
</style>
