<template>
  <div class="game-history">
    <div v-if="isLoading" class="loading-state">
      <div class="loading-spinner"></div>
      <span>게임 기록을 불러오는 중...</span>
    </div>

    <div v-else-if="gameResults.length === 0" class="no-data">
      <span>아직 참여한 게임이 없습니다.</span>
    </div>

    <div v-else class="history-list">
      <div class="history-container">
        <div
          v-for="(game, index) in gameResults"
          :key="game.gameResultId"
          class="game-item slide-up"
          :class="{
            'slide-down': isLeaving,
            [`result-${getGameResultStatus(game)}`]: getGameResultStatus(game),
          }"
          :style="{ 'animation-delay': isLeaving ? `${(gameResults.length - 1 - index) * 0.1}s` : `${index * 0.1}s` }"
          @click="openGameResultModal(game.gameResultId)"
        >
          <div class="game-header">
            <div class="status-indicator" v-if="getGameResultStatus(game)" :class="getResultStatusClass(getGameResultStatus(game))">
              {{ getResultStatusTextEn(getGameResultStatus(game)) }}
            </div>
            <div class="game-name">{{ game.gameName }}</div>
            <div class="problem-level" :class="getProblemLevelClass(game.problemLevel)">
              {{ game.problemLevel }}
            </div>
            <div class="max-players">{{ game.maxPlayers }}인</div>
            <div class="problem-name">{{ game.problemName }}</div>
            <div class="game-time">{{ getRelativeTime(game.startedAt) }}</div>
            <div class="view-result-btn">
              <i class="fas fa-external-link-alt"></i>
              <span>결과 보기</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 게임 결과 모달 - Teleport to body -->
    <Teleport to="body">
      <div v-if="showModal" class="modal-overlay" @click="closeModal">
        <div class="modal-container" @click.stop>
          <div class="modal-header">
            <h3>게임 결과</h3>
            <button class="close-btn" @click="closeModal">✕</button>
          </div>
          <div class="modal-body">
            <GameResultPage :game-result-id="selectedGameId" :is-modal="true" />
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useAuth } from '@/composables/useAuth';
import GameResultPage from '@/pages/game/components/result/GameResultPage.vue';

const { fetchGameResults, user } = useAuth();

const isLoading = ref(false);
const gameResults = ref([]);
const showModal = ref(false);
const selectedGameId = ref(null);
const isLeaving = ref(false);

const loadGameResults = async () => {
  try {
    isLoading.value = true;
    const results = await fetchGameResults();
    gameResults.value = results || [];
  } catch (error) {
    console.error('게임 결과 로딩 실패:', error);
    gameResults.value = [];
  } finally {
    isLoading.value = false;
  }
};

const openGameResultModal = gameResultId => {
  selectedGameId.value = gameResultId;
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
  selectedGameId.value = null;
};

const getRelativeTime = dateString => {
  const gameTime = new Date(dateString);
  // 한국 시간으로 변환 (UTC+9)
  const koreaTime = new Date(gameTime.getTime() + 9 * 60 * 60 * 1000);
  const now = new Date();
  const diffMs = now.getTime() - koreaTime.getTime();

  const minutes = Math.floor(diffMs / (1000 * 60));
  const hours = Math.floor(diffMs / (1000 * 60 * 60));
  const days = Math.floor(diffMs / (1000 * 60 * 60 * 24));

  if (days > 0) {
    return `${days}일 전`;
  } else if (hours > 0) {
    return `${hours}시간 전`;
  } else if (minutes > 0) {
    return `${minutes}분 전`;
  } else {
    return '방금 전';
  }
};

const getProblemLevelClass = level => {
  const levelMap = {
    쉬움: 'easy',
    중간: 'medium',
    어려움: 'hard',
  };
  return levelMap[level] || 'medium';
};

// 사용자의 게임 결과 상태 판단 (승리/무승부/패배)
const getGameResultStatus = game => {
  if (!user.value?.id || !game.playerResults) return null;

  // 현재 사용자의 결과 찾기
  const userResult = game.playerResults.find(player => player.memberId === user.value.id);
  if (!userResult) return null;

  const totalPlayers = game.playerResults.length;
  const userRank = userResult.rank;

  // 모든 플레이어가 같은 등수인지 확인
  const allSameRank = game.playerResults.every(player => player.rank === userRank);
  if (allSameRank) {
    return 'draw';
  }

  // 승리 조건: 상위 절반 (참여인원/2 이상의 등수)
  const winThreshold = Math.ceil(totalPlayers / 2);

  if (userRank <= winThreshold) {
    // 홀수 인원이고 정확히 중간 등수인 경우 무승부
    if (totalPlayers % 2 === 1 && userRank === winThreshold) {
      return 'draw';
    }
    return 'win';
  }

  return 'loss';
};

// 게임 결과 상태에 따른 CSS 클래스 반환
const getResultStatusClass = status => {
  const statusMap = {
    win: 'status-win',
    draw: 'status-draw',
    loss: 'status-loss',
  };
  return statusMap[status] || '';
};

// 게임 결과 상태에 따른 영어 텍스트 반환
const getResultStatusTextEn = status => {
  const statusMap = {
    win: 'WIN',
    draw: 'DRAW',
    loss: 'LOSE',
  };
  return statusMap[status] || '';
};

// 퇴장 애니메이션 트리거 함수
const triggerLeaveAnimation = () => {
  isLeaving.value = true;
};

// 부모 컴포넌트에서 호출할 수 있도록 노출
defineExpose({
  triggerLeaveAnimation,
});

onMounted(() => {
  loadGameResults();
});
</script>

<style lang="scss" scoped>
.game-history {
  width: 100%;
  height: 100%;
  overflow-y: auto;

  .loading-state,
  .no-data {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100%;
    gap: calc(2vh);

    span {
      font-family: $font-primary;
      font-size: calc(1.8vh);
      color: rgba($text-primary, 0.7);
    }
  }

  .loading-spinner {
    width: calc(4vh);
    height: calc(4vh);
    border: calc(0.3vh) solid rgba($electric-blue, 0.3);
    border-top: calc(0.3vh) solid $electric-blue;
    border-radius: 50%;
    animation: spin 1s linear infinite;

    &.small {
      width: calc(2vh);
      height: calc(2vh);
      border-width: calc(0.2vh);
    }
  }

  .history-list {
    padding: calc(2vh);

    .history-container {
      display: flex;
      flex-direction: column;
      gap: calc(1.5vh);
    }

    .game-item {
      background: linear-gradient(135deg, rgba($card-bg, 0.8), rgba($card-bg, 0.6));
      border: calc(0.5vh) solid rgba($electric-blue, 0.3);
      border-radius: calc(1vh);
      padding: calc(2vh);
      cursor: pointer;
      transition: all 2s ease;
      backdrop-filter: blur(calc(1vh));
      opacity: 0;
      transform: translateY(2vh);

      &.slide-up {
        animation: slideUp 0.8s cubic-bezier(0.25, 0.46, 0.45, 0.94) forwards;
      }

      &.slide-down {
        opacity: 1;
        transform: translateY(0);
        animation: slideDown 0.8s cubic-bezier(0.25, 0.46, 0.45, 0.94) forwards;
      }

      &:hover {
        border-color: rgba($electric-blue, 0.5);
        box-shadow: 0 0 calc(1vh) rgba($electric-blue, 0.2);
      }

      &.expanded {
        border-color: $electric-blue;
        box-shadow: 0 0 calc(1.5vh) rgba($electric-blue, 0.3);
      }

      &.result-win {
        position: relative;
        border: calc(0.5vh) solid rgba($synthwave-purple, 0.8);
        background: linear-gradient(135deg, rgba(0, 0, 0, 0.7), rgba($synthwave-purple, 0.15), rgba(0, 0, 0, 0.9));
        box-shadow:
          0 0 calc(1.5vh) rgba($synthwave-purple, 0.4),
          inset 0 calc(0.1vh) calc(0.5vh) rgba($synthwave-purple, 0.2);

        &::before {
          content: '';
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background: linear-gradient(135deg, rgba($synthwave-purple, 0.6), rgba(0, 0, 0, 0.9), rgba(0, 0, 0, 0.8));
          opacity: 0;
          transition: opacity 1s ease;
          border-radius: inherit;
          pointer-events: none;
          z-index: -1;
        }

        &:hover {
          border-color: rgba($synthwave-purple, 1);
          box-shadow:
            0 0 calc(2vh) rgba($synthwave-purple, 0.6),
            inset 0 calc(0.1vh) calc(0.8vh) rgba($synthwave-purple, 0.3);

          &::before {
            opacity: 1;
          }
        }
      }

      &.result-draw {
        position: relative;
        border: calc(0.5vh) solid rgba(100, 100, 120, 0.7);
        background: linear-gradient(135deg, rgba(0, 0, 0, 0.7), rgba(80, 80, 100, 0.75), rgba(0, 0, 0, 0.7));
        box-shadow:
          0 0 calc(1vh) rgba(100, 100, 120, 0.3),
          inset 0 calc(0.1vh) calc(0.3vh) rgba(120, 120, 140, 0.2);

        &::before {
          content: '';
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background: linear-gradient(135deg, rgba(100, 100, 120, 1), rgba(0, 0, 0, 0.9), rgba(0, 0, 0, 0.9));
          opacity: 0;
          transition: opacity 1s ease;
          border-radius: inherit;
          pointer-events: none;
          z-index: -1;
        }

        &:hover {
          border-color: rgba(120, 120, 140, 0.9);
          box-shadow:
            0 0 calc(1.5vh) rgba(120, 120, 140, 0.9),
            inset 0 calc(0.1vh) calc(0.5vh) rgba(120, 120, 140, 0.3);

          &::before {
            opacity: 1;
          }
        }
      }

      &.result-loss {
        position: relative;
        border: calc(0.5vh) solid rgba(180, 50, 70, 0.8);
        background: linear-gradient(135deg, rgba(0, 0, 0, 0.7), rgba(180, 50, 70, 0.55), rgba(0, 0, 0, 0.7));
        box-shadow:
          0 0 calc(1.5vh) rgba(180, 50, 70, 0.4),
          inset 0 calc(0.1vh) calc(0.5vh) rgba(180, 50, 70, 0.2);

        &::before {
          content: '';
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background: linear-gradient(135deg, rgba(180, 50, 70, 0.95), rgba(0, 0, 0, 1), rgba(0, 0, 0, 0.95));
          opacity: 0;
          transition: opacity 1s ease;
          border-radius: inherit;
          pointer-events: none;
          z-index: -1;
        }

        &:hover {
          border-color: rgba(200, 60, 80, 1);
          box-shadow:
            0 0 calc(2vh) rgba(180, 50, 70, 0.6),
            inset 0 calc(0.1vh) calc(0.8vh) rgba(180, 50, 70, 0.3);

          &::before {
            opacity: 1;
          }
        }
      }

      .game-header {
        display: flex;
        align-items: center;
        gap: calc(2vh);

        .status-indicator {
          flex: 0 0 auto;
          display: flex;
          align-items: center;
          justify-content: center;
          font-family: $font-accent;
          font-style: italic;
          font-size: calc(3.2vh);
          font-weight: 900;
          text-align: center;
          min-width: calc(12vh);
          width: calc(12vh);

          &.status-win {
            color: $synthwave-purple;
            text-shadow: 0 0 calc(0.5vh) rgba($synthwave-purple, 0.8);
          }

          &.status-draw {
            color: rgb(160, 160, 180);
            text-shadow: 0 0 calc(0.5vh) rgba(120, 120, 140, 0.8);
          }

          &.status-loss {
            color: rgb(220, 80, 100);
            text-shadow: 0 0 calc(0.5vh) rgba(200, 60, 80, 0.8);
          }
        }

        .game-name {
          flex: 1;
          font-family: $font-primary;
          font-size: calc(1.8vh);
          font-weight: $font-weight-bold;
          color: $electric-blue;
          text-shadow: 0 0 calc(0.3vh) rgba($electric-blue, 0.3);
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        .problem-level {
          flex: 0 0 auto;
          padding: calc(0.5vh) calc(1.2vh);
          border-radius: calc(0.6vh);
          font-size: calc(1.8vh);
          font-weight: $font-weight-bold;
          font-family: $font-primary;

          &.easy {
            background: rgba(76, 175, 80, 0.25);
            color: #4caf50;
            border: calc(0.15vh) solid rgba(76, 175, 80, 0.4);
            box-shadow: 0 0 calc(0.3vh) rgba(76, 175, 80, 0.2);
          }

          &.medium {
            background: rgba(255, 152, 0, 0.25);
            color: #ff9800;
            border: calc(0.15vh) solid rgba(255, 152, 0, 0.4);
            box-shadow: 0 0 calc(0.3vh) rgba(255, 152, 0, 0.2);
          }

          &.hard {
            background: rgba(244, 67, 54, 0.25);
            color: #f44336;
            border: calc(0.15vh) solid rgba(244, 67, 54, 0.4);
            box-shadow: 0 0 calc(0.3vh) rgba(244, 67, 54, 0.2);
          }
        }

        .max-players {
          flex: 0 0 auto;
          font-family: $font-primary;
          font-size: calc(1.9vh);
          font-weight: $font-weight-bold;
          color: $electric-blue;
          background: rgba($electric-blue, 0.15);
          padding: calc(0.4vh) calc(1vh);
          border-radius: calc(0.5vh);
          border: calc(0.1vh) solid rgba($electric-blue, 0.3);
          box-shadow: 0 0 calc(0.3vh) rgba($electric-blue, 0.2);
        }

        .problem-name {
          flex: 1.5;
          font-family: $font-primary;
          font-size: calc(1.9vh);
          color: $text-primary;
          font-weight: $font-weight-medium;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        .game-time {
          flex: 0 0 auto;
          font-family: $font-primary;
          font-size: calc(1.4vh);
          color: rgba($text-primary, 0.6);
          font-weight: $font-weight-medium;
          min-width: calc(8vh);
          text-align: center;
        }

        .view-result-btn {
          flex: 0 0 auto;
          display: flex;
          align-items: center;
          gap: calc(0.8vh);
          padding: calc(1vh) calc(1.5vh);
          background: rgba($electric-blue, 0.1);
          color: $electric-blue;
          border: calc(0.1vh) solid rgba($electric-blue, 0.3);
          border-radius: calc(0.5vh);
          font-family: $font-primary;
          font-size: calc(1.4vh);
          font-weight: $font-weight-medium;
          transition: all 0.3s ease;

          &:hover {
            background: rgba($electric-blue, 0.2);
            border-color: rgba($electric-blue, 0.5);
            box-shadow: 0 0 calc(0.5vh) rgba($electric-blue, 0.3);
          }

          i {
            font-size: calc(1.2vh);
          }
        }
      }
    }
  }
}

// 모달 스타일
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.8);
  backdrop-filter: blur(calc(0.5vh));
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: $z-overlay;
  animation: fadeIn 0.3s ease-out;

  .modal-container {
    background: linear-gradient(135deg, rgba($card-bg, 0.95), rgba($card-bg, 0.9));
    border: calc(0.2vh) solid rgba($electric-blue, 0.4);
    border-radius: calc(1.5vh);
    width: 90vw;
    height: 90vh;
    max-width: calc(160vh);
    max-height: calc(90vh);
    position: relative;
    box-shadow: 0 0 calc(3vh) rgba($electric-blue, 0.3);
    overflow: hidden;
    animation: modalSlideIn 0.4s ease-out;

    .modal-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: calc(2vh) calc(2.5vh);
      border-bottom: calc(0.1vh) solid rgba($electric-blue, 0.2);
      background: rgba($electric-blue, 0.05);

      h3 {
        font-family: $font-primary;
        font-size: calc(2.4vh);
        font-weight: $font-weight-bold;
        color: $electric-blue;
        margin: 0;
        text-shadow: 0 0 calc(0.3vh) rgba($electric-blue, 0.3);
      }

      .close-btn {
        display: flex;
        align-items: center;
        justify-content: center;
        width: calc(3.5vh);
        height: calc(3.5vh);
        background: rgba($electric-blue, 0.1);
        border: calc(0.1vh) solid rgba($electric-blue, 0.3);
        border-radius: calc(0.8vh);
        color: $electric-blue;
        cursor: pointer;
        transition: all 0.4s ease;
        font-size: calc(1.6vh);

        &:hover {
          background: rgba($electric-blue, 0.2);
          border-color: rgba($electric-blue, 0.5);
          box-shadow: 0 0 calc(0.8vh) rgba($electric-blue, 0.3);
        }
      }
    }

    .modal-body {
      height: calc(100% - 7vh);
      overflow: hidden;

      // GameResultPage가 모달 내에서 적절히 표시되도록 조정
      :deep(.game-result-page) {
        width: 100%;
        height: 100%;
        padding: calc(3vh) calc(3vh);
        overflow-y: auto;
      }
    }
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes modalSlideIn {
  from {
    opacity: 0;
    transform: scale(0.9) translateY(-2vh);
  }
  to {
    opacity: 1;
    transform: scale(1) translateY(0);
  }
}

// 슬라이드 업 애니메이션 (PlayerSlot과 동일)
@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(4vh);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 슬라이드 다운 애니메이션 (퇴장용)
@keyframes slideDown {
  from {
    opacity: 1;
    transform: translateY(0);
  }
  to {
    opacity: 0;
    transform: translateY(4vh);
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
