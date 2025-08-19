<template>
  <div class="room-list-card" :class="{ 'room-list-card--in-game': roomStatus === 'in-game' }" @click="handleCardClick">
    <div class="room-list-card__background"></div>
    <div class="room-list-card__content">
      <!-- 게임 타입 태그 -->
      <span class="game-type-tag" :class="gameType === 'RANKED' ? 'game-type-tag--rank' : 'game-type-tag--general'">
        {{ gameType === 'RANKED' ? '랭크' : '일반' }}
      </span>

      <!-- 언어 -->
      <div class="language">{{ language }}</div>

      <!-- 방 이름 -->
      <div class="room-info">
        <h3 class="room-name">{{ roomName }}</h3>
        <span class="room-owner">{{ roomOwner }}</span>
      </div>

      <!-- 플렉서블 스페이서 -->
      <div class="spacer"></div>

      <!-- 참가자 인디케이터 -->
      <div class="player-indicators">
        <div
          v-for="index in maxPlayers"
          :key="index"
          class="player-dot"
          :class="index <= currentPlayers ? 'filled' : 'empty'"
          :style="index <= currentPlayers ? { backgroundColor: getBorderColorValue(gameType) } : {}"
        ></div>
      </div>

      <!-- 참가자 수 -->
      <div class="player-count">
        <div class="player-number">{{ currentPlayers }}</div>
        <div class="player-label">Players</div>
      </div>

      <!-- 입장 버튼 -->
      <button
        class="join-btn"
        :class="roomStatus === 'in-game' ? 'join-btn--in-game' : 'join-btn--available'"
        :disabled="roomStatus === 'in-game'"
        @click.stop="handleJoinClick"
      >
        {{ roomStatus === 'in-game' ? '게임중' : '참가' }}
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RoomListCard',
  props: {
    roomId: String,
    roomName: String,
    gameType: String,
    language: String,
    currentPlayers: Number,
    maxPlayers: Number,
    roomStatus: String,
    borderColor: String,
    roomOwner: String,
  },
  methods: {
    handleCardClick() {
      if (this.roomStatus !== 'in-game') {
        this.$emit('join-room', this.roomId);
      }
    },
    playClickSound() {
      try {
        const audio = new Audio('/audio/interaction/common-click-sound.wav');
        audio.volume = 0.6;
        audio.play().catch(error => {
          console.warn('Audio play failed:', error);
        });
      } catch (error) {
        console.warn('Audio creation failed:', error);
      }
    },
    handleJoinClick() {
      if (this.roomStatus !== 'in-game') {
        this.playClickSound();
      }
      this.$emit('join-room', this.roomId);
    },
    getBorderColorValue(gameType) {
      switch (gameType) {
        case 'RANKED':
          return '#ff6b9d';
        case 'NORMAL':
          return '#f9ca24';
        default:
          return '#00d2ff';
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.room-list-card {
  position: relative;
  width: 100%;
  height: calc(8vh);
  border-radius: $radius-nested;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  overflow: hidden;
  box-sizing: border-box;
  margin-bottom: calc(1vh);

  &:last-child {
    margin-bottom: 0;
  }

  &__background {
    position: absolute;
    inset: 0;
    border-radius: $radius-nested;
    background: linear-gradient(
      90deg,
      rgba($card-bg, 0.9) 0%,
      rgba($card-bg, 0.5) 15%,
      rgba($card-bg, 0.75) 50%,
      rgba($card-bg, 0.48) 85%,
      rgba($card-bg, 0.9) 100%
    );
    border: 0.2vh solid rgba($electric-blue, 0.4);
    box-shadow:
      0vh 0.6vh 2vh 0vh rgba(0, 0, 0, 0.4),
      inset 0vh 0.1vh 0vh 0vh rgba(255, 255, 255, 0.08);

    &::before {
      content: '';
      position: absolute;
      inset: 0;
      background: repeating-linear-gradient(
        45deg,
        transparent,
        transparent 2px,
        rgba($electric-blue, 0.02) 2px,
        rgba($electric-blue, 0.02) 4px
      );
      border-radius: calc($radius-nested - 0.2vh);
    }
  }

  &__content {
    position: absolute;
    inset: 0;
    z-index: 1;
    display: flex;
    align-items: center;
    padding: calc(2vh) calc(3vh);
    gap: calc(2.5vh);
    box-sizing: border-box;
  }

  &:hover:not(&--in-game) {
    transform: translateY(-0.2vh) scale(1.01);

    .room-list-card__background {
      background: linear-gradient(135deg, rgba($card-bg, 0.95) 0%, rgba($card-bg, 0.8) 50%, rgba($primary-bg, 0.9) 100%);
      border-color: rgba($electric-blue, 0.8);
      @include neon-glow-strong($electric-blue);

      &::before {
        background: repeating-linear-gradient(
          45deg,
          transparent,
          transparent 2px,
          rgba($electric-blue, 0.08) 2px,
          rgba($electric-blue, 0.08) 4px
        );
      }
    }
  }

  &--in-game {
    cursor: not-allowed;
    filter: blur(0.1vh) grayscale(0.5);
    opacity: 0.7;
    transform: scale(0.99);

    .room-list-card__background {
      background: linear-gradient(135deg, rgba($synthwave-purple, 0.3) 0%, rgba($synthwave-purple, 0.2) 50%, rgba($primary-bg, 0.9) 100%);
      border-color: $synthwave-purple;
    }
  }
}

.game-type-tag {
  padding: calc(0.8vh) calc(1.8vh);
  border-radius: calc(2vh);
  font-family: $font-primary;
  font-weight: $font-weight-regular;
  font-size: calc(1.5vh);
  color: $text-primary;
  width: calc(8vh);
  text-align: center;
  flex-shrink: 0;
  border: calc(0.3vh) solid;
}

.game-type-tag--general {
  background: rgba($electric-blue, 0.4); // 투명도 증가
  border-color: $electric-blue;
}

.game-type-tag--rank {
  background: rgba($cyberpunk-yellow, 0.4); // 투명도 증가
  border-color: $cyberpunk-yellow;
}

.language {
  font-family: $font-accent;
  font-weight: $font-weight-regular;
  font-size: calc(2.5vh);
  color: $text-primary;
  width: calc(10vh);
  flex-shrink: 0;
}

.room-info {
  display: flex;
  align-items: center;
  gap: calc(1.5vh);
  flex-shrink: 0;
}

.room-name {
  font-family: $font-primary;
  font-weight: $font-weight-medium;
  font-size: calc(2.5vh);
  color: $text-primary;
  margin: 0;

  max-width: calc(15vh);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.room-owner {
  font-family: $font-primary;
  font-weight: $font-weight-regular;
  font-size: calc(1.8vh);
  color: $text-primary;
  opacity: $opacity-text-secondary;
}

.spacer {
  flex: 1;
  min-width: calc(2.5vh);
}

.player-count {
  display: flex;
  align-items: center;
  gap: calc(1vh);
  width: calc(12vh);
  flex-shrink: 0;
}

.player-number {
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: calc(5vh);
  color: $text-primary;
  line-height: 1;
}

.player-label {
  font-family: $font-primary;
  font-weight: $font-weight-regular;
  font-size: calc(1.5vh);
  color: $text-primary;
  opacity: $opacity-text-secondary;
}

.player-indicators {
  display: flex;
  gap: calc(0.6vh);
  min-width: calc(8vh);
  flex-shrink: 0;
  justify-content: flex-start;
}

.player-dot {
  width: calc(2vh);
  height: calc(2vh);
  border-radius: 50%;
  border: calc(0.2vh) solid $neon-green;
  flex-shrink: 0;
}

.player-dot.empty {
  background: transparent;
  border-color: rgba($text-primary, 0.3);
}

.join-btn {
  background: linear-gradient(135deg, rgba($neon-green, 0.2) 0%, rgba($neon-green, 0.1) 50%, rgba($card-bg, 0.8) 100%);
  border: calc(0.3vh) solid rgba($neon-green, 0.8);
  border-radius: $radius-medium;
  color: $neon-green;
  cursor: pointer;
  padding: calc(1.5vh) calc(3vh);
  width: calc(10vh);
  height: calc(5vh);
  font-family: $font-primary;
  font-size: calc(1.8vh);
  font-weight: $font-weight-semibold;
  flex-shrink: 0;
  position: relative;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  text-shadow: 0 0 0.8vh rgba($neon-green, 0.6);
  @include neon-glow($neon-green);

  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(90deg, transparent 0%, rgba($neon-green, 0.3) 50%, transparent 100%);
    opacity: 0;
    transform: translateX(-100%);
    transition: all 0.6s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  }
}

.join-btn--available {
  &:hover {
    background: linear-gradient(135deg, rgba($neon-green, 0.3) 0%, rgba($neon-green, 0.15) 50%, rgba($card-bg, 0.9) 100%);
    border-color: $neon-green;
    color: lighten($neon-green, 20%);
    transform: translateY(-0.2vh) scale(1.02);
    @include neon-glow-strong($neon-green);

    &::before {
      opacity: 1;
      transform: translateX(100%);
    }
  }

  &:active {
    transform: translateY(0) scale(0.98);
  }
}

.join-btn--in-game {
  background: linear-gradient(135deg, rgba($synthwave-purple, 0.2) 0%, rgba($synthwave-purple, 0.1) 50%, rgba($card-bg, 0.8) 100%);
  border-color: rgba($synthwave-purple, 0.8);
  color: $synthwave-purple;
  opacity: 0.7;
  cursor: not-allowed;
  text-shadow: 0 0 0.8vh rgba($synthwave-purple, 0.6);
  @include neon-glow($synthwave-purple);

  &:hover {
    transform: none;
  }
}
</style>
