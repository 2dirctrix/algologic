<template>
  <div class="player-list-panel">
    <div class="player-list-panel__background"></div>
    <div class="player-list-panel__content">
      <PlayerSlot
        v-for="(player, index) in players"
        :key="player.id"
        :player="player"
        :slot-index="index"
        :is-completed="props.completedPlayerIndices.includes(player.id)"
      />
    </div>
  </div>
</template>

<script setup>
import PlayerSlot from './PlayerSlot.vue';

const props = defineProps({
  players: {
    type: Array,
    required: true,
  },
  completedPlayerIndices: {
    type: Array,
    default: () => [],
  },
});
</script>

<style lang="scss" scoped>
.player-list-panel {
  position: relative;
  width: 100%;
  height: 100%;

  &__background {
    position: absolute;
    inset: 0;
    background: rgba($card-bg, 0.6);
    border: 0.15vh solid $electric-blue;
    border-radius: 2vh;
    box-shadow:
      0vh 0.8vh 3.2vh 0vh rgba(0, 0, 0, 0.4),
      inset 0vh 0.1vh 0vh 0vh rgba(255, 255, 255, 0.1);
  }

  &__content {
    position: relative;
    z-index: 2;
    height: fit-content;
    min-height: 100%;
    padding: 1.5vh;
    display: flex;
    flex-direction: column;
    gap: 1vh;
  }

  &__slot {
    min-height: 8vh;
    transition: all $transition-base;

    // 활성 턴 상태
    &--active {
      :deep(.player-slot) {
        position: relative;
        overflow: hidden;

        &::before {
          content: '';
          background-image: conic-gradient($cyberpunk-yellow 20deg, transparent 120deg);
          width: 150%;
          height: 150%;
          position: absolute;
          top: -25%;
          left: -25%;
          animation: rotate 2s linear infinite;
          z-index: -1;
        }

        &::after {
          content: '';
          position: absolute;
          inset: 0.3vh;
          background: rgba(0, 0, 0, 0.7);
          border-radius: inherit;
          z-index: 0;
        }

        .player-content {
          position: relative;
          z-index: 1;
        }
      }
    }
  }
}

@keyframes rotate {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(-360deg);
  }
}
</style>
