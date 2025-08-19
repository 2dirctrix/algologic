<template>
  <main class="main-content">
    <div class="content-grid">
      <!-- 좌측 컬럼 -->
      <div class="left-column">
        <!-- Live Competition -->
        <Transition name="slide-from-top" appear>
          <section v-if="!isExiting" class="hero-section">
            <LiveBanner @join-live="handleJoinLive" />
          </section>
        </Transition>

        <!-- Action Section -->
        <Transition name="slide-from-left" appear>
          <section v-if="!isExiting" class="action-section">
            <ActionButtons @create-battle="$emit('create-battle')" @explore-rooms="handleExploreRooms" />
          </section>
        </Transition>

        <!-- Chat Section -->
        <Transition name="slide-from-bottom" appear>
          <section v-if="!isExiting" class="chat-section">
            <ChatSystem chatType="global" />
          </section>
        </Transition>
      </div>

      <!-- 우측 컬럼 -->
      <div class="right-column">
        <!-- Statistics Section -->
        <Transition name="slide-from-right" appear>
          <section v-if="!isExiting" class="statistics-section">
            <StatisticsSection />
          </section>
        </Transition>

        <!-- Top Players Section -->
        <Transition name="slide-from-right" appear>
          <section v-if="!isExiting" class="players-section">
            <TopPlayersSection />
          </section>
        </Transition>
      </div>
    </div>
  </main>
</template>

<script setup>
import { ref } from 'vue';
import LiveBanner from '../hero/LiveBanner.vue';
import StatisticsSection from '../rankings/most/StatisticsSection.vue';
import TopPlayersSection from '../rankings/players/TopPlayersSection.vue';
import ActionButtons from '../actions/ActionButtons.vue';
import ChatSystem from '@/pages/common/chat/ChatSystem.vue';

const emit = defineEmits(['create-battle', 'explore-rooms']);

const isExiting = ref(false);

const handleExploreRooms = () => {
  if (isExiting.value) return;
  isExiting.value = true; // 모든 섹션 v-if false → leave 애니메이션 시작

  // leave 애니메이션이 0.4s → 여유로 0.5s 후 화면 전환
  setTimeout(() => {
    emit('explore-rooms');
  }, 500);
};

const handleJoinLive = () => {
  handleExploreRooms();
};
</script>

<style lang="scss" scoped>
.main-content {
  flex: 1;
  width: 80%;
  height: 100vh;
  margin: 0 auto;
  padding: calc(14vh) calc(4vh) calc(2vh);
  box-sizing: border-box;
  position: relative;
  z-index: 1;
  overflow: hidden;
}

.content-grid {
  display: grid;
  grid-template-columns: 1fr calc(38vh);
  gap: calc(2vh);
  height: 100%;
  width: 100%;
  align-items: stretch;
}

.left-column {
  display: flex;
  flex-direction: column;
  gap: calc(1vh);
  height: 100%;
  min-height: 0;
}

.right-column {
  display: flex;
  flex-direction: column;
  gap: calc(2vh);
  height: 100%;
  min-height: 0;
}

.hero-section {
  flex: 0 0 40%;
  min-height: 0;
  width: 100%;
}

.action-section {
  flex: 0 0 15%;
  min-height: 0;
  width: 100%;
  display: flex;
  flex-direction: column;
}

.chat-section {
  flex: 1;
  min-height: 0;
  width: 100%;
}

.statistics-section {
  flex: 0 0 45%;
  min-height: 0;
  width: 100%;
  height: 100%;
}

.players-section {
  flex: 1;
  min-height: 0;
  width: 100%;
  height: 100%;
}

// 위에서 슬라이드 애니메이션 (날렵)
.slide-from-top-enter-active,
.slide-from-top-leave-active {
  transition: all 0.7s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.slide-from-top-enter-from,
.slide-from-top-leave-to {
  opacity: 0;
  transform: translateY(-50px);
}

.slide-from-top-enter-to,
.slide-from-top-leave-from {
  opacity: 1;
  transform: translateY(0);
}

// 좌측에서 슬라이드 애니메이션 (날렵)
.slide-from-left-enter-active,
.slide-from-left-leave-active {
  transition: all 0.9s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.slide-from-left-enter-from,
.slide-from-left-leave-to {
  opacity: 0;
  transform: translateX(-100px);
}

.slide-from-left-enter-to,
.slide-from-left-leave-from {
  opacity: 1;
  transform: translateX(0);
}

// 우측에서 슬라이드 애니메이션 (날렵)
.slide-from-right-enter-active,
.slide-from-right-leave-active {
  transition: all 0.9s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.slide-from-right-enter-from,
.slide-from-right-leave-to {
  opacity: 0;
  transform: translateX(90px);
}

.slide-from-right-enter-to,
.slide-from-right-leave-from {
  opacity: 1;
  transform: translateX(0);
}

// 아래에서 슬라이드 애니메이션 (날렵)
.slide-from-bottom-enter-active,
.slide-from-bottom-leave-active {
  transition: all 1.2s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.slide-from-bottom-enter-from,
.slide-from-bottom-leave-to {
  opacity: 0;
  transform: translateY(40px);
}

.slide-from-bottom-enter-to,
.slide-from-bottom-leave-from {
  opacity: 1;
  transform: translateY(0);
}
</style>
