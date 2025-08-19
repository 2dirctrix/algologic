<template>
  <div class="room-list-header">
    <div class="header-left">
      <button class="back-btn" @click="handleBackToHome" title="홈으로 돌아가기">
        <svg class="back-icon" viewBox="0 0 24 24" fill="currentColor">
          <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z" />
        </svg>
      </button>
      <h1 class="room-list-title">Battle Rooms</h1>
    </div>

    <div class="header-actions">
      <CreateRoomButton @create-room="handleCreateRoom" />
      <FilterButton label="Languages" border-color="neon-green" filter-type="language" @filter-change="handleFilterChange" />
      <FilterButton label="Types" border-color="neon-magenta" filter-type="gameType" @filter-change="handleFilterChange" />
      <FilterButton label="Filter" border-color="neon-magenta" filter-type="search" @filter-change="handleFilterChange" />
      <button
        class="refresh-btn"
        @click="handleRefresh"
        title="새로고침"
        :class="{ 'has-notification': hasUpdateNotification, 'is-animating': isAnimating }"
      >
        <svg class="refresh-icon" viewBox="0 0 24 24" fill="currentColor">
          <path
            d="M17.65 6.35C16.2 4.9 14.21 4 12 4c-4.42 0-7.99 3.58-7.99 8s3.57 8 7.99 8c3.73 0 6.84-2.55 7.73-6h-2.08c-.82 2.33-3.04 4-5.65 4-3.31 0-6-2.69-6-6s2.69-6 6-6c1.66 0 3.14.69 4.22 1.78L13 11h7V4l-2.35 2.35z"
          />
        </svg>
        <div v-if="hasUpdateNotification" class="notification-dot"></div>
      </button>
      <SearchInput @search="handleSearch" />
    </div>
  </div>
</template>

<script>
import CreateRoomButton from './CreateRoomButton.vue';
import FilterButton from './FilterButton.vue';
import SearchInput from '@/pages/main/components/room-list/components/header/searchInput.vue';

export default {
  name: 'RoomListHeader',
  components: {
    CreateRoomButton,
    FilterButton,
    SearchInput,
  },
  inject: ['resetToHome'],
  props: {
    hasUpdateNotification: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      isAnimating: false,
    };
  },
  watch: {
    hasUpdateNotification(newVal) {
      if (newVal) {
        this.playRefreshSound();
        this.startPulseAnimation();
      }
    },
  },
  methods: {
    playRefreshSound() {
      try {
        const audio = new Audio('/audio/interaction/refresh-sound.wav');
        audio.volume = 0.5;
        audio.play().catch(error => {
          console.warn('Audio play failed:', error);
        });
      } catch (error) {
        console.warn('Audio creation failed:', error);
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
    startPulseAnimation() {
      this.isAnimating = true;
      setTimeout(() => {
        this.isAnimating = false;
      }, 1000);
    },
    handleCreateRoom() {
      this.$emit('create-room');
    },
    handleFilterChange(filterData) {
      this.$emit('filter-change', filterData);
    },
    handleSearch(searchData) {
      this.$emit('search', searchData);
    },
    handleRefresh() {
      this.playClickSound();
      // 새로고침 알림 해제
      if (this.hasUpdateNotification) {
        this.$emit('clear-update-notification');
      }
      this.$emit('refresh');
    },
    handleBackToHome() {
      if (this.resetToHome) {
        this.resetToHome();
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.room-list-header {
  background: linear-gradient(135deg, rgba($card-bg, 0.7) 0%, rgba($card-bg, 0.4) 50%, rgba($primary-bg, 0.7) 100%);
  backdrop-filter: blur(2.5vh);
  border: calc(0.2vh) solid rgba($electric-blue, 0.4);
  border-radius: calc(1.5vh);
  padding: calc(4vh) calc(2vh);
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 0;
  height: calc(10vh);
  box-sizing: border-box;

  // Responsive 2-row layout for smaller viewports
  @media (max-width: 1200px) {
    flex-direction: column;
    height: auto;
    min-height: calc(8vh);
    gap: calc(1vh);
    padding: calc(1.5vh) calc(2vh) calc(2vh);
  }

  // Extra small screens
  @media (max-width: 800px) {
    padding: calc(1vh) calc(1.5vh);
    gap: calc(0.8vh);
  }
}

.header-left {
  display: flex;
  align-items: center;
  gap: calc(1.5vh);
  height: 100%;
  flex-shrink: 0;

  @media (max-width: 1200px) {
    width: 100%;
    justify-content: space-between;
    height: calc(4.5vh);
    gap: calc(1vh);
  }

  @media (max-width: 800px) {
    gap: calc(0.8vh);
  }
}

.room-list-title {
  font-family: $font-accent;
  font-weight: $font-weight-bold;
  font-size: calc(3vh);
  color: $electric-blue;
  margin: 0;

  @media (max-width: 1200px) {
    font-size: calc(2.5vh);
  }

  @media (max-width: 800px) {
    font-size: calc(2.2vh);
  }
}

.header-actions {
  display: flex;
  align-items: center;
  gap: calc(1.2vh);
  height: 100%;
  flex-shrink: 0;

  @media (max-width: 1200px) {
    width: 100%;
    justify-content: space-between;
    height: calc(4.5vh);
    gap: calc(0.8vh);
  }

  @media (max-width: 800px) {
    gap: calc(0.6vh);
    flex-wrap: wrap;
    height: auto;
    min-height: calc(4.5vh);
  }
}

.refresh-btn {
  background: linear-gradient(135deg, rgba($neon-green, 0.1) 0%, rgba($card-bg, 0.8) 50%, rgba($primary-bg, 0.9) 100%);
  border: calc(0.3vh) solid rgba($neon-green, 0.6);
  color: $neon-green;
  border-radius: $radius-nested;
  padding: calc(1vh);
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  height: calc(4.5vh);
  width: calc(4.5vh);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  flex-shrink: 0;
  overflow: visible;
  text-shadow: 0 0 0.5vh rgba($neon-green, 0.4);

  @include neon-glow($neon-green);

  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: radial-gradient(circle, rgba($neon-green, 0.2) 0%, transparent 70%);
    opacity: 0;
    transform: scale(0.8);
    transition: all 0.4s ease;
  }

  @media (max-width: 800px) {
    height: calc(4vh);
    width: calc(4vh);
    padding: calc(0.8vh);
  }

  &:hover {
    background: linear-gradient(135deg, rgba($neon-green, 0.2) 0%, rgba($card-bg, 0.9) 50%, rgba($primary-bg, 0.95) 100%);
    transform: translateY(-0.2vh) scale(1.05);

    &::before {
      opacity: 1;
      transform: scale(1.2);
    }
  }

  &:active {
    transform: translateY(0) scale(0.95);
  }

  .refresh-icon {
    width: calc(2vh);
    height: calc(2vh);
    transition: transform 0.3s ease;
  }

  &:hover .refresh-icon {
    transform: rotate(180deg);
  }

  &.has-notification {
    border-color: $cyberpunk-pink;
    color: $cyberpunk-pink;

    @include neon-glow($cyberpunk-pink);
  }

  &.is-animating {
    animation: refresh-pulse 1s ease-out;

    .refresh-icon {
      animation: refresh-spin 1s ease-out;
    }
  }
}

@keyframes refresh-pulse {
  0% {
    transform: scale(1);
  }
  15% {
    transform: scale(1.5);
  }
  30% {
    transform: scale(1.2);
  }
  45% {
    transform: scale(1.6);
  }
  60% {
    transform: scale(1.25);
  }
  75% {
    transform: scale(1.15);
  }
  100% {
    transform: scale(1);
  }
}

@keyframes refresh-spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.notification-dot {
  position: absolute;
  top: calc(-0.5vh);
  right: calc(-0.5vh);
  width: calc(1.8vh);
  height: calc(1.8vh);
  background: $cyberpunk-pink;
  border-radius: 50%;
  border: calc(0.3vh) solid $primary-bg;
  box-shadow:
    0 0 8px $cyberpunk-pink,
    0 0 16px rgba($cyberpunk-pink, 0.6);
  animation: pulse-notification 2s infinite;
}

@keyframes pulse-notification {
  0%,
  100% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: 0.7;
    transform: scale(1.1);
  }
}

.back-btn {
  background: linear-gradient(135deg, rgba($electric-blue, 0.1) 0%, rgba($card-bg, 0.8) 50%, rgba($primary-bg, 0.9) 100%);
  border: calc(0.3vh) solid rgba($electric-blue, 0.6);
  color: $electric-blue;
  border-radius: $radius-nested;
  padding: calc(1vh);
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  height: calc(4.5vh);
  width: calc(4.5vh);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  position: relative;
  overflow: hidden;
  text-shadow: 0 0 0.5vh rgba($electric-blue, 0.4);

  @include neon-glow($electric-blue);

  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: radial-gradient(circle, rgba($electric-blue, 0.2) 0%, transparent 70%);
    opacity: 0;
    transform: scale(0.8);
    transition: all 0.4s ease;
  }

  @media (max-width: 800px) {
    height: calc(4vh);
    width: calc(4vh);
    padding: calc(0.8vh);
  }

  &:hover {
    background: linear-gradient(135deg, rgba($cyberpunk-pink, 0.15) 0%, rgba($card-bg, 0.9) 50%, rgba($primary-bg, 0.95) 100%);
    color: $cyberpunk-pink;
    transform: translateY(-0.2vh) scale(1.05);

    &::before {
      opacity: 1;
      transform: scale(1.2);
      background: radial-gradient(circle, rgba($cyberpunk-pink, 0.2) 0%, transparent 70%);
    }
  }

  .back-icon {
    width: calc(2.5vh);
    height: calc(2.5vh);
    transition: all $transition-base;

    @media (max-width: 800px) {
      width: calc(2.2vh);
      height: calc(2.2vh);
    }
  }
}
</style>
