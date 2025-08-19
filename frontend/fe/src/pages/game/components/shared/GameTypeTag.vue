<template>
  <div class="game-type-tag" :class="`game-type-tag--${type}`">
    {{ displayText }}
  </div>
</template>

<script>
export default {
  name: 'GameTypeTag',
  props: {
    type: {
      type: String,
      required: true,
      default: 'rank',
      validator: value => ['rank', 'normal'].includes(value),
    },
  },
  computed: {
    displayText() {
      return this.type === 'rank' ? '랭크전' : '일반전';
    },
  },
};
</script>

<style lang="scss" scoped>
.game-type-tag {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: calc(0.7vh) calc(1.8vh);
  min-width: calc(7vh);
  height: calc(3.5vh);
  border-radius: calc(1vh);
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: calc(1.6vh);
  line-height: 1.3;
  text-align: center;
  white-space: nowrap;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  position: relative;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);

  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(
      90deg,
      transparent 0%,
      rgba(255, 255, 255, 0.2) 50%,
      transparent 100%
    );
    opacity: 0;
    transform: translateX(-100%);
    transition: all 0.6s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  }

  // 랭크전 스타일
  &--rank {
    background: linear-gradient(135deg, 
      rgba($cyberpunk-pink, 0.3) 0%, 
      rgba($cyberpunk-pink, 0.2) 30%,
      rgba($cyberpunk-pink, 0.1) 60%,
      rgba($card-bg, 0.8) 100%
    );
    border: calc(0.2vh) solid rgba($cyberpunk-pink, 0.8);
    color: lighten($cyberpunk-pink, 20%);
    text-shadow: 0 0 0.8vh rgba($cyberpunk-pink, 0.8);
    @include neon-glow-strong($cyberpunk-pink);
    animation: rankPulse 3s ease-in-out infinite;

    &:hover {
      background: linear-gradient(135deg, 
        rgba($cyberpunk-pink, 0.4) 0%, 
        rgba($cyberpunk-pink, 0.3) 30%,
        rgba($cyberpunk-pink, 0.15) 60%,
        rgba($card-bg, 0.9) 100%
      );
      border-color: $cyberpunk-pink;
      transform: translateY(-0.1vh) scale(1.05);

      &::before {
        opacity: 1;
        transform: translateX(100%);
      }
    }
  }

  // 일반전 스타일
  &--normal {
    background: linear-gradient(135deg, 
      rgba($electric-blue, 0.3) 0%, 
      rgba($electric-blue, 0.2) 30%,
      rgba($electric-blue, 0.1) 60%,
      rgba($card-bg, 0.8) 100%
    );
    border: calc(0.2vh) solid rgba($electric-blue, 0.8);
    color: lighten($electric-blue, 20%);
    text-shadow: 0 0 0.8vh rgba($electric-blue, 0.8);
    @include neon-glow($electric-blue);

    &:hover {
      background: linear-gradient(135deg, 
        rgba($electric-blue, 0.4) 0%, 
        rgba($electric-blue, 0.3) 30%,
        rgba($electric-blue, 0.15) 60%,
        rgba($card-bg, 0.9) 100%
      );
      border-color: $electric-blue;
      transform: translateY(-0.1vh) scale(1.05);

      &::before {
        opacity: 1;
        transform: translateX(100%);
      }
    }
  }
}

@keyframes rankPulse {
  0%, 100% {
    box-shadow: 
      0 0 calc(1vh) rgba($cyberpunk-pink, 0.4),
      0 0 calc(2vh) rgba($cyberpunk-pink, 0.2);
  }
  50% {
    box-shadow: 
      0 0 calc(1.5vh) rgba($cyberpunk-pink, 0.6),
      0 0 calc(3vh) rgba($cyberpunk-pink, 0.3);
  }
}
</style>
