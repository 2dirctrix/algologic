<template>
  <div class="modal-overlay" @click="$emit('close')">
    <div class="modal-content" @click.stop>
      <slot />
    </div>
  </div>
</template>

<script>
export default {
  name: 'ModalOverlay',
  emits: ['close'],
};
</script>

<style scoped lang="scss">
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba($primary-bg, $opacity-backdrop);
  backdrop-filter: blur(25px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: $z-modal;
}

.modal-content {
  background: linear-gradient(135deg, rgba($card-bg, 0.6) 0%, rgba($card-bg, 0.5) 50%, rgba($primary-bg, 0.4) 100%);
  border: calc(0.3vh) solid rgba($electric-blue, 0.6);
  border-radius: calc(2.4vh);
  width: calc(110vh);
  height: calc(85vh);
  position: relative;
  overflow: hidden;
  box-shadow:
    0px calc(2vh) calc(6vh) 0px rgba(0, 0, 0, 0.4),
    inset 0vh 0.1vh 0vh 0vh rgba(255, 255, 255, 0.1);

  @include neon-glow($electric-blue);

  // 미묘한 패턴 효과
  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: repeating-linear-gradient(
      45deg,
      transparent,
      transparent 3px,
      rgba($electric-blue, 0.02) 3px,
      rgba($electric-blue, 0.02) 6px
    );
    border-radius: calc(2.4vh);
    pointer-events: none;
  }
}
</style>
