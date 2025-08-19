<!-- src/pages/game/components/in-game/components/game-tools/SpellSection.vue -->
<template>
  <div class="spell-section">
    <div class="section-content">
      <h3 class="section-title">스펠</h3>
      <div class="spell-slots">
        <SpellCard
          v-for="spell in purchasedSpells"
          :key="`spell-${spell.spellId}`"
          :icon-type="`spell-${spell.spellId}`"
          :spell-id="spell.spellId"
          :remaining-count="spell.remainingCount"
          @drag-start="handleSpellDragStart"
          @drag-end="handleSpellDragEnd"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';
import SpellCard from './SpellCard.vue';

const emit = defineEmits(['spellDragStart', 'spellDragEnd']);

const inGameStore = useInGameStore();

const purchasedSpells = computed(() => {
  const spells = inGameStore.banPickBuyResults?.purchasedSpells || [];
  const spellList = inGameStore.spellList || [];
  
  // 구매한 스펠과 스펠 정보를 매칭하여 remainingCount 추가
  return spells.map(purchasedSpell => {
    const spellInfo = spellList.find(spell => spell.spellId === purchasedSpell.spellId);
    return {
      ...purchasedSpell,
      // remainingCount가 없으면 기본값을 설정 (spellInfo의 maxUsage 또는 기본값 1)
      remainingCount: purchasedSpell.remainingCount !== undefined 
        ? purchasedSpell.remainingCount 
        : (spellInfo?.maxUsage || 1)
    };
  });
});

const handleSpellDragStart = data => {
  emit('spellDragStart', data);
};

const handleSpellDragEnd = () => {
  emit('spellDragEnd');
};
</script>

<style lang="scss" scoped>
.spell-section {
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

.spell-slots {
  display: flex;
  flex-direction: column; // 스펠 슬롯들을 수직으로 배치
  gap: 1vh;
  align-items: center;
}

.spell-slot {
  width: 8vh;
  height: 8vh;
  background: rgba($card-bg, 0.4);
  //background: rgba($text-primary, $opacity-minimal);
  border: 1px solid rgba($text-primary, $opacity-active);
  border-radius: $radius-button;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10px);
}

.spell-placeholder {
  width: 2vh;
  height: 2vh;
  background: rgba($text-primary, 0.05);
  border-radius: 2px;
}
</style>
