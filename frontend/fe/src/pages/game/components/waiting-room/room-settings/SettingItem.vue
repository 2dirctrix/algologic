<template>
  <div class="setting-item">
    <span class="setting-label">{{ label }}</span>
    <div v-if="showBadge" class="setting-badge">
      {{ value }}
    </div>
    <span v-else class="setting-value">{{ value }}</span>
  </div>
</template>

<script>
export default {
  name: 'SettingItem',
  props: {
    label: {
      type: String,
      required: true,
    },
    value: {
      type: String,
      required: true,
    },
    showBadge: {
      type: Boolean,
      default: false,
    },
  },
};
</script>

<style lang="scss" scoped>
.setting-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: calc(1vh);
  min-height: 0;
}

.setting-label {
  font-family: $font-primary;
  font-weight: $font-weight-regular;
  font-size: calc(1.7vh);
  line-height: 1.3;
  color: rgba(255, 255, 255, 0.8);
  flex-shrink: 0;
  white-space: nowrap;
}

.setting-value {
  font-family: $font-primary;
  font-weight: $font-weight-regular;
  font-size: calc(1.7vh);
  line-height: 1.3;
  color: rgba(255, 255, 255, 0.8);
}

.setting-badge {
  background: linear-gradient(135deg, 
    rgba($electric-blue, 0.2) 0%, 
    rgba($electric-blue, 0.1) 50%, 
    rgba($electric-blue, 0.15) 100%
  );
  border: calc(0.2vh) solid rgba($electric-blue, 0.6);
  border-radius: calc(1vh);
  color: lighten($electric-blue, 20%);
  padding: calc(0.5vh) calc(1.2vh);
  min-height: calc(3vh);
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: calc(1.6vh);
  line-height: 1.2;
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: calc(5vh);
  max-width: calc(10vh);
  white-space: nowrap;
  text-overflow: ellipsis;
  text-shadow: 0 0 0.5vh rgba($electric-blue, 0.6);
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  @include neon-glow($electric-blue);

  &:hover {
    background: linear-gradient(135deg, 
      rgba($electric-blue, 0.3) 0%, 
      rgba($electric-blue, 0.15) 50%, 
      rgba($electric-blue, 0.2) 100%
    );
    border-color: $electric-blue;
    transform: scale(1.05);
  }
}
</style>
