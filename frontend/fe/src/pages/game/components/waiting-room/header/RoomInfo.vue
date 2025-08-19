<template>
  <div class="room-info">
    <GameTypeTag :type="gameType" />
    <span class="room-detail">방장 : {{ hostName }}</span>
  </div>
</template>

<script>
import GameTypeTag from '@/pages/game/components/shared/GameTypeTag.vue';

export default {
  name: 'RoomInfo',
  components: {
    GameTypeTag,
  },
  props: {
    gameType: {
      type: String,
      required: true,
      default: 'rank',
    },
    maxPlayers: {
      type: Number,
      required: true,
      default: 6,
    },
    language: {
      type: String,
      required: true,
      default: 'C++',
    },
    hostName: {
      type: String,
      required: true,
      default: 'CodeMaster',
    },
  },
};
</script>

<style lang="scss" scoped>
.room-info {
  display: flex;
  align-items: center;
  gap: calc(2vh);
  flex-shrink: 0;
}

.room-detail {
  font-family: $font-primary;
  font-weight: $font-weight-semibold;
  font-size: calc(1.8vh);
  color: rgba($text-primary, 0.9);
  background: linear-gradient(135deg, 
    rgba($electric-blue, 0.1) 0%, 
    rgba($electric-blue, 0.05) 50%, 
    rgba($card-bg, 0.4) 100%
  );
  border: calc(0.15vh) solid rgba($electric-blue, 0.3);
  border-radius: calc(0.8vh);
  padding: calc(0.6vh) calc(1.4vh);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(
      90deg,
      transparent 0%,
      rgba($electric-blue, 0.1) 50%,
      transparent 100%
    );
    opacity: 0;
    transform: translateX(-100%);
    transition: all 0.6s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  }

  &:hover {
    background: linear-gradient(135deg, 
      rgba($electric-blue, 0.15) 0%, 
      rgba($electric-blue, 0.08) 50%, 
      rgba($card-bg, 0.5) 100%
    );
    border-color: rgba($electric-blue, 0.5);
    color: $text-primary;
    transform: translateY(-0.1vh);
    @include neon-glow($electric-blue);

    &::before {
      opacity: 1;
      transform: translateX(100%);
    }
  }

  // 방장 정보는 특별한 스타일 적용
  background: linear-gradient(135deg, 
    rgba($cyberpunk-yellow, 0.1) 0%, 
    rgba($cyberpunk-yellow, 0.05) 50%, 
    rgba($card-bg, 0.4) 100%
  );
  border-color: rgba($cyberpunk-yellow, 0.3);
  color: rgba($cyberpunk-yellow, 0.9);
  font-weight: $font-weight-bold;

  &::before {
    background: linear-gradient(
      90deg,
      transparent 0%,
      rgba($cyberpunk-yellow, 0.1) 50%,
      transparent 100%
    );
  }

  &:hover {
    background: linear-gradient(135deg, 
      rgba($cyberpunk-yellow, 0.15) 0%, 
      rgba($cyberpunk-yellow, 0.08) 50%, 
      rgba($card-bg, 0.5) 100%
    );
    border-color: rgba($cyberpunk-yellow, 0.5);
    color: $cyberpunk-yellow;
    @include neon-glow($cyberpunk-yellow);
  }
}
</style>
