<template>
  <div class="player-tier-info">
    <span class="tier-name">{{ tier }}</span>
    <div class="rating-badge">
      {{ rating }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'PlayerTierInfo',
  props: {
    tier: {
      type: String,
      required: true,
    },
    rating: {
      type: Number,
      required: true,
    },
  },
};
</script>

<style lang="scss" scoped>
.player-tier-info {
  display: flex;
  align-items: center;
  gap: clamp(6px, 1vw, 12px);
}

.tier-name {
  font-family: $font-accent;
  font-weight: $font-weight-regular;
  font-size: clamp(1rem, 3vh, 1.8rem);
  line-height: 0.8;
  color: rgba(255, 255, 255, 0.7);
}

.rating-badge {
  background: $cyberpunk-yellow;
  color: $text-gold;
  padding: clamp(2px, 0.5vh, 4px) clamp(4px, 1vw, 8px);
  border-radius: $radius-button;
  font-family: $font-primary;
  font-weight: $font-weight-regular;
  font-size: clamp(0.6rem, 1.5vh, 0.9rem);
  line-height: 1.3;
  min-width: clamp(35px, 6vw, 50px);
  text-align: center;
}
</style>
