<template>
  <button 
    type="button" 
    class="language-button" 
    :class="{ 
      'language-button--selected': isSelected,
      'language-button--disabled': isDisabled 
    }" 
    @click="handleClick"
    :disabled="isDisabled"
  >
    <span class="language-button__text text-xs">{{ language }}</span>
  </button>
</template>

<script>
export default {
  name: 'LanguageButton',
  props: {
    language: {
      type: String,
      required: true,
    },
    value: {
      type: String,
      required: true,
    },
    isSelected: {
      type: Boolean,
      default: false,
    },
  },
  emits: ['select'],
  computed: {
    isDisabled() {
      // Python, C++, Java만 허용
      const allowedLanguages = ['PYTHON', 'CPP', 'JAVA'];
      return !allowedLanguages.includes(this.value);
    },
  },
  methods: {
    handleClick() {
      if (this.isDisabled) return;
      this.$emit('select', this.value);
    },
  },
};
</script>

<style scoped lang="scss">
.language-button {
  width: calc(18vh);
  height: calc(5vh);
  background: rgba(255, 0, 255, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;

  &:hover {
    box-shadow:
      0 0 10px rgba(255, 255, 255, 0.5),
      0 0 20px rgba(255, 255, 255, 0.5);
  }

  &--selected {
    background: rgba(255, 0, 255, 0.2);
    border-color: #ff00ff;
    box-shadow:
      0 0 10px #ff00ff,
      0 0 20px #ff00ff;
  }

  &--disabled {
    background: rgba(100, 100, 100, 0.2);
    border-color: rgba(255, 255, 255, 0.1);
    cursor: not-allowed;
    opacity: 0.3;

    &:hover {
      box-shadow: none;
      transform: none;
    }

    .language-button__text {
      color: rgba(255, 255, 255, 0.3);
    }
  }

  &__text {
    color: rgba(255, 255, 255, 0.8);
    font-weight: 500;
    letter-spacing: 0.5px;
    font-size: calc(1.4vh);
  }
}
</style>
