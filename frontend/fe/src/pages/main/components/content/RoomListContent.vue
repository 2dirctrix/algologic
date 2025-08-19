<template>
  <main class="room-list-content">
    <!-- 방 리스트 헤더 -->
    <Transition name="slide-from-top" appear>
      <section class="header-section">
        <RoomListHeader
          :has-update-notification="hasUpdateNotification"
          @create-room="$emit('create-room')"
          @filter-change="handleFilterChange"
          @search="handleSearch"
          @refresh="handleRefresh"
          @clear-update-notification="$emit('clear-update-notification')"
        />
      </section>
    </Transition>

    <!-- 페이지 인디케이터 및 뷰 모드 토글 -->
    <section class="pagination-section" v-if="totalPages >= 1">
      <div class="pagination-container">
        <PageIndicator :current-page="currentPage" :total-pages="totalPages" @page-change="handlePageChange" />
        <ViewModeToggle :current-mode="viewMode" @toggle="handleViewModeChange" />
      </div>
    </section>

    <!-- 방 그리드 (아래에서 등장) -->
    <Transition name="slide-from-bottom" appear>
      <section class="grid-section">
        <div v-if="isLoading" class="loading-indicator">로딩 중...</div>
        <RoomGrid v-else :view-mode="viewMode" :rooms="filteredRooms" @join-room="handleJoinRoom" />
      </section>
    </Transition>
  </main>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import RoomListHeader from '../room-list/components/header/RoomListHeader.vue';
import RoomGrid from '../room-list/components/room-cards/RoomGrid.vue';
import PageIndicator from '../room-list/components/shared/PageIndicator.vue';
import ViewModeToggle from '../room-list/components/shared/ViewModeToggle.vue';
import { useRoom } from '@/composables/useRoom.js';
import { useWebSocketStore } from '@/stores/websocket/websocket.js';
import { useAlertStore } from '@/stores/alertStore.js';
import { getRoomJoinErrorMessage } from '@/constants/errorMessages.js';

const props = defineProps({
  initialViewMode: {
    type: String,
    default: 'card',
    validator(value) {
      return ['card', 'list'].includes(value);
    },
  },
  hasUpdateNotification: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['create-room', 'join-room', 'clear-update-notification']);

const router = useRouter();
const { rooms, isLoading, totalPages, currentPage, fetchRoomList, searchRooms, refreshRoomList, changePage, joinRoom } = useRoom();
const webSocketStore = useWebSocketStore();
const alertStore = useAlertStore();
const viewMode = ref(props.initialViewMode);
const searchType = ref('roomName'); // 'roomName' or 'hostNickname'

// 방 데이터는 useRoom에서 관리되므로 별도 필터링 불필요
const filteredRooms = computed(() => rooms.value);

const handleFilterChange = async filterData => {
  const params = {};

  if (filterData.type === 'language') {
    params.programmingLanguage = filterData.value;
  } else if (filterData.type === 'gameType') {
    params.gameType = filterData.value;
  } else if (filterData.type === 'search') {
    searchType.value = filterData.value === 'ownerName' ? 'hostNickname' : 'roomName';
  }

  await searchRooms(params);
};

const handleSearch = async searchData => {
  const params = {};
  if (searchData.query && searchData.query.trim() !== '') {
    params[searchType.value] = searchData.query.trim();
  } else {
    // 빈 문자열이면 검색 필터를 초기화
    params[searchType.value] = null;
  }
  await searchRooms(params);
};

const handleRefresh = async () => {
  // 새로고침 알림 해제
  emit('clear-update-notification');
  // 방 목록 새로고침
  await refreshRoomList();
};

const handlePageChange = async page => {
  await changePage(page);
};

const handleJoinRoom = async roomId => {
  try {
    // WebSocket이 연결되어 있지 않으면 연결
    if (!webSocketStore.isConnected) {
      await webSocketStore.connect();
    }

    // useRoom의 joinRoom 사용 (API 요청 + 라우팅)
    await joinRoom(roomId);
  } catch (error) {
    const errorCode = error.response?.data?.status;
    const errorInfo = getRoomJoinErrorMessage(errorCode);
    alertStore.showAlert(errorInfo.message, errorInfo.type, 5000);
  }
};

const handleViewModeChange = mode => {
  viewMode.value = mode;
};

// 초기 데이터 로드
onMounted(async () => {
  await fetchRoomList();
});

defineExpose({
  refreshRoomList: handleRefresh,
});
</script>

<style lang="scss" scoped>
.room-list-content {
  flex: 1;
  width: 80%;
  height: 100vh;
  margin: 0 auto;
  padding: calc(18vh) calc(4vh) calc(2vh);
  box-sizing: border-box;
  overflow: hidden;
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
}

.header-section {
  flex: 0 0 calc(10vh);
  overflow: visible;
  margin-bottom: calc(0vh);
  position: relative;
  z-index: 100;
}

.pagination-section {
  flex: 0 0 auto;
  padding: calc(0.5vh) 0;
}

.pagination-container {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  position: relative;
}

.pagination-container > :last-child {
  position: absolute;
  right: 0;
}

.grid-section {
  flex: 1;
  min-height: 0;
  position: relative;

  &::-webkit-scrollbar {
    width: calc(1vh);
  }

  &::-webkit-scrollbar-track {
    background: rgba($primary-bg, 0.3);
    border-radius: calc(0.5vh);
  }

  &::-webkit-scrollbar-thumb {
    background: $electric-blue;
    border-radius: calc(0.5vh);

    &:hover {
      background: $cyberpunk-pink;
    }
  }
}

.loading-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  height: calc(25vh);
  font-family: $font-primary;
  font-size: calc(2vh);
  color: $text-primary;
}

//* 위쪽에서 슬라이드 애니메이션 */
.slide-from-top-enter-active,
.slide-from-top-leave-active {
  transition: all 0.7s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.slide-from-top-enter-from,
.slide-from-top-leave-to {
  transform: translateY(-50px);
}

.slide-from-top-enter-to,
.slide-from-top-leave-from {
  transform: translateY(0);
}

/* 아래쪽에서 슬라이드 애니메이션 추가 */
.slide-from-bottom-enter-active,
.slide-from-bottom-leave-active {
  transition: all 0.7s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.slide-from-bottom-enter-from,
.slide-from-bottom-leave-to {
  transform: translateY(50px);
}

.slide-from-bottom-enter-to,
.slide-from-bottom-leave-from {
  transform: translateY(0);
}
</style>
