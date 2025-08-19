<!-- src/pages/game/components/in-game/components/problem-view/ProblemPanel.vue -->
<template>
  <div class="problem-panel">
    <!-- 로딩 상태 -->
    <div v-if="loading" class="loading-state">
      <div class="loading-spinner"></div>
      <p>문제를 불러오는 중...</p>
    </div>

    <!-- 에러 상태 -->
    <div v-else-if="error" class="error-state">
      <div class="error-icon">⚠️</div>
      <p>{{ error }}</p>
      <button @click="retryFetch" class="retry-btn">다시 시도</button>
    </div>

    <!-- 문제 데이터 -->
    <template v-else-if="problem">
      <ProblemHeader
        :title="problem.name"
        :difficulty="problem.level"
        :categories="problem.category"
        :time-limit="problem.timeLimit"
        :memory-limit="problem.memoryLimit"
      />
      <div class="problem-content">
        <ProblemDescription :description="problem.description" />
        <ProblemExample :examples="formatExamples(problem.examples)" />
        <ProblemConstraints :constraints="problem.constraint" />
      </div>
    </template>

    <!-- 문제 ID가 없는 경우 -->
    <div v-else class="no-problem-state">
      <div class="warning-icon">🔍</div>
      <p>문제 정보가 없습니다.</p>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, watch } from 'vue';
import ProblemHeader from './ProblemHeader.vue';
import ProblemDescription from './ProblemDescription.vue';
import ProblemExample from './ProblemExample.vue';
import ProblemConstraints from './ProblemConstraints.vue';
import { useProblem } from '@/composables/useProblem.js';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';

const { problem, loading, error, fetchProblem, formatExamples } = useProblem();
const inGameStore = useInGameStore();

// 스토어에서 문제 ID 가져오기
const problemId = computed(() => inGameStore.banPickBuyResults?.pickedProblem);

// 문제 조회 함수
const loadProblem = async () => {
  if (problemId.value) {
    await fetchProblem(problemId.value);
  } else {
    console.error('[ProblemPanel] 문제 ID가 없습니다. 스토어 상태:', inGameStore.banPickBuyResults);
  }
};

// 컴포넌트 마운트 시 문제 조회
onMounted(() => {
  loadProblem();
});

// problemId가 변경되면 새로 조회
watch(
  problemId,
  (newId, oldId) => {
    if (newId && newId !== oldId) {
      loadProblem();
    }
  },
  { immediate: true },
);
</script>

<style lang="scss" scoped>
.problem-panel {
  background: linear-gradient(to bottom, rgba($primary-bg, 0.1) 0%, rgba($primary-bg, 0.3) 100%);
  backdrop-filter: blur(2.5vh);
  border: 0.2vh solid $cyberpunk-pink;
  border-radius: $radius-small;
  height: 100%;
  display: flex;
  flex-direction: column;
  @include neon-glow($cyberpunk-pink);
  overflow: hidden;
}

.problem-content {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;

  // 사이버펑크 스타일 스크롤바
  &::-webkit-scrollbar {
    width: 0.1vh;
  }

  &::-webkit-scrollbar-track {
    background: rgba($primary-bg, 0.3);
    border-radius: 1vh;
  }

  &::-webkit-scrollbar-thumb {
    background: $electric-blue;
    border-radius: 3px;

    &:hover {
      background: $cyberpunk-pink;
    }
  }

  // Firefox 스크롤바
  scrollbar-width: thin;
  scrollbar-color: $electric-blue rgba($primary-bg, 0.3);
}

// 상태별 스타일
.loading-state,
.error-state,
.no-problem-state {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: clamp(20px, 4vh, 40px);
  text-align: center;
  color: $text-primary;
}

.loading-state {
  .loading-spinner {
    width: clamp(32px, 4vh, 48px);
    height: clamp(32px, 4vh, 48px);
    border: 3px solid rgba($electric-blue, 0.3);
    border-top: 3px solid $electric-blue;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin-bottom: clamp(12px, 2vh, 16px);
  }

  p {
    font-size: clamp(14px, 1.4vh, 16px);
    color: rgba($text-primary, 0.8);
  }
}

.error-state {
  .error-icon {
    font-size: clamp(32px, 4vh, 48px);
    margin-bottom: clamp(12px, 2vh, 16px);
  }

  p {
    font-size: clamp(14px, 1.4vh, 16px);
    color: $cyberpunk-pink;
    margin-bottom: clamp(16px, 2vh, 20px);
  }

  .retry-btn {
    padding: clamp(8px, 1vh, 12px) clamp(16px, 2vh, 24px);
    background: rgba($electric-blue, 0.2);
    border: 1px solid $electric-blue;
    color: $electric-blue;
    border-radius: clamp(4px, 0.6vh, 6px);
    cursor: pointer;
    font-family: $font-primary;
    font-size: clamp(12px, 1.2vh, 14px);
    transition: all 0.3s ease;

    &:hover {
      background: rgba($electric-blue, 0.3);
      @include neon-glow($electric-blue);
    }
  }
}

.no-problem-state {
  .warning-icon {
    font-size: clamp(32px, 4vh, 48px);
    margin-bottom: clamp(12px, 2vh, 16px);
  }

  p {
    font-size: clamp(14px, 1.4vh, 16px);
    color: rgba($text-primary, 0.6);
  }
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
