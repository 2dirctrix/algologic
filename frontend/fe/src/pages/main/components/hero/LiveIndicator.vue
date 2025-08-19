<template>
  <div class="live-indicator">
    <span class="live-text">Live</span>
  </div>
</template>

<script setup>
// Live 상태 관리는 추후 필요시 추가
</script>

<style lang="scss" scoped>
.live-indicator {
  width: calc(6vh);
  height: calc(2.8vh);
  background: $hot-pink;
  border-radius: $radius-nested;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0px 0px 10px rgba($hot-pink, 0.6);
}

.live-text {
  font-family: $font-primary;
  font-size: calc(2vh);
  font-weight: 400;
  color: $text-primary;
  line-height: 0.9em;
}
</style>
