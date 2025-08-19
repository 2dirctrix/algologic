<template>
  <div class="tier-container">
    <!-- 1행 영역 -->
    <div class="tier-info">
      <!-- 티어 아이콘 -->
      <div class="tier-display">
        <div class="tier-icon-wrapper">
          <img :src="tierIcon" :alt="tierDisplayName" class="tier-icon" />
          <span class="tier-name">{{ tierDisplayName }}</span>
        </div>
      </div>

      <!-- 진행률 그래프들 -->
      <div class="progress-section">
        <ProgressCircle :percentage="progressPercentage" :color="progressColor" label="NEXT TIER" />
        <ProgressCircle :percentage="userDetail.winRate || 0" :color="winRateColor" label="WIN RATE" :decimals="1" />
        <ProgressCircle :percentage="userDetail.problemSolvedRate || 0" :color="solvedRateColor" label="SOLVED RATE" :decimals="1" />
      </div>
    </div>

    <!-- 2행 영역: 활동 히트맵 -->
    <div class="activity-section">
      <ActivityHeatmap />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useAuthStore } from '@/stores/auth/auth';
import { getScoreToTier, TIERS } from '@/constants/tiers';
import ProgressCircle from './ProgressCircle.vue';
import ActivityHeatmap from './ActivityHeatmap.vue';

const authStore = useAuthStore();

const userInfo = computed(() => authStore.user || {});
const userDetail = computed(() => authStore.userDetail || {});

// 현재 티어 계산
const currentTier = computed(() => {
  const score = userInfo.value.score || 0;
  return getScoreToTier(score);
});

// 티어 아이콘 경로
const tierIcon = computed(() => {
  const tierName = currentTier.value.name;

  if (tierName === 'UNRANKED') {
    return new URL('/src/assets/images/default-tier.png', import.meta.url).href;
  }

  const fileName = tierName.charAt(0).toUpperCase() + tierName.slice(1).toLowerCase();

  console.log(fileName);

  return new URL(`/src/assets/tier-icons/${fileName}.png`, import.meta.url).href;
});

// 티어 표시명
const tierDisplayName = computed(() => {
  const tierName = currentTier.value.name;
  const division = currentTier.value.division;

  let displayName = tierName.charAt(0) + tierName.slice(1).toLowerCase();

  if (division) {
    return `${displayName} ${division}`;
  }

  return displayName;
});

// 진행률 계산 (현재 티어 내에서의 진행률)
const progressPercentage = computed(() => {
  const currentScore = userInfo.value.score || 0;
  const tierName = currentTier.value.name;
  const division = currentTier.value.division;

  if (tierName === 'CHALLENGER') return 100;
  if (tierName === 'UNRANKED') {
    return Math.min(Math.round((currentScore / 300) * 100), 100);
  }

  const tierIndex = TIERS.findIndex(tier => tier.name === tierName);
  if (tierIndex === -1) return 0;

  const tierData = TIERS[tierIndex];
  let currentMin = 0;
  let currentMax = 0;

  if (tierData.divisions && division) {
    const divIndex = tierData.divisions.findIndex(div => div.division === division);
    if (divIndex !== -1) {
      currentMin = tierData.divisions[divIndex].min;
      currentMax = tierData.divisions[divIndex].max;
    }
  } else {
    currentMin = tierData.min;
    currentMax = tierData.max;
  }

  const progress = ((currentScore - currentMin) / (currentMax + 1 - currentMin)) * 100;
  return Math.min(Math.max(Math.round(progress), 0), 99);
});

// 진행률 색상
const progressColor = computed(() => {
  const percentage = progressPercentage.value;
  if (percentage >= 80) return '#00ff88';
  if (percentage >= 60) return '#00d4ff';
  if (percentage >= 40) return '#ffaa00';
  if (percentage >= 20) return '#ff6b9d';
  return '#ff1744';
});

// 승률 색상
const winRateColor = computed(() => {
  const rate = userDetail.value.winRate || 0;
  if (rate >= 70) return '#00ff88';
  if (rate >= 55) return '#00d4ff';
  if (rate >= 45) return '#ffaa00';
  if (rate >= 30) return '#ff6b9d';
  return '#ff1744';
});

// 정답률 색상
const solvedRateColor = computed(() => {
  const rate = userDetail.value.problemSolvedRate || 0;
  if (rate >= 80) return '#00ff88';
  if (rate >= 65) return '#00d4ff';
  if (rate >= 50) return '#ffaa00';
  if (rate >= 35) return '#ff6b9d';
  return '#ff1744';
});
</script>

<style lang="scss" scoped>
.tier-info {
  display: flex;
  align-items: baseline;
  gap: calc(3vh);
  height: 100%;
  width: 100%;
  padding: calc(4vh) calc(2vh) calc(2vh);

  .tier-display {
    display: flex;
    align-items: center;
    justify-content: center;
    flex: 0 0 auto;

    .tier-icon-wrapper {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: calc(1.5vh);

      .tier-icon {
        width: calc(20vh);
        height: calc(20vh);
        object-fit: contain;
        filter: drop-shadow(0 0 calc(1vh) rgba($electric-blue, 0.3));
        cursor: pointer;
        transition: all 0.9s cubic-bezier(0.4, 0, 0.2, 1);
        position: relative;
        overflow: hidden;

        &:hover {
          transform: scale(1.05);
          filter: drop-shadow(0 0 calc(1.5vh) rgba($electric-blue, 0.5));
        }

        &:hover::before {
          content: '';
          position: absolute;
          top: 0;
          left: -100%;
          width: 100%;
          height: 100%;
          background: linear-gradient(90deg, transparent, rgba($electric-blue, 0.2), transparent);
          animation: shimmer 0.6s ease-out;
          pointer-events: none;
        }
      }

      .tier-name {
        font-family: $font-accent;
        font-size: calc(2.5vh);
        //font-style: italic;
        font-weight: $font-weight-bold;
        color: $electric-blue;
        text-shadow: 0 0 calc(0.3vh) rgba($electric-blue, 0.3);
        text-align: center;
      }
    }
  }

  .progress-section {
    display: flex;
    gap: calc(2vh);
    align-items: center;
    justify-content: space-around;
    flex: 1;
  }
}

.tier-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  padding: 0 1vh;

  .tier-info {
    flex: 0 0 auto;
    height: 50%;
  }

  .activity-section {
    flex: 1;
    padding: 1vh calc(2vh);
    overflow: hidden;
    display: flex;
    flex-direction: column;
  }
}

// 키프레임 애니메이션
@keyframes shimmer {
  0% {
    left: -100%;
  }
  100% {
    left: 100%;
  }
}
</style>
