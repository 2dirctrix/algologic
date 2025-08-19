<template>
  <div class="view-mode-toggle">
    <button class="toggle-btn" :class="{ active: currentMode === 'card' }" @click="toggleToCard" title="카드 뷰">
      <!-- 카드 뷰 아이콘 (격자) -->
      <svg width="16" height="16" viewBox="0 0 16 16" fill="currentColor">
        <rect x="1" y="1" width="6" height="6" rx="1" />
        <rect x="9" y="1" width="6" height="6" rx="1" />
        <rect x="1" y="9" width="6" height="6" rx="1" />
        <rect x="9" y="9" width="6" height="6" rx="1" />
      </svg>
    </button>

    <button class="toggle-btn" :class="{ active: currentMode === 'list' }" @click="toggleToList" title="리스트 뷰">
      <!-- 리스트 뷰 아이콘 (수평선) -->
      <svg width="16" height="16" viewBox="0 0 16 16" fill="currentColor">
        <rect x="1" y="3" width="14" height="2" rx="1" />
        <rect x="1" y="7" width="14" height="2" rx="1" />
        <rect x="1" y="11" width="14" height="2" rx="1" />
      </svg>
    </button>
  </div>
</template>

<script>
export default {
  name: 'ViewModeToggle',
  props: {
    currentMode: {
      type: String,
      default: 'card',
      validator(value) {
        return ['card', 'list'].includes(value);
      },
    },
  },
  methods: {
    toggleToCard() {
      if (this.currentMode !== 'card') {
        this.$emit('toggle', 'card');
      }
    },
    toggleToList() {
      if (this.currentMode !== 'list') {
        this.$emit('toggle', 'list');
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.view-mode-toggle {
  display: flex;
  gap: calc(0.5vh);
  padding: calc(0.5vh);
  background: rgba($card-bg, 0.8);
  border: calc(0.3vh) solid rgba($electric-blue, 0.3);
  border-radius: calc(1vh);
  backdrop-filter: blur(4px);

  @media (max-width: 800px) {
    gap: calc(0.3vh);
    padding: calc(0.3vh);
  }
}

.toggle-btn {
  background: transparent;
  border: calc(0.3vh) solid rgba($text-primary, 0.2);
  border-radius: calc(0.8vh);
  padding: calc(1vh);
  cursor: pointer;
  transition: all 0.3s ease;
  color: rgba($text-primary, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  width: calc(4vh);
  height: calc(4vh);

  @media (max-width: 800px) {
    width: calc(3.5vh);
    height: calc(3.5vh);
    padding: calc(0.8vh);
  }

  &:hover {
    background: rgba($electric-blue, 0.1);
    border-color: rgba($electric-blue, 0.5);
    color: $electric-blue;
    transform: translateY(-1px);
  }

  &.active {
    background: rgba($electric-blue, 0.2);
    border-color: $electric-blue;
    color: $electric-blue;

    @include neon-glow($electric-blue);
  }

  svg {
    width: calc(2vh);
    height: calc(2vh);
  }
}
</style>
