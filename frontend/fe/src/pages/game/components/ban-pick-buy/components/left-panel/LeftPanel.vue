<template>
  <div class="left-panel">
    <!-- 플레이어 목록 영역 -->
    <div class="left-panel__players">
      <PlayerListPanel :players="players" :completed-player-indices="completedPlayerIndices" />
    </div>

    <!-- 채팅 시스템 영역 -->
    <div class="left-panel__chat">
      <ChatSystem :room-id="roomId" />
    </div>
  </div>
</template>

<script setup>
import { computed, ref, watch } from 'vue';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';
import PlayerListPanel from './PlayerListPanel.vue';
import ChatSystem from '@/pages/common/chat/ChatSystem.vue';

const inGameStore = useInGameStore();

// useInGameStore에서 직접 데이터 가져오기
const players = computed(() => {
  return inGameStore.players;
});

const roomId = computed(() => {
  return inGameStore.currentRoomId;
});

// 플레이어의 선택 완료 상태를 memberId 기반으로 반환
const completedPlayerIndices = ref([]);

watch(
  () => [inGameStore.gameSync?.players, inGameStore.gameSync?.phase],
  ([newPlayers, phase]) => {
    if (newPlayers && inGameStore.players && Array.isArray(newPlayers)) {
      let completedMemberIds = [];
      
      if (phase === 'PURCHASE') {
        // PURCHASE 단계에서는 purchased 필드 확인
        completedMemberIds = newPlayers.filter(player => player && player.purchased === true).map(player => player.memberId);
      } else {
        // 다른 단계에서는 chosen 필드 확인
        completedMemberIds = newPlayers.filter(player => player && player.chosen === true).map(player => player.memberId);
      }

      // completedMemberIds와 매칭되는 플레이어 ID들을 찾음
      completedPlayerIndices.value = completedMemberIds.filter(memberId => inGameStore.players.some(player => player.id === memberId));
    } else {
      completedPlayerIndices.value = [];
    }
  },
  { immediate: true, deep: true },
);
</script>

<style lang="scss" scoped>
.left-panel {
  display: flex;
  flex-direction: column;
  height: 100%;
  gap: 1vh;

  &__players {
    flex: 0 0 auto;
    min-height: 0;
  }

  &__chat {
    flex: 1; // 약 36% 비율 (302/833)
    min-height: 0;
  }
}
</style>
