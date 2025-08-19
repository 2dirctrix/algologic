<template>
  <div class="action-buttons">
    <button class="action-btn retry-btn" @click="$emit('retry')">다시 도전하기</button>

    <button class="action-btn back-btn" @click="$emit('back-to-main')">메인으로 돌아가기</button>
  </div>
</template>

<script setup>
defineEmits(['retry', 'back-to-main']);
</script>

<style lang="scss" scoped>
.action-buttons {
  width: 100%;
  display: flex;
  justify-content: center;
  gap: 2vw;
  margin-top: auto;
  padding-bottom: 2vh;
}

.action-btn {
  width: 18vw;
  height: 6vh;
  border-radius: $radius-small;
  font-family: $font-primary;
  font-size: 1.6vh;
  font-weight: $font-weight-semibold;
  cursor: pointer;
  transition: all $transition-base;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(1vh);
  position: relative;
  overflow: hidden;

  &:hover {
    transform: translateY(-0.2vh);
  }

  &:active {
    transform: translateY(0.1vh);
  }
}

.retry-btn {
  background: rgba($electric-blue, 0.2);
  border: 0.1vh solid $electric-blue;
  color: $electric-blue;

  &:hover {
    background: rgba($electric-blue, 0.3);
    @include neon-glow($electric-blue);
  }

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba($electric-blue, 0.1), transparent);
    transition: left 0.5s ease;
  }

  &:hover::before {
    left: 100%;
  }
}

.back-btn {
  background: rgba($cyberpunk-pink, 0.1);
  border: 0.1vh solid $cyberpunk-pink;
  color: $cyberpunk-pink;

  &:hover {
    background: rgba($cyberpunk-pink, 0.2);
    @include neon-glow($cyberpunk-pink);
  }

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba($cyberpunk-pink, 0.1), transparent);
    transition: left 0.5s ease;
  }

  &:hover::before {
    left: 100%;
  }
}
</style>
