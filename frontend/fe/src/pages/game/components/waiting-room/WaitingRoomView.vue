<template>
  <div class="waiting-room-view">
    <transition name="slide-from-top" appear>
      <RoomHeader v-if="!isExiting" :room-data="waitingRoomStore.roomData" />
    </transition>

    <div class="room-content">
      <transition name="slide-from-left" appear>
        <PlayerSlotGrid
          v-if="!isExiting"
          :players="waitingRoomStore.players"
          :max-players="waitingRoomStore.roomData.maxPlayers"
          :is-owner="waitingRoomStore.currentUser.isHost"
          :current-user-id="currentUserId"
          @kick-player="handleKickPlayer"
        />
      </transition>

      <div class="room-sidebar">
        <transition name="slide-from-right" appear>
          <RoomSettingsPanel v-if="!isExiting" :room-data="waitingRoomStore.roomData" />
        </transition>

        <transition name="slide-from-right-delayed" appear>
          <ChatSystem v-if="!isExiting" chat-type="ingame" :room-id="roomId" class="room-chat" />
        </transition>
      </div>
    </div>

    <transition name="slide-from-bottom" appear>
      <RoomControlButtons v-if="!isExiting" @start-game-click="handleStartGameClick" />
    </transition>
  </div>
</template>

<script setup>
import { computed, watch, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';
import { useWebSocketStore } from '@/stores/websocket/websocket.js';
import { useAuthStore } from '@/stores/auth/auth.js';
import { useRoom } from '@/composables/useRoom.js';
import { WS_ENDPOINTS } from '@/websocket/constants/endpoints.js';
import RoomHeader from './header/RoomHeader.vue';
import RoomControlButtons from './controls/RoomControlButtons.vue';
import PlayerSlotGrid from './player-slots/PlayerSlotGrid.vue';
import RoomSettingsPanel from './room-settings/RoomSettingsPanel.vue';
import ChatSystem from '@/pages/common/chat/ChatSystem.vue';

const route = useRoute();
const router = useRouter();
const waitingRoomStore = useInGameStore();
const webSocketStore = useWebSocketStore();
const authStore = useAuthStore();
const { startGame } = useRoom();

const roomId = computed(() => route.query.roomId);
const isExiting = ref(false);

// 현재 사용자 ID
const currentUserId = computed(() => authStore.currentUser?.id);

let initialized = false;

// 게임 시작 버튼 클릭 핸들러
const handleStartGameClick = () => {
  // 퇴장 애니메이션 시작
  startExitAnimation();
};

const startExitAnimation = () => {
  isExiting.value = true;

  // 가장 긴 애니메이션 시간(0.5s) 완료 후 실제 게임 시작 API 호출
  setTimeout(async () => {
    try {
      // API 호출만 하고 gameStatus는 아직 변경하지 않음
      await startGame(waitingRoomStore.currentRoomId);
    } catch (error) {
      console.error('게임 시작 실패:', error);
      // 실패 시 애니메이션을 되돌림
      isExiting.value = false;
    }
  }, 700);
};

// 강퇴 처리 핸들러
const handleKickPlayer = async playerId => {
  try {
    const roomId = waitingRoomStore.currentRoomId;
    const endpoint = WS_ENDPOINTS.ROOM.KICK(roomId);

    const webSocketService = webSocketStore.getService();
    webSocketService.sendMessage(endpoint, { targetMemberId: playerId });
  } catch (error) {
    console.error('강퇴 요청 실패:', error);
  }
};

watch(
  () => ({
    connected: webSocketStore.isConnected,
    hydrated: waitingRoomStore.hydrated,
    roomId: roomId.value,
  }),
  async ({ connected, hydrated, roomId }) => {
    if (initialized) return;
    if (connected && hydrated && roomId) {
      try {
        await waitingRoomStore.initializeRoom(roomId);
        initialized = true;
      } catch (error) {
        console.error('대기실 초기화 실패:', error);
        router.push({ name: 'main' });
      }
    }
  },
  { immediate: true, deep: true },
);
</script>

<style lang="scss" scoped>
.waiting-room-view {
  height: 100vh;
  display: grid;
  grid-template-rows: auto 1fr auto;
  width: 100%;
  max-width: calc(144vh);
  margin: 0 auto;
  padding: calc(2vh) calc(3vh);
  gap: calc(2vh);
  overflow: hidden;
  position: relative;

  // 배경 그라디언트
  &::before {
    content: '';
    position: absolute;
    inset: 0;
    z-index: -2;
  }

  // 그리드 패턴
  &::after {
    content: '';
    position: absolute;
    inset: 0;
    background-image:
      linear-gradient(rgba($electric-blue, 0.05) 1px, transparent 1px),
      linear-gradient(90deg, rgba($electric-blue, 0.05) 1px, transparent 1px);
    background-size: 50px 50px;
    z-index: -1;
    opacity: 0.5;
  }
}

.room-content {
  display: grid;
  grid-template-columns: 1fr calc(40vh);
  gap: calc(3vh);
  align-items: stretch;
  height: 100%;
  min-height: 0;
}

.room-sidebar {
  display: flex;
  flex-direction: column;
  gap: calc(2vh);
  height: 100%;
  min-height: 0;
  width: 100%;
}

.room-chat {
  flex: 1;
  min-height: 0;
}

/* 위에서 등장 및 퇴장 */
.slide-from-top-enter-active,
.slide-from-top-leave-active {
  transition: all 0.2s ease-out;
}
.slide-from-top-enter-from,
.slide-from-top-leave-to {
  opacity: 0;
  transform: translateY(-30px);
}
.slide-from-top-enter-to,
.slide-from-top-leave-from {
  opacity: 1;
  transform: translateY(0);
}

/* 아래에서 등장 및 퇴장 */
.slide-from-bottom-enter-active,
.slide-from-bottom-leave-active {
  transition: all 0.2s ease-out;
}
.slide-from-bottom-enter-from,
.slide-from-bottom-leave-to {
  opacity: 0;
  transform: translateY(30px);
}
.slide-from-bottom-enter-to,
.slide-from-bottom-leave-from {
  opacity: 1;
  transform: translateY(0);
}

/* 좌측에서 등장 및 퇴장 */
.slide-from-left-enter-active,
.slide-from-left-leave-active {
  transition: all 0.3s ease-out;
}
.slide-from-left-enter-from,
.slide-from-left-leave-to {
  opacity: 0;
  transform: translateX(-60px);
}
.slide-from-left-enter-to,
.slide-from-left-leave-from {
  opacity: 1;
  transform: translateX(0);
}

/* 우측에서 등장 및 퇴장 */
.slide-from-right-enter-active,
.slide-from-right-leave-active {
  transition: all 0.4s ease-out;
}
.slide-from-right-enter-from,
.slide-from-right-leave-to {
  opacity: 0;
  transform: translateX(60px);
}
.slide-from-right-enter-to,
.slide-from-right-leave-from {
  opacity: 1;
  transform: translateX(0);
}

/* 우측에서 등장 지연 및 퇴장 */
.slide-from-right-delayed-enter-active,
.slide-from-right-delayed-leave-active {
  transition: all 0.5s ease-out;
}
.slide-from-right-delayed-enter-from,
.slide-from-right-delayed-leave-to {
  opacity: 0;
  transform: translateX(60px);
}
.slide-from-right-delayed-enter-to,
.slide-from-right-delayed-leave-from {
  opacity: 1;
  transform: translateX(0);
}
</style>
