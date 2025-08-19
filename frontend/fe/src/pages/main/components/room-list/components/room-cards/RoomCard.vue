<template>
  <div class="room-card" :class="{ 'room-card--in-game': roomStatus === 'in-game' }" @click="handleCardClick">
    <div class="room-card__background"></div>
    <div class="room-card__content">
      <!-- 헤더: 게임 타입 태그 + 방 이름 -->
      <div class="room-header">
        <span class="game-type-tag" :class="gameType === 'RANKED' ? 'game-type-tag--rank' : 'game-type-tag--general'">
          {{ gameType === 'RANKED' ? '랭크' : '일반' }}
        </span>
        <h3 class="room-name">{{ roomName }}</h3>
      </div>

      <!-- 중앙 그룹: 언어 + 참가자 수 + 인디케이터 -->
      <div class="center-group">
        <!-- 언어 -->
        <div class="language">{{ language }}</div>

        <!-- 참가자 수 -->
        <div class="player-count">
          <div class="player-number">{{ currentPlayers }}</div>
          <div class="player-label">Players</div>
          <div class="room-owner">{{ roomOwner }}</div>
        </div>

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
  name: 'RoomCard',
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
.room-card {
  position: relative;
  width: 100%;
  height: 100%;
  border-radius: 1vh;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  overflow: hidden;
  box-sizing: border-box;

  &__background {
    position: absolute;
    inset: 0;
    border-radius: 2vh;
    background: linear-gradient(
      185deg,
      rgba($card-bg, 0.65) 0%,
      rgba($card-bg, 0.59) 40%,
      rgba($card-bg, 0.8) 70%,
      rgba($card-bg, 0.65) 100%
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
      border-radius: 1.8vh;
    }
  }

  &__content {
    position: absolute;
    inset: 0;
    z-index: 1;
    display: flex;
    flex-direction: column;
    padding: 1.5vh;
    box-sizing: border-box;
  }

  &:hover:not(&--in-game) {
    transform: translateY(-0.3vh) scale(1.03);

    .room-card__background {
      background: linear-gradient(135deg, rgba($card-bg, 0.95) 0%, rgba($card-bg, 0.8) 50%, rgba($primary-bg, 0.9) 100%);
      border-color: $electric-blue;
      border-width: 0.3vh;
      @include neon-glow-strong($electric-blue);
      box-shadow:
        0vh 0.8vh 3vh 0vh rgba($electric-blue, 0.3),
        0vh 0.6vh 2vh 0vh rgba(0, 0, 0, 0.4),
        inset 0vh 0.1vh 0vh 0vh rgba(255, 255, 255, 0.08);

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
    transform: scale(0.98);

    .room-card__background {
      background: linear-gradient(135deg, rgba($synthwave-purple, 0.3) 0%, rgba($synthwave-purple, 0.2) 50%, rgba($primary-bg, 0.9) 100%);
      border-color: $synthwave-purple;
    }

    .room-card__border-frame {
      border-color: rgba($synthwave-purple, 0.4);

      &::before,
      &::after {
        border-color: $synthwave-purple;
      }
    }
  }
}

.room-header {
  display: flex;
  align-items: center;
  gap: calc(1vh);
  margin-bottom: calc(1.5vh);
}

.game-type-tag {
  padding: calc(0.6vh) calc(1vh);
  border-radius: calc(1vh);
  font-family: $font-primary;
  font-weight: $font-weight-regular;
  font-size: calc(1.2vh);
  color: $text-primary;
  min-width: calc(6vh);
  text-align: center;
  flex-shrink: 0;
  border: calc(0.3vh) solid;
}

.game-type-tag--general {
  background: rgba($electric-blue, 0.4);
  border-color: $electric-blue;
}

.game-type-tag--rank {
  background: rgba($cyberpunk-yellow, 0.4);
  border-color: $cyberpunk-yellow;
}

.room-name {
  font-family: $font-primary;
  font-weight: $font-weight-medium;
  font-size: calc(2.2vh);
  color: $text-primary;
  margin: 0;
  flex: 1;
  line-height: 1.3;
  min-width: 0;

  // 말줄임 처리
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.center-group {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: calc(1vh);
}

.language {
  font-family: $font-accent;
  font-weight: $font-weight-regular;
  font-size: calc(2.8vh);
  color: $text-primary;
  text-align: center;
  line-height: 1;
}

.player-count {
  text-align: center;
}

.player-number {
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: calc(3.2vh);
  color: $text-primary;
  line-height: 1;
}

.player-label {
  font-family: $font-primary;
  font-weight: $font-weight-regular;
  font-size: calc(1vh);
  color: $text-primary;
  opacity: $opacity-text-secondary;
  margin-top: calc(0.3vh);
}

.room-owner {
  font-family: $font-primary;
  font-weight: $font-weight-regular;
  font-size: calc(1.2vh);
  color: $text-primary;
  opacity: $opacity-text-secondary;
  margin-top: calc(0.2vh);
  text-align: center;
}

.player-indicators {
  display: flex;
  justify-content: center;
  gap: calc(0.5vh);
}

.player-dot {
  width: calc(1.8vh);
  height: calc(1.8vh);
  border-radius: 50%;
  border: calc(0.3vh) solid $neon-green;
}

.player-dot.empty {
  background: transparent;
  border-color: rgba($text-primary, 0.3);
}

.join-btn {
  background: linear-gradient(135deg, rgba($neon-green, 0.2) 0%, rgba($neon-green, 0.1) 50%, rgba($card-bg, 0.8) 100%);
  border: calc(0.3vh) solid rgba($neon-green, 0.8);
  border-radius: calc(1.2vh);
  color: $neon-green;
  cursor: pointer;
  padding: calc(1.2vh) calc(1.8vh);
  margin-top: calc(1vh);
  font-family: $font-primary;
  font-size: calc(1.2vh);
  font-weight: $font-weight-semibold;
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
