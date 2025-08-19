<template>
  <div class="status-badge" :class="`status-badge--${status}`">
    {{ statusText }}
  </div>
</template>

<script>
export default {
  name: 'StatusBadge',
  props: {
    status: {
      type: String,
      required: true,
      validator: (value) => ['waiting', 'ready', 'playing', 'finished'].includes(value)
    }
  },
  computed: {
    statusConfig() {
      const configs = {
        waiting: { text: '대기 중', icon: '○' },
        ready: { text: '준비 완료', icon: '✓' },
        playing: { text: '게임 중', icon: '🎮' },
        finished: { text: '게임 종료', icon: '🏁' }
      }
      return configs[this.status]
    },
    statusText() {
      return this.statusConfig.text
    },
    statusIcon() {
      return this.statusConfig.icon
    }
  }
}
</script>

<style lang="scss" scoped>
.status-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 30px;
  border-radius: $radius-small;
  font-family: $font-primary;
  font-size: 12px;
  font-weight: $font-weight-regular;
  text-align: center;
  
  &--waiting {
    background: $cyberpunk-yellow;
    color: #22C55E;
  }
  
  &--ready {
    background: $neon-green;
    color: #22C55E;
  }
  
  &--playing {
    background: $electric-blue;
    color: $text-primary;
  }
  
  &--finished {
    background: $synthwave-purple;
    color: $text-primary;
  }
}

@media (max-width: 768px) {
  .status-badge {
    width: 50px;
    height: 24px;
    font-size: 10px;
  }
}
</style>