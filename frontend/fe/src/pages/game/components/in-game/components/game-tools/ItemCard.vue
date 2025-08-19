<template>
  <div 
    class="item-card" 
    :class="{ dragging: isDragging, disabled: isDisabled }" 
    :draggable="!isDisabled" 
    @dragstart="handleDragStart" 
    @dragend="handleDragEnd"
  >
    <div class="remaining-count" v-if="remainingCount !== undefined">
      {{ remainingCount }}
    </div>
    <img :src="iconPath" :alt="iconType" class="item-icon" @error="onImageError" />
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';

const props = defineProps({
  iconType: { type: String, required: true },
  itemId: { type: Number, required: true },
  remainingCount: { type: Number, default: undefined },
});

const emit = defineEmits(['dragStart', 'dragEnd']);
const isDragging = ref(false);

const iconPath = computed(() => {
  try {
    // new URL 방식 → Vite 빌드 시 경로 자동 변환
    return new URL(`/src/game/assets/item-icon/${props.iconType}.png`, import.meta.url).href;
  } catch {
    console.warn(`Icon not found: ${props.iconType}.png`);
    return '';
  }
});

const isDisabled = computed(() => {
  return props.remainingCount !== undefined && props.remainingCount <= 0;
});

const handleDragStart = e => {
  if (isDisabled.value) {
    e.preventDefault();
    return;
  }
  isDragging.value = true;
  e.dataTransfer.setData(
    'application/json',
    JSON.stringify({
      itemId: props.itemId,
      iconType: props.iconType,
    }),
  );
  e.dataTransfer.effectAllowed = 'move';
  emit('dragStart', { itemId: props.itemId, iconType: props.iconType });
};

const handleDragEnd = () => {
  isDragging.value = false;
  emit('dragEnd');
};

const onImageError = () => {
  console.warn(`Failed to load icon: ${props.iconType}.png`);
};
</script>

<style lang="scss" scoped>
.item-card {
  position: relative;
  width: 8vh;
  height: 8vh;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: $radius-button;
  background: rgba($text-primary, $opacity-minimal);
  border: 1px solid rgba($text-primary, $opacity-active);
  backdrop-filter: blur(10px);
  cursor: grab;
  transition: all 0.2s ease;

  &:hover:not(.disabled) {
    transform: scale(1.05);
    border-color: $neon-green;
  }

  &.dragging {
    opacity: 0.7;
    transform: scale(1.1);
    cursor: grabbing;
    border-color: $neon-green;
    box-shadow: 0 0 1vh rgba($neon-green, 0.5);
  }

  &.disabled {
    opacity: 0.4;
    cursor: not-allowed;
    background: rgba($text-primary, 0.1);
    border-color: rgba($text-primary, 0.2);
    
    &:hover {
      transform: none;
      border-color: rgba($text-primary, 0.2);
    }

    .item-icon {
      filter: grayscale(100%);
    }

    .remaining-count {
      background: rgba(#ff4444, 0.8);
      color: $text-primary;
    }
  }

  .remaining-count {
    position: absolute;
    top: -0.5vh;
    left: -0.5vh;
    width: 2.5vh;
    height: 2.5vh;
    background: rgba($neon-green, 0.9);
    color: $text-primary;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 1.2vh;
    font-weight: bold;
    border: 0.2vh solid $text-primary;
    z-index: 1;
  }

  .item-icon {
    width: 6vh;
    height: 6vh;
    object-fit: cover;
    pointer-events: none;
    border-radius: 1vh;
    transition: filter 0.2s ease;
  }
}
</style>
