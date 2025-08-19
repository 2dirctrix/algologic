<template>
  <div class="monaco-editor-container">
    <VueMonacoEditor
      ref="editor"
      v-model:value="code"
      :language="language || editorConfig.language"
      :options="editorConfig.options"
      @change="handleCodeChange"
      @mount="handleEditorMounted"
      class="monaco-editor-wrapper"
    />
  </div>
</template>

<script>
import { VueMonacoEditor } from '@guolao/vue-monaco-editor';
import { editorConfig, loadAndRegisterTheme, getSkeletonCode } from '../../constants/editor-config';

export default {
  name: 'MonacoEditor',
  components: {
    VueMonacoEditor,
  },
  props: {
    language: {
      type: String,
      default: null,
    },
    initialCode: {
      type: String,
      default: '',
    },
    theme: {
      type: String,
      default: null,
    },
    fontSize: {
      type: Number,
      default: null,
    },
  },
  emits: ['code-change', 'editor-ready'],
  data() {
    return {
      code: this.getInitialCode(),
      editorConfig,
    };
  },
  mounted() {},
  watch: {
    theme: {
      handler(newTheme) {
        if (newTheme && this.editorInstance) {
          this.changeTheme(newTheme);
        }
      },
    },
    fontSize: {
      handler(newFontSize) {
        if (newFontSize && this.editorInstance) {
          this.changeFontSize(newFontSize);
        }
      },
    },
    language: {
      handler(newLanguage) {
        if (newLanguage && this.editorInstance) {
          this.changeLanguage(newLanguage);
        }
      },
    },
  },
  methods: {
    handleCodeChange(value) {
      this.$emit('code-change', value);
    },

    async handleEditorMounted(editor, monaco) {
      this.editorInstance = editor;

      // 커스텀 테마 로드 및 적용
      try {
        const themeId = this.editorConfig.theme || 'night-owl';
        const success = await loadAndRegisterTheme(monaco, themeId);

        if (success) {
          this.$nextTick(() => {
            monaco.editor.setTheme(themeId);
          });
        } else {
          // 테마 로드 실패 시 기본 테마 적용
          monaco.editor.setTheme('vs-dark');
        }
      } catch (error) {
        console.error('테마 적용 중 오류:', error);
      }

      // Undo/Redo 키 바인딩 비활성화
      this.disableUndoRedo(editor);

      this.$emit('editor-ready', editor);
    },

    /**
     * Undo/Redo 기능 비활성화
     */
    disableUndoRedo(editor) {
      if (!editor || !window.monaco) return;

      // Ctrl+Z (Undo) 비활성화
      editor.addCommand(window.monaco.KeyMod.CtrlCmd | window.monaco.KeyCode.KeyZ, () => {});

      // Ctrl+Shift+Z (Redo) 비활성화
      editor.addCommand(window.monaco.KeyMod.CtrlCmd | window.monaco.KeyMod.Shift | window.monaco.KeyCode.KeyZ, () => {});

      // Ctrl+Y (Redo 대체) 비활성화
      editor.addCommand(window.monaco.KeyMod.CtrlCmd | window.monaco.KeyCode.KeyY, () => {});
    },

    setValue(code) {
      this.code = code;
    },

    getValue() {
      return this.code;
    },

    setReadOnly(readOnly) {
      if (this.editorInstance) {
        this.editorInstance.updateOptions({ readOnly });
      }
    },

    async changeTheme(themeId) {
      if (!this.editorInstance || !window.monaco) return;

      try {
        const success = await loadAndRegisterTheme(window.monaco, themeId);
        if (success) {
          window.monaco.editor.setTheme(themeId);
          return true;
        }
      } catch (error) {
        console.error(`테마 변경 실패: ${themeId}`, error);
      }
      return false;
    },

    changeFontSize(fontSize) {
      if (this.editorInstance) {
        this.editorInstance.updateOptions({ fontSize });
      }
    },

    changeLanguage(language) {
      if (!this.editorInstance || !window.monaco) return;

      const supportedLanguages = ['python', 'javascript', 'java', 'cpp', 'c'];
      const targetLanguage = supportedLanguages.includes(language.toLowerCase()) ? language.toLowerCase() : 'python';

      const model = this.editorInstance.getModel();
      if (model) {
        // 언어 변경
        window.monaco.editor.setModelLanguage(model, targetLanguage);

        // 해당 언어의 스켈레톤 코드 적용
        const skeletonCode = getSkeletonCode(targetLanguage);
        if (skeletonCode && skeletonCode.trim() !== '') {
          // 현재 코드가 비어있거나 다른 언어의 스켈레톤 코드인 경우에만 적용
          const currentCode = this.editorInstance.getValue().trim();
          if (currentCode === '' || this.isPreviousSkeletonCode(currentCode)) {
            this.code = skeletonCode;
          }
        } else if (targetLanguage === 'python') {
          // Python의 경우 빈 코드로 시작
          const currentCode = this.editorInstance.getValue().trim();
          if (this.isPreviousSkeletonCode(currentCode)) {
            this.code = '';
          }
        }
      }
    },

    // 초기 코드 반환 (props.initialCode 우선, 없으면 해당 언어의 스켈레톤 코드)
    getInitialCode() {
      if (this.initialCode && this.initialCode.trim() !== '') {
        return this.initialCode;
      }

      const currentLanguage = this.language || this.editorConfig.language;
      return getSkeletonCode(currentLanguage);
    },

    // 현재 코드가 다른 언어의 스켈레톤 코드인지 확인
    isPreviousSkeletonCode(currentCode) {
      const skeletonCodes = [
        getSkeletonCode('java').trim(),
        getSkeletonCode('cpp').trim(),
        getSkeletonCode('c').trim(),
        getSkeletonCode('javascript').trim(),
      ];
      return skeletonCodes.includes(currentCode);
    },
  },
};
</script>

<style lang="scss" scoped>
.monaco-editor-container {
  flex: 1;
  height: calc(100% - 6vh);
  min-height: 0;
  overflow: hidden;
}

.monaco-editor-wrapper {
  width: 100%;
  height: 100%;
}
</style>
