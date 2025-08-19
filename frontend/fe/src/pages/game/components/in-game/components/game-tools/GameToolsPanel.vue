<!-- src/pages/game/components/in-game/components/game-tools/GameToolsPanel.vue -->
<template>
  <div class="game-tools-panel">
    <ItemSection 
      @item-drag-start="handleItemDragStart"
      @item-drag-end="handleItemDragEnd"
    />
    <SpellSection 
      @spell-drag-start="handleSpellDragStart"
      @spell-drag-end="handleSpellDragEnd"
    />
  </div>
</template>

<script setup>
import ItemSection from './ItemSection.vue';
import SpellSection from './SpellSection.vue';

const emit = defineEmits(['itemDragStart', 'itemDragEnd', 'spellDragStart', 'spellDragEnd']);

const handleItemDragStart = (data) => {
  emit('itemDragStart', data);
};

const handleItemDragEnd = () => {
  emit('itemDragEnd');
};

const handleSpellDragStart = (data) => {
  emit('spellDragStart', data);
};

const handleSpellDragEnd = () => {
  emit('spellDragEnd');
};
</script>

<style lang="scss" scoped>
.game-tools-panel {
  display: flex;
  flex-direction: column; // 아이템과 스펠 영역을 수직으로 배치
  gap: 2vh;
  height: 100%;
  width: 16vh;
}
</style>
