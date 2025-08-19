<template>
  <div
    class="player-slot"
    :class="{
      'player-slot--empty': !player,
      'player-slot--ready': player?.readyStatus === 'ready',
      'player-slot--waiting': player?.readyStatus === 'waiting',
      'player-slot--disconnected': player && player.connected === false,
    }"
  >
    <!-- 플레이어가 있는 경우 -->
    <template v-if="player">
      <!-- 하단 버튼 영역 -->
      <div class="bottom-actions">
        <!-- 강퇴 버튼 (방장이고 자신이 아닐 때만 표시) -->
        <div 
          v-if="showKickButton" 
          class="kick-button"
          @click="handleKickPlayer"
          title="강퇴"
        >
          강퇴
        </div>
        
        <ReadyStatusBadge :status="player.readyStatus" :connected="player.connected" class="ready-badge" />
      </div>
      
      <div class="player-content">
        <div class="player-info">
          <div class="player-name-section">
            <div class="tier-icon" v-if="tierIconSrc">
              <img :src="tierIconSrc" :alt="tierDisplay" @error="handleImageError" />
            </div>
            <h3 class="player-nickname">{{ player.nickname }}</h3>
          </div>
          <div class="player-tier-info">
            <span class="tier-name">{{ tierDisplay }}</span>
            <div class="rating-badge">
              {{ player.rating }}
            </div>
          </div>
        </div>
      </div>
    </template>

    <!-- 빈 슬롯인 경우 -->
    <template v-else>
      <div class="empty-slot">
        <span class="empty-text">참가자를 기다리는 중...</span>
      </div>
    </template>
  </div>
</template>

<script>
import { getScoreToTier } from '@/constants/tiers.js';
import ReadyStatusBadge from './ReadyStatusBadge.vue';

export default {
  name: 'PlayerSlot',
  components: {
    ReadyStatusBadge,
  },
  props: {
    player: {
      type: Object,
      default: null,
    },
    slotIndex: {
      type: Number,
      required: true,
    },
    isOwner: {
      type: Boolean,
      default: false,
    },
    currentUserId: {
      type: Number,
      default: null,
    },
  },
  data() {
    return {
      imageError: false,
    };
  },
  computed: {
    tierDisplay() {
      if (!this.player || !this.player.rating) {
        return 'UNRANKED';
      }

      const tierInfo = getScoreToTier(this.player.rating);

      if (tierInfo.division) {
        return `${tierInfo.name} ${tierInfo.division}`;
      }

      return tierInfo.name;
    },
    tierIconSrc() {
      if (!this.player || !this.player.rating || this.imageError) {
        return null;
      }

      const tierInfo = getScoreToTier(this.player.rating);
      const tierName = tierInfo.name;

      // UNRANKED는 아이콘이 없음
      if (tierName === 'UNRANKED') {
        return null;
      }

      try {
        // 파일명 규칙에 맞게 변환
        const fileName = tierName.charAt(0).toUpperCase() + tierName.slice(1).toLowerCase();
        return new URL(`/src/assets/tier-icons/${fileName}.png`, import.meta.url).href;
      } catch {
        return null;
      }
    },
    // 강퇴 버튼 표시 여부
    showKickButton() {
      return this.isOwner && // 방장이고
             this.player && // 플레이어가 있고
             this.player.id !== this.currentUserId; // 자신이 아닐 때
    },
  },
  methods: {
    handleImageError() {
      this.imageError = true;
    },
    // 강퇴 버튼 클릭 핸들러
    handleKickPlayer() {
      this.$emit('kick-player', this.player.id);
    },
  },
};
</script>

<style lang="scss" scoped>
.player-slot {
  width: 100%;
  height: 100%;
  min-height: calc(12vh);
  border-radius: calc(2vh);
  position: relative;

  // 플레이어가 있는 슬롯
  &:not(.player-slot--empty) {
    background: linear-gradient(
      135deg,
      rgba($electric-blue, 0.1) 0%,
      rgba($card-bg, 0.9) 25%,
      rgba($card-bg, 0.7) 50%,
      rgba($primary-bg, 0.8) 75%,
      rgba($cyberpunk-pink, 0.1) 100%
    );
    background-size: 200% 200%;
    animation: gradientShift 8s ease infinite;
    border: calc(0.2vh) solid rgba($electric-blue, 0.4);
    overflow: hidden;
    transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);

    // 미묘한 패턴 효과
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
      z-index: 1;
      pointer-events: none;
    }

    &:hover {
      border-color: rgba($electric-blue, 0.6);
      transform: translateY(-0.1vh) scale(1.01);
      @include neon-glow($electric-blue);
      animation-duration: 4s;
    }
  }

  // 준비완료 상태
  &.player-slot--ready {
    background: linear-gradient(
      135deg,
      rgba($neon-green, 0.05) 15%,
      rgba($neon-green, 0.15) 25%,
      rgba($neon-green, 0.2) 45%,
      rgba($card-bg, 0.6) 60%
    ) !important;
    background-size: 200% 200% !important;
    animation: gradientShiftReady 4s ease infinite !important;
    border: calc(0.3vh) solid rgba($neon-green, 0.7) !important;
    transform: scale(1.02);
    @include neon-glow-strong($neon-green);
    position: relative;
    overflow: hidden;

    // 준비중과 같은 일렁이는 효과 추가
    &::after {
      content: '';
      position: absolute;
      inset: -50%;
      background:
        radial-gradient(circle at 30% 50%, rgba($neon-green, 0.3) 0%, transparent 40%),
        radial-gradient(circle at 70% 50%, rgba($neon-green, 0.2) 0%, transparent 40%);
      animation: greenPulse 3s ease-in-out infinite alternate;
      pointer-events: none;
      z-index: 0;
    }

    .player-nickname {
      color: lighten($neon-green, 20%) !important;
      text-shadow: 0 0 0.8vh rgba($neon-green, 0.6) !important;
    }

    .rating-badge {
      border-color: $neon-green !important;
      color: $neon-green !important;
      text-shadow: 0 0 4px rgba($neon-green, 0.8) !important;
      box-shadow: 0 0 8px rgba($neon-green, 0.5) !important;
    }
  }

  // 준비중 상태
  &.player-slot--waiting {
    background: linear-gradient(
      135deg,
      rgba($cyberpunk-yellow, 0.1) 0%,
      rgba($cyberpunk-yellow, 0.15) 20%,
      rgba($card-bg, 0.9) 40%,
      rgba($card-bg, 0.8) 60%,
      rgba($cyberpunk-yellow, 0.35) 80%,
      rgba($cyberpunk-yellow, 0.05) 100%
    ) !important;
    background-size: 200% 200% !important;
    animation: gradientShiftWaiting 6s ease infinite !important;
    border: calc(0.3vh) solid rgba($cyberpunk-yellow, 0.6) !important;
    @include neon-glow($cyberpunk-yellow);
    position: relative;
    overflow: hidden;

    // 일렁이는 효과를 위한 추가 레이어
    &::after {
      content: '';
      position: absolute;
      inset: -50%;
      background:
        radial-gradient(circle at 30% 50%, rgba($cyberpunk-yellow, 0.3) 0%, transparent 40%),
        radial-gradient(circle at 70% 50%, rgba($cyberpunk-yellow, 0.2) 0%, transparent 40%);
      animation: yellowPulse 3s ease-in-out infinite alternate;
      pointer-events: none;
      z-index: 0;
    }

    .player-nickname {
      color: lighten($cyberpunk-yellow, 15%) !important;
    }
  }

  // 빈 슬롯
  &.player-slot--empty {
    background: linear-gradient(135deg, rgba($electric-blue, 0.05) 0%, rgba($card-bg, 0.3) 50%, rgba($electric-blue, 0.03) 100%);
    border: calc(0.3vh) dashed rgba($electric-blue, 0.4);
    transition: all 0.3s ease;

    &:hover {
      background: linear-gradient(135deg, rgba($electric-blue, 0.1) 0%, rgba($card-bg, 0.4) 50%, rgba($electric-blue, 0.05) 100%);
      border-color: rgba($electric-blue, 0.6);
      @include neon-glow($electric-blue);
    }
  }

  // 연결유실 상태
  &.player-slot--disconnected {
    border: calc(0.3vh) solid rgba(255, 0, 0, 0.7);
    box-shadow: 0 0 calc(2vh) rgba(255, 0, 0, 0.3);

    // 플레이어 콘텐츠만 블러 처리
    .player-content {
      filter: blur(0.3vh);
      opacity: 0.6;
    }

    &::before {
      background: rgba(0, 0, 0, 0.85);
    }

    // 강퇴 버튼은 블러 효과에서 제외하고 명확하게 표시
    .kick-button {
      filter: none;
      opacity: 1;
      z-index: 10;
      background: linear-gradient(135deg, rgba(255, 0, 0, 1) 0%, rgba(255, 0, 0, 0.9) 50%, rgba(255, 0, 0, 0.95) 100%);
      border-color: rgba(255, 0, 0, 1);
      @include neon-glow-strong(rgba(255, 0, 0, 1));
    }
  }
}

.bottom-actions {
  position: absolute;
  bottom: calc(2vh);
  right: calc(2vh);
  display: flex;
  align-items: center;
  gap: calc(1vh);
  z-index: 3;
}

.kick-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: calc(8vh);
  height: calc(3.5vh);
  border-radius: calc(1vh);
  background: linear-gradient(135deg, rgba(255, 0, 0, 0.9) 0%, rgba(255, 0, 0, 0.7) 50%, rgba(255, 0, 0, 0.8) 100%);
  border: calc(0.2vh) solid rgba(255, 0, 0, 0.9);
  color: $text-primary;
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: calc(1.4vh);
  line-height: 1.3;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  position: relative;
  overflow: hidden;
  text-shadow: 0 0 0.5vh rgba(255, 0, 0, 0.8);
  @include neon-glow-strong(rgba(255, 0, 0, 0.9));
  animation: kickFlash 1.5s ease-in-out infinite;

  &:hover {
    background: linear-gradient(135deg, rgba(255, 0, 0, 1) 0%, rgba(255, 0, 0, 0.8) 50%, rgba(255, 0, 0, 0.9) 100%);
    border-color: rgba(255, 0, 0, 1);
    transform: scale(1.05);
    @include neon-glow-strong(rgba(255, 0, 0, 1));
  }

  &:active {
    transform: scale(0.95);
  }
}

@keyframes kickFlash {
  0%,
  100% {
    opacity: 0.9;
  }
  50% {
    opacity: 1;
  }
}

.player-content {
  position: relative;
  z-index: 2;
  height: 100%;
  padding: calc(2vh) calc(2vh);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.player-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: calc(2vh);
}

.player-name-section {
  display: flex;
  align-items: center;
  gap: calc(1vh);
}

.tier-icon {
  flex-shrink: 0;
  width: calc(4vh);
  height: calc(4vh);
  display: flex;
  align-items: center;
  justify-content: center;

  img {
    width: 100%;
    height: 100%;
    object-fit: contain;
    filter: drop-shadow(0 0 3px rgba(255, 255, 255, 0.3));
  }
}

.player-nickname {
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: calc(2.5vh);
  line-height: 0.8;
  color: $electric-blue;
  margin: 0;
  flex: 1;
}

.ready-badge {
  position: relative;
}

.empty-slot {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
  position: absolute;
  top: 0;
  left: 0;
}

.empty-text {
  font-family: $font-primary;
  font-weight: $font-weight-regular;
  font-size: calc(2vh);
  line-height: 1.3;
  color: rgba(255, 255, 255, 0.4);
  text-align: center;
}

.player-tier-info {
  display: flex;
  align-items: center;
  gap: calc(1vh);
}

.tier-name {
  font-family: $font-accent;
  font-weight: $font-weight-regular;
  font-size: calc(2.3vh);
  line-height: 0.8;
  color: rgba(255, 255, 255, 0.9);
}

.rating-badge {
  background: rgba($cyberpunk-yellow, $opacity-backdrop);
  border: calc(0.15vh) solid $cyberpunk-yellow;
  border-radius: calc(1vh);
  color: $text-primary;
  padding: calc(0.5vh) calc(1vh);
  font-family: $font-primary;
  font-weight: $font-weight-semibold;
  font-size: calc(1.2vh);
  line-height: 1.3;
  min-width: calc(5vh);
  text-align: center;
  box-shadow: 0 0 calc(1vh) rgba(255, 204, 0, 0.3);
}

// 애니메이션 키프레임 정의
@keyframes gradientShift {
  0%,
  100% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
}

@keyframes gradientShiftReady {
  0%,
  100% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
}

@keyframes gradientShiftWaiting {
  0%,
  100% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
}

@keyframes yellowPulse {
  0% {
    transform: scale(1) translateX(0);
    opacity: 0.3;
  }
  50% {
    transform: scale(1.2) translateX(10%);
    opacity: 0.5;
  }
  100% {
    transform: scale(1) translateX(-10%);
    opacity: 0.3;
  }
}

@keyframes greenPulse {
  0% {
    transform: scale(1) translateX(0);
    opacity: 0.3;
  }
  50% {
    transform: scale(1.2) translateX(10%);
    opacity: 0.5;
  }
  100% {
    transform: scale(1) translateX(-10%);
    opacity: 0.3;
  }
}
</style>
