<template>
  <div class="ban-pick-buy-page">
    <!-- 로딩 인디케이터 -->
    <div v-if="showLoading" class="loading-container">
      <div class="loading-spinner">
        <div class="spinner"></div>
        <p class="loading-text">게임을 준비하고 있습니다...</p>
      </div>
    </div>

    <!-- 메인 컨텐츠 -->
    <template v-else>
      <!-- 헤더 영역 -->
      <transition name="slide-from-top" appear>
        <div v-if="!isExiting" class="ban-pick-buy-page__header">
          <PhaseHeader />
        </div>
      </transition>

      <!-- 메인 컨텐츠 영역 -->
      <div class="ban-pick-buy-page__content">
        <!-- 좌측 패널 -->
        <transition name="slide-from-left" appear>
          <div v-if="!isExiting" class="ban-pick-buy-page__left">
            <LeftPanel />
          </div>
        </transition>

        <!-- 우측 알고리즘/아이템 선택 영역 -->
        <transition name="slide-from-right" appear>
          <div v-if="!isExiting" class="ban-pick-buy-page__right">
            <!-- 구매 단계일 때 아이템/스펠 선택 패널 표시 -->
            <ItemSpellSelectionPanel
              v-if="currentPhase === PHASE_STATES.BUY"
              :remaining-time="remainingTime"
              :current-phase="currentPhase"
              :is-selection-complete="isSelectionComplete"
              @purchase-items-spells="handlePurchaseItemsSpells"
            />
            <!-- 밴/픽 단계일 때 알고리즘 선택 패널 표시 -->
            <AlgorithmSelectionPanel
              v-else
              :remaining-time="remainingTime"
              :current-phase="currentPhase"
              :is-selection-complete="isSelectionComplete"
              :banned-algorithms="bannedAlgorithms"
              :picked-algorithms="pickedAlgorithms"
              @algorithm-select="handleAlgorithmSelect"
            />
          </div>
        </transition>
      </div>
    </template>

    <audio ref="backgroundAudio">
      <source src="/audio/ban-pick-buy-sound.mp3" type="audio/mp3" />
      브라우저가 오디오를 지원하지 않습니다.
    </audio>
  </div>
</template>

<script setup>
import { computed, ref, watch, onMounted, onUnmounted, nextTick } from 'vue';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';
import { useWebSocketStore } from '@/stores/websocket/websocket.js';
import { useGame } from '@/composables/useGame.js';
import { useProblem } from '@/composables/useProblem.js';
import { useItem } from '@/game/services/useItem.js';
import PhaseHeader from '@/pages/game/components/ban-pick-buy/components/header/PhaseHeader.vue';
import LeftPanel from '@/pages/game/components/ban-pick-buy/components/left-panel/LeftPanel.vue';
import AlgorithmSelectionPanel from '@/pages/game/components/ban-pick-buy/components/algorithm-selection/AlgorithmSelectionPanel.vue';
import ItemSpellSelectionPanel from '@/pages/game/components/ban-pick-buy/components/item-spell-section/ItemSpellSelectionPanel.vue';
import { PHASE_STATES } from '../ban-pick-buy/constants/phase-status.js';

const inGameStore = useInGameStore();
const webSocketStore = useWebSocketStore();
const { subscribeInGameEvents } = useGame();
const { fetchAlgorithmCategories, banAlgorithm, pickAlgorithm } = useProblem();
const { fetchItemsAndSpells, purchaseItemsAndSpells } = useItem();

const currentPhase = computed(() => {
  // null인 경우 기본값으로 BAN_CHOICE를 반환하여 타입 에러 방지
  return inGameStore.gameSync.phase || PHASE_STATES.BAN;
});

const remainingTime = computed(() => {
  return inGameStore.gameSync.remainingTime;
});

const roomId = computed(() => inGameStore.currentRoomId);

// 밴된 알고리즘과 픽된 알고리즘 목록 (배열 형태로 전달)
const bannedAlgorithms = computed(() => {
  const bannedId = inGameStore.banPickBuyResults.bannedAlgorithm;
  return bannedId ? [bannedId] : [];
});

const pickedAlgorithms = computed(() => {
  const pickedId = inGameStore.banPickBuyResults.pickedAlgorithm;
  return pickedId ? [pickedId] : [];
});

// 선택 완료 상태 관리 (버튼 비활성화용)
const isSelectionComplete = ref(false);

// 로딩 상태 관리
const showLoading = ref(true);

// 퇴장 애니메이션 상태 관리
const isExiting = ref(false);

// 배경음악 오디오 참조
const backgroundAudio = ref(null);

// 페이즈 변경 시 선택 상태 초기화
watch(currentPhase, (newPhase, oldPhase) => {
  if (newPhase !== oldPhase && newPhase) {
    isSelectionComplete.value = false;
  }
});

// 구매 완료 후 퇴장 애니메이션 시작 신호 감지
watch(
  () => inGameStore.shouldStartExitAnimation,
  shouldStart => {
    if (shouldStart) {
      startExitAnimation();
    }
  },
);

// 퇴장 애니메이션 시작
const startExitAnimation = () => {
  isExiting.value = true;

  // 가장 긴 애니메이션 시간(0.6s) 완료 후 실제 게임 시작
  setTimeout(() => {
    inGameStore.completeBanPickBuyExitAnimation();
  }, 600);
};

// Methods
const handleAlgorithmSelect = async algorithm => {
  if (isSelectionComplete.value) {
    return;
  }

  try {
    let success = false;

    if (currentPhase.value === PHASE_STATES.BAN) {
      success = await banAlgorithm(algorithm.categoryId);
      if (success) {
        inGameStore.setBannedAlgorithm(algorithm.categoryId);
      }
    } else if (currentPhase.value === PHASE_STATES.PICK) {
      success = await pickAlgorithm(algorithm.categoryId);
      if (success) {
        inGameStore.setPickedAlgorithm(algorithm.categoryId);
      }
    }

    if (success) {
      isSelectionComplete.value = true;
    } else {
      console.error(`[BAN-PICK-BUY] ${currentPhase.value} 선택 실패:`, algorithm);
    }
  } catch (error) {
    console.error(`[BAN-PICK-BUY] ${currentPhase.value} 선택 에러:`, error);
  }
};

// 아이템/스펠 구매 처리
const handlePurchaseItemsSpells = async purchaseData => {
  if (isSelectionComplete.value) {
    return;
  }

  try {
    await purchaseItemsAndSpells(purchaseData);

    // 200 OK 응답 시 구매하기 버튼 비활성화
    isSelectionComplete.value = true;
  } catch (error) {
    console.error('[BAN-PICK-BUY] 아이템/스펠 구매 실패:', error);
  }
};

// 웹소켓 연결 및 hydration 완료 시 초기화 실행
watch(
  () => webSocketStore.isConnected && inGameStore.hydrated,
  async ready => {
    if (ready) {
      try {
        // 알고리즘 카테고리 로드
        await fetchAlgorithmCategories();

        // 아이템 및 스펠 리스트 로드
        await fetchItemsAndSpells();

        // 게임 이벤트 구독
        await subscribeInGameEvents(roomId.value);
        // await manualSync(roomId.value);
      } catch (error) {
        console.error('[BAN-PICK-BUY] 게임 동기화 실패:', error);
      }
    }
  },
  { immediate: true },
);

// 컴포넌트 마운트 시 2초 로딩 후 컨텐츠 표시 및 배경음악 시작
onMounted(async () => {
  // 2초 후 로딩 해제 및 배경음악 시작
  setTimeout(async () => {
    showLoading.value = false;

    // 컴포넌트 등장 후 배경음악 시작
    await nextTick();
    if (backgroundAudio.value) {
      backgroundAudio.value.volume = 0.1;
      try {
        await backgroundAudio.value.play();
      } catch (error) {
        console.warn('[BanPickBuyPage] 배경음악 자동재생 실패:', error);
      }
    }
  }, 2000);

  // 전역 BGM 비활성화
  const { useBgmStore } = await import('@/stores/bgm.js');
  const bgmStore = useBgmStore();
  bgmStore.disableGlobalBgm();
});

onUnmounted(async () => {
  inGameStore.stopLocalTimer();

  // 컴포넌트 언마운트 시 전역 BGM 다시 활성화
  const { useBgmStore } = await import('@/stores/bgm.js');
  const bgmStore = useBgmStore();
  bgmStore.enableGlobalBgm();
});
</script>

<style lang="scss" scoped>
.ban-pick-buy-page {
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  padding: calc(2vh) calc(20vh);
  gap: calc(2vh);
  position: relative;

  &__header {
    flex-shrink: 0;
    padding: 1vh 0 0.5vh 0;
  }

  &__content {
    flex: 1;
    display: flex;
    gap: 1.5vh;
    padding: 0.5vh 0 1vh 0;
    min-height: 0;
    max-height: 85vh;
  }

  &__left {
    width: 55vh;
    flex-shrink: 0;
  }

  &__right {
    flex: 1;
    min-width: 0;
  }
}

// 로딩 컨테이너 스타일
.loading-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.loading-spinner {
  text-align: center;
  color: white;
}

.spinner {
  width: 60px;
  height: 60px;
  border: 4px solid rgba(255, 255, 255, 0.3);
  border-top: 4px solid #ffffff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

.loading-text {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  opacity: 0.9;
  font-family: 'Pretendard', sans-serif;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

// 헤더 애니메이션 (위에서 등장 및 퇴장)
.slide-from-top-enter-active,
.slide-from-top-leave-active {
  transition: all 0.4s ease-out;
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

// 좌측 패널 애니메이션 (좌측에서 등장 및 퇴장)
.slide-from-left-enter-active,
.slide-from-left-leave-active {
  transition: all 0.5s ease-out;
}
.slide-from-left-enter-from,
.slide-from-left-leave-to {
  opacity: 0;
  transform: translateX(-80px);
}
.slide-from-left-enter-to,
.slide-from-left-leave-from {
  opacity: 1;
  transform: translateX(0);
}

// 우측 영역 애니메이션 (우측에서 등장 및 퇴장)
.slide-from-right-enter-active,
.slide-from-right-leave-active {
  transition: all 0.6s ease-out;
}
.slide-from-right-enter-from,
.slide-from-right-leave-to {
  opacity: 0;
  transform: translateX(80px);
}
.slide-from-right-enter-to,
.slide-from-right-leave-from {
  opacity: 1;
  transform: translateX(0);
}
</style>
