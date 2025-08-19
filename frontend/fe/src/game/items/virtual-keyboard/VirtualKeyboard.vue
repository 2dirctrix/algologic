<!-- VirtualKeyboard.vue -->
<template>
  <div class="virtual-keyboard">
    <!-- 키보드 레이아웃만 표시 -->
    <KeyboardLayout
      :layout="currentLayout"
      :shift-active="shiftActive"
      :caps-lock-active="capsLockActive"
      :disabled="disabled"
      @key-press="handleKeyPress"
      @key-down="handleKeyDown"
      @key-up="handleKeyUp"
    />
  </div>
</template>

<script setup>
import { ref, computed, watch, nextTick } from 'vue';
import KeyboardLayout from './KeyboardLayout.vue';

const props = defineProps({
  modelValue: {
    type: String,
    default: '',
  },
  title: {
    type: String,
    default: '가상 키보드',
  },
  placeholder: {
    type: String,
    default: '가상 키보드로 입력하세요...',
  },
  layout: {
    type: String,
    default: 'qwerty',
    validator: value => ['qwerty', 'programming', 'numeric'].includes(value),
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  inputReadonly: {
    type: Boolean,
    default: false,
  },
  showHeader: {
    type: Boolean,
    default: true,
  },
  showInput: {
    type: Boolean,
    default: true,
  },
  showStatus: {
    type: Boolean,
    default: true,
  },
  showLayoutToggle: {
    type: Boolean,
    default: false,
  },
  showCloseButton: {
    type: Boolean,
    default: false,
  },
  showClearButton: {
    type: Boolean,
    default: true,
  },
  showQuickActions: {
    type: Boolean,
    default: true,
  },
  maxLength: {
    type: Number,
    default: null,
  },
});

const emit = defineEmits(['update:modelValue', 'key-press', 'input-change', 'layout-change', 'close', 'focus', 'blur']);

// 템플릿 참조
const inputRef = ref(null);

// 상태 관리
const currentValue = ref(props.modelValue);
const currentLayout = ref(props.layout);
const shiftActive = ref(false);
const capsLockActive = ref(false);
const inputFocused = ref(false);

// 레이아웃 순환을 위한 배열
const availableLayouts = ['qwerty', 'programming', 'numeric'];

// 계산된 속성
const layoutButtonText = computed(() => {
  const layoutNames = {
    qwerty: 'QWERTY',
    programming: '프로그래밍',
    numeric: '숫자',
  };
  return layoutNames[currentLayout.value] || 'QWERTY';
});

// modelValue 동기화
watch(
  () => props.modelValue,
  newValue => {
    currentValue.value = newValue;
  },
);

watch(currentValue, newValue => {
  emit('update:modelValue', newValue);
  emit('input-change', newValue);
});

// 키 이벤트 핸들러
const handleKeyPress = key => {
  if (props.disabled) return;

  emit('key-press', key);

  // 특수 키 처리
  switch (key.code) {
    case 'Backspace':
      handleBackspace();
      break;
    case 'Enter':
      insertNewline();
      break;
    case 'Tab':
      insertTab();
      break;
    case 'Space':
      insertSpace();
      break;
    case 'ShiftLeft':
    case 'ShiftRight':
      toggleShift();
      break;
    case 'CapsLock':
      toggleCapsLock();
      break;
    default:
      if (key.type === 'normal') {
        insertCharacter(key.display);
      }
      break;
  }

  // Shift가 활성화된 상태에서 일반 키를 누르면 Shift 해제
  if (shiftActive.value && key.type === 'normal') {
    shiftActive.value = false;
  }
};

const handleKeyDown = key => {
  emit('key-press', { ...key, type: 'keydown' });
};

const handleKeyUp = key => {
  emit('key-press', { ...key, type: 'keyup' });
};

// 입력 관련 메서드
const insertCharacter = char => {
  if (props.maxLength && currentValue.value.length >= props.maxLength) {
    return;
  }

  const cursorPos = getCursorPosition();
  const beforeCursor = currentValue.value.substring(0, cursorPos);
  const afterCursor = currentValue.value.substring(cursorPos);

  currentValue.value = beforeCursor + char + afterCursor;

  // 커서 위치 업데이트
  nextTick(() => {
    setCursorPosition(cursorPos + 1);
  });
};

const handleBackspace = () => {
  const cursorPos = getCursorPosition();
  if (cursorPos > 0) {
    const beforeCursor = currentValue.value.substring(0, cursorPos - 1);
    const afterCursor = currentValue.value.substring(cursorPos);

    currentValue.value = beforeCursor + afterCursor;

    nextTick(() => {
      setCursorPosition(cursorPos - 1);
    });
  }
};

const insertTab = () => {
  insertCharacter('    '); // 4개 공백으로 탭 구현
};

const insertNewline = () => {
  insertCharacter('\n');
};

const insertSpace = () => {
  insertCharacter(' ');
};

const clearInput = () => {
  currentValue.value = '';
  if (inputRef.value) {
    inputRef.value.focus();
  }
};

// 커서 위치 관리
const getCursorPosition = () => {
  if (!inputRef.value) return 0;
  return inputRef.value.selectionStart || 0;
};

const setCursorPosition = position => {
  if (!inputRef.value) return;
  inputRef.value.setSelectionRange(position, position);
};

// 수정자 키 토글
const toggleShift = () => {
  shiftActive.value = !shiftActive.value;
};

const toggleCapsLock = () => {
  capsLockActive.value = !capsLockActive.value;
};

// 레이아웃 변경
const toggleLayout = () => {
  const currentIndex = availableLayouts.indexOf(currentLayout.value);
  const nextIndex = (currentIndex + 1) % availableLayouts.length;
  currentLayout.value = availableLayouts[nextIndex];
  emit('layout-change', currentLayout.value);
};

// 입력 필드 이벤트
const handleInput = event => {
  currentValue.value = event.target.value;
};

const handleInputFocus = () => {
  inputFocused.value = true;
  emit('focus');
};

const handleInputBlur = () => {
  inputFocused.value = false;
  emit('blur');
};

// 키보드 닫기
const closeKeyboard = () => {
  emit('close');
};

// 공개 메서드
const focus = () => {
  if (inputRef.value) {
    inputRef.value.focus();
  }
};

const blur = () => {
  if (inputRef.value) {
    inputRef.value.blur();
  }
};

const setValue = value => {
  currentValue.value = value;
};

const getValue = () => {
  return currentValue.value;
};

const setLayout = layout => {
  if (availableLayouts.includes(layout)) {
    currentLayout.value = layout;
    emit('layout-change', layout);
  }
};

// 컴포넌트 인스턴스 노출
defineExpose({
  focus,
  blur,
  setValue,
  getValue,
  setLayout,
  clearInput,
  insertTab,
  insertNewline,
  insertSpace,
});
</script>

<style lang="scss" scoped>
.virtual-keyboard {
  width: 100%;
  height: 100%;
  padding: 1vh;
}
</style>
