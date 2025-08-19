<template>
  <div class="room-control-buttons">
    <ExitButton @click="handleExit" />
    <div class="button-group-right">
      <ReadyButton />
      <StartButton @start-game-click="handleStartGameClick" />
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';
import ExitButton from './ExitButton.vue';
import ReadyButton from './ReadyButton.vue';
import StartButton from './StartButton.vue';

const emit = defineEmits(['start-game-click']);

const router = useRouter();
const waitingRoomStore = useInGameStore();

const handleStartGameClick = () => {
  emit('start-game-click');
};

const handleExit = async () => {
  try {
    // 대기실 나가기 (WebSocket 메시지 전송 및 상태 초기화)
    await waitingRoomStore.leaveWaitingRoom();

    await router.push({
      name: 'main',
      query: { view: 'rooms' },
    });
  } catch (error) {
    console.error('방 나가기 실패:', error);

    // 에러가 발생해도 메인 페이지로 이동
    await router.push({
      name: 'main',
      query: { view: 'rooms' },
    });
  }
};
</script>

<style lang="scss" scoped>
.room-control-buttons {
  background: linear-gradient(135deg, 
    rgba($card-bg, 0.95) 0%, 
    rgba($card-bg, 0.85) 50%, 
    rgba($primary-bg, 0.9) 100%
  );
  border: calc(0.3vh) solid rgba($electric-blue, 0.6);
  border-radius: calc(2vh);
  box-shadow: 
    0vh 0.8vh 3.2vh 0vh rgba(0, 0, 0, 0.4),
    inset 0vh 0.1vh 0vh 0vh rgba(255, 255, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  min-height: calc(8vh);
  max-height: calc(12vh);
  padding: calc(2vh) calc(3vh);
  position: relative;
  overflow: hidden;
  @include neon-glow($electric-blue);

  // 미묘한 패턴 효과
  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: repeating-linear-gradient(
      45deg,
      transparent,
      transparent 3px,
      rgba($electric-blue, 0.02) 3px,
      rgba($electric-blue, 0.02) 6px
    );
    border-radius: calc(2vh);
    pointer-events: none;
  }
}

.button-group-right {
  display: flex;
  align-items: center;
  gap: calc(2vh);
}
</style>
