<template>
  <div class="modal-header">
    <h1 class="modal-title">새 게임 생성</h1>
    <button class="close-button" @click="$emit('close')">×</button>
  </div>
</template>

<script>
export default {
  name: 'ModalHeader',
  emits: ['close'],
};
</script>

<style scoped lang="scss">
.modal-header {
  position: relative;
  padding: calc(4.5vh) calc(9.7vh);
}

.modal-title {
  font-family: $font-primary;
  font-size: calc(4vh);
  font-weight: 400;
  line-height: 0.9em;
  color: $electric-blue;
  margin: 0;
}

.close-button {
  position: absolute;
  top: calc(2.2vh);
  right: calc(2.2vh);
  width: calc(7.5vh);
  height: calc(4.5vh);
  background: rgba($text-primary, $opacity-minimal);
  border: none;
  border-radius: calc(5vh);
  color: $text-primary;
  font-size: calc(2vh);
  font-family: 'Poppins', sans-serif;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: $transition-base;

  &:hover {
    background: rgba($text-primary, $opacity-inactive);
    @include neon-glow($text-primary);
  }
}
</style>
