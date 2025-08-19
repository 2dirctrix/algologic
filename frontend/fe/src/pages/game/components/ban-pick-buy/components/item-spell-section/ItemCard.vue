<template>
  <div class="item-card" :class="{ selected: isSelected, disabled }" @click="handleClick" @mouseenter="handleMouseEnter">
    <div class="item-card__icon">
      <img :src="itemIconSrc" :alt="item.itemName" @error="handleImageError" />
    </div>
    <div class="item-card__info">
      <div class="item-card__name">{{ item.itemName }}</div>
      <div class="item-card__cost">{{ item.cost }}G</div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';

// Props
const props = defineProps({
  item: {
    type: Object,
    required: true,
  },
  isSelected: {
    type: Boolean,
    default: false,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
});

// Emits
const emit = defineEmits(['select']);

// Reactive data
const imageError = ref(false);

// Computed
const itemIconSrc = computed(() => {
  if (imageError.value) {
    // 기본 아이템 아이콘으로 대체
    return new URL(`/src/game/assets/item-icon/default-item.png`, import.meta.url).href;
  }
  return new URL(`/src/game/assets/item-icon/item-${props.item.itemId}.png`, import.meta.url).href;
});

// Methods
const playHoverSound = () => {
  try {
    const audio = new Audio('/audio/interaction/button-hover-sound.wav');
    audio.volume = 0.3;
    audio.play().catch(error => {
      console.warn('Audio play failed:', error);
    });
  } catch (error) {
    console.warn('Audio creation failed:', error);
  }
};

const playClickSound = () => {
  try {
    const audio = new Audio('/audio/interaction/common-click-sound.wav');
    audio.volume = 0.6;
    audio.play().catch(error => {
      console.warn('Audio play failed:', error);
    });
  } catch (error) {
    console.warn('Audio creation failed:', error);
  }
};

const handleMouseEnter = () => {
  if (!props.disabled) {
    playHoverSound();
  }
};

const handleClick = () => {
  if (!props.disabled) {
    playClickSound();
    emit('select', props.item);
  }
};

const handleImageError = () => {
  imageError.value = true;
};
</script>

<style lang="scss" scoped>
@use 'sass:color';

.item-card {
  position: relative;
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 1vh;
  padding: 1vh;
  border-radius: 1vh;
  width: 100%;
  height: 8vh;
  min-height: 8vh;
  max-height: 8vh;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  background: linear-gradient(135deg, rgba($card-bg, 0.95) 0%, rgba($card-bg, 0.8) 50%, rgba($primary-bg, 0.9) 100%);
  border: 0.2vh solid rgba($electric-blue, 0.4);
  box-shadow:
    0vh 0.4vh 1.6vh 0vh rgba(0, 0, 0, 0.3),
    inset 0vh 0.1vh 0vh 0vh rgba(255, 255, 255, 0.05);

  // 배경 패턴
  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: repeating-linear-gradient(
      45deg,
      transparent,
      transparent 2px,
      rgba($electric-blue, 0.02) 2px,
      rgba($electric-blue, 0.02) 4px
    );
    border-radius: 0.8vh;
    pointer-events: none;
  }

  // 호버 효과
  &:hover:not(.disabled) {
    transform: translateY(-0.2vh) scale(1.02);
    background: linear-gradient(
      135deg,
      rgba($electric-blue, 0.15) 0%,
      rgba($card-bg, 0.9) 30%,
      rgba($card-bg, 0.7) 70%,
      rgba($electric-blue, 0.1) 100%
    );
    border-color: rgba($electric-blue, 0.8);
    @include neon-glow-strong($electric-blue);

    .item-card__icon {
      transform: scale(1.05);
      filter: brightness(1.2) drop-shadow(0 0 0.6vh rgba($electric-blue, 0.5));
    }

    .item-card__name {
      color: color.adjust($text-primary, $lightness: 15%);
      text-shadow: 0 0 0.5vh rgba($electric-blue, 0.5);
    }

    .item-card__cost {
      background: rgba($text-gold, 0.2);
      border-color: rgba($text-gold, 0.5);
      box-shadow: 0 0 0.8vh rgba($text-gold, 0.3);
    }
  }

  // 선택된 상태
  &.selected {
    transform: translateY(-0.1vh) scale(1.01);
    background: linear-gradient(135deg, rgba($neon-green, 0.2) 0%, rgba($neon-green, 0.1) 50%, rgba($electric-blue, 0.05) 100%);
    border-color: $neon-green;
    @include neon-glow-strong($neon-green);

    .item-card__icon {
      filter: brightness(1.1) drop-shadow(0 0 0.5vh rgba($neon-green, 0.6));
    }

    .item-card__name {
      color: color.adjust($neon-green, $lightness: 25%);
      text-shadow: 0 0 0.4vh rgba($neon-green, 0.6);
    }

    .item-card__cost {
      background: rgba($text-gold, 0.25);
      border-color: rgba($text-gold, 0.6);
      color: color.adjust($text-gold, $lightness: 20%);
    }
  }

  // 비활성화 상태
  &.disabled {
    cursor: not-allowed;
    opacity: 0.3;
    filter: grayscale(0.5);
    transform: scale(0.98);

    &:hover {
      transform: scale(0.98);
      box-shadow:
        0vh 0.4vh 1.6vh 0vh rgba(0, 0, 0, 0.3),
        inset 0vh 0.1vh 0vh 0vh rgba(255, 255, 255, 0.05);
    }
  }

  &__icon {
    position: relative;
    flex-shrink: 0;
    width: 6vh;
    height: 6vh;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 0.8vh;
    overflow: hidden;
    background: rgba($primary-bg, 0.6);
    border: 0.1vh solid rgba($electric-blue, 0.3);
    transition: all 0.3s ease;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      filter: drop-shadow(0 0 0.3vh rgba(0, 0, 0, 0.5));
    }
  }

  &__info {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 0.8vh;
    z-index: 1;
  }

  &__name {
    font-family: $font-primary;
    font-size: 1.8vh;
    font-weight: 600;
    color: $text-primary;
    line-height: 1.2;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    max-width: 100%;
    text-transform: uppercase;
    letter-spacing: 0.02em;
    transition: all 0.3s ease;
  }

  &__cost {
    display: inline-flex;
    align-items: center;
    font-family: $font-primary;
    font-size: 1.7vh;
    font-weight: 700;
    color: $text-gold;
    background: rgba($text-gold, 0.15);
    padding: 0.3vh 0.8vh;
    border-radius: 0.5vh;
    border: 0.1vh solid rgba($text-gold, 0.4);
    width: fit-content;
    white-space: nowrap;
    transition: all 0.3s ease;

    // 코인 아이콘 효과
    &::before {
      content: '💰';
      margin-right: 0.3vh;
      font-size: 1.1vh;
    }
  }
}
</style>
