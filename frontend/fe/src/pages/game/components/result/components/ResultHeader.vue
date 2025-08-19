<template>
  <div class="result-header" :class="{ 'modal-mode': isModal }">
    <div class="header-container">
      <!-- 모달 모드: 간단한 헤더 -->
      <template v-if="isModal">
        <div class="modal-header-content">
          <h3 class="modal-title">{{ problemName }}</h3>
          <div class="modal-details">
            <span class="detail-item">{{ duration }}</span>
            <span class="separator">•</span>
            <span class="detail-item">{{ problemLevel }}</span>
            <span class="separator">•</span>
            <span class="detail-item">{{ maxPlayers }}명</span>
          </div>
        </div>
      </template>

      <!-- 일반 모드: 전체 헤더 -->
      <template v-else>
        <!-- 문제 정보 (메인 헤더) -->
        <h1 class="main-title">{{ problemName }}</h1>

        <!-- 방 이름 (서브 헤더) -->
        <h2 class="room-name">{{ gameName }}</h2>

        <!-- 기본 게임 정보 -->
        <div class="game-details">
          <span class="detail-item">{{ duration }}</span>
          <span class="separator">•</span>
          <span class="detail-item">{{ problemLevel }} 난이도</span>
          <span class="separator">•</span>
          <span class="detail-item">{{ maxPlayers }}명 참가</span>
        </div>

        <!-- 밴/픽 알고리즘 정보 -->
        <div class="algorithm-summary">
          <div v-if="bannedAlgorithms.length > 0" class="algorithm-group">
            <div class="algorithm-items">
              <span v-for="algorithm in bannedAlgorithms" :key="algorithm" class="algorithm-card banned">{{ algorithm }}</span>
            </div>
          </div>
          <div v-if="pickedAlgorithms.length > 0" class="algorithm-group">
            <div class="algorithm-items">
              <span v-for="algorithm in pickedAlgorithms" :key="algorithm" class="algorithm-card picked">{{ algorithm }}</span>
            </div>
          </div>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  gameName: {
    type: String,
    required: true,
  },
  problemName: {
    type: String,
    required: true,
  },
  problemLevel: {
    type: String,
    required: true,
  },
  maxPlayers: {
    type: Number,
    required: true,
  },
  duration: {
    type: String,
    required: true,
  },
  players: {
    type: Array,
    required: true,
  },
  isModal: {
    type: Boolean,
    default: false,
  },
});

const bannedAlgorithms = computed(() => {
  const banned = props.players.map(player => player.bannedProblemCategoryName).filter(algorithm => algorithm);
  return [...new Set(banned)];
});

const pickedAlgorithms = computed(() => {
  const picked = props.players.map(player => player.pickedProblemCategoryName).filter(algorithm => algorithm);
  return [...new Set(picked)];
});
</script>

<style lang="scss" scoped>
.result-header {
  width: 100%;
  padding: 1vh 0;
}

.header-container {
  width: 100%;
  background: linear-gradient(135deg, rgba($card-bg, 0.7) 0%, rgba($card-bg, 0.4) 50%, rgba($primary-bg, 0.7) 100%);
  backdrop-filter: blur(2.5vh);
  border: calc(0.2vh) solid rgba($electric-blue, 0.4);
  border-radius: calc(1.5vh);
  padding: calc(3vh) calc(2vh);
  text-align: center;
  box-sizing: border-box;
}

.main-title {
  font-family: $font-primary;
  font-size: 5vh;
  font-weight: $font-weight-bold;
  color: $text-primary;
  margin: 0 0 0.5vh 0;
}

.room-name {
  font-family: $font-primary;
  font-size: 3vh;
  font-weight: $font-weight-medium;
  color: rgba($text-primary, 0.8);
  margin: 0 0 0.5vh 0;
}

.game-details {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1.5vw;
  margin-bottom: 2vh;
}

.detail-item {
  font-family: $font-primary;
  font-size: 1.6vh;
  font-weight: $font-weight-medium;
  color: rgba($text-primary, 0.8);
}

.separator {
  font-family: $font-primary;
  font-size: 1.6vh;
  color: rgba($text-primary, 0.4);
}

.algorithm-summary {
  display: flex;
  justify-content: center;
  gap: 2vw;
  flex-wrap: wrap;
  margin-top: 2vh;
}

.algorithm-group {
  display: flex;
  align-items: center;
  gap: 1.5vw;
}

.algorithm-label {
  font-family: $font-primary;
  font-size: 1.4vh;
  font-weight: $font-weight-semibold;
  min-width: 3vw;

  &.banned {
    color: $cyberpunk-pink;
  }

  &.picked {
    color: $electric-blue;
  }
}

.algorithm-items {
  display: flex;
  flex-wrap: wrap;
  gap: 1vw;
}

.algorithm-card {
  font-family: $font-primary;
  font-size: 1.4vh;
  font-weight: $font-weight-medium;
  color: $text-primary;
  border-radius: $radius-button;
  padding: 0.8vh 1.4vw;
  cursor: pointer;
  transition: all $transition-base;
  backdrop-filter: blur(1vh);
  position: relative;
  overflow: hidden;

  &.banned {
    background: rgba($cyberpunk-pink, 0.2);
    border: 0.1vh solid $cyberpunk-pink;
    color: $cyberpunk-pink;

    &:hover {
      background: rgba($cyberpunk-pink, 0.3);
      transform: translateY(-0.2vh);
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

  &.picked {
    background: rgba($electric-blue, 0.2);
    border: 0.1vh solid $electric-blue;
    color: $electric-blue;

    &:hover {
      background: rgba($electric-blue, 0.3);
      transform: translateY(-0.2vh);
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
}

// 모달 모드 스타일
.modal-mode {
  padding: 0;

  .header-container {
    padding: calc(1.5vh) calc(2vh);
    border-radius: calc(1vh);
  }
}

.modal-header-content {
  display: flex;
  flex-direction: column;
  gap: calc(0.8vh);
}

.modal-title {
  font-family: $font-primary;
  font-size: calc(2.8vh);
  font-weight: $font-weight-bold;
  color: $text-primary;
  margin: 0;
}

.modal-details {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: calc(1.2vh);
  
  .detail-item {
    font-size: calc(1.4vh);
  }
  
  .separator {
    font-size: calc(1.4vh);
  }
}
</style>
