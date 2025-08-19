<template>
  <div class="final-rankings">
    <div class="rankings-list">
      <div
        v-for="(player, index) in sortedPlayers"
        :key="player.playerResultId"
        class="player-ranking-card"
        :class="{
          'first-place': player.rank === 1,
          'slide-up': showAnimation,
        }"
        :style="{ animationDelay: `${index * 0.15}s` }"
      >
        <div class="card-content">
          <!-- 순위 -->
          <div class="rank-number">{{ player.rank }}</div>

          <!-- 티어 아이콘 -->
          <div class="player-profile">
            <div class="tier-icon">
              <img :src="getTierIcon(player.score)" :alt="getTierName(player.score)" />
            </div>
          </div>

          <!-- 플레이어 정보 -->
          <div class="player-info">
            <div class="player-name">{{ player.nickname }}</div>
            <div class="player-tier">{{ getTierName(player.score) }} {{ player.score }}점</div>
          </div>

          <!-- 평가 지표 -->
          <div class="performance-metrics">
            <div class="metric-item submission-time">
              <span class="metric-label">제출</span>
              <span class="metric-value">{{ player.solveDuration || '-' }}<span class="metric-unit">초</span></span>
            </div>
            <div class="metric-item execution-speed">
              <span class="metric-label">속도</span>
              <span class="metric-value">{{ player.runningTime || '-' }}<span class="metric-unit">ms</span></span>
            </div>
            <div class="metric-item memory-usage">
              <span class="metric-label">메모리</span>
              <span class="metric-value">{{ formatMemoryWithComma(player.memory) }}<span class="metric-unit">KB</span></span>
            </div>
          </div>

          <!-- 보상 정보 -->
          <div class="rewards">
            <div class="reward-item coin-reward">
              <span class="reward-value">+{{ formatCoinWithComma(player.earnedCoin) }}</span>
              <span class="reward-label">COIN</span>
            </div>
            <div class="reward-item xp-reward">
              <span class="reward-value">{{ player.earnedScore }}</span>
              <span class="reward-label">XP</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { getScoreToTier } from '@/constants/tiers.js';

const props = defineProps({
  players: {
    type: Array,
    required: true,
  },
  showAnimation: {
    type: Boolean,
    default: false,
  },
});

const sortedPlayers = computed(() => {
  return [...props.players].sort((a, b) => a.rank - b.rank);
});

const getTierName = score => {
  const tierInfo = getScoreToTier(score);
  if (tierInfo.division) {
    return `${tierInfo.name} ${tierInfo.division}`;
  }
  return tierInfo.name;
};

const getTierIcon = score => {
  const tierInfo = getScoreToTier(score);
  const tierName = tierInfo.name;

  // 티어 이름을 파일명에 맞게 변환
  const tierFileMap = {
    BRONZE: 'Bronze',
    SILVER: 'Silver',
    GOLD: 'Gold',
    PLATINUM: 'Platinum',
    DIAMOND: 'Diamond',
    MASTER: 'Master',
    GRANDMASTER: 'Grandmaster',
    CHALLENGER: 'Challenger',
    UNRANKED: 'Bronze', // 기본값
  };

  const fileName = tierFileMap[tierName] || 'Bronze';
  return new URL(`../../../../../assets/tier-icons/${fileName}.png`, import.meta.url).href;
};

const formatMemoryWithComma = memory => {
  if (memory === null || memory === undefined) return '-';
  return Number(memory).toLocaleString();
};

const formatCoinWithComma = coin => {
  if (coin === null || coin === undefined) return '0';
  return Number(coin).toLocaleString();
};
</script>

<style lang="scss" scoped>
@use 'sass:color';

.final-rankings {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 3vh;
  min-height: 0; // 플렉스 자식이 overflow 되도록 허용
  flex: 1; // 가능한 공간 최대 활용
}

.section-title {
  font-family: $font-accent;
  font-size: 3.2vh;
  font-weight: 700;
  color: $text-gold;
  margin: 0;
  text-shadow: 0 0 1vh rgba($text-gold, 0.5);
}

.rankings-list {
  display: flex;
  flex-direction: column;
  gap: 1.5vh;
  max-height: 65vh; // 최대 높이 제한으로 스크롤 유도
  overflow-y: auto; // 세로 스크롤 활성화
  padding-right: 1vw; // 스크롤바 공간 확보
  padding-left: 1vw;
  padding-top: 1vw;

  // 커스텀 스크롤바 스타일
  &::-webkit-scrollbar {
    width: 0.8vw;
  }

  &::-webkit-scrollbar-track {
    background: rgba($primary-bg, 0.2);
    border-radius: 1vw;
    border: 0.1vh solid rgba($text-primary, 0.1);
  }

  &::-webkit-scrollbar-thumb {
    background: linear-gradient(135deg, $electric-blue, $cyberpunk-pink);
    border-radius: 1vw;
    border: 0.1vh solid rgba($text-primary, 0.2);

    &:hover {
      background: linear-gradient(135deg, color.adjust($cyberpunk-pink, $lightness: 10%), color.adjust($cyberpunk-pink, $lightness: 10%));
    }
  }

  // Firefox 스크롤바 스타일
  scrollbar-width: thin;
  scrollbar-color: $electric-blue rgba($primary-bg, 0.2);
}

.player-ranking-card {
  width: 100%;
  height: 12vh;
  position: relative;
  cursor: default;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  overflow: hidden;
  box-sizing: border-box;
  margin-bottom: calc(0.5vh);
  opacity: 0;
  transform: translateY(4vh);

  &.slide-up {
    animation: slideUp 0.6s cubic-bezier(0.25, 0.46, 0.45, 0.94) forwards;
  }

  &:last-child {
    margin-bottom: 0;
  }

  // 배경 레이어
  &::before {
    content: '';
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
    z-index: 0;
  }

  // 패턴 오버레이
  &::after {
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
    z-index: 1;
  }

  // 콘텐츠 레이어
  .card-content {
    position: relative;
    z-index: 2;
    display: flex;
    align-items: center;
    padding: 0 2vw;
    gap: 2vw;
    height: 100%;
    box-sizing: border-box;
  }

  &:hover {
    transform: translateY(-0.2vh) scale(1.01);

    &::before {
      background: linear-gradient(135deg, rgba($card-bg, 0.95) 0%, rgba($card-bg, 0.8) 50%, rgba($primary-bg, 0.9) 100%);
      border-color: rgba($electric-blue, 0.8);
      @include neon-glow-strong($electric-blue);
    }

    &::after {
      background: repeating-linear-gradient(
        45deg,
        transparent,
        transparent 2px,
        rgba($electric-blue, 0.08) 2px,
        rgba($electric-blue, 0.08) 4px
      );
    }
  }

  &.first-place {
    &::before {
      border-color: $text-gold;
      @include neon-glow($text-gold);
    }

    &::after {
      background: repeating-linear-gradient(45deg, transparent, transparent 2px, rgba($text-gold, 0.03) 2px, rgba($text-gold, 0.03) 4px);
    }

    &:hover::before {
      border-color: color.adjust($text-gold, $lightness: 10%);
      @include neon-glow-strong($text-gold);
    }

    &:hover::after {
      background: repeating-linear-gradient(45deg, transparent, transparent 2px, rgba($text-gold, 0.08) 2px, rgba($text-gold, 0.08) 4px);
    }
  }
}

.rank-number {
  font-family: $font-accent;
  font-size: 4.8vh;
  font-style: italic;
  font-weight: 700;
  color: $electric-blue;
  min-width: 3vw;
  text-align: center;
  text-shadow: 0 0 1vh rgba($text-gold, 0.5);
}

.player-profile {
  display: flex;
  align-items: center;
}

.tier-icon {
  width: 7vh;
  height: 7vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0.1vh;
  transition: all $transition-base;

  img {
    width: 100%;
    height: 100%;
    object-fit: contain;
    filter: drop-shadow(0 0 0.7vh rgba($electric-blue, 0.6));
  }

  &:hover {
    transform: scale(1.1);
    img {
      filter: drop-shadow(0 0 1.2vh rgba($electric-blue, 0.8));
    }
  }
}

.player-info {
  display: flex;
  flex-direction: column;
  gap: 0.5vh;
  min-width: 15vw;
}

.player-details {
  display: flex;
  align-items: center;
  gap: 1vw;
}

.player-name {
  font-family: $font-primary;
  font-size: 2.5vh;
  font-weight: $font-weight-semibold;
  color: $text-primary;
}

.player-tier {
  font-family: $font-primary;
  font-size: 1.6vh;
  font-weight: 400;
  color: rgba($text-primary, 1);
}

.score-info {
  font-family: $font-primary;
  font-size: 1.5vh;
  font-weight: 400;
  color: $electric-blue;
}

.performance-metrics {
  display: flex;
  gap: 0.8vw;
  margin: 0 auto;
  flex: 1;
  justify-content: center;
}

.rewards {
  display: flex;
  gap: 1vw;
  margin-left: auto;
}

.metric-item {
  position: relative;
  display: flex;
  flex-direction: row;
  align-items: baseline;
  gap: 1.2vh;
  padding: 0.8vh 1.2vw;
  background: linear-gradient(135deg, rgba($card-bg, 0.4) 0%, rgba($card-bg, 0.2) 100%);
  border: 0.1vh solid rgba($text-primary, 0.15);
  border-radius: $radius-small;
  backdrop-filter: blur(1vh);
  transition: all $transition-base;
  overflow: hidden;
  min-width: 8vw;

  // 배경 그라데이션 애니메이션
  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent 0%, rgba($electric-blue, 0.1) 50%, transparent 100%);
    transition: left 0.8s ease;
  }

  &:hover {
    transform: translateY(-0.2vh) scale(1.02);

    &::before {
      left: 100%;
    }
  }

  // 각 지표별 색상 테마
  &.submission-time {
    border-color: rgba($neon-green, 0.3);
    background: linear-gradient(135deg, rgba($neon-green, 0.08) 0%, rgba($card-bg, 0.2) 100%);

    &:hover {
      border-color: rgba($neon-green, 0.6);
      box-shadow: 0 0 1vh rgba($neon-green, 0.2);
    }

    .metric-label {
      color: $neon-green;
    }
  }

  &.execution-speed {
    border-color: rgba($electric-blue, 0.3);
    background: linear-gradient(135deg, rgba($electric-blue, 0.08) 0%, rgba($card-bg, 0.2) 100%);

    &:hover {
      border-color: rgba($electric-blue, 0.6);
      box-shadow: 0 0 1vh rgba($electric-blue, 0.2);
    }

    .metric-label {
      color: $electric-blue;
    }
  }

  &.memory-usage {
    border-color: rgba($cyberpunk-pink, 0.3);
    background: linear-gradient(135deg, rgba($cyberpunk-pink, 0.08) 0%, rgba($card-bg, 0.2) 100%);

    &:hover {
      border-color: rgba($cyberpunk-pink, 0.6);
      box-shadow: 0 0 1vh rgba($cyberpunk-pink, 0.2);
    }

    .metric-label {
      color: $cyberpunk-pink;
    }
  }

  .metric-label {
    font-family: $font-primary;
    font-size: 1.6vh;
    font-weight: $font-weight-semibold;
    text-transform: uppercase;
    letter-spacing: 0.05em;
    opacity: 0.8;
  }

  .metric-value {
    font-family: $font-accent;
    font-size: 2vh;
    font-weight: 700;
    font-style: italic;
    color: $text-primary;
    white-space: nowrap;
    display: flex;
    align-items: baseline;
    gap: 0.2vw;

    .metric-unit {
      font-family: $font-primary;
      font-size: 1.6vh;
      font-weight: $font-weight-medium;
      opacity: 0.6;
      margin-left: 0.1vw;
    }
  }
}

.reward-item {
  position: relative;
  display: flex;
  align-items: baseline;
  gap: 0.3vw;
  padding: 0.8vh 0.2vw;
  transition: all $transition-base;
  overflow: hidden;
  min-width: 4vw;

  &.coin-reward {
    .reward-label {
      color: $cyberpunk-yellow;
    }

    .reward-value {
      color: $cyberpunk-yellow;
    }
  }

  &.xp-reward {
    .reward-label {
      color: $neon-green;
    }

    .reward-value {
      color: $neon-green;
    }
  }

  .reward-label {
    font-family: $font-accent;
    font-size: 1.6vh;
    font-style: italic;
    font-weight: $font-weight-semibold;
    text-transform: uppercase;
    letter-spacing: 0.05em;
    opacity: 0.8;
  }

  .reward-value {
    font-family: $font-accent;
    font-size: 2.3vh;
    font-weight: 700;
    font-style: italic;
    white-space: nowrap;
  }
}

// 슬라이드 업 애니메이션
@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(4vh);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
