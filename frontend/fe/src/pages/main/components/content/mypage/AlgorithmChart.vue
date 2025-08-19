<template>
  <div class="algorithm-chart">
    <div class="chart-container" v-if="chartData.length > 0">
      <svg width="100%" height="100%" viewBox="0 0 400 400" class="hexagon-svg">
        <!-- 배경 육각형 격자 -->
        <g class="grid-lines">
          <polygon
            v-for="level in 5"
            :key="level"
            :points="getHexagonPoints(centerX, centerY, (maxRadius * level) / 5)"
            fill="none"
            stroke="rgba(0, 212, 255, 0.2)"
            stroke-width="1"
          />
        </g>

        <!-- 축선들 -->
        <g class="axis-lines">
          <line
            v-for="(point, index) in hexagonVertices"
            :key="index"
            :x1="centerX"
            :y1="centerY"
            :x2="point.x"
            :y2="point.y"
            stroke="rgba(0, 212, 255, 0.3)"
            stroke-width="1"
          />
        </g>

        <!-- 데이터 영역 -->
        <polygon :points="dataPolygonPoints" fill="rgba(0, 255, 136, 0.3)" stroke="#00ff88" stroke-width="2" class="data-polygon" />

        <!-- 알고리즘 유형 라벨 -->
        <text
          v-for="(vertex, index) in hexagonVertices"
          :key="`label-${index}`"
          :x="vertex.labelX"
          :y="vertex.labelY"
          text-anchor="middle"
          dominant-baseline="central"
          class="vertex-label"
        >
          {{ getAlgorithmLabel(index) }}
        </text>

        <!-- 데이터 포인트들 -->
        <circle
          v-for="(point, index) in dataPoints"
          :key="index"
          :cx="point.x"
          :cy="point.y"
          r="4"
          fill="#00ff88"
          class="data-point"
          @mouseenter="showTooltip($event, chartData[index])"
          @mouseleave="hideTooltip"
        />
      </svg>

      <!-- 툴팁 -->
      <div
        v-if="tooltip.visible"
        class="tooltip"
        :style="{
          left: tooltip.x + 'px',
          top: tooltip.y + 'px',
        }"
      >
        <div class="tooltip-content">
          <div class="tooltip-title">{{ tooltip.data?.problemCategoryName }}</div>
          <div class="tooltip-body">
            <div class="tooltip-value">
              <span class="value-number">{{ tooltip.data?.solvedCount }}</span>
              <span class="value-unit">개 해결</span>
            </div>
            <div class="tooltip-progress">
              <div class="progress-bar">
                <div 
                  class="progress-fill" 
                  :style="{ width: (tooltip.data?.normalizedValue * 100) + '%' }"
                ></div>
              </div>
              <span class="progress-text">{{ Math.round((tooltip.data?.normalizedValue || 0) * 100) }}%</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 로딩 상태 -->
    <div v-else-if="isLoading" class="loading-state">
      <div class="loading-spinner"></div>
    </div>

    <!-- 데이터 없음 -->
    <div v-else class="no-data">
      <span>알고리즘 데이터 없음</span>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

const props = defineProps({
  data: {
    type: Array,
    default: () => [],
  },
  size: {
    type: Number,
    default: 200,
  },
  isLoading: {
    type: Boolean,
    default: false,
  },
});

// 차트 설정
const centerX = 200;
const centerY = 200;
const maxRadius = 120;

// 툴팁 상태
const tooltip = ref({
  visible: false,
  x: 0,
  y: 0,
  data: null,
});

// 차트 데이터 정규화 (최대 6개 항목)
const chartData = computed(() => {
  if (!props.data || props.data.length === 0) return [];

  // 최대 6개로 제한
  const limitedData = props.data.slice(0, 6);

  // 6개가 안되면 빈 데이터로 채움
  const filledData = [...limitedData];
  while (filledData.length < 6) {
    filledData.push({
      problemCategoryId: -1,
      problemCategoryName: '미설정',
      solvedCount: 0,
    });
  }

  // 최대값 계산하여 정규화
  const maxCount = Math.max(...limitedData.map(item => item.solvedCount), 1);

  return filledData.map(item => ({
    ...item,
    normalizedValue: item.solvedCount / maxCount,
  }));
});

// 육각형 꼭짓점 계산
const hexagonVertices = computed(() => {
  const vertices = [];
  const labelDistance = maxRadius + 35; // 라벨을 꼭짓점에서 더 멀리 배치하여 텍스트 잘림 방지

  for (let i = 0; i < 6; i++) {
    const angle = (i * Math.PI) / 3 - Math.PI / 2; // -90도부터 시작
    vertices.push({
      x: centerX + maxRadius * Math.cos(angle),
      y: centerY + maxRadius * Math.sin(angle),
      labelX: centerX + labelDistance * Math.cos(angle),
      labelY: centerY + labelDistance * Math.sin(angle),
    });
  }
  return vertices;
});

// 데이터 포인트 계산
const dataPoints = computed(() => {
  return chartData.value.map((item, index) => {
    const distance = maxRadius * item.normalizedValue;
    const angle = (index * Math.PI) / 3 - Math.PI / 2;

    return {
      x: centerX + distance * Math.cos(angle),
      y: centerY + distance * Math.sin(angle),
    };
  });
});

// 데이터 폴리곤 포인트 문자열
const dataPolygonPoints = computed(() => {
  return dataPoints.value.map(point => `${point.x},${point.y}`).join(' ');
});

// 육각형 포인트 생성 함수
const getHexagonPoints = (cx, cy, radius) => {
  const points = [];
  for (let i = 0; i < 6; i++) {
    const angle = (i * Math.PI) / 3 - Math.PI / 2;
    const x = cx + radius * Math.cos(angle);
    const y = cy + radius * Math.sin(angle);
    points.push(`${x},${y}`);
  }
  return points.join(' ');
};

// 툴팁 표시
const showTooltip = (event, data) => {
  if (data.solvedCount === 0) return;

  const svg = event.target.closest('.hexagon-svg');
  const svgRect = svg.getBoundingClientRect();

  // 현재 호버된 circle 요소에서 좌표 가져오기
  const circle = event.target;
  const cx = parseFloat(circle.getAttribute('cx'));
  const cy = parseFloat(circle.getAttribute('cy'));

  // SVG viewBox (400x400)에서 실제 렌더링 크기로 변환
  const scaleX = svgRect.width / 400;
  const scaleY = svgRect.height / 400;

  const actualX = cx * scaleX;
  const actualY = cy * scaleY;

  tooltip.value = {
    visible: true,
    x: actualX + 15, // 데이터 포인트 오른쪽에 표시
    y: actualY - 10, // 데이터 포인트 위쪽에 표시
    data,
  };
};

// 툴팁 숨김
const hideTooltip = () => {
  tooltip.value.visible = false;
};

// 알고리즘 라벨 가져오기
const getAlgorithmLabel = index => {
  if (chartData.value[index] && chartData.value[index].problemCategoryName !== '미설정') {
    return chartData.value[index].problemCategoryName;
  }

  const defaultLabels = ['DP', '그래프', '그리디', '구현', '수학', '문자열'];
  return defaultLabels[index] || '';
};
</script>

<style lang="scss" scoped>
.algorithm-chart {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;

  .chart-container {
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    width: calc(50vh);
    height: calc(50vh);

    .hexagon-svg {
      filter: drop-shadow(0 0 calc(0.5vh) rgba($electric-blue, 0.3));

      .data-polygon {
        transition: all 0.3s ease-in-out;
      }

      .data-point {
        transition: all 0.2s ease-in-out;
        cursor: pointer;

        &:hover {
          r: 5;
          fill: $neon-green;
          filter: drop-shadow(0 0 calc(0.3vh) rgba($neon-green, 0.6));
        }
      }

      .vertex-label {
        font-family: $font-primary;
        font-size: calc(1.3vh);
        font-weight: $font-weight-semibold;
        fill: rgba($electric-blue, 0.8);
        user-select: none;
        pointer-events: none;
      }
    }
  }

  .tooltip {
    position: absolute;
    pointer-events: none;
    z-index: 1000;

    .tooltip-content {
      background: linear-gradient(135deg, rgba($card-bg, 0.98), rgba($card-bg, 0.92));
      border: calc(0.15vh) solid rgba($electric-blue, 0.6);
      border-radius: calc(0.8vh);
      padding: calc(1.5vh) calc(2vh);
      backdrop-filter: blur(calc(1.5vh));
      box-shadow:
        0 calc(0.8vh) calc(3vh) rgba(0, 0, 0, 0.4),
        0 0 calc(1vh) rgba($electric-blue, 0.3),
        inset 0 calc(0.1vh) calc(0.3vh) rgba(255, 255, 255, 0.1);
      min-width: calc(20vh);
      transform: translateY(calc(-0.5vh));

      &::before {
        content: '';
        position: absolute;
        top: calc(-0.15vh);
        left: calc(-0.15vh);
        right: calc(-0.15vh);
        bottom: calc(-0.15vh);
        background: linear-gradient(135deg, rgba($electric-blue, 0.4), rgba($neon-green, 0.2));
        border-radius: calc(0.8vh);
        z-index: -1;
      }

      .tooltip-title {
        font-family: $font-accent;
        font-size: calc(2.2vh);
        font-weight: $font-weight-bold;
        color: $electric-blue;
        margin-bottom: calc(1.2vh);
        text-shadow: 0 0 calc(0.3vh) rgba($electric-blue, 0.3);
      }

      .tooltip-body {
        .tooltip-value {
          display: flex;
          align-items: baseline;
          gap: calc(0.5vh);
          margin-bottom: calc(1vh);

          .value-number {
            font-family: $font-accent;
            font-size: calc(2.8vh);
            font-weight: $font-weight-bold;
            color: $neon-green;
            text-shadow: 0 0 calc(0.3vh) rgba($neon-green, 0.4);
          }

          .value-unit {
            font-family: $font-primary;
            font-size: calc(1.6vh);
            font-weight: $font-weight-medium;
            color: rgba($text-primary, 0.8);
          }
        }

        .tooltip-progress {
          display: flex;
          align-items: center;
          gap: calc(1vh);

          .progress-bar {
            flex: 1;
            height: calc(0.6vh);
            background: rgba($electric-blue, 0.2);
            border-radius: calc(0.3vh);
            overflow: hidden;
            border: calc(0.1vh) solid rgba($electric-blue, 0.3);

            .progress-fill {
              height: 100%;
              background: linear-gradient(90deg, $electric-blue, $neon-green);
              border-radius: calc(0.2vh);
              transition: width 0.3s ease-out;
              box-shadow: 0 0 calc(0.3vh) rgba($neon-green, 0.3);
            }
          }

          .progress-text {
            font-family: $font-primary;
            font-size: calc(1.4vh);
            font-weight: $font-weight-semibold;
            color: $electric-blue;
            min-width: calc(4vh);
            text-align: right;
          }
        }
      }
    }
  }

  .loading-state {
    display: flex;
    align-items: center;
    justify-content: center;
    width: calc(15vh);
    height: calc(15vh);

    .loading-spinner {
      width: calc(4vh);
      height: calc(4vh);
      border: calc(0.3vh) solid rgba($electric-blue, 0.3);
      border-top: calc(0.3vh) solid $electric-blue;
      border-radius: 50%;
      animation: spin 1s linear infinite;
    }
  }

  .no-data {
    display: flex;
    align-items: center;
    justify-content: center;
    width: calc(15vh);
    height: calc(15vh);

    span {
      font-family: $font-primary;
      font-size: calc(1.2vh);
      color: rgba($text-primary, 0.6);
      text-align: center;
    }
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
