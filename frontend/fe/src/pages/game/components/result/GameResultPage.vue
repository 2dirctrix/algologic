<template>
  <div class="game-result-page">
    <!-- 로딩 인디케이터 -->
    <div v-if="isLoading" class="loading-container">
      <div class="loading-spinner"></div>
      <p class="loading-text">결과 집계 중...</p>
    </div>

    <!-- 결과 페이지 컨텐츠 -->
    <div v-else class="result-content">
      <ResultHeader
        :game-name="gameResult.gameName"
        :problem-name="gameResult.problemName"
        :problem-level="gameResult.problemLevel"
        :max-players="gameResult.maxPlayers"
        :duration="gameDuration"
        :players="gameResult.playerResults"
        :is-modal="isModal"
        :class="{ 'fade-in': showHeader }"
      />

      <FinalRankings :players="gameResult.playerResults" :show-animation="showPlayers" />

      <ActionButtons v-if="!isModal" @retry="handleRetry" @back-to-main="handleBackToMain" :class="{ 'fade-in': showButtons }" />
    </div>
  </div>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useInGameStore } from '@/pages/game/store/useInGameStore';
import { useAuth } from '@/composables/useAuth';
import ResultHeader from './components/ResultHeader.vue';
import FinalRankings from './components/FinalRankings.vue';
import ActionButtons from './components/ActionButtons.vue';

const props = defineProps({
  gameResultId: {
    type: [String, Number],
    default: null,
  },
  isModal: {
    type: Boolean,
    default: false,
  },
});

const router = useRouter();
const inGameStore = useInGameStore();
const { fetchGameResultDetail } = useAuth();

// 애니메이션 상태 관리
const isLoading = ref(true);
const showHeader = ref(false);
const showPlayers = ref(false);
const showButtons = ref(false);

// 모달용 게임 결과 데이터
const modalGameResult = ref(null);

// 게임 결과 데이터 - 모달인 경우 modalGameResult, 아니면 inGameStore 사용
const gameResult = computed(() => {
  if (props.isModal && modalGameResult.value) {
    return modalGameResult.value;
  }

  return (
    inGameStore.gameResult || {
      gameResultId: null,
      gameName: '',
      maxPlayers: 0,
      problemName: '',
      problemLevel: '',
      startedAt: '',
      finishedAt: '',
      playerResults: [],
    }
  );
});

const gameDuration = computed(() => {
  if (!gameResult.value.startedAt || !gameResult.value.finishedAt) return '0분 0초';

  const start = new Date(gameResult.value.startedAt);
  const end = new Date(gameResult.value.finishedAt);
  const diff = Math.floor((end - start) / 1000);

  const minutes = Math.floor(diff / 60);
  const seconds = diff % 60;

  return `${minutes}분 ${seconds}초`;
});

const handleRetry = () => {
  // 재도전을 위해 메인 페이지의 방 목록으로 이동
  router.push({ path: '/main', query: { view: 'rooms' } });
};

const handleBackToMain = () => {
  router.push('/main');
};

// 모달용 게임 결과 데이터 로드
const loadModalGameResult = async () => {
  if (!props.gameResultId) return;

  try {
    const result = await fetchGameResultDetail(props.gameResultId);
    modalGameResult.value = result;
  } catch (error) {
    console.error('게임 결과 로드 실패:', error);
  }
};

// 애니메이션 시퀀스 실행
const startAnimations = () => {
  // 헤더 페이드인
  setTimeout(() => {
    showHeader.value = true;

    // 플레이어 애니메이션 시작
    setTimeout(() => {
      showPlayers.value = true;

      // 버튼 표시
      setTimeout(() => {
        showButtons.value = true;
      }, 300);
    }, 300);
  }, 100);
};

// 페이지 로드 시 실행
onMounted(async () => {
  if (props.isModal && props.gameResultId) {
    // 모달 모드: 게임 결과 데이터 로드 후 애니메이션
    await loadModalGameResult();
    isLoading.value = false;
    startAnimations();
  } else {
    // 일반 모드: 기존 로직 (2초 후 애니메이션)
    setTimeout(() => {
      isLoading.value = false;
      startAnimations();
    }, 2000);
  }
});
</script>

<style lang="scss" scoped>
.game-result-page {
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
  gap: 2vh;
  padding: 2vh 8vw;
  overflow: hidden;
  position: relative;
}

.loading-container {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2vh;
}

.loading-spinner {
  width: 8vh;
  height: 8vh;
  border: 0.4vh solid rgba($electric-blue, 0.2);
  border-top: 0.4vh solid $electric-blue;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.loading-text {
  font-family: $font-primary;
  font-size: 2.6vh;
  color: $text-primary;
  text-shadow: 0 0 1vh rgba($electric-blue, 0.5);
}

.result-content {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  gap: 2vh;
}

// 페이드인 애니메이션
.fade-in {
  animation: fadeIn 0.6s ease-out forwards;
  opacity: 0;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>
