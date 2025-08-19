<template>
  <div class="room-settings-panel">
    <h4 class="settings-title">방 설정</h4>

    <div class="settings-list">
      <SettingItem label="게임 유형:" :value="gameTypeDisplay" :show-badge="true" />

      <SettingItem label="최대 인원:" :value="`${roomData.maxPlayers} 명`" :show-badge="true" />

      <SettingItem label="제한 시간:" :value="`${roomData.timeLimit}분`" :show-badge="true" />

      <SettingItem label="언어:" :value="roomData.language" :show-badge="true" />
    </div>
  </div>
</template>

<script>
import SettingItem from './SettingItem.vue';

export default {
  name: 'RoomSettingsPanel',
  components: {
    SettingItem,
  },
  props: {
    roomData: {
      type: Object,
      required: true,
    },
  },
  computed: {
    gameTypeDisplay() {
      return this.roomData.gameType === 'rank' ? 'rank' : 'normal';
    },
  },
};
</script>

<style lang="scss" scoped>
.room-settings-panel {
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
  width: 100%;
  padding: calc(2vh) calc(1.5vh);
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  flex-shrink: 0;
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

.settings-title {
  font-family: $font-primary;
  font-weight: $font-weight-semibold;
  font-size: calc(2vh);
  line-height: 1.3;
  color: $text-primary;
  margin: 0 0 calc(1.5vh) 0;
  flex-shrink: 0;
}

.settings-list {
  display: flex;
  flex-direction: column;
  gap: calc(1.5vh);
  flex: 1;
  min-height: 0;
}
</style>
