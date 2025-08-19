<template>
  <div class="search-input-container">
    <input
      ref="searchInput"
      type="text"
      class="search-input"
      placeholder="Enter your text here..."
      @keydown.enter="handleEnter"
      @compositionstart="handleCompositionStart"
      @compositionend="handleCompositionEnd"
      @focus="handleFocus"
      @blur="handleBlur"
      @input="handleInput"
    />
  </div>
</template>

<script>
export default {
  name: 'SearchInput',
  data() {
    return {
      isComposing: false,
    };
  },
  methods: {
    handleEnter(event) {
      // 한국어 입력 중이면 검색하지 않음
      if (this.isComposing) return;

      const searchValue = event.target.value;
      this.$emit('search', { query: searchValue });
    },
    handleCompositionStart() {
      this.isComposing = true;
    },
    handleCompositionEnd() {
      this.isComposing = false;
    },
    handleFocus() {
      this.$emit('focus');
    },
    handleBlur() {
      this.$emit('blur');
    },
    handleInput() {
      // Input handling can be added here if needed
    },
  },
};
</script>

<style lang="scss" scoped>
@use 'sass:color';
.search-input-container {
  position: relative;
  display: flex;
  align-items: center;
  height: 100%;
}

.search-input {
  background: linear-gradient(135deg, 
    rgba($card-bg, 0.9) 0%, 
    rgba($card-bg, 0.7) 50%, 
    rgba($primary-bg, 0.8) 100%
  );
  border: calc(0.3vh) solid rgba($cyan, 0.6);
  border-radius: calc(1.5vh);
  padding: calc(1vh) calc(2vh);
  font-family: $font-primary;
  font-weight: $font-weight-regular;
  font-size: calc(1.2vh);
  color: $text-primary;
  width: calc(20vh);
  height: calc(4.5vh);
  outline: none;
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  box-sizing: border-box;
  text-shadow: 0 0 0.3vh rgba($text-primary, 0.3);
  @include neon-glow($cyan);

  // Responsive adjustments
  @media (max-width: 1200px) {
    width: calc(18vh);
    font-size: calc(1.1vh);
    padding: calc(0.8vh) calc(1.8vh);
  }

  @media (max-width: 800px) {
    width: calc(16vh);
    min-width: calc(12vh);
    font-size: calc(1vh);
    padding: calc(0.6vh) calc(1.5vh);
    flex: 1;
    max-width: calc(20vh);
  }

  &::placeholder {
    color: rgba($text-primary, $opacity-placeholder);
  }

  &:focus {
    border-color: $cyan;
    background: linear-gradient(135deg, 
      rgba($cyan, 0.1) 0%, 
      rgba($card-bg, 0.8) 50%, 
      rgba($primary-bg, 0.9) 100%
    );
    transform: scale(1.01);
    @include neon-glow-strong($cyan);
  }

  &:hover {
    border-color: lighten($cyan, 15%);
    background: linear-gradient(135deg, 
      rgba($cyan, 0.05) 0%, 
      rgba($card-bg, 0.8) 50%, 
      rgba($primary-bg, 0.85) 100%
    );
    transform: translateY(-0.1vh);
  }
}
</style>
