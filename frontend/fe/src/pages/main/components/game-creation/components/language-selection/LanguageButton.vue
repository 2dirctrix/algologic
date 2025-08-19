<template>
  <button
    :class="['language-button', { selected: isSelected, disabled: isDisabled }]"
    :data-language="language.id"
    @click="handleClick"
    :disabled="isDisabled"
  >
    <span class="language-name">{{ language.name }}</span>
    <span v-if="isSelected" class="check-indicator">
      <span class="check-mark">✓</span>
    </span>
  </button>
</template>

<script setup>
const props = defineProps({
  language: {
    type: Object,
    required: true,
    default: () => ({ id: '', name: '' }),
  },
  isSelected: {
    type: Boolean,
    default: false,
  },
  isDisabled: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['select']);

const handleClick = () => {
  if (props.isDisabled) return;
  if (props.language && props.language.id) {
    emit('select', props.language.id);
  }
};
</script>

<style scoped lang="scss">
@use 'sass:color';

.language-button {
  position: relative;
  width: 100%;
  height: calc(7.5vh);
  background: linear-gradient(135deg, rgba($electric-blue, 0.08) 0%, rgba($card-bg, 0.7) 50%, rgba($electric-blue, 0.05) 100%);
  border: calc(0.2vh) solid rgba($electric-blue, 0.3);
  border-radius: calc(1.2vh);
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  overflow: hidden;

  display: flex;
  align-items: center;
  justify-content: center;

  // 내부 글로우 효과
  &::before {
    content: '';
    position: absolute;
    inset: 0.15vh;
    background: linear-gradient(135deg, rgba($electric-blue, 0.03) 0%, transparent 50%, rgba($electric-blue, 0.03) 100%);
    border-radius: calc(1vh);
    transition: all 0.4s ease;
    z-index: -1;
  }

  &:not(.selected):not(.disabled) {
    &:hover {
      background: linear-gradient(135deg, rgba($electric-blue, 0.15) 0%, rgba($card-bg, 0.8) 50%, rgba($electric-blue, 0.1) 100%);
      border-color: rgba($electric-blue, 0.5);
      transform: translateY(-0.1vh) scale(1.02);
      @include neon-glow($electric-blue);

      &::before {
        background: linear-gradient(135deg, rgba($electric-blue, 0.08) 0%, rgba($electric-blue, 0.03) 50%, rgba($electric-blue, 0.08) 100%);
      }
    }

    &:active {
      transform: translateY(0) scale(1.01);
      transition: all 0.15s ease;
    }
  }

  &.disabled {
    background: linear-gradient(135deg, rgba($card-bg, 0.3) 0%, rgba($primary-bg, 0.5) 50%, rgba($card-bg, 0.2) 100%) !important;
    border-color: rgba($electric-blue, 0.1) !important;
    cursor: not-allowed !important;
    opacity: 0.4;
    transform: none !important;

    .language-name {
      color: rgba($text-primary, 0.3);
    }

    &::before {
      background: rgba($electric-blue, 0.01);
    }

    &:hover {
      background: linear-gradient(135deg, rgba($card-bg, 0.3) 0%, rgba($primary-bg, 0.5) 50%, rgba($card-bg, 0.2) 100%) !important;
      border-color: rgba($electric-blue, 0.1) !important;
      transform: none !important;
    }
  }
}

.language-button[data-language='python'].selected {
  background: linear-gradient(135deg, rgba($neon-magenta, 0.3) 0%, rgba($neon-magenta, 0.2) 50%, rgba($electric-blue, 0.1) 100%) !important;
  border: calc(0.3vh) solid $neon-magenta !important;
  transform: scale(1.05) !important;
  @include neon-glow-strong($neon-magenta);

  &::before {
    background: linear-gradient(135deg, rgba($neon-magenta, 0.1) 0%, transparent 50%, rgba($electric-blue, 0.05) 100%) !important;
  }

  .language-name {
    color: color.adjust($neon-magenta, $lightness: 15%) !important;
    font-weight: $font-weight-bold !important;
  }

  .check-mark {
    background: $neon-magenta;
    @include neon-glow($neon-magenta);
  }
}

.language-button[data-language='java'].selected {
  background: linear-gradient(
    135deg,
    rgba($cyberpunk-yellow, 0.3) 0%,
    rgba($cyberpunk-yellow, 0.2) 50%,
    rgba($electric-blue, 0.1) 100%
  ) !important;
  border: calc(0.3vh) solid $cyberpunk-yellow !important;
  transform: scale(1.05) !important;
  @include neon-glow-strong($cyberpunk-yellow);

  &::before {
    background: linear-gradient(135deg, rgba($cyberpunk-yellow, 0.1) 0%, transparent 50%, rgba($electric-blue, 0.05) 100%) !important;
  }

  .language-name {
    color: color.adjust($cyberpunk-yellow, $lightness: 15%) !important;
    font-weight: $font-weight-bold !important;
  }

  .check-mark {
    background: $cyberpunk-yellow;
    color: $primary-bg;
    @include neon-glow($cyberpunk-yellow);
  }
}

.language-button[data-language='cpp'].selected {
  background: linear-gradient(135deg, rgba(#0000ff, 0.3) 0%, rgba(#0000ff, 0.2) 50%, rgba($electric-blue, 0.1) 100%) !important;
  border: calc(0.3vh) solid #0000ff !important;
  transform: scale(1.05) !important;
  @include neon-glow-strong(#0000ff);

  &::before {
    background: linear-gradient(135deg, rgba(#0000ff, 0.1) 0%, transparent 50%, rgba($electric-blue, 0.05) 100%) !important;
  }

  .language-name {
    color: color.adjust(#0000ff, $lightness: 25%) !important;
    font-weight: $font-weight-bold !important;
  }

  .check-mark {
    background: #0000ff;
    @include neon-glow(#0000ff);
  }
}

.language-button[data-language='javascript'].selected {
  background: linear-gradient(135deg, rgba($neon-green, 0.3) 0%, rgba($neon-green, 0.2) 50%, rgba($electric-blue, 0.1) 100%) !important;
  border: calc(0.3vh) solid $neon-green !important;
  transform: scale(1.05) !important;
  @include neon-glow-strong($neon-green);

  &::before {
    background: linear-gradient(135deg, rgba($neon-green, 0.1) 0%, transparent 50%, rgba($electric-blue, 0.05) 100%) !important;
  }

  .language-name {
    color: color.adjust($neon-green, $lightness: 15%) !important;
    font-weight: $font-weight-bold !important;
  }

  .check-mark {
    background: $neon-green;
    color: $primary-bg;
    @include neon-glow($neon-green);
  }
}

.language-button[data-language='go'].selected {
  background: linear-gradient(
    135deg,
    rgba($electric-blue, 0.3) 0%,
    rgba($electric-blue, 0.2) 50%,
    rgba($cyberpunk-yellow, 0.1) 100%
  ) !important;
  border: calc(0.3vh) solid $electric-blue !important;
  transform: scale(1.05) !important;
  @include neon-glow-strong($electric-blue);

  &::before {
    background: linear-gradient(135deg, rgba($electric-blue, 0.1) 0%, transparent 50%, rgba($cyberpunk-yellow, 0.05) 100%) !important;
  }

  .language-name {
    color: color.adjust($electric-blue, $lightness: 15%) !important;
    font-weight: $font-weight-bold !important;
  }

  .check-mark {
    background: $electric-blue;
    color: $primary-bg;
    @include neon-glow($electric-blue);
  }
}

.language-button[data-language='rust'].selected {
  background: linear-gradient(135deg, rgba(#ff4d66, 0.3) 0%, rgba(#ff4d66, 0.2) 50%, rgba($electric-blue, 0.1) 100%) !important;
  border: calc(0.3vh) solid #ff4d66 !important;
  transform: scale(1.05) !important;
  @include neon-glow-strong(#ff4d66);

  &::before {
    background: linear-gradient(135deg, rgba(#ff4d66, 0.1) 0%, transparent 50%, rgba($electric-blue, 0.05) 100%) !important;
  }

  .language-name {
    color: color.adjust(#ff4d66, $lightness: 15%) !important;
    font-weight: $font-weight-bold !important;
  }

  .check-mark {
    background: #ff4d66;
    @include neon-glow(#ff4d66);
  }
}

.language-name {
  font-family: $font-primary;
  font-weight: $font-weight-medium;
  font-size: calc(2vh);
  color: $text-primary;
  text-align: center;
  flex: 1;
}

.check-indicator {
  position: absolute;
  right: calc(1.2vh);
  top: 50%;
  transform: translateY(-50%);
  width: calc(1.2vh);
  height: calc(1.4vh);
  display: flex;
  align-items: center;
  justify-content: center;
}

.check-mark {
  width: calc(1.2vh);
  height: calc(1.2vh);
  background: $neon-magenta;
  border-radius: 50%;
  color: $text-primary;
  font-size: calc(0.8vh);
  font-weight: bold;
  display: flex;
  align-items: center;
  justify-content: center;
  line-height: 1;
}
</style>
