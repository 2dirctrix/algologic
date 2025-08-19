<template>
  <div class="ready-status-badge" :class="badgeClass">
    {{ displayText }}
  </div>
</template>

<script>
export default {
  name: 'ReadyStatusBadge',
  props: {
    status: {
      type: String,
      required: true,
      validator: value => ['ready', 'waiting'].includes(value),
    },
    connected: {
      type: Boolean,
      default: true,
    },
  },
  computed: {
    displayText() {
      if (this.connected === false) {
        return '연결유실';
      }
      return this.status === 'ready' ? '준비완료' : '준비 중';
    },
    badgeClass() {
      if (this.connected === false) {
        return 'ready-status-badge--disconnected';
      }
      return `ready-status-badge--${this.status}`;
    },
  },
};
</script>

<style lang="scss" scoped>
@use 'sass:color';

.ready-status-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: calc(8vh);
  height: calc(3.5vh);
  border-radius: calc(1vh);
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: calc(1.4vh);
  line-height: 1.3;
  text-align: center;
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  position: relative;
  overflow: hidden;

  &--ready {
    background: linear-gradient(135deg, rgba($neon-green, 0.9) 0%, rgba($neon-green, 0.7) 50%, rgba($neon-green, 0.8) 100%);
    border: calc(0.2vh) solid $neon-green;
    color: color.scale($text-primary, $lightness: -10%);
    text-shadow: 0 0 0.5vh rgba($neon-green, 0.8);
    @include neon-glow-strong($neon-green);
    animation: readyPulse 2s ease-in-out infinite;
  }

  &--waiting {
    background: linear-gradient(
      135deg,
      rgba($cyberpunk-yellow, 0.8) 0%,
      rgba($cyberpunk-yellow, 0.6) 50%,
      rgba($cyberpunk-yellow, 0.7) 100%
    );
    border: calc(0.2vh) solid $cyberpunk-yellow;
    color: color.scale($text-primary, $lightness: -10%);
    text-shadow: 0 0 0.5vh rgba($cyberpunk-yellow, 0.8);
    @include neon-glow($cyberpunk-yellow);
  }

  &--disconnected {
    background: linear-gradient(135deg, rgba(255, 0, 0, 0.9) 0%, rgba(255, 0, 0, 0.7) 50%, rgba(255, 0, 0, 0.8) 100%);
    border: calc(0.2vh) solid rgba(255, 0, 0, 0.9);
    color: $text-primary;
    text-shadow: 0 0 0.5vh rgba(255, 0, 0, 0.8);
    @include neon-glow-strong(rgba(255, 0, 0, 0.9));
    animation: disconnectFlash 1s ease-in-out infinite;
  }
}

@keyframes readyPulse {
  0%,
  100% {
    opacity: 0.9;
    transform: scale(1);
  }
  50% {
    opacity: 1;
    transform: scale(1.05);
  }
}

@keyframes disconnectFlash {
  0%,
  100% {
    opacity: 0.8;
  }
  50% {
    opacity: 1;
  }
}
</style>
