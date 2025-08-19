<!-- src/pages/game/components/in-game/InGamePage.vue -->
<template>
  <div class="in-game-page">
    <!-- 게임 시작 로딩 인디케이터 -->
    <div v-if="showGameLoadingIndicator || !componentsMounted" class="game-loading-overlay">
      <div class="game-loading-content">
        <div class="loading-spinner"></div>
        <div class="loading-text" v-if="showGameLoadingIndicator">게임을 시작합니다..</div>
        <div class="loading-text" v-else>화면 공유를 준비하고 있습니다..</div>
      </div>
    </div>

    <div
      v-else
      class="in-game-container"
      :class="{ 'virtual-keyboard-active': isVirtualKeyboardActive, 'components-mounted': componentsMounted }"
    >
      <!-- 상대방 현황 영역 -->
      <transition name="slide-from-top" appear>
        <div v-if="componentsMounted" class="player-status-area">
          <PlayerStatusBar v-if="isWebRTCReady" @item-dropped="handleItemDropped" />
          <div v-else class="loading-placeholder">WebRTC 초기화 중...</div>
        </div>
      </transition>

      <!-- 문제보기 영역 -->
      <transition name="slide-from-left" appear>
        <div v-if="componentsMounted" class="problem-view-area" @drop="handleSpellDrop" @dragover.prevent @dragenter.prevent>
          <ProblemPanel />
        </div>
      </transition>

      <!-- 코드 에디터 영역 -->
      <transition name="slide-from-bottom" appear>
        <div v-if="componentsMounted" class="code-editor-area" @drop="handleSpellDrop" @dragover.prevent @dragenter.prevent>
          <CodeEditor @editor-ready="handleEditorReady" @surrender-requested="handleSurrenderRequested" />
        </div>
      </transition>

      <!-- 아이템 및 스펠 영역 -->
      <transition name="slide-from-right" appear>
        <div v-if="componentsMounted" class="items-spells-area">
          <GameToolsPanel
            @item-drag-start="handleItemDragStart"
            @item-drag-end="handleItemDragEnd"
            @spell-drag-start="handleSpellDragStart"
            @spell-drag-end="handleSpellDragEnd"
          />
        </div>
      </transition>

      <!-- 가상 키보드 영역 (아이템 효과 시에만 표시) -->
      <div v-if="isVirtualKeyboardActive" class="virtual-keyboard-area">
        <VirtualKeyboard
          v-model="currentCodeInput"
          :title="'가상 키보드 (아이템 효과)'"
          :layout="'qwerty'"
          :show-input="false"
          :show-close-button="false"
          :show-layout-toggle="false"
          @key-press="handleVirtualKeyPress"
        />
      </div>
    </div>

    <!-- 항복 확인 모달 -->
    <SurrenderConfirmModal v-if="showSurrenderModal" @cancel="showSurrenderModal = false" @confirm="confirmSurrender" />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue';

import PlayerStatusBar from './components/player-status/PlayerStatusBar.vue';
import ProblemPanel from './components/problem-view/ProblemPanel.vue';
import CodeEditor from './components/code-editor/CodeEditor.vue';
import GameToolsPanel from './components/game-tools/GameToolsPanel.vue';
import VirtualKeyboard from '@/game/items/virtual-keyboard/VirtualKeyboard.vue';
import SurrenderConfirmModal from './components/code-editor/SurrenderConfirmModal.vue';
import { useWebRTC } from '@/composables/useWebRTC.js';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';
import { useGame } from '@/composables/useGame.js';
import { useWebSocketStore } from '@/stores/websocket/websocket.js';
import { useWebRTCStore } from '@/stores/webrtc/webrtc.js';
import { useBattle } from '@/composables/useBattle.js';
import { useItem } from '@/game/services/useItem.js';
import { useSpell } from '@/game/services/useSpell.js';
// 컴포넌트 마운트 상태
const showGameLoadingIndicator = ref(true);
const componentsMounted = ref(false);

// WebRTC 준비 상태
const isWebRTCReady = ref(false);
// WebRTC 컴포저블 사용

const { initializeForGame, cleanupForGame, isInitialized, screenShareReady } = useWebRTC();
// InGameStore 사용

const inGameStore = useInGameStore();
const webSocketStore = useWebSocketStore();
const webrtcStore = useWebRTCStore();
const { manualSync } = useGame();
const { subscribeToBattleEvents, unsubscribeFromBattleEvents, subscribeToBattleEndEvents, unsubscribeFromBattleEndEvents, sendSurrender } =
  useBattle();
const { startItemEffectMonitoring } = useItem();
const { startSpellEffectMonitoring, useSpellToMe } = useSpell();

// 항복 모달 상태
const showSurrenderModal = ref(false);

// 가상 키보드 관련 상태
const isVirtualKeyboardActive = ref(false);
const currentCodeInput = ref('');

// 가상 키보드 활성화 상태 모니터링
watch(
  () => window.virtualKeyboardActive,
  newValue => {
    isVirtualKeyboardActive.value = newValue;
  },
  { immediate: true },
);

// 전역 변수 폴링 (더 안정적인 감지를 위해)
let virtualKeyboardCheckInterval = null;

const startVirtualKeyboardMonitoring = () => {
  virtualKeyboardCheckInterval = setInterval(() => {
    const shouldBeActive = Boolean(window.virtualKeyboardActive);
    if (isVirtualKeyboardActive.value !== shouldBeActive) {
      isVirtualKeyboardActive.value = shouldBeActive;
    }
  }, 100);
};

const stopVirtualKeyboardMonitoring = () => {
  if (virtualKeyboardCheckInterval) {
    clearInterval(virtualKeyboardCheckInterval);
    virtualKeyboardCheckInterval = null;
  }
};

// Monaco Editor 인스턴스 참조
let monacoEditorInstance = null;

// 타이핑 감지를 위한 타이머
let typingTimeout = null;

// Monaco Editor 준비 완료 처리 및 타이핑 이벤트 설정
const handleEditorReady = editorInstance => {
  monacoEditorInstance = editorInstance;

  // 타이핑 이벤트 감지
  monacoEditorInstance.onDidChangeModelContent(() => {
    // 타이핑 시작 알림
    webrtcStore.sendTypingStatus(true);

    // 기존 타이머 클리어
    clearTimeout(typingTimeout);

    // 1.5초 후 타이핑 중지 상태로 변경 (더 빠른 반응성)
    typingTimeout = setTimeout(() => {
      webrtcStore.sendTypingStatus(false);
    }, 1500);
  });
};

// 가상 키보드 키 입력 처리
const handleVirtualKeyPress = key => {
  // Monaco Editor 인스턴스가 있으면 직접 조작
  if (monacoEditorInstance) {
    const position = monacoEditorInstance.getPosition();

    switch (key.code) {
      case 'Backspace':
        if (position.column > 1 || position.lineNumber > 1) {
          const range = new window.monaco.Range(position.lineNumber, position.column - 1, position.lineNumber, position.column);
          monacoEditorInstance.executeEdits('virtual-keyboard', [
            {
              range: range,
              text: '',
            },
          ]);
        }
        break;

      case 'Enter':
        monacoEditorInstance.executeEdits('virtual-keyboard', [
          {
            range: new window.monaco.Range(position.lineNumber, position.column, position.lineNumber, position.column),
            text: '\n',
          },
        ]);
        break;

      case 'Tab':
        monacoEditorInstance.executeEdits('virtual-keyboard', [
          {
            range: new window.monaco.Range(position.lineNumber, position.column, position.lineNumber, position.column),
            text: '    ',
          },
        ]);
        break;

      case 'Space':
        monacoEditorInstance.executeEdits('virtual-keyboard', [
          {
            range: new window.monaco.Range(position.lineNumber, position.column, position.lineNumber, position.column),
            text: ' ',
          },
        ]);
        break;

      default:
        if (key.type === 'normal') {
          monacoEditorInstance.executeEdits('virtual-keyboard', [
            {
              range: new window.monaco.Range(position.lineNumber, position.column, position.lineNumber, position.column),
              text: key.display,
            },
          ]);
        }
        break;
    }
  }
};

// Drag and Drop 핸들러들
const handleItemDragStart = () => {};

const handleItemDragEnd = () => {};

const handleSpellDragStart = () => {};

const handleSpellDragEnd = () => {};

const handleItemDropped = data => {
  const { from, to, itemId, targetPlayer, success, error } = data;

  if (success) {
    // 성공 시 UI 피드백 (예: 토스트 알림, 애니메이션 등)
    // TODO: 성공 피드백 UI 구현
  } else {
    console.error('[InGamePage] 아이템 사용 실패:', error);

    // 실패 시 UI 피드백 (예: 에러 토스트 알림)
    // TODO: 실패 피드백 UI 구현
  }
};

// 스펠 드롭 핸들러 (나에게 사용하는 스펠)
const handleSpellDrop = async event => {
  event.preventDefault();

  try {
    const spellData = JSON.parse(event.dataTransfer.getData('application/json'));

    if (spellData.type === 'spell') {
      // 스펠을 나에게 사용
      await useSpellToMe(spellData.spellId);
    }
  } catch (error) {
    console.error('[InGamePage] 스펠 드롭 처리 실패:', error);
  }
};

// 항복 요청 핸들러
const handleSurrenderRequested = () => {
  // 이미 항복한 상태라면 모달을 띄우지 않음
  if (inGameStore.banPickBuyResults.surrender) {
    return;
  }
  showSurrenderModal.value = true;
};

// 항복 확인 핸들러
const confirmSurrender = () => {
  showSurrenderModal.value = false;
  sendSurrender();
};

// 컴포넌트 마운트 시 3초 로딩 후 WebRTC 초기화 대기
onMounted(async () => {
  setTimeout(() => {
    showGameLoadingIndicator.value = false;
  }, 3000);

  // 전역 BGM 비활성화
  const { useBgmStore } = await import('@/stores/bgm.js');
  const bgmStore = useBgmStore();
  bgmStore.disableGlobalBgm();
});

// WebRTC 화면 공유 준비 완료 시 컴포넌트 등장
watch(
  screenShareReady,
  ready => {
    if (ready) {
      // WebRTC 화면 공유가 준비된 후 컴포넌트 애니메이션 시작
      setTimeout(() => {
        componentsMounted.value = true;
      }, 100);
    }
  },
  { immediate: true },
);

// 웹소켓 연결 및 hydration 완료 시 초기화 실행
watch(
  () => webSocketStore.isConnected && inGameStore.hydrated,
  async ready => {
    if (ready) {
      try {
        await subscribeToBattleEvents();
        await subscribeToBattleEndEvents();
        await initializeForGame();
        isWebRTCReady.value = isInitialized.value;
        await manualSync(inGameStore.currentRoomId);

        // 아이템 효과 모니터링 시작
        startItemEffectMonitoring();

        // 스펠 효과 모니터링 시작
        startSpellEffectMonitoring();

        // 가상 키보드 모니터링 시작
        startVirtualKeyboardMonitoring();
      } catch (error) {
        console.error('[BAN-PICK-BUY] 게임 동기화 실패:', error);
      }
    }
  },
  { immediate: true },
);

onUnmounted(async () => {
  // 타이핑 타이머 정리
  clearTimeout(typingTimeout);

  await cleanupForGame();
  unsubscribeFromBattleEndEvents();
  unsubscribeFromBattleEvents();
  stopVirtualKeyboardMonitoring();
  isWebRTCReady.value = false;

  // 가상 키보드 정리
  if (window.cleanupVirtualKeyboard) {
    window.cleanupVirtualKeyboard();
  }

  // 컴포넌트 언마운트 시 전역 BGM 다시 활성화
  const { useBgmStore } = await import('@/stores/bgm.js');
  const bgmStore = useBgmStore();
  bgmStore.enableGlobalBgm();
});
</script>

<style lang="scss" scoped>
.in-game-page {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  background:
    linear-gradient(rgba($primary-bg, 0.5), rgba($primary-bg, 0.5)),
    url('@/pages/game/assets/waiting-room-wallpaper.jpg') no-repeat fixed center center;
  background-size: cover;
}

// 게임 시작 로딩 인디케이터
.game-loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.game-loading-content {
  text-align: center;
  color: $text-primary;
}

.loading-spinner {
  width: 4vh;
  height: 4vh;
  border: 0.4vh solid rgba($electric-blue, 0.8);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 2vh auto;
}

.loading-text {
  font-family: $font-primary;
  font-size: 3vh;
  font-weight: bold;
  color: $electric-blue;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.in-game-container {
  display: grid;
  grid-template-areas:
    'player-status player-status player-status'
    'problem-view code-editor items-spells';
  grid-template-rows: 25vh 1fr; // 상단 고정, 하단은 남은 공간 전체 사용
  grid-template-columns: 5fr 6fr 1fr; // 아이템/스펠 영역을 고정 너비로 설정
  gap: 1vh;
  height: 100vh;
  padding: 1vh;
  box-sizing: border-box;

  // 가상 키보드 활성화 시 레이아웃 변경
  &.virtual-keyboard-active {
    grid-template-areas:
      'player-status player-status player-status'
      'problem-view code-editor items-spells'
      'virtual-keyboard virtual-keyboard virtual-keyboard';
    grid-template-rows: 25vh 1fr 35vh; // 가상 키보드 영역 추가
  }
}

// Vue transition 애니메이션
.slide-from-top-enter-active,
.slide-from-top-leave-active {
  transition: all 0.8s ease-out;
}
.slide-from-top-enter-from,
.slide-from-top-leave-to {
  opacity: 0;
  transform: translateY(-100%);
}
.slide-from-top-enter-to,
.slide-from-top-leave-from {
  opacity: 1;
  transform: translateY(0);
}

.slide-from-left-enter-active,
.slide-from-left-leave-active {
  transition: all 0.8s ease-out;
}
.slide-from-left-enter-from,
.slide-from-left-leave-to {
  opacity: 0;
  transform: translateX(-100%);
}
.slide-from-left-enter-to,
.slide-from-left-leave-from {
  opacity: 1;
  transform: translateX(0);
}

.slide-from-bottom-enter-active,
.slide-from-bottom-leave-active {
  transition: all 0.8s ease-out;
}
.slide-from-bottom-enter-from,
.slide-from-bottom-leave-to {
  opacity: 0;
  transform: translateY(100%);
}
.slide-from-bottom-enter-to,
.slide-from-bottom-leave-from {
  opacity: 1;
  transform: translateY(0);
}

.slide-from-right-enter-active,
.slide-from-right-leave-active {
  transition: all 0.8s ease-out;
}
.slide-from-right-enter-from,
.slide-from-right-leave-to {
  opacity: 0;
  transform: translateX(100%);
}
.slide-from-right-enter-to,
.slide-from-right-leave-from {
  opacity: 1;
  transform: translateX(0);
}

.player-status-area {
  grid-area: player-status;
  min-height: 0;
}

.problem-view-area {
  grid-area: problem-view;
  min-height: 0;
  min-width: 30vh; // 최소 너비 보장
  overflow: hidden;
}

.code-editor-area {
  grid-area: code-editor;
  min-height: 0;
  min-width: 40vh; // 최소 너비 보장
  overflow: hidden;
}

.items-spells-area {
  grid-area: items-spells;
  min-height: 0;
  width: 16vh; // 최소 너비 보장
  display: flex;
  flex-direction: column; // 수직 배치
}

.virtual-keyboard-area {
  grid-area: virtual-keyboard;
  min-height: 0;
  background: rgba($primary-bg, 0.9);
  border: 0.2vh solid $electric-blue;
  border-radius: 1vh;
  padding: 1vh;
  overflow-y: auto;
}

.loading-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  background: rgba($primary-bg, 0.3);
  border: 0.2vh solid $electric-blue;
  border-radius: 1vh;
  color: $text-primary;
  font-family: $font-primary;
  font-size: 2vh;
}
</style>
