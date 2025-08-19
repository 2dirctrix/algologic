<template>
  <div class="activity-heatmap">
    <CalendarHeatmap
      :values="heatmapData"
      :end-date="endDate"
      :max="maxCount"
      :round="2"
      :tooltip="true"
      :dark-mode="false"
      :locale="{
        months: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
        days: ['S', 'M', 'T', 'W', 'T', 'F', 'S'],
        on: '일',
      }"
      tooltip-unit="게임"
      :no-data-text="'플레이 기록 없음'"
    />
  </div>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue';
import { CalendarHeatmap } from 'vue3-calendar-heatmap';
import { useAuth } from '@/composables/useAuth';

const { fetchDailyGames } = useAuth();

// 활동 데이터
const dailyGames = ref([]);

// 일일 게임 데이터 로드
const loadDailyGames = async () => {
  try {
    const data = await fetchDailyGames();
    dailyGames.value = data || [];
  } catch (error) {
    console.error('일일 게임 데이터 조회 실패:', error);
    dailyGames.value = [];
  }
};

// 히트맵용 데이터 변환
const heatmapData = computed(() => {
  if (!dailyGames.value || !Array.isArray(dailyGames.value)) return [];

  return dailyGames.value.map(item => ({
    date: item.date, // YYYY-MM-DD 형식의 문자열
    count: item.gameCount || 0,
  }));
});

// 종료 날짜 (오늘) - 문자열 형식으로 변환
const endDate = computed(() => {
  const today = new Date();
  return today.toISOString().split('T')[0]; // YYYY-MM-DD 형식
});

// 최대 게임 수
const maxCount = computed(() => {
  if (!dailyGames.value || !dailyGames.value.length) return 10;
  return Math.max(...dailyGames.value.map(item => item.gameCount || 0));
});

// 컴포넌트 마운트 시 데이터 로드
onMounted(async () => {
  await loadDailyGames();
});
</script>

<style lang="scss" scoped>
.activity-heatmap {
  width: 100%;
  overflow-x: auto;
  overflow-y: hidden; // 세로 스크롤 방지

  :deep(.vch__container) {
    font-family: $font-primary;
    min-width: 100vh; // 최소 너비 설정으로 스크롤 생성
    height: 100%; // 컨테이너 높이를 부모에 맞춤

    // SVG 컨테이너 스타일 - 크기 조정
    svg {
      width: 100%;
      height: 100%; // 고정 높이
      overflow: visible;
    }

    // 범례 스타일 조정
    .vch__legend {
      display: flex !important;
      align-items: center !important;
      justify-content: flex-end !important;
      gap: 2vh !important;
      margin-top: 1vh !important;
    }

    // 모든 텍스트 요소 강제 스타일 설정 (최우선순위)
    text {
      fill: #ffffff !important;
      font-size: 1vh !important;
    }

    // 요일 라벨 스타일링 - 모든 요일 강제 표시
    .vch__days__labels text {
      font-size: 0.25vh !important;
      fill: #ffffff !important;
      opacity: 0.6 !important;
      font-weight: 300 !important;

      // 모든 요일을 강제로 표시
      display: block !important;
      visibility: visible !important;
    }

    // 혹시 숨겨진 요일들을 강제로 표시
    .vch__days__labels text[style*='display: none'],
    .vch__days__labels text[style*='visibility: hidden'] {
      display: block !important;
      visibility: visible !important;
    }

    // nth-child 관련 모든 규칙 무효화
    .vch__days__labels text:nth-child(n) {
      display: block !important;
      visibility: visible !important;
    }

    // 라이브러리에서 설정한 스타일 강제 덮어쓰기
    .vch__days__labels {
      * {
        display: block !important;
        visibility: visible !important;
      }
    }

    // 날짜 셀 스타일
    .vch__day__square {
      &:hover {
        stroke: $neon-green;
        stroke-width: 1.5;
        cursor: not-allowed; // 클릭 불가 커서
        pointer-events: none;
      }
    }

    // 툴팁 스타일 커스터마이징
    .vch__tooltip {
      background-color: rgba(0, 0, 0, 0.9) !important;
      color: #ffffff !important;
      border: 1px solid rgba(255, 255, 255, 0.2) !important;
      border-radius: 6px !important;
      padding: 8px 12px !important;
      font-size: 12px !important;
      font-family: $font-primary !important;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4) !important;
      backdrop-filter: blur(8px) !important;
      z-index: 1000 !important;

      // 툴팁 내부 텍스트 스타일
      * {
        color: #ffffff !important;
        font-size: 12px !important;
      }
    }
  }
}

// 전역 툴팁 스타일 (스코프 밖에서도 적용되도록)
:deep(.vue-tooltip) {
  background-color: rgba(0, 0, 0, 0.9) !important;
  color: #ffffff !important;
  border: 1px solid rgba(255, 255, 255, 0.2) !important;
  border-radius: 6px !important;
  padding: 8px 12px !important;
  font-size: 12px !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4) !important;
}
</style>
