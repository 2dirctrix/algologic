<!-- src/pages/game/components/in-game/components/code-editor/CodeEditor.vue -->
<template>
  <div class="code-editor">
    <div class="editor-header">
      <ControlButtons
        :current-language="currentLanguage"
        :current-theme="currentTheme"
        :font-size="currentFontSize"
        @theme-change="handleThemeChange"
        @font-size-change="handleFontSizeChange"
      />
      <ActionButtons @submit="handleSubmit" @surrender-requested="handleSurrenderRequested" :is-submitting="isSubmitting" />
    </div>
    <MonacoEditor
      ref="monacoEditor"
      :language="currentLanguage"
      :theme="currentTheme"
      :font-size="currentFontSize"
      :initial-code="currentCode"
      @code-change="handleCodeChange"
      @editor-ready="handleEditorReady"
      class="monaco-editor-full"
    />

    <!-- Judge Result Component - Overlay -->
    <JudgeResult v-if="showJudgeResult" :test-case-count="testCaseCount" :initial-expanded="true" class="judge-result-overlay" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import ActionButtons from './ActionButtons.vue';
import ControlButtons from './ControlButtons.vue';
import MonacoEditor from './MonacoEditor.vue';
import JudgeResult from './JudgeResult.vue';
import { useProblem } from '@/composables/useProblem.js';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';

// Composables
const { submitProblem } = useProblem();
const inGameStore = useInGameStore();

// Reactive data
const currentTheme = ref('night-owl');
const currentFontSize = ref(18);
const showJudgeResult = ref(false);
const testCaseCount = ref(0);

// Computed properties
const currentLanguage = computed(() => {
  return inGameStore.roomData.language.toLowerCase() || 'java';
});

const isSubmitting = computed(() => {
  return inGameStore.isSubmittingCode;
});

// 스토어의 코드와 동기화
const currentCode = computed({
  get: () => inGameStore.currentCode,
  set: value => inGameStore.updateCurrentCode(value),
});

// Methods
const handleThemeChange = newTheme => {
  currentTheme.value = newTheme;
};

const handleFontSizeChange = newFontSize => {
  currentFontSize.value = newFontSize;
};

const handleCodeChange = code => {
  currentCode.value = code;
};

const emit = defineEmits(['editor-ready', 'surrender-requested']);

const handleEditorReady = editorInstance => {
  emit('editor-ready', editorInstance);
};

const handleSurrenderRequested = () => {
  emit('surrender-requested');
};

const handleSubmit = async () => {
  if (isSubmitting.value) return;

  try {
    inGameStore.setCodeSubmissionState(true);
    // 코드 제출
    const testCaseCountResult = await submitProblem(currentCode.value);

    if (testCaseCountResult) {
      testCaseCount.value = testCaseCountResult;
      showJudgeResult.value = true;
    } else {
      // submitProblem이 null을 반환한 경우 (400 에러 등)
      inGameStore.setCodeSubmissionState(false);
    }
  } catch (error) {
    console.error('코드 제출 실패:', error);
    inGameStore.setCodeSubmissionState(false);
  }
};
</script>

<style lang="scss" scoped>
.code-editor {
  border: 0.2vh solid $electric-blue;
  border-radius: $radius-small;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  backdrop-filter: blur(1vh);
  position: relative;
  @include neon-glow($electric-blue);
}

.editor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(to bottom, rgba($primary-bg, 0.1) 0%, rgba($primary-bg, 0.3) 100%);
  backdrop-filter: blur(1.5vh);
  border-bottom: 0.1vh solid rgba($text-primary, 0.1);
  height: 6vh;
  min-height: 6vh;
  max-height: 6vh;
  flex-shrink: 0;
  position: relative;
  z-index: 1000;
}

.monaco-editor-full {
  flex: 1;
  overflow: hidden;
  min-height: 0;
}

.judge-result-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 10;
  max-height: 100vh;
  animation: slideUpFromBottom 0.4s ease-out;
}

@keyframes slideUpFromBottom {
  from {
    transform: translateY(100%);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}
</style>
