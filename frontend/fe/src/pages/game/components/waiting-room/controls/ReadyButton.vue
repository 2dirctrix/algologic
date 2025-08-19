<template>
  <button class="ready-button" @click="handleClick" @mouseenter="handleMouseEnter">
    {{ buttonText }}
  </button>
</template>

<script setup>
import { computed } from 'vue';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';

const waitingRoomStore = useInGameStore();

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

const handleClick = async () => {
  try {
    playClickSound();
    await waitingRoomStore.toggleReady();
  } catch (error) {
    console.error('준비 상태 변경 실패:', error);
  }
};

const buttonText = computed(() => {
  return waitingRoomStore.currentUser?.isReady ? '준비 취소' : '준비 완료';
});
</script>

<style lang="scss" scoped>
.ready-button {
  background: linear-gradient(135deg, 
    rgba($neon-green, 0.2) 0%, 
    rgba($neon-green, 0.1) 50%, 
    rgba($card-bg, 0.8) 100%
  );
  border: calc(0.3vh) solid rgba($neon-green, 0.8);
  border-radius: calc(1.2vh);
  color: $neon-green;
  cursor: pointer;
  padding: calc(1.2vh) calc(2.4vh);
  min-width: calc(12vh);
  height: calc(5.5vh);
  font-family: $font-primary;
  font-size: calc(2.2vh);
  font-weight: $font-weight-bold;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  text-shadow: 0 0 0.8vh rgba($neon-green, 0.6);
  @include neon-glow($neon-green);

  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(
      90deg,
      transparent 0%,
      rgba($neon-green, 0.3) 50%,
      transparent 100%
    );
    opacity: 0;
    transform: translateX(-100%);
    transition: all 0.6s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  }

  &:hover {
    background: linear-gradient(135deg, 
      rgba($neon-green, 0.3) 0%, 
      rgba($neon-green, 0.15) 50%, 
      rgba($card-bg, 0.9) 100%
    );
    border-color: $neon-green;
    color: lighten($neon-green, 20%);
    transform: translateY(-0.2vh) scale(1.02);
    @include neon-glow-strong($neon-green);

    &::before {
      opacity: 1;
      transform: translateX(100%);
    }
  }

  &:active {
    transform: translateY(0) scale(0.98);
  }
}
</style>
