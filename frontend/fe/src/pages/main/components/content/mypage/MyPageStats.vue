<template>
  <div class="mypage-stats">
    <!-- 토글 헤더 -->
    <div class="stats-header">
      <div class="toggle-section" :class="{ 'stats-active': activeTab === 'stats' }">
        <button class="toggle-btn" :class="{ active: activeTab === 'tier' }" @click="activeTab = 'tier'">티어</button>
        <button class="toggle-btn" :class="{ active: activeTab === 'stats' }" @click="activeTab = 'stats'">통계</button>
      </div>
    </div>

    <!-- 바디 -->
    <div class="stats-body">
      <Transition name="zoom" mode="out-in">
        <div v-if="activeTab === 'tier'" key="tier" class="tab-content">
          <TierInfo />
        </div>
        <div v-else key="stats" class="tab-content">
          <StatsInfo />
        </div>
      </Transition>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import TierInfo from './TierInfo.vue';
import StatsInfo from './StatsInfo.vue';

// 활성 탭 상태
const activeTab = ref('tier');
</script>

<style lang="scss" scoped>
.mypage-stats {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, rgba($card-bg, 0.7) 0%, rgba($card-bg, 0.4) 50%, rgba($primary-bg, 0.7) 100%);
  backdrop-filter: blur(calc(2.5vh));
  border: calc(0.2vh) solid rgba($electric-blue, 0.4);
  border-radius: $radius-medium;
  overflow: hidden;

  .stats-header {
    flex: 0 0 auto;
    padding: calc(1vh);

    .toggle-section {
      display: flex;
      width: 100%;
      gap: 0;
      background: rgba($primary-bg, 0.6);
      border: calc(0.1vh) solid rgba($electric-blue, 0.2);
      border-radius: calc(1vh);
      overflow: hidden;
      position: relative;
      height: calc(5vh);

      // 슬라이딩 인디케이터
      &::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 0;
        width: 50%;
        height: calc(0.3vh);
        background: linear-gradient(90deg, $electric-blue, $neon-green);
        box-shadow: 0 0 calc(0.5vh) rgba($electric-blue, 0.4);
        transition: transform 0.4s cubic-bezier(0.4, 0, 0.2, 1);
        transform: translateX(0);
      }

      // 통계 탭이 활성화될 때 인디케이터를 오른쪽으로 이동
      &.stats-active::after {
        transform: translateX(100%);
      }

      .toggle-btn {
        flex: 1;
        padding: calc(1.5vh) calc(2vh);
        background: transparent;
        color: rgba($text-primary, 0.6);
        border: none;
        font-family: $font-primary;
        font-size: calc(1.8vh);
        font-weight: $font-weight-semibold;
        cursor: pointer;
        transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
        position: relative;
        overflow: hidden;

        &:hover {
          color: $text-primary;
          
          &::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba($electric-blue, 0.1), transparent);
            animation: shimmer 0.6s ease-out;
          }
        }

        &.active {
          color: $electric-blue;
          background: rgba($electric-blue, 0.1);
          text-shadow: 0 0 calc(0.3vh) rgba($electric-blue, 0.4);
        }
      }
    }
  }

  .stats-body {
    flex: 1;
    overflow: hidden;
    display: flex;
    flex-direction: column;

    .tab-content {
      height: 100%;
      display: flex;
      flex-direction: column;
    }
  }
}

// 줌 애니메이션 (퇴장은 줌아웃, 등장은 줌인)
.zoom-enter-active,
.zoom-leave-active {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.zoom-enter-from {
  opacity: 0;
  transform: scale(0.8);
}

.zoom-leave-to {
  opacity: 0;
  transform: scale(0.8);
}

// 키프레임 애니메이션
@keyframes shimmer {
  0% { left: -100%; }
  100% { left: 100%; }
}
</style>
