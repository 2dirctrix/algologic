<template>
  <div class="mypage-content">
    <Transition name="header" appear>
      <MyPageHeader v-if="!isLeavingPage" :activeTab="headerActiveTab" @tab-change="handleTabChange" />
    </Transition>

    <div class="mypage-body">
      <div v-if="activeTab === 'info'" class="info-section">
        <Transition name="slide-left" appear>
          <div v-if="!isTransitioning" class="left-section">
            <MyPageProfile />
          </div>
        </Transition>
        <Transition name="slide-right" appear>
          <div v-if="!isTransitioning" class="right-section">
            <MyPageStats />
          </div>
        </Transition>
      </div>
      <div v-else class="history-section">
        <Transition name="fade" appear>
          <GameHistory ref="gameHistoryRef" />
        </Transition>
      </div>
    </div>
  </div>
</template>

<script setup>
import MyPageHeader from './mypage/MyPageHeader.vue';
import MyPageProfile from './mypage/MyPageProfile.vue';
import MyPageStats from './mypage/MyPageStats.vue';
import GameHistory from './mypage/GameHistory.vue';
import { onMounted, ref } from 'vue';
import { useAuth } from '@/composables/useAuth';

const { fetchUserDetail } = useAuth();

const activeTab = ref('info');
const headerActiveTab = ref('info'); // 헤더용 별도 상태
const isLeavingPage = ref(false);
const isTransitioning = ref(false);
const gameHistoryRef = ref(null);

// 탭 변경 처리 (애니메이션 먼저 실행)
const handleTabChange = newTab => {
  if (newTab === activeTab.value || isLeavingPage.value || isTransitioning.value) return;

  // 헤더 인디케이터는 즉시 변경
  headerActiveTab.value = newTab;
  isTransitioning.value = true;

  // 현재가 history 탭이고 다른 탭으로 변경하는 경우 퇴장 애니메이션 실행
  if (activeTab.value === 'history' && gameHistoryRef.value) {
    gameHistoryRef.value.triggerLeaveAnimation();
  }

  // 애니메이션 시간 후 실제 탭 변경
  setTimeout(() => {
    activeTab.value = newTab;
    isTransitioning.value = false;
  }, 600); // 애니메이션 시간과 맞춤
};

onMounted(async () => {
  await fetchUserDetail();
});
</script>

<style lang="scss" scoped>
.mypage-content {
  flex: 1;
  width: 95%;
  height: 100vh;
  margin: 0 auto;
  padding: calc(14vh) calc(4vh) calc(2vh);
  box-sizing: border-box;
  position: relative;
  z-index: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  gap: calc(2vh);

  .mypage-body {
    height: 100%;
    min-height: 0;
    width: 100%;

    .info-section {
      display: flex;
      gap: calc(3vh);
      height: 100%;
      min-height: 0;
      width: 100%;

      .left-section {
        flex: 0 0 calc(60vh);
        min-height: 0;
        overflow: hidden;
        width: calc(50vh);
      }

      .right-section {
        flex: 1;
        min-height: 0;
        min-width: 0;
        overflow: hidden;
      }
    }

    .history-section {
      height: 100%;
      width: 100%;
    }
  }
}

// 헤더 애니메이션 (위에서 등장, 위로 퇴장)
.header-enter-active,
.header-leave-active {
  transition: all 0.6s cubic-bezier(0.4, 0, 0.2, 1);
}

.header-enter-from {
  transform: translateY(-100%);
}

.header-leave-to {
  transform: translateY(-100%);
}

// 왼쪽 섹션 애니메이션 (왼쪽에서 등장, 왼쪽으로 퇴장)
.slide-left-enter-active,
.slide-left-leave-active {
  transition: all 0.7s cubic-bezier(0.4, 0, 0.2, 1);
}

.slide-left-enter-from {
  transform: translateX(-100%);
}

.slide-left-leave-to {
  transform: translateX(-100%);
}

// 오른쪽 섹션 애니메이션 (오른쪽에서 등장, 오른쪽으로 퇴장)
.slide-right-enter-active,
.slide-right-leave-active {
  transition: all 0.6s cubic-bezier(0.4, 0, 0.2, 1);
}

.slide-right-enter-from {
  transform: translateX(80%);
}

.slide-right-leave-to {
  transform: translateX(100%);
}

// 페이드 애니메이션 (대전기록용)
.fade-enter-active,
.fade-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.fade-enter-from {
  opacity: 0;
  transform: scale(0.95);
}

.fade-leave-to {
  opacity: 0;
  transform: scale(0.95);
}
</style>
