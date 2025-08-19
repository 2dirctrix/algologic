<template>
  <button class="create-room-btn" @click="handleCreateRoom" @mouseenter="handleMouseEnter">Create</button>
</template>

<script>
export default {
  name: 'CreateRoomButton',
  methods: {
    playHoverSound() {
      try {
        const audio = new Audio('/audio/interaction/button-hover-sound.wav');
        audio.volume = 0.3;
        audio.play().catch(error => {
          console.warn('Audio play failed:', error);
        });
      } catch (error) {
        console.warn('Audio creation failed:', error);
      }
    },
    playClickSound() {
      try {
        const audio = new Audio('/audio/interaction/common-click-sound.wav');
        audio.volume = 0.6;
        audio.play().catch(error => {
          console.warn('Audio play failed:', error);
        });
      } catch (error) {
        console.warn('Audio creation failed:', error);
      }
    },
    handleMouseEnter() {
      this.playHoverSound();
    },
    handleCreateRoom() {
      this.playClickSound();
      this.$emit('create-room');
    },
  },
};
</script>

<style lang="scss" scoped>
.create-room-btn {
  background: linear-gradient(135deg, rgba($electric-blue, 0.2) 0%, rgba($electric-blue, 0.1) 50%, rgba($card-bg, 0.8) 100%);
  border: calc(0.3vh) solid rgba($electric-blue, 0.8);
  border-radius: $radius-nested;
  color: $electric-blue;
  cursor: pointer;
  padding: calc(1vh) calc(3vh);
  min-width: calc(12vh);
  height: calc(4.5vh);
  font-family: $font-accent;
  font-size: calc(1.8vh);
  font-weight: $font-weight-regular;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  text-shadow: 0 0 0.8vh rgba($electric-blue, 0.6);
  @include neon-glow($electric-blue);

  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(90deg, transparent 0%, rgba($electric-blue, 0.3) 50%, transparent 100%);
    opacity: 0;
    transform: translateX(-100%);
    transition: all 0.6s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  }

  // Responsive adjustments
  @media (max-width: 1200px) {
    min-width: calc(10vh);
    font-size: calc(1.6vh);
    padding: calc(0.8vh) calc(2.5vh);
  }

  @media (max-width: 800px) {
    min-width: calc(8vh);
    font-size: calc(1.4vh);
    padding: calc(0.6vh) calc(2vh);
    flex: 1;
    max-width: calc(15vh);
  }

  &:hover {
    background: linear-gradient(135deg, rgba($electric-blue, 0.3) 0%, rgba($electric-blue, 0.15) 50%, rgba($card-bg, 0.9) 100%);
    border-color: $electric-blue;
    color: lighten($electric-blue, 20%);
    transform: translateY(-0.2vh) scale(1.02);
    @include neon-glow-strong($electric-blue);

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
