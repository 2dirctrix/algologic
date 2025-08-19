<!-- src/pages/game/components/in-game/components/game-tools/ItemSection.vue -->
<template>
  <div class="item-section">
    <div class="section-content">
      <h3 class="section-title">아이템</h3>
      <div class="item-slots">
        <ItemCard
          v-for="item in purchasedItems"
          :key="`item-${item.itemId}`"
          :icon-type="`item-${item.itemId}`"
          :item-id="item.itemId"
          :remaining-count="item.remainingCount"
          @drag-start="handleItemDragStart"
          @drag-end="handleItemDragEnd"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';
import ItemCard from './ItemCard.vue';

const emit = defineEmits(['itemDragStart', 'itemDragEnd']);

const inGameStore = useInGameStore();

const purchasedItems = computed(() => {
  const items = inGameStore.banPickBuyResults?.purchasedItems || [];
  const itemList = inGameStore.itemList || [];
  
  // 구매한 아이템과 아이템 정보를 매칭하여 remainingCount 추가
  return items.map(purchasedItem => {
    const itemInfo = itemList.find(item => item.itemId === purchasedItem.itemId);
    return {
      ...purchasedItem,
      // remainingCount가 없으면 기본값을 설정 (itemInfo의 maxUsage 또는 기본값 1)
      remainingCount: purchasedItem.remainingCount !== undefined 
        ? purchasedItem.remainingCount 
        : (itemInfo?.maxUsage || 1)
    };
  });
});

const handleItemDragStart = data => {
  emit('itemDragStart', data);
};

const handleItemDragEnd = () => {
  emit('itemDragEnd');
};
</script>

<style lang="scss" scoped>
.item-section {
  background: linear-gradient(to bottom, rgba($primary-bg, 0.1) 0%, rgba($primary-bg, 0.3) 100%);
  backdrop-filter: blur(2.5vh);
  border: 0.2vh solid $neon-green;
  border-radius: $radius-small;
  padding: 3vh 0.5vh;
  width: 100%;
  flex: 1; // 남은 공간을 차지
  display: flex;
  justify-content: center;
  align-items: center;
  @include neon-glow($neon-green);
}

.section-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: fit-content;
}

.section-title {
  font-family: $font-primary;
  font-weight: $font-weight-semibold;
  font-size: 1.4vh;
  color: $neon-green;
  margin: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 3vh;
}

.item-slots {
  display: flex;
  flex-direction: column; // 아이템 슬롯들을 수직으로 배치
  gap: 1vh;
  align-items: center;
}
</style>
