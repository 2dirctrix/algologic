<template>
  <div class="modal-overlay" @click="$emit('cancel')">
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <h2>게임 종료 확인</h2>
      </div>

      <div class="modal-body">
        <p class="confirm-message">
          코드 제출을 종료하시겠습니까?<br>
          종료 시 더 이상 제출이 불가능합니다.
        </p>
      </div>

      <div class="modal-actions">
        <button class="cancel-button" @click="$emit('cancel')">취소</button>
        <button class="confirm-button" @click="$emit('confirm')">수락</button>
      </div>
    </div>
  </div>
</template>

<script setup>
defineEmits(['cancel', 'confirm']);
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
  background: rgba($card-bg, 0.35);
  border: calc(0.2vh) solid $electric-blue;
  border-radius: calc(2.4vh);
  width: 50vh;
  min-height: 25vh;
  position: relative;
  box-shadow: 0px calc(2vh) calc(6vh) 0px rgba(0, 0, 0, 0.4);
  display: flex;
  flex-direction: column;
  padding: 3vh 4vh;

  @include neon-glow($electric-blue);
}

.modal-header {
  text-align: center;
  margin-bottom: 3vh;

  h2 {
    font-family: $font-primary;
    font-weight: $font-weight-bold;
    font-size: 2.5vh;
    color: $text-primary;
    margin: 0;
  }
}

.modal-body {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 3vh;

  .confirm-message {
    font-family: $font-primary;
    font-weight: $font-weight-medium;
    font-size: 2vh;
    color: $text-primary;
    text-align: center;
    line-height: 1.5;
    margin: 0;
  }
}

.modal-actions {
  display: flex;
  gap: 2vh;
  width: 100%;
}

.cancel-button,
.confirm-button {
  flex: 1;
  height: 5.4vh;
  border-radius: 1.2vh;
  font-family: $font-primary;
  font-weight: $font-weight-semibold;
  font-size: 2vh;
  line-height: 1.2;
  cursor: pointer;
  transition: all $transition-base;
  display: flex;
  align-items: center;
  justify-content: center;
}

.cancel-button {
  background: rgba($text-primary, $opacity-minimal);
  border: 0.2vh solid $electric-blue;
  color: $text-primary;

  &:hover {
    background: rgba($text-primary, $opacity-inactive);
    @include neon-glow($electric-blue);
  }
}

.confirm-button {
  background: $cyberpunk-pink;
  border: none;
  color: $text-primary;
  box-shadow: 0px 0.4vh 1.5vh 0px rgba($cyberpunk-pink, 0.3);

  &:hover {
    @include neon-glow($cyberpunk-pink);
  }
}
</style>
