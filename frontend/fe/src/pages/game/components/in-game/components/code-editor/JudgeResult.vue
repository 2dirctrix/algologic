<!-- src/pages/game/components/in-game/components/code-editor/JudgeResult.vue -->
<template>
  <div class="judge-result" :class="{ expanded: isExpanded }" :style="{ height: containerHeight }">
    <div class="judge-header">
      <div class="judge-title-section">
        <span class="judge-title">채점 결과</span>
        <!-- 전체 채점 결과 표시 -->
        <div
          v-if="overallResult"
          class="overall-result"
          :class="{ 'all-solved': overallResult.isSolved, 'has-failed': !overallResult.isSolved }"
        >
          <span class="result-text">{{ overallResult.isSolved ? '맞았습니다!' : '틀렸습니다' }}</span>
          <span v-if="overallResult.isSolved && overallResult.maxRunningTime" class="performance-item">
            실행시간: {{ overallResult.maxRunningTime * 1000 }}ms
          </span>
          <span v-if="overallResult.isSolved && overallResult.maxMemoryUsage" class="performance-item">
            메모리: {{ formatMemory(overallResult.maxMemoryUsage) }}
          </span>
        </div>
      </div>

      <button class="toggle-btn" @click="toggleExpanded" :title="isExpanded ? '결과 숨기기' : '결과 보기'">
        <svg class="arrow-icon" :class="{ rotated: isExpanded }" width="12" height="12" viewBox="0 0 12 12" fill="currentColor">
          <path d="M2 4l4 4 4-4H2z" />
        </svg>
      </button>
    </div>

    <div class="judge-content" v-if="isExpanded">
      <div class="testcase-list" v-if="testCases.length > 0">
        <div v-for="testCase in testCases" :key="testCase.id" class="testcase-item" :class="getTestCaseClass(testCase)">
          <div class="testcase-header">
            <span class="testcase-number">테스트케이스 #{{ testCase.id }}</span>
            <div class="testcase-status">
              <div class="loading-spinner" v-if="testCase.status === 'judging'">
                <div v-for="i in 8" :key="i" class="spinner-dot" :style="{ animationDelay: `${(i - 1) * 0.15}s` }"></div>
              </div>
              <span class="status-text" :class="getStatusClass(testCase.status)">
                {{ getStatusText(testCase.status) }}
              </span>
            </div>
          </div>

          <div
            v-if="testCase.status === 'solved' || testCase.status === 'failed' || testCase.status === 'compile-error'"
            class="testcase-details"
          >
            <div class="detail-item" v-if="testCase.runningTime !== null">
              <span class="detail-label">실행 시간:</span>
              <span class="detail-value">{{ testCase.runningTime * 1000 }}ms</span>
            </div>
            <div class="detail-item" v-if="testCase.memoryUsage !== null">
              <span class="detail-label">메모리:</span>
              <span class="detail-value">{{ formatMemory(testCase.memoryUsage) }}</span>
            </div>
            <div class="detail-item" v-if="testCase.failure">
              <span class="detail-label">실패 원인:</span>
              <span class="detail-value failure">{{ testCase.failure }}</span>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="no-results">채점 결과가 없습니다.</div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';

// Props
const props = defineProps({
  testCaseCount: {
    type: Number,
    default: 0,
  },
  initialExpanded: {
    type: Boolean,
    default: true,
  },
});

// Store
const inGameStore = useInGameStore();

// Reactive state
const isExpanded = ref(props.initialExpanded);
const testCases = ref([]);

// Computed
const containerHeight = computed(() => {
  return isExpanded.value ? '40vh' : '4vh';
});

const overallResult = computed(() => {
  return inGameStore.judgeResultInfo;
});

// Methods
const initializeTestCases = count => {
  testCases.value = Array.from({ length: count }, (_, index) => ({
    id: index + 1,
    status: 'judging',
    runningTime: null,
    memoryUsage: null,
    failure: null,
  }));
};

const extractFailureMessage = failure => {
  if (typeof failure === 'string') {
    return failure;
  }
  if (failure && typeof failure === 'object' && failure.cause) {
    return failure.cause;
  }
  return '알 수 없는 오류';
};

const updateTestCasesFromResults = judgeData => {
  // 컴파일 에러인 경우
  if (judgeData.testcaseResults === null && judgeData.failure) {
    testCases.value = testCases.value.map(testCase => ({
      id: testCase.id,
      status: 'compile-error',
      runningTime: null,
      memoryUsage: null,
      failure: extractFailureMessage(judgeData.failure),
    }));
    return;
  }

  // 일반적인 테스트케이스 결과 처리
  const testcaseResults = judgeData.testcaseResults || [];

  // 랜덤한 시간차를 두고 순차적으로 결과 업데이트
  testCases.value.forEach((testCase, index) => {
    const result = testcaseResults.find(r => r.testcaseNumber === testCase.id);
    
    if (result) {
      // 200ms ~ 800ms 사이의 랜덤한 지연시간
      const randomDelay = Math.random() * 600 + 200;
      
      setTimeout(() => {
        // 해당 테스트케이스만 업데이트
        const testCaseIndex = testCases.value.findIndex(tc => tc.id === testCase.id);
        if (testCaseIndex !== -1) {
          testCases.value[testCaseIndex] = {
            id: testCase.id,
            status: result.isSolved ? 'solved' : 'failed',
            runningTime: result.runningTime,
            memoryUsage: result.memoryUsage,
            failure: result.failure ? extractFailureMessage(result.failure) : null,
          };
        }
      }, randomDelay);
    } else {
      console.warn(`[JudgeResult] 테스트케이스 ${testCase.id}의 결과를 찾을 수 없음`);
    }
  });
};

const toggleExpanded = () => {
  isExpanded.value = !isExpanded.value;
};

const getTestCaseClass = testCase => {
  return {
    judging: testCase.status === 'judging',
    solved: testCase.status === 'solved',
    failed: testCase.status === 'failed',
    'compile-error': testCase.status === 'compile-error',
  };
};

const getStatusClass = status => {
  return {
    'status-judging': status === 'judging',
    'status-solved': status === 'solved',
    'status-failed': status === 'failed',
    'status-compile-error': status === 'compile-error',
  };
};

const getStatusText = status => {
  const statusMap = {
    judging: '채점중',
    solved: '맞았습니다',
    failed: '틀렸습니다',
    'compile-error': '컴파일 에러',
  };
  return statusMap[status] || '알 수 없음';
};

const formatMemory = memory => {
  if (!memory) return 'N/A';
  if (memory >= 1024 * 1024) {
    return `${(memory / 1024 / 1024).toFixed(2)}MB`;
  } else if (memory >= 1024) {
    return `${(memory / 1024).toFixed(2)}KB`;
  }
  return `${memory}B`;
};

// Watchers
// testCaseCount props 감시
watch(
  () => props.testCaseCount,
  newCount => {
    if (newCount > 0) {
      initializeTestCases(newCount);
    }
  },
  { immediate: true },
);

// 스토어의 judgeResultMessage 감시
watch(
  () => inGameStore.judgeResultMessage,
  message => {
    // TODO: 재제출 테스트
    if (message === null) {
      // 재제출로 초기화 신호로 간주
      initializeTestCases(props.testCaseCount);
      return;
    }

    if (message) {
      const judgeData = message;

      // 메시지 검증
      if (judgeData.problemId && judgeData.programmingLanguage !== undefined && judgeData.isSolved !== undefined) {
        updateTestCasesFromResults(judgeData);
      } else {
        console.warn('[JudgeResult] 유효하지 않은 메시지 형식:', message);
      }
    }
  },
  { deep: true },
);
</script>

<style lang="scss" scoped>
.judge-result {
  background: rgba($card-bg, 0.4);
  border: 0.2vh solid $electric-blue;
  border-radius: $radius-small;
  width: 100%;
  display: flex;
  flex-direction: column;
  backdrop-filter: blur(1vh);
  overflow: hidden;
  transition: height 0.3s ease;
  @include neon-glow($electric-blue);
}

.judge-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(to bottom, rgba($primary-bg, 0.1) 0%, rgba($primary-bg, 0.3) 100%);
  backdrop-filter: blur(1.5vh);
  border-bottom: 0.1vh solid rgba($text-primary, 0.1);
  height: 7vh;
  padding: 0 1.5vh;
}

.judge-title-section {
  display: flex;
  align-items: center;
  gap: 2vh;
  flex: 1;
}

.judge-title {
  color: $electric-blue;
  font-family: $font-primary;
  font-weight: $font-weight-semibold;
  font-size: 2vh;
  flex-shrink: 0;
}

.overall-result {
  display: flex;
  align-items: center;
  gap: 1vh;

  .result-text {
    font-family: $font-primary;
    font-weight: $font-weight-bold;
    font-size: 1.6vh;
    flex-shrink: 0;

  }

  .performance-item {
    font-family: $font-primary;
    font-weight: $font-weight-medium;
    font-size: 1.2vh;
    color: rgba($text-primary, 0.8);
    background: rgba($electric-blue, 0.1);
    padding: 0.2vh 0.5vh;
    border-radius: 0.3vh;
    border: 0.05vh solid rgba($electric-blue, 0.3);
    white-space: nowrap;
    flex-shrink: 0;
  }
}

.overall-result.all-solved {
  .result-text {
    color: $neon-green;
  }
}

.overall-result.has-failed {
  .result-text {
    color: $cyberpunk-pink;
  }
}

.toggle-btn {
  background: transparent;
  border: 0.1vh solid rgba($text-primary, 0.2);
  border-radius: 0.4vh;
  padding: 0.4vh;
  cursor: pointer;
  transition: all 0.3s ease;
  color: rgba($text-primary, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  width: 2.4vh;
  height: 2.4vh;

  &:hover {
    background: rgba($electric-blue, 0.1);
    border-color: rgba($electric-blue, 0.5);
    color: $electric-blue;
  }
}

.arrow-icon {
  transition: transform 0.3s ease;

  &.rotated {
    transform: rotate(180deg);
  }
}

.judge-content {
  flex: 1;
  overflow-y: auto;
  padding: 1vh;

  &::-webkit-scrollbar {
    width: 0.4vh;
  }

  &::-webkit-scrollbar-track {
    background: transparent;
  }

  &::-webkit-scrollbar-thumb {
    background: rgba($electric-blue, 0.3);
    border-radius: 0.2vh;
  }
}

.testcase-list {
  display: flex;
  flex-direction: column;
  gap: 1vh;
}

.testcase-item {
  background: rgba($primary-bg, 0.3);
  border: 0.1vh solid rgba($text-primary, 0.1);
  border-radius: 0.6vh;
  padding: 1vh;
  transition: all 0.5s ease;

  &.solved {
    border-color: rgba($neon-green, 0.5);
    background: rgba($neon-green, 0.05);
    animation: resultAppear 0.6s ease-out;
  }

  &.failed {
    border-color: rgba($cyberpunk-pink, 0.5);
    background: rgba($cyberpunk-pink, 0.05);
    animation: resultAppear 0.6s ease-out;
  }

  &.judging {
    border-color: rgba($cyberpunk-yellow, 0.5);
    background: rgba($cyberpunk-yellow, 0.05);
  }

  &.compile-error {
    border-color: rgba($cyberpunk-pink, 0.7);
    background: rgba($cyberpunk-pink, 0.1);
    animation: resultAppear 0.6s ease-out;
  }
}

.testcase-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5vh;
}

.testcase-number {
  font-family: $font-primary;
  font-weight: $font-weight-medium;
  font-size: 1.6vh;
  color: $text-primary;
}

.testcase-status {
  display: flex;
  align-items: center;
  gap: 0.8vh;
}

.loading-spinner {
  display: flex;
  align-items: center;
  gap: 0.4vh;
  height: 1.6vh;
}

.spinner-dot {
  width: 0.8vh;
  height: 0.8vh;
  background: $cyberpunk-yellow;
  border-radius: 50%;
  animation: pulse 1.2s ease-in-out infinite;
  opacity: 0.3;
}

@keyframes pulse {
  0%,
  80%,
  100% {
    opacity: 0.3;
    transform: scale(1);
  }
  40% {
    opacity: 1;
    transform: scale(1.3);
  }
}

.status-text {
  font-family: $font-primary;
  font-weight: $font-weight-medium;
  font-size: 1.6vh;

  &.status-judging {
    color: $cyberpunk-yellow;
  }

  &.status-solved {
    color: $neon-green;
  }

  &.status-failed {
    color: $cyberpunk-pink;
  }

  &.status-compile-error {
    color: $cyberpunk-pink;
  }
}

.testcase-details {
  display: flex;
  flex-wrap: wrap;
  gap: 1vh;
  padding-top: 0.5vh;
  border-top: 0.1vh solid rgba($text-primary, 0.1);
}

.detail-item {
  display: flex;
  align-items: center;
  gap: 0.5vh;
  font-size: 0.9vh;
}

.detail-label {
  font-family: $font-primary;
  font-weight: $font-weight-medium;
  color: rgba($text-primary, 0.7);
}

.detail-value {
  font-family: $font-primary;
  font-weight: $font-weight-medium;
  color: $text-primary;

  &.failure {
    color: $cyberpunk-pink;
  }
}

.no-results {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 10vh;
  color: rgba($text-primary, 0.6);
  font-family: $font-primary;
  font-size: 1.1vh;
}

.expanded {
  .judge-content {
    animation: slideDown 0.3s ease-in-out;
  }
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-1vh);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes resultAppear {
  0% {
    opacity: 0.5;
    transform: translateX(-1vh) scale(0.95);
    box-shadow: none;
  }
  50% {
    transform: translateX(0) scale(1.02);
  }
  100% {
    opacity: 1;
    transform: translateX(0) scale(1);
    box-shadow: 0 0 1vh rgba($electric-blue, 0.3);
  }
}
</style>
