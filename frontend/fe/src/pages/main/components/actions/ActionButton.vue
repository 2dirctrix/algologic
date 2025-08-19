<template>
  <button class="action-button" @click="handleClick" @mouseenter="handleMouseEnter">
    {{ label }}
  </button>
</template>

<script setup>
defineProps({
  label: {
    type: String,
    required: true,
  },
});

const emit = defineEmits(['click']);

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
  playHoverSound();
};

const handleClick = () => {
  playClickSound();
  emit('click');
};
</script>

<style lang="scss" scoped>
.action-button {
  width: 100%;
  height: 100%;
  background:
    linear-gradient(
      135deg,
      rgba($cyberpunk-pink, 0.15) 0%,
      rgba($electric-blue, 0.12) 30%,
      rgba($cyberpunk-yellow, 0.12) 70%,
      rgba($cyberpunk-pink, 0.15) 100%
    ),
    rgba($card-bg, 0.8);
  background-size:
    200% 200%,
    100% 100%;
  border: calc(0.2vh) solid rgba($cyberpunk-pink, 0.4);
  border-radius: calc(2vh);
  color: $text-primary;
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: calc(2.2vh);
  line-height: 1.2;
  text-align: center;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  position: relative;
  overflow: hidden;
  animation: gradientShift 6s ease infinite;
  @include neon-glow($cyberpunk-pink);

  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(90deg, transparent 0%, rgba(255, 255, 255, 0.1) 50%, transparent 100%);
    opacity: 0;
    transform: translateX(-100%);
    transition: all 4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  }

  &:hover {
    background:
      linear-gradient(
        135deg,
        rgba($cyberpunk-yellow, 0.35) 0%,
        rgba($cyberpunk-pink, 0.3) 30%,
        rgba($electric-blue, 0.5) 70%,
        rgba($cyberpunk-yellow, 0.45) 100%
      ),
      rgba($card-bg, 0.9);
    background-size:
      200% 200%,
      100% 100%;
    animation: reverseGradientShift 4s ease infinite;
    border-color: lighten($cyberpunk-pink, 15%);
    color: lighten($text-primary, 20%);
    transform: translateY(-0.2vh) scale(1.02);
    @include neon-glow-strong($cyberpunk-pink);

    &::before {
      opacity: 1;
      transform: translateX(100%);
    }
  }

  &:focus {
    outline: none;
  }

  &:active {
    transform: translateY(0.1vh) scale(0.98);
  }
}

@keyframes gradientShift {
  0%,
  100% {
    background-position:
      0% 50%,
      0% 0%;
  }
  50% {
    background-position:
      100% 50%,
      0% 0%;
  }
}

@keyframes reverseGradientShift {
  0%,
  100% {
    background-position:
      100% 50%,
      0% 0%;
  }
  50% {
    background-position:
      0% 50%,
      0% 0%;
  }
}
</style>
