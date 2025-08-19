<!-- src/pages/game/components/in-game/components/player-status/PlayerStatusBar.vue -->
<template>
  <div class="player-status-bar">
    <div class="status-container">
      <div class="player-cards-wrapper">
        <PlayerCard
          v-for="participant in otherParticipants"
          :key="participant.participantIdentity"
          :player="{
            id: participant.participantIdentity,
            name: getPlayerName(participant.participantIdentity),
            status: 'active',
            isCurrentPlayer: false,
            participant: participant,
          }"
          :is-typing="getTypingStatus(participant.participantIdentity)"
          @item-dropped="handleItemDropped"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useAuthStore } from '@/stores/auth/auth.js';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';
import { useWebRTCStore } from '@/stores/webrtc/webrtc.js';
import PlayerCard from './PlayerCard.vue';

// Stores
const authStore = useAuthStore();
const inGameStore = useInGameStore();
const webrtcStore = useWebRTCStore();

const emit = defineEmits(['itemDropped']);

// 본인을 제외한 다른 참가자들의 화상공유를 표시
const otherParticipants = computed(() => {
  // authStore.id를 사용 (사용자의 pk 값)
  const currentUserId = authStore.currentUser.id; // nickname 대신 id 사용
  const remoteTracksArray = Array.from(webrtcStore.remoteTracksMap.values());

  // participantIdentity는 문자열로 오므로 비교시 타입 맞춰주기
  return remoteTracksArray.filter(track => {
    const trackParticipantId = String(track.participantIdentity);
    const currentUserIdStr = String(currentUserId);

    return trackParticipantId !== currentUserIdStr;
  });
});

// participantIdentity를 통해 실제 플레이어 이름 조회
const getPlayerName = participantIdentity => {
  const participantIdNumber = Number(participantIdentity); // 숫자로 변환
  const player = inGameStore.players.find(p => p.id === participantIdNumber);

  return player ? player.nickname : `Player ${participantIdentity}`;
};

// 특정 참가자의 타이핑 상태 조회
const getTypingStatus = participantIdentity => {
  return webrtcStore.getParticipantTypingStatus(participantIdentity);
};

const handleItemDropped = (data) => {
  emit('itemDropped', data);
};
</script>

<style lang="scss" scoped>
.player-status-bar {
  width: 100%;
  height: 100%;
}

.status-container {
  background: linear-gradient(to bottom, rgba($primary-bg, 0.1) 0%, rgba($primary-bg, 0.3) 100%);
  border: 0.2vh solid $electric-blue;
  border-radius: 1vh;
  padding: 1.5vh;
  height: 100%;
  backdrop-filter: blur(2.5vh);
  @include neon-glow($electric-blue);
}

.player-cards-wrapper {
  display: grid;
  grid-template-columns: repeat(5, 1fr); // 항상 5개 컬럼으로 고정
  gap: 1.5vh;
  height: 100%;
}
</style>
