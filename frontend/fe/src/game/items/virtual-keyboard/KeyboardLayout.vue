<!-- KeyboardLayout.vue -->
<template>
  <div class="keyboard-layout">
    <div v-for="(row, rowIndex) in currentLayout" :key="rowIndex" class="keyboard-row">
      <button
        v-for="key in row"
        :key="key.code"
        class="keyboard-key"
        :class="[
          key.type,
          {
            active: activeKeys.includes(key.code),
            disabled: disabled,
          },
        ]"
        :style="{
          width: key.width,
          fontSize: key.fontSize,
        }"
        @click="handleKeyClick(key)"
        @mousedown="handleKeyDown(key)"
        @mouseup="handleKeyUp(key)"
        :disabled="disabled"
      >
        {{ key.display }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';

const props = defineProps({
  layout: {
    type: String,
    default: 'qwerty',
    validator: value => ['qwerty', 'programming', 'numeric'].includes(value),
  },
  shiftActive: {
    type: Boolean,
    default: false,
  },
  capsLockActive: {
    type: Boolean,
    default: false,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['key-press', 'key-down', 'key-up']);

// 상태 관리
const activeKeys = ref([]);

// 키보드 레이아웃 정의
const layouts = {
  qwerty: {
    normal: [
      [
        { code: 'Backquote', display: '`', type: 'normal', width: '40px' },
        { code: 'Digit1', display: '1', type: 'normal', width: '40px' },
        { code: 'Digit2', display: '2', type: 'normal', width: '40px' },
        { code: 'Digit3', display: '3', type: 'normal', width: '40px' },
        { code: 'Digit4', display: '4', type: 'normal', width: '40px' },
        { code: 'Digit5', display: '5', type: 'normal', width: '40px' },
        { code: 'Digit6', display: '6', type: 'normal', width: '40px' },
        { code: 'Digit7', display: '7', type: 'normal', width: '40px' },
        { code: 'Digit8', display: '8', type: 'normal', width: '40px' },
        { code: 'Digit9', display: '9', type: 'normal', width: '40px' },
        { code: 'Digit0', display: '0', type: 'normal', width: '40px' },
        { code: 'Minus', display: '-', type: 'normal', width: '40px' },
        { code: 'Equal', display: '=', type: 'normal', width: '40px' },
        { code: 'Backspace', display: '⌫', type: 'function', width: '80px' },
      ],
      [
        { code: 'Tab', display: 'Tab', type: 'function', width: '60px' },
        { code: 'KeyQ', display: 'q', type: 'normal', width: '40px' },
        { code: 'KeyW', display: 'w', type: 'normal', width: '40px' },
        { code: 'KeyE', display: 'e', type: 'normal', width: '40px' },
        { code: 'KeyR', display: 'r', type: 'normal', width: '40px' },
        { code: 'KeyT', display: 't', type: 'normal', width: '40px' },
        { code: 'KeyY', display: 'y', type: 'normal', width: '40px' },
        { code: 'KeyU', display: 'u', type: 'normal', width: '40px' },
        { code: 'KeyI', display: 'i', type: 'normal', width: '40px' },
        { code: 'KeyO', display: 'o', type: 'normal', width: '40px' },
        { code: 'KeyP', display: 'p', type: 'normal', width: '40px' },
        { code: 'BracketLeft', display: '[', type: 'normal', width: '40px' },
        { code: 'BracketRight', display: ']', type: 'normal', width: '40px' },
        { code: 'Backslash', display: '\\', type: 'normal', width: '60px' },
      ],
      [
        { code: 'CapsLock', display: 'Caps', type: 'modifier', width: '80px' },
        { code: 'KeyA', display: 'a', type: 'normal', width: '40px' },
        { code: 'KeyS', display: 's', type: 'normal', width: '40px' },
        { code: 'KeyD', display: 'd', type: 'normal', width: '40px' },
        { code: 'KeyF', display: 'f', type: 'normal', width: '40px' },
        { code: 'KeyG', display: 'g', type: 'normal', width: '40px' },
        { code: 'KeyH', display: 'h', type: 'normal', width: '40px' },
        { code: 'KeyJ', display: 'j', type: 'normal', width: '40px' },
        { code: 'KeyK', display: 'k', type: 'normal', width: '40px' },
        { code: 'KeyL', display: 'l', type: 'normal', width: '40px' },
        { code: 'Semicolon', display: ';', type: 'normal', width: '40px' },
        { code: 'Quote', display: "'", type: 'normal', width: '40px' },
        { code: 'Enter', display: '↵', type: 'function', width: '80px' },
      ],
      [
        { code: 'ShiftLeft', display: 'Shift', type: 'modifier', width: '100px' },
        { code: 'KeyZ', display: 'z', type: 'normal', width: '40px' },
        { code: 'KeyX', display: 'x', type: 'normal', width: '40px' },
        { code: 'KeyC', display: 'c', type: 'normal', width: '40px' },
        { code: 'KeyV', display: 'v', type: 'normal', width: '40px' },
        { code: 'KeyB', display: 'b', type: 'normal', width: '40px' },
        { code: 'KeyN', display: 'n', type: 'normal', width: '40px' },
        { code: 'KeyM', display: 'm', type: 'normal', width: '40px' },
        { code: 'Comma', display: ',', type: 'normal', width: '40px' },
        { code: 'Period', display: '.', type: 'normal', width: '40px' },
        { code: 'Slash', display: '/', type: 'normal', width: '40px' },
        { code: 'ShiftRight', display: 'Shift', type: 'modifier', width: '100px' },
      ],
      [{ code: 'Space', display: 'Space', type: 'function', width: '320px' }],
    ],
    shift: [
      [
        { code: 'Backquote', display: '~', type: 'normal', width: '40px' },
        { code: 'Digit1', display: '!', type: 'normal', width: '40px' },
        { code: 'Digit2', display: '@', type: 'normal', width: '40px' },
        { code: 'Digit3', display: '#', type: 'normal', width: '40px' },
        { code: 'Digit4', display: '$', type: 'normal', width: '40px' },
        { code: 'Digit5', display: '%', type: 'normal', width: '40px' },
        { code: 'Digit6', display: '^', type: 'normal', width: '40px' },
        { code: 'Digit7', display: '&', type: 'normal', width: '40px' },
        { code: 'Digit8', display: '*', type: 'normal', width: '40px' },
        { code: 'Digit9', display: '(', type: 'normal', width: '40px' },
        { code: 'Digit0', display: ')', type: 'normal', width: '40px' },
        { code: 'Minus', display: '_', type: 'normal', width: '40px' },
        { code: 'Equal', display: '+', type: 'normal', width: '40px' },
        { code: 'Backspace', display: '⌫', type: 'function', width: '80px' },
      ],
      [
        { code: 'Tab', display: 'Tab', type: 'function', width: '60px' },
        { code: 'KeyQ', display: 'Q', type: 'normal', width: '40px' },
        { code: 'KeyW', display: 'W', type: 'normal', width: '40px' },
        { code: 'KeyE', display: 'E', type: 'normal', width: '40px' },
        { code: 'KeyR', display: 'R', type: 'normal', width: '40px' },
        { code: 'KeyT', display: 'T', type: 'normal', width: '40px' },
        { code: 'KeyY', display: 'Y', type: 'normal', width: '40px' },
        { code: 'KeyU', display: 'U', type: 'normal', width: '40px' },
        { code: 'KeyI', display: 'I', type: 'normal', width: '40px' },
        { code: 'KeyO', display: 'O', type: 'normal', width: '40px' },
        { code: 'KeyP', display: 'P', type: 'normal', width: '40px' },
        { code: 'BracketLeft', display: '{', type: 'normal', width: '40px' },
        { code: 'BracketRight', display: '}', type: 'normal', width: '40px' },
        { code: 'Backslash', display: '|', type: 'normal', width: '60px' },
      ],
      [
        { code: 'CapsLock', display: 'Caps', type: 'modifier', width: '80px' },
        { code: 'KeyA', display: 'A', type: 'normal', width: '40px' },
        { code: 'KeyS', display: 'S', type: 'normal', width: '40px' },
        { code: 'KeyD', display: 'D', type: 'normal', width: '40px' },
        { code: 'KeyF', display: 'F', type: 'normal', width: '40px' },
        { code: 'KeyG', display: 'G', type: 'normal', width: '40px' },
        { code: 'KeyH', display: 'H', type: 'normal', width: '40px' },
        { code: 'KeyJ', display: 'J', type: 'normal', width: '40px' },
        { code: 'KeyK', display: 'K', type: 'normal', width: '40px' },
        { code: 'KeyL', display: 'L', type: 'normal', width: '40px' },
        { code: 'Semicolon', display: ':', type: 'normal', width: '40px' },
        { code: 'Quote', display: '"', type: 'normal', width: '40px' },
        { code: 'Enter', display: '↵', type: 'function', width: '80px' },
      ],
      [
        { code: 'ShiftLeft', display: 'Shift', type: 'modifier', width: '100px' },
        { code: 'KeyZ', display: 'Z', type: 'normal', width: '40px' },
        { code: 'KeyX', display: 'X', type: 'normal', width: '40px' },
        { code: 'KeyC', display: 'C', type: 'normal', width: '40px' },
        { code: 'KeyV', display: 'V', type: 'normal', width: '40px' },
        { code: 'KeyB', display: 'B', type: 'normal', width: '40px' },
        { code: 'KeyN', display: 'N', type: 'normal', width: '40px' },
        { code: 'KeyM', display: 'M', type: 'normal', width: '40px' },
        { code: 'Comma', display: '<', type: 'normal', width: '40px' },
        { code: 'Period', display: '>', type: 'normal', width: '40px' },
        { code: 'Slash', display: '?', type: 'normal', width: '40px' },
        { code: 'ShiftRight', display: 'Shift', type: 'modifier', width: '100px' },
      ],
      [{ code: 'Space', display: 'Space', type: 'function', width: '320px' }],
    ],
  },
  programming: [
    [
      { code: 'Tab', display: 'Tab', type: 'function', width: '60px' },
      { code: 'BracketLeft', display: '[', type: 'normal', width: '40px' },
      { code: 'BracketRight', display: ']', type: 'normal', width: '40px' },
      { code: 'Semicolon', display: ';', type: 'normal', width: '40px' },
      { code: 'Quote', display: "'", type: 'normal', width: '40px' },
      { code: 'Backspace', display: '⌫', type: 'function', width: '80px' },
    ],
    [
      { code: 'BracketLeft', display: '{', type: 'normal', width: '40px' },
      { code: 'BracketRight', display: '}', type: 'normal', width: '40px' },
      { code: 'Backslash', display: '\\', type: 'normal', width: '40px' },
      { code: 'Slash', display: '/', type: 'normal', width: '40px' },
      { code: 'Enter', display: '↵', type: 'function', width: '80px' },
    ],
    [{ code: 'Space', display: 'Space', type: 'function', width: '280px' }],
  ],
  numeric: [
    [
      { code: 'Digit7', display: '7', type: 'normal', width: '60px' },
      { code: 'Digit8', display: '8', type: 'normal', width: '60px' },
      { code: 'Digit9', display: '9', type: 'normal', width: '60px' },
      { code: 'Backspace', display: '⌫', type: 'function', width: '60px' },
    ],
    [
      { code: 'Digit4', display: '4', type: 'normal', width: '60px' },
      { code: 'Digit5', display: '5', type: 'normal', width: '60px' },
      { code: 'Digit6', display: '6', type: 'normal', width: '60px' },
      { code: 'Plus', display: '+', type: 'normal', width: '60px' },
    ],
    [
      { code: 'Digit1', display: '1', type: 'normal', width: '60px' },
      { code: 'Digit2', display: '2', type: 'normal', width: '60px' },
      { code: 'Digit3', display: '3', type: 'normal', width: '60px' },
      { code: 'Minus', display: '-', type: 'normal', width: '60px' },
    ],
    [
      { code: 'Digit0', display: '0', type: 'normal', width: '120px' },
      { code: 'Period', display: '.', type: 'normal', width: '60px' },
      { code: 'Enter', display: '↵', type: 'function', width: '60px' },
    ],
  ],
};

// 현재 레이아웃 계산
const currentLayout = computed(() => {
  if (props.layout === 'qwerty') {
    const shouldUseShift = props.shiftActive || props.capsLockActive;
    return shouldUseShift ? layouts.qwerty.shift : layouts.qwerty.normal;
  }
  return layouts[props.layout] || layouts.qwerty.normal;
});

// 이벤트 핸들러
const handleKeyClick = key => {
  if (props.disabled) return;
  emit('key-press', key);
};

const handleKeyDown = key => {
  if (props.disabled) return;
  if (!activeKeys.value.includes(key.code)) {
    activeKeys.value.push(key.code);
  }
  emit('key-down', key);
};

const handleKeyUp = key => {
  if (props.disabled) return;
  const index = activeKeys.value.indexOf(key.code);
  if (index > -1) {
    activeKeys.value.splice(index, 1);
  }
  emit('key-up', key);
};

// Shift 상태 감시
watch(
  () => props.shiftActive,
  () => {
    // Shift 상태 변경 시 추가 로직 필요하면 여기에
  },
);
</script>

<style lang="scss" scoped>
.keyboard-layout {
  background: $input-bg;
  border: 1px solid $electric-blue;
  border-radius: $radius-small;
  user-select: none;
}

.keyboard-row {
  display: flex;
  justify-content: center;
  gap: $spacing-xs;
  margin-bottom: $spacing-xs;

  &:last-child {
    margin-bottom: 0;
  }
}

.keyboard-key {
  height: 20px;
  background: rgba($electric-blue, $opacity-inactive);
  border: 1px solid rgba($electric-blue, 0.3);
  color: $text-primary;
  border-radius: $radius-button;
  font-family: $font-primary;
  font-weight: $font-weight-medium;
  font-size: 14px;
  cursor: pointer;
  transition: $transition-base;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 40px;

  &:hover:not(:disabled) {
    background: rgba($electric-blue, $opacity-active);
    @include neon-glow($electric-blue);
  }

  &:active:not(:disabled),
  &.active {
    background: rgba($electric-blue, 0.3);
    transform: scale(0.95);
  }

  &.function {
    background: rgba($cyberpunk-pink, $opacity-inactive);
    border-color: rgba($cyberpunk-pink, 0.3);

    &:hover:not(:disabled) {
      background: rgba($cyberpunk-pink, $opacity-active);
      @include neon-glow($cyberpunk-pink);
    }
  }

  &.modifier {
    background: rgba($neon-green, $opacity-inactive);
    border-color: rgba($neon-green, 0.3);

    &:hover:not(:disabled) {
      background: rgba($neon-green, $opacity-active);
      @include neon-glow($neon-green);
    }

    &.active {
      background: rgba($neon-green, 0.3);
    }
  }

  &:disabled,
  &.disabled {
    opacity: 0.3;
    cursor: not-allowed;
    transform: none;
  }
}

// 반응형
@include tablet-up {
  .keyboard-layout {
    padding: $spacing-lg;
  }

  .keyboard-row {
    gap: $spacing-sm;
    margin-bottom: $spacing-sm;
  }

  .keyboard-key {
    height: 44px;
    font-size: 16px;
  }
}
</style>
