<template>
  <button v-if="waitingRoomStore.currentUser.isHost" class="start-button" :disabled="!waitingRoomStore.canStartGame" @click="handleClick" @mouseenter="handleMouseEnter">
    게임 시작
  </button>
</template>

<script setup>
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';

const emit = defineEmits(['startGameClick']);

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
  if (waitingRoomStore.canStartGame) {
    playHoverSound();
  }
};

const handleClick = () => {
  if (!waitingRoomStore.canStartGame) return;
  
  playClickSound();
  // 상위 컴포넌트에 게임 시작 요청 이벤트 전파
  emit('startGameClick');
};
</script>

<style lang="scss" scoped>
.start-button {
  background: linear-gradient(135deg, 
    rgba($cyberpunk-yellow, 0.2) 0%, 
    rgba($cyberpunk-yellow, 0.1) 50%, 
    rgba($card-bg, 0.8) 100%
  );
  border: calc(0.3vh) solid rgba($cyberpunk-yellow, 0.8);
  border-radius: calc(1.2vh);
  color: $cyberpunk-yellow;
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
  text-shadow: 0 0 0.8vh rgba($cyberpunk-yellow, 0.6);

  &:not(:disabled) {
    @include neon-glow($cyberpunk-yellow);

    &::before {
      content: '';
      position: absolute;
      inset: 0;
      background: linear-gradient(
        90deg,
        transparent 0%,
        rgba($cyberpunk-yellow, 0.3) 50%,
        transparent 100%
      );
      opacity: 0;
      transform: translateX(-100%);
      transition: all 0.6s cubic-bezier(0.25, 0.46, 0.45, 0.94);
    }

    &:hover {
      background: linear-gradient(135deg, 
        rgba($cyberpunk-yellow, 0.3) 0%, 
        rgba($cyberpunk-yellow, 0.15) 50%, 
        rgba($card-bg, 0.9) 100%
      );
      border-color: $cyberpunk-yellow;
      color: lighten($cyberpunk-yellow, 20%);
      transform: translateY(-0.2vh) scale(1.02);
      @include neon-glow-strong($cyberpunk-yellow);

      &::before {
        opacity: 1;
        transform: translateX(100%);
      }
    }

    &:active {
      transform: translateY(0) scale(0.98);
    }
  }

  &:disabled {
    opacity: 0.4;
    cursor: not-allowed;
    background: linear-gradient(135deg, 
      rgba($card-bg, 0.7) 0%, 
      rgba($card-bg, 0.6) 50%, 
      rgba($card-bg, 0.5) 100%
    );
    border-color: rgba($cyberpunk-yellow, 0.3);
    color: rgba($cyberpunk-yellow, 0.5);
    text-shadow: none;
  }
}
</style>
