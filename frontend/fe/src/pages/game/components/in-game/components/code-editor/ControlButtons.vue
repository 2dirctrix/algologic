<template>
  <div class="control-buttons">
    <!-- 현재 언어 텍스트 -->
    <div class="language-display">
      {{ currentLanguage.toUpperCase() }}
    </div>

    <!-- 테마 선택 버튼 -->
    <div class="theme-selector" :class="{ 'show-dropdown': showThemeDropdown }">
      <button class="theme-toggle-btn" @click.stop="toggleThemeDropdown" :title="`테마: ${currentTheme}`">
        <!-- 테마 아이콘 -->
        <svg width="16" height="16" viewBox="0 0 16 16" fill="currentColor">
          <path d="M8 1a7 7 0 1 0 0 14 7 7 0 0 0 0-14zm0 2a5 5 0 0 1 5 5 5 5 0 0 1-5 5V3z" />
        </svg>
      </button>

      <!-- 테마 드롭다운 -->
      <div v-if="showThemeDropdown" class="theme-dropdown" @click.stop>
        <div class="theme-list">
          <button
            v-for="theme in themes"
            :key="theme.id"
            class="theme-option"
            :class="{ active: currentTheme === theme.id }"
            @click="selectTheme(theme.id)"
          >
            <div class="theme-preview" :style="{ backgroundColor: theme.color }"></div>
            <span class="theme-name">{{ theme.name }}</span>
          </button>
        </div>
      </div>
    </div>

    <!-- 폰트 사이즈 조절 버튼들 -->
    <div class="font-size-controls">
      <button class="font-btn" @click="decreaseFontSize" title="폰트 크기 줄이기">
        <!-- 마이너스 아이콘 -->
        <svg width="14" height="14" viewBox="0 0 14 14" fill="currentColor">
          <rect x="3" y="7" width="10" height="2" rx="1" />
        </svg>
      </button>

      <button class="font-btn" @click="increaseFontSize" title="폰트 크기 키우기">
        <!-- 플러스 아이콘 -->
        <svg width="16" height="16" viewBox="0 0 16 16" fill="currentColor">
          <rect x="3" y="7" width="10" height="2" rx="1" />
          <rect x="7" y="3" width="2" height="10" rx="1" />
        </svg>
      </button>
    </div>
  </div>
</template>

<script>
import { availableThemes } from '../../constants/editor-config';

export default {
  name: 'ControlButtons',
  props: {
    currentLanguage: {
      type: String,
      default: 'java',
    },
    currentTheme: {
      type: String,
      default: 'night-owl',
    },
    fontSize: {
      type: Number,
      default: 18,
    },
  },
  emits: ['theme-change', 'font-size-change'],
  data() {
    return {
      themes: [
        // Monaco 기본 테마들
        { id: 'vs-dark', name: 'VS Dark', color: '#1e1e1e' },
        { id: 'vs', name: 'VS Light', color: '#ffffff' },
        { id: 'hc-black', name: 'High Contrast Dark', color: '#000000' },
        { id: 'hc-light', name: 'High Contrast Light', color: '#ffffff' },
        // 커스텀 테마들
        { id: 'night-owl', name: 'Night Owl', color: '#011627' },
        { id: 'monokai', name: 'Monokai', color: '#2D2A2E' },
        { id: 'github-dark', name: 'GitHub Dark', color: '#0d1117' },
        { id: 'github-light', name: 'GitHub Light', color: '#ffffff' },
        { id: 'dracula', name: 'Dracula', color: '#282a36' },
        { id: 'solarized-dark', name: 'Solarized Dark', color: '#002b36' },
        { id: 'cobalt2', name: 'Cobalt2', color: '#193549' },
      ],
      showThemeDropdown: false,
      minFontSize: 12,
      maxFontSize: 24,
    };
  },
  mounted() {
    // 외부 클릭으로 드롭다운 닫기
    document.addEventListener('click', this.handleOutsideClick);
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleOutsideClick);
  },
  methods: {
    toggleThemeDropdown() {
      this.showThemeDropdown = !this.showThemeDropdown;
    },

    handleOutsideClick(event) {
      // 드롭다운이 열려있고, 클릭한 곳이 theme-selector 밖이면 닫기
      if (this.showThemeDropdown && !this.$el.querySelector('.theme-selector').contains(event.target)) {
        this.showThemeDropdown = false;
      }
    },

    closeThemeDropdown() {
      this.showThemeDropdown = false;
    },

    selectTheme(themeId) {
      this.$emit('theme-change', themeId);
      this.showThemeDropdown = false;
    },

    decreaseFontSize() {
      const newSize = Math.max(this.fontSize - 2, this.minFontSize);
      if (newSize !== this.fontSize) {
        this.$emit('font-size-change', newSize);
      }
    },

    increaseFontSize() {
      const newSize = Math.min(this.fontSize + 2, this.maxFontSize);
      if (newSize !== this.fontSize) {
        this.$emit('font-size-change', newSize);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.control-buttons {
  display: flex;
  gap: 1vh;
  align-items: center;
  padding: 0 1.5vh;
  height: 100%;
}

.language-display {
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: 1.2vh;
  color: $electric-blue;
  padding: 0.5vh 1vh;
  background: rgba($electric-blue, 0.1);
  border: 0.2vh solid rgba($electric-blue, 0.3);
  border-radius: 0.6vh;
  backdrop-filter: blur(4px);
  min-width: 5vh;
  height: 3.5vh;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.theme-selector {
  position: relative;
}

.theme-toggle-btn {
  background: transparent;
  border: 0.2vh solid rgba($text-primary, 0.2);
  border-radius: 0.8vh;
  padding: 1vh;
  cursor: pointer;
  transition: all 0.3s ease;
  color: rgba($text-primary, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  width: 4vh;
  height: 4vh;

  &:hover {
    background: rgba($electric-blue, 0.1);
    border-color: rgba($electric-blue, 0.5);
    color: $electric-blue;
    transform: translateY(-1px);
  }

  .show-dropdown & {
    background: rgba($electric-blue, 0.2);
    border-color: $electric-blue;
    color: $electric-blue;
    @include neon-glow($electric-blue);
  }

  svg {
    width: clamp(14px, 2vh, 16px);
    height: clamp(14px, 2vh, 16px);
  }
}

.theme-dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  margin-top: 0.5vh;
  z-index: 2000;
  background: rgba($card-bg, 0.95);
  border: 1px solid rgba($electric-blue, 0.3);
  border-radius: 1vh;
  backdrop-filter: blur(20px);
  box-shadow:
    0 8px 32px rgba($electric-blue, 0.1),
    0 4px 16px rgba(0, 0, 0, 0.3);
  min-width: 16vh;
  max-height: 25vh;
  overflow-y: auto;
  will-change: transform;

  &::-webkit-scrollbar {
    width: 4px;
  }

  &::-webkit-scrollbar-track {
    background: transparent;
  }

  &::-webkit-scrollbar-thumb {
    background: rgba($electric-blue, 0.3);
    border-radius: 2px;
  }
}

.theme-list {
  padding: 0.5vh;
}

.theme-option {
  display: flex;
  align-items: center;
  gap: 0.8vh;
  width: 100%;
  padding: 0.8vh 1vh;
  background: transparent;
  border: 1px solid transparent;
  border-radius: 0.6vh;
  cursor: pointer;
  transition: all 0.2s ease;
  color: $text-primary;
  font-family: $font-primary;
  font-size: 1.1vh;
  font-weight: $font-weight-medium;

  &:hover {
    background: rgba($electric-blue, 0.1);
    border-color: rgba($electric-blue, 0.3);
    color: $electric-blue;
  }

  &.active {
    background: rgba($electric-blue, 0.2);
    border-color: $electric-blue;
    color: $electric-blue;
    @include neon-glow($electric-blue);
  }
}

.theme-preview {
  width: 1.5vh;
  height: 1.5vh;
  border-radius: 50%;
  border: 1px solid rgba($text-primary, 0.2);
  flex-shrink: 0;
}

.theme-name {
  flex: 1;
  text-align: left;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.font-size-controls {
  display: flex;
  gap: 0.5vh;
  padding: 0.5vh;
  background: rgba($card-bg, 0.8);
  border: 0.2vh solid rgba($electric-blue, 0.3);
  border-radius: 1vh;
  backdrop-filter: blur(4px);
  height: 4vh;
  align-items: center;
}

.font-btn {
  background: transparent;
  border: 0.15vh solid rgba($text-primary, 0.2);
  border-radius: 0.6vh;
  padding: 0.7vh;
  cursor: pointer;
  transition: all 0.3s ease;
  color: rgba($text-primary, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  width: 3vh;
  height: 3vh;

  &:hover {
    background: rgba($electric-blue, 0.1);
    border-color: rgba($electric-blue, 0.5);
    color: $electric-blue;
    transform: translateY(-1px);
  }

  &:active {
    background: rgba($electric-blue, 0.2);
    border-color: $electric-blue;
    color: $electric-blue;
    @include neon-glow($electric-blue);
  }

  svg {
    width: 1.5vh;
    height: 1.5vh;
  }
}
</style>
