<template>
  <div class="main-page">
    <!-- Header -->
    <MainHeader />

    <!-- 조건부 콘텐츠 렌더링 -->
    <HomeContent v-if="currentView === 'home'" key="home" @create-battle="openGameCreationModal" @explore-rooms="switchToRooms" />

    <RoomListContent
      v-else-if="currentView === 'rooms'"
      key="rooms"
      ref="roomListContent"
      :view-mode="roomViewMode"
      :has-update-notification="hasRoomListUpdate"
      @create-room="openGameCreationModal"
      @join-room="handleJoinRoom"
      @clear-update-notification="clearRoomListUpdate"
    />

    <MyPageContent v-else-if="currentView === 'mypage'" key="mypage" />

    <!-- 게임 생성 모달 -->
    <GameCreationModal :isVisible="isGameCreationModalVisible" @close="closeGameCreationModal" @create="handleGameCreation" />
  </div>
</template>

<script setup>
import { ref, provide, onMounted, onUnmounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useWebSocketStore } from '@/stores/websocket/websocket.js';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';
import { WS_ENDPOINTS } from '@/websocket/constants/endpoints.js';
import MainHeader from '@/pages/common/header/MainHeader.vue';
import HomeContent from './components/content/HomeContent.vue';
import RoomListContent from './components/content/RoomListContent.vue';
import MyPageContent from './components/content/MyPageContent.vue';
import GameCreationModal from './components/game-creation/GameCreationModal.vue';
import { useRoom } from '@/composables/useRoom.js';

// 라우터 및 스토어
const route = useRoute();
const router = useRouter();
const webSocketStore = useWebSocketStore();
const waitingRoomStore = useInGameStore();

// useRoom composable에서 reConnectRoom 함수 추출
const { reConnectRoom } = useRoom();

// 화면 상태 관리 (쿼리 파라미터 또는 기본값)
const currentView = ref(route.query.view || 'home'); // 'home' | 'rooms' | 'mypage'
const roomViewMode = ref('card'); // 'card' | 'list'

// 모달 상태 관리
const isGameCreationModalVisible = ref(false);

// 방 목록 갱신 알림 상태
const hasRoomListUpdate = ref(false);
let roomRefreshSubscriptionId = null;

// 컴포넌트 ref
const roomListContent = ref(null);

// 화면 전환 메서드
const switchToRooms = () => {
  currentView.value = 'rooms';
  // URL에 view=rooms 쿼리 파라미터 추가
  router.replace({ query: { view: 'rooms' } });
};

const switchToHome = () => {
  currentView.value = 'home';
  // URL에서 view 쿼리 파라미터 제거
  router.replace({ query: {} });
};

const switchToMyPage = () => {
  currentView.value = 'mypage';
  // URL에 view=mypage 쿼리 파라미터 추가
  router.replace({ query: { view: 'mypage' } });
};

// 모달 관리 메서드
const openGameCreationModal = () => {
  isGameCreationModalVisible.value = true;
};

const closeGameCreationModal = () => {
  isGameCreationModalVisible.value = false;
};

// 이벤트 핸들러
const handleGameCreation = gameData => {
  closeGameCreationModal();

  // 방 생성 후 방 리스트 새로고침
  if (roomListContent.value && currentView.value === 'rooms') {
    roomListContent.value.refreshRoomList();
  }
};

const handleJoinRoom = roomName => {
  // TODO: 방 참가 로직 구현
};

// 뷰 모드 전환
const toggleViewMode = () => {
  roomViewMode.value = roomViewMode.value === 'card' ? 'list' : 'card';
};

provide('resetToHome', switchToHome);
provide('switchToMyPage', switchToMyPage);

// 방 목록 갱신 알림 핸들러
const handleRoomRefreshMessage = message => {
  // REFRESHED 이벤트인지 확인
  if (message.type === 'EVENT' && message.code === 200 && message.data?.event === 'REFRESHED') {
    hasRoomListUpdate.value = true;
  }
};

// 방 목록 갱신 알림 해제
const clearRoomListUpdate = () => {
  hasRoomListUpdate.value = false;
};

// WaitingRoomStore hydration 완료 후 방 재접속 로직
watch(
  () => webSocketStore.isConnected && waitingRoomStore.hydrated,
  async ready => {
    if (ready && waitingRoomStore.currentRoomId) {
      try {
        const result = await reConnectRoom(waitingRoomStore.currentRoomId);

        // 재접속 실패 시 처리
        if (!result.success) {
          // 방 상태가 유효하지 않은 경우 저장된 방 정보 초기화
          if (result.error === 'ROOM_CONFLICT' || result.error === 'SERVER_ERROR') {
            waitingRoomStore.reset();
          }
        }
      } catch (error) {
        // 예상치 못한 에러가 발생한 경우에만 로그 출력
        waitingRoomStore.reset();
        // waitingRoomStore.forceCleanLocalStorage();
      }
    }
  },
  { immediate: true },
);

// 컴포넌트 마운트 시 구독 초기화 (WebSocket 연결은 전역에서 관리)
onMounted(async () => {
  try {
    // WebSocket이 연결되어 있으면 방 목록 갱신 알림 토픽 구독
    if (webSocketStore.isConnected) {
      const webSocketService = webSocketStore.getService();
      roomRefreshSubscriptionId = webSocketService.subscribe(WS_ENDPOINTS.ROOM.REFRESH_TOPIC, handleRoomRefreshMessage);
    }
  } catch (error) {
    console.error('구독 초기화 실패:', error);
    // TODO: 사용자에게 연결 실패 알림 표시
  }
});

// 컴포넌트 언마운트 시 구독 해제
onUnmounted(() => {
  if (roomRefreshSubscriptionId && webSocketStore.isConnected) {
    try {
      const webSocketService = webSocketStore.getService();
      webSocketService.unsubscribe(roomRefreshSubscriptionId);
    } catch (error) {
      console.error('구독 해제 실패:', error);
    }
  }
});

// 전역으로 노출 (개발자 도구에서 테스트용)
window.switchToRooms = switchToRooms;
window.switchToHome = switchToHome;
window.switchToMyPage = switchToMyPage;
window.toggleViewMode = toggleViewMode;
</script>

<style lang="scss" scoped>
.main-page {
  background:
    linear-gradient(rgba($primary-bg, 0.5), rgba($primary-bg, 0.5)),
    url('@/pages/main/assets/images/main-wallpaper-2.jpg') no-repeat center center;
  background-size: cover;
}
</style>
