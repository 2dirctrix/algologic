<template>
  <div :class="viewMode === 'card' ? 'room-grid' : 'room-list'">
    <template v-if="viewMode === 'card'">
      <RoomCard
        v-for="room in rooms"
        :key="room.roomId"
        :room-id="room.roomId"
        :room-name="room.roomName"
        :game-type="room.gameType"
        :language="room.programmingLanguage"
        :current-players="room.size"
        :max-players="room.maxSize"
        :room-status="room.status"
        :border-color="getBorderColor(room.gameType)"
        :room-owner="room.hostNickname"
        @join-room="handleJoinRoom"
      />
    </template>

    <template v-else>
      <RoomListCard
        v-for="room in rooms"
        :key="room.roomId"
        :room-id="room.roomId"
        :room-name="room.roomName"
        :game-type="room.gameType"
        :language="room.programmingLanguage"
        :current-players="room.size"
        :max-players="room.maxSize"
        :room-status="room.status"
        :border-color="getBorderColor(room.gameType)"
        :room-owner="room.hostNickname"
        @join-room="handleJoinRoom"
      />
    </template>
  </div>
</template>

<script>
import RoomCard from './RoomCard.vue';
import RoomListCard from './RoomListCard.vue';

export default {
  name: 'RoomGrid',
  components: {
    RoomCard,
    RoomListCard,
  },
  props: {
    rooms: {
      type: Array,
      default: () => [],
    },
    viewMode: {
      type: String,
      default: 'card',
      validator(value) {
        return ['card', 'list'].includes(value);
      },
    },
  },
  methods: {
    handleJoinRoom(roomId) {
      this.$emit('join-room', roomId);
    },
    getBorderColor(gameType) {
      // 게임 타입에 따른 테두리 색상 반환
      switch (gameType) {
        case 'RANKED':
          return 'cyberpunk-pink';
        case 'NORMAL':
          return 'cyberpunk-yellow';
        default:
          return 'electric-blue';
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.room-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-template-rows: 1fr 1fr;
  gap: calc(1vh);
  padding: 0;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
}

.room-list {
  display: flex;
  flex-direction: column;
  gap: 0.3vh;
  padding: 1vh 1vh;
  width: 100%;
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  box-sizing: border-box;

  // 커스텀 스크롤바 스타일
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
</style>
