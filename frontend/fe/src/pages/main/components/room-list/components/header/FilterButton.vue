<template>
  <div class="filter-wrapper" ref="filterWrapper">
    <button class="filter-btn" :class="[`filter-btn--${borderColor}`, { 'filter-btn--active': isExpanded }]" @click="toggleDropdown">
      {{ displayLabel }}
      <svg
        class="dropdown-icon"
        :class="{ 'dropdown-icon--rotated': isExpanded }"
        width="12"
        height="12"
        viewBox="0 0 12 12"
        fill="currentColor"
      >
        <path d="M6 8L2 4h8z" />
      </svg>
    </button>

    <!-- 드롭다운 메뉴 -->
    <div v-if="isExpanded" class="dropdown-menu" :class="`dropdown-menu--${borderColor}`">
      <div
        v-for="option in filterOptions"
        :key="option.value"
        class="dropdown-option"
        :class="{ 'dropdown-option--selected': selectedValue === option.value }"
        @click="selectOption(option)"
      >
        {{ option.label }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FilterButton',
  props: {
    label: {
      type: String,
      required: true,
    },
    borderColor: {
      type: String,
      default: 'neon-green',
      validator: value => ['neon-green', 'neon-magenta'].includes(value),
    },
    filterType: {
      type: String,
      default: 'language',
      validator: value => ['language', 'gameType', 'search'].includes(value),
    },
  },
  data() {
    return {
      isExpanded: false,
      selectedValue: null,
    };
  },
  computed: {
    displayLabel() {
      if (this.selectedValue) {
        const selectedOption = this.filterOptions.find(opt => opt.value === this.selectedValue);
        return selectedOption ? selectedOption.label : this.label;
      }
      return this.label;
    },
    filterOptions() {
      switch (this.filterType) {
        case 'language':
          return [
            { value: null, label: 'All Languages' },
            { value: 'JAVA', label: 'Java' },
            { value: 'CPP', label: 'C++' },
            { value: 'PYTHON', label: 'Python' },
          ];
        case 'gameType':
          return [
            { value: null, label: 'All Types' },
            { value: 'RANKED', label: '랭크전' },
            { value: 'NORMAL', label: '일반전' },
          ];
        case 'search':
          return [
            { value: 'roomName', label: '방 이름' },
            { value: 'ownerName', label: '방장' },
          ];
        default:
          return [];
      }
    },
  },
  mounted() {
    // 외부 클릭 시 드롭다운 닫기
    document.addEventListener('click', this.handleOutsideClick);
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleOutsideClick);
  },
  methods: {
    toggleDropdown() {
      this.isExpanded = !this.isExpanded;
    },
    selectOption(option) {
      this.selectedValue = option.value;
      this.isExpanded = false;
      this.$emit('filter-change', {
        type: this.filterType,
        value: option.value,
        label: option.label,
      });
    },
    handleOutsideClick(event) {
      if (this.$refs.filterWrapper && !this.$refs.filterWrapper.contains(event.target)) {
        this.isExpanded = false;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.filter-wrapper {
  position: relative;
  display: inline-block;
}

.filter-btn {
  background: linear-gradient(135deg, rgba($card-bg, 0.9) 0%, rgba($card-bg, 0.7) 50%, rgba($primary-bg, 0.8) 100%);
  border: calc(0.3vh) solid;
  color: $text-primary;
  border-radius: $radius-nested;
  padding: calc(1vh) calc(2vh);
  font-family: $font-accent;
  font-weight: $font-weight-medium;
  font-size: calc(1.8vh);
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  min-width: calc(12vh);
  height: calc(4.5vh);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: calc(1vh);
  position: relative;
  overflow: hidden;
  text-shadow: 0 0 0.5vh rgba($text-primary, 0.3);

  // Responsive adjustments
  @media (max-width: 1200px) {
    min-width: calc(10vh);
    font-size: calc(1.6vh);
    padding: calc(0.8vh) calc(1.5vh);
    gap: calc(0.8vh);
  }

  @media (max-width: 800px) {
    min-width: calc(8vh);
    font-size: calc(1.4vh);
    padding: calc(0.6vh) calc(1.2vh);
    gap: calc(0.6vh);
    flex: 1;
    max-width: calc(15vh);
  }

  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(90deg, transparent 0%, rgba($text-primary, 0.1) 50%, transparent 100%);
    opacity: 0;
    transform: translateX(-100%);
    transition: all 0.6s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  }

  &--neon-green {
    border-color: rgba($neon-green, 0.6);
    color: $neon-green;
    text-shadow: 0 0 0.5vh rgba($neon-green, 0.4);

    @include neon-glow($neon-green);

    &::before {
      background: linear-gradient(90deg, transparent 0%, rgba($neon-green, 0.2) 50%, transparent 100%);
    }
  }

  &--neon-magenta {
    border-color: rgba($neon-magenta, 0.6);
    color: $neon-magenta;
    text-shadow: 0 0 0.5vh rgba($neon-magenta, 0.4);

    @include neon-glow($neon-magenta);

    &::before {
      background: linear-gradient(90deg, transparent 0%, rgba($neon-magenta, 0.2) 50%, transparent 100%);
    }
  }

  &--active {
    background: linear-gradient(135deg, rgba($electric-blue, 0.2) 0%, rgba($card-bg, 0.8) 50%, rgba($primary-bg, 0.9) 100%);
    transform: scale(1.02);
  }

  &:hover {
    transform: translateY(-0.2vh) scale(1.02);

    &::before {
      opacity: 1;
      transform: translateX(100%);
    }

    &.filter-btn--neon-green {
      border-color: $neon-green;
      @include neon-glow-strong($neon-green);
    }

    &.filter-btn--neon-magenta {
      border-color: $neon-magenta;
      @include neon-glow-strong($neon-magenta);
    }
  }

  &:active {
    transform: translateY(0) scale(0.98);
  }
}

.dropdown-icon {
  width: calc(1.5vh);
  height: calc(1.5vh);
  transition: transform $transition-base;

  &--rotated {
    transform: rotate(180deg);
  }
}

.dropdown-menu {
  position: absolute;
  top: calc(100% + calc(0.5vh));
  left: 0;
  right: 0;
  background: rgba($card-bg, 0.95);
  border: calc(0.3vh) solid;
  border-radius: $radius-small;
  backdrop-filter: blur(8px);
  z-index: $z-dropdown;
  overflow: hidden;

  &--neon-green {
    border-color: $neon-green;
    box-shadow: 0 4px 20px rgba($neon-green, 0.3);
  }

  &--neon-magenta {
    border-color: $neon-magenta;
    box-shadow: 0 4px 20px rgba($neon-magenta, 0.3);
  }
}

.dropdown-option {
  padding: calc(1.5vh) calc(2vh);
  font-family: $font-primary;
  font-weight: $font-weight-medium;
  font-size: calc(1.8vh);
  color: $text-primary;
  cursor: pointer;
  transition: background-color $transition-fast;
  border-bottom: calc(0.3vh) solid rgba($text-primary, 0.1);

  &:last-child {
    border-bottom: none;
  }

  &:hover {
    background: rgba($text-primary, $opacity-minimal);
  }

  &--selected {
    background: rgba($electric-blue, $opacity-active);
    color: $electric-blue;
  }
}
</style>
