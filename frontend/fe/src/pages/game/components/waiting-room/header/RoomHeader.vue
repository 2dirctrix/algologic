<template>
  <div class="room-header">
    <div class="room-title">
      {{ roomData.title }}
    </div>
    <div class="room-info">
      <RoomInfo
        :game-type="roomData.gameType"
        :max-players="roomData.maxPlayers"
        :language="roomData.language"
        :host-name="roomData.host.name"
      />
    </div>
  </div>
</template>

<script>
import RoomInfo from './RoomInfo.vue';

export default {
  name: 'RoomHeader',
  components: {
    RoomInfo,
  },
  props: {
    roomData: {
      type: Object,
      required: true,
      default: () => ({
        title: '알고리즘 마스터 배틀룸',
        gameType: 'rank',
        maxPlayers: 6,
        language: 'C++',
        host: {
          name: 'CodeMaster',
        },
      }),
    },
  },
};
</script>

<style lang="scss" scoped>
.room-header {
  background: linear-gradient(45deg, rgba($card-bg, 0.25) 0%, rgba($card-bg, 0.35) 50%, rgba($primary-bg, 0.6) 100%);
  border: calc(0.3vh) solid rgba($electric-blue, 0.6);
  border-radius: calc(2vh);
  box-shadow:
    0vh 0.8vh 3.2vh 0vh rgba(0, 0, 0, 0.4),
    inset 0vh 0.1vh 0vh 0vh rgba(255, 255, 255, 0.1);
  width: 100%;
  min-height: calc(8vh);
  padding: calc(1.5vh) calc(3vh);
  backdrop-filter: blur(calc(2.5vh));
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: relative;
  overflow: hidden;
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
    border-radius: calc(2vh);
    pointer-events: none;
  }
}

.room-title {
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: calc(3.5vh);
  color: $text-primary;
  position: relative;
  z-index: 1;
}

.room-info {
  display: flex;
  align-items: center;
  position: relative;
  z-index: 1;
}
</style>
