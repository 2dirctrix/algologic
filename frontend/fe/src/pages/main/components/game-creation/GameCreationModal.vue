<template>
  <ModalOverlay v-if="isVisible" @close="closeModal">
    <div class="game-creation-modal">
      <ModalHeader @close="closeModal" />

      <div class="modal-body">
        <!-- 방 이름 입력 -->
        <RoomNameInput v-model="formData.roomName" />

        <!-- 게임 타입 선택 -->
        <GameTypeToggle v-model="formData.gameType" />

        <!-- 참가 인원 선택 -->
        <PlayerCountSlider v-model="formData.playerCount" />

        <!-- 언어 선택 -->
        <LanguageGrid v-model="formData.language" />

        <!-- 액션 버튼들 -->
        <ModalActionButtons :canCreate="isFormValid && !isLoading" :isLoading="isLoading" @cancel="closeModal" @create="createRoom" />
      </div>
    </div>
  </ModalOverlay>
</template>

<script setup>
import { ref, computed } from 'vue';
import ModalOverlay from './components/shared/ModalOverlay.vue';
import ModalHeader from './components/shared/ModalHeader.vue';
import RoomNameInput from './components/room-settings/RoomNameInput.vue';
import GameTypeToggle from './components/room-settings/GameTypeToggle.vue';
import PlayerCountSlider from './components/room-settings/PlayerCountSlider.vue';
import LanguageGrid from './components/language-selection/LanguageGrid.vue';
import ModalActionButtons from './components/shared/ModalActionButtons.vue';
import { useRoom } from '@/composables/useRoom.js';

const props = defineProps({
  isVisible: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['close', 'create']);

const { createRoom: createRoomAPI, isLoading } = useRoom();

const formData = ref({
  roomName: '',
  gameType: 'RANKED',
  playerCount: 4,
  language: 'Python',
});

const isFormValid = computed(() => {
  return formData.value.roomName.trim().length > 0;
});

const closeModal = () => {
  emit('close');
};

const createRoom = async () => {
  if (isFormValid.value && !isLoading.value) {
    try {
      const roomData = {
        roomName: formData.value.roomName.trim(),
        maxSize: formData.value.playerCount,
        gameType: formData.value.gameType,
        programmingLanguage: formData.value.language.toUpperCase(),
      };

      const response = await createRoomAPI(roomData);

      // 폼 초기화
      formData.value = {
        roomName: '',
        gameType: 'RANKED',
        playerCount: 4,
        language: 'Python',
      };

      // 성공 시 부모에게 알리고 모달 닫기 (부모에서 모달을 닫도록)
      emit('create', response);
    } catch (error) {
      console.error('방 생성 오류:', error);
    }
  }
};
</script>

<style scoped lang="scss">
.game-creation-modal {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  overflow: hidden;
}

.modal-body {
  flex: 1;
  padding: 0 calc(9.7vh) calc(3vh);
  display: flex;
  flex-direction: column;
  gap: calc(1.2vh);
  min-height: 0;
  overflow: hidden;
  box-sizing: border-box;
  justify-content: space-between;

  // 각 섹션이 비례적으로 크기 조정되도록 설정
  > * {
    flex-shrink: 1;
  }

  // 액션 버튼은 항상 하단에 고정
  > :last-child {
    flex-shrink: 0;
    margin-top: auto;
  }
}
</style>
