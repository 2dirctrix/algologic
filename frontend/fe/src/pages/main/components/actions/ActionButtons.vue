<template>
  <div class="action-buttons">
    <ActionButton label="대전 생성" @click="handleCreateBattle" />
    <ActionButton label="대전 찾기" @click="handleFindBattle" />
  </div>
</template>

<script setup>
import ActionButton from './ActionButton.vue';

const emit = defineEmits(['create-battle', 'explore-rooms']);

const handleCreateBattle = () => {
  emit('create-battle');
};

const handleFindBattle = () => {
  emit('explore-rooms');
};
</script>

<style lang="scss" scoped>
.action-buttons {
  display: flex;
  gap: calc(2vh);
  align-items: center;
  width: 100%;
  height: 100%;
  justify-content: center;
}

.action-buttons > * {
  flex: 1;
  height: calc(10vh);
}
</style>
