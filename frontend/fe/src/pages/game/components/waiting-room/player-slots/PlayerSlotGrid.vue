<template>
  <div class="player-slot-grid-container">
    <div class="player-slot-grid">
      <PlayerSlot 
        v-for="(player, index) in playerSlots" 
        :key="`slot-${index}`" 
        :player="player" 
        :slot-index="index"
        :is-owner="isOwner"
        :current-user-id="currentUserId"
        @kick-player="handleKickPlayer"
      />
    </div>
  </div>
</template>

<script>
import PlayerSlot from './PlayerSlot.vue';

export default {
  name: 'PlayerSlotGrid',
  components: {
    PlayerSlot,
  },
  props: {
    players: {
      type: Array,
      default: () => [
        {
          id: 1,
          nickname: 'CodeMaster',
          tier: 'Diamond',
          rating: 2847,
          readyStatus: 'ready',
          avatar: '',
        },
        {
          id: 2,
          nickname: 'yunbal',
          tier: 'Diamond',
          rating: 2847,
          readyStatus: 'ready',
          avatar: '',
        },
        {
          id: 3,
          nickname: 'jungeun',
          tier: 'Diamond',
          rating: 2847,
          readyStatus: 'ready',
          avatar: '',
        },
        {
          id: 4,
          nickname: 'jebeop',
          tier: 'Diamond',
          rating: 2847,
          readyStatus: 'waiting',
          avatar: '',
        },
      ],
    },
    maxPlayers: {
      type: Number,
      default: 6,
    },
    isOwner: {
      type: Boolean,
      default: false,
    },
    currentUserId: {
      type: Number,
      default: null,
    },
  },
  computed: {
    playerSlots() {
      const slots = Array(this.maxPlayers).fill(null);
      this.players.forEach((player, index) => {
        if (index < this.maxPlayers) {
          slots[index] = player;
        }
      });
      return slots;
    },
  },
  methods: {
    handleKickPlayer(playerId) {
      this.$emit('kick-player', playerId);
    },
  },
};
</script>

<style lang="scss" scoped>
.player-slot-grid-container {
  background: linear-gradient(135deg, rgba($card-bg, 0.95) 0%, rgba($card-bg, 0.55) 50%, rgba($primary-bg, 0.7) 100%);
  border: calc(0.3vh) solid rgba($electric-blue, 0.6);
  border-radius: calc(2vh);
  box-shadow:
    0vh 0.8vh 3.2vh 0vh rgba(0, 0, 0, 0.4),
    inset 0vh 0.1vh 0vh 0vh rgba(255, 255, 255, 0.1);
  padding: calc(3vh) calc(2vh);
  width: 100%;
  height: 100%;
  min-height: 0;
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

.player-slot-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  grid-template-rows: repeat(3, 1fr);
  gap: calc(3vh) calc(2vh);
  width: 100%;
  height: 100%;
}
</style>
