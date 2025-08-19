<template>
  <div class="item-spell-selection-panel">
    <div class="item-spell-selection-panel__background"></div>

    <!-- 상단 타이머 및 코인 정보 -->
    <div class="item-spell-selection-panel__header">
      <PhaseTimer :remaining-time="remainingTime" :message="timerMessage" />
      <div class="coin-display">
        <div class="coin-icon">🪙</div>
        <span class="coin-amount">{{ userCoin }}</span>
      </div>
    </div>

    <!-- 아이템 & 스킬 선택 영역 -->
    <div class="item-spell-selection-panel__content">
      <!-- 아이템 선택 -->
      <div class="item-section">
        <div class="section-title">아이템</div>

        <!-- 아이템 목록 -->
        <div class="item-list">
          <ItemCard
            v-for="item in itemList"
            :key="item.itemId"
            :item="item"
            :is-selected="selectedItem?.itemId === item.itemId"
            :disabled="isInteractionDisabled"
            @select="handleItemSelect"
          />
        </div>

        <!-- 아이템 슬롯 (4칸) -->
        <div class="slots-container">
          <div class="slots-title">선택된 아이템</div>
          <div class="item-slots">
            <div v-for="slotIndex in 4" :key="`item-${slotIndex}`" class="slot" :class="{ filled: selectedItems[slotIndex - 1] }">
              <template v-if="selectedItems[slotIndex - 1]">
                <button class="slot-remove-btn" @click="removeItem(slotIndex - 1)">✕</button>
                <div class="slot-name">{{ selectedItems[slotIndex - 1].itemName }}</div>
                <div class="slot-quantity" v-if="selectedItems[slotIndex - 1].quantity > 1">
                  {{ selectedItems[slotIndex - 1].quantity }}
                </div>
              </template>
              <template v-else>
                <div class="slot-empty">빈 슬롯</div>
              </template>
            </div>
          </div>
        </div>
      </div>

      <!-- 스킬 선택 -->
      <div class="spell-section">
        <div class="section-title">스펠</div>

        <!-- 스킬 목록 -->
        <div class="spell-list">
          <SpellCard
            v-for="spell in spellList"
            :key="spell.spellId"
            :spell="spell"
            :is-selected="selectedSpell?.spellId === spell.spellId"
            :disabled="isInteractionDisabled"
            @select="handleSpellSelect"
          />
        </div>

        <!-- 스킬 슬롯 (2칸) -->
        <div class="slots-container">
          <div class="slots-title">선택된 스펠</div>
          <div class="spell-slots">
            <div v-for="slotIndex in 2" :key="`spell-${slotIndex}`" class="slot" :class="{ filled: selectedSpells[slotIndex - 1] }">
              <template v-if="selectedSpells[slotIndex - 1]">
                <button class="slot-remove-btn" @click="removeSpell(slotIndex - 1)">✕</button>
                <div class="slot-name">{{ selectedSpells[slotIndex - 1].spellName }}</div>
                <div class="slot-quantity" v-if="selectedSpells[slotIndex - 1].quantity > 1">
                  {{ selectedSpells[slotIndex - 1].quantity }}
                </div>
              </template>
              <template v-else>
                <div class="slot-empty">빈 슬롯</div>
              </template>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 구매 정보 및 버튼 -->
    <div class="item-spell-selection-panel__actions">
      <div class="purchase-info">
        <div class="cost-display" :class="{ insufficient: isInsufficientCoin }">
          <span class="cost-label">총 비용:</span>
          <span class="cost-amount">{{ totalCost }} 🪙</span>
        </div>
        <div v-if="isInsufficientCoin" class="insufficient-warning">코인이 부족합니다! ({{ userCoin - totalCost }} 🪙 부족)</div>
      </div>
      <button
        class="item-spell-selection-panel__purchase-btn"
        :disabled="!canPurchase || isInteractionDisabled"
        @click="handlePurchaseConfirm"
      >
        {{ props.isSelectionComplete ? '구매 완료' : '구매하기' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';
import { useAuthStore } from '@/stores/auth/auth.js';
import PhaseTimer from '../header/PhaseTimer.vue';
import ItemCard from './ItemCard.vue';
import SpellCard from './SpellCard.vue';

// Props
const props = defineProps({
  remainingTime: {
    type: Number,
    default: 0,
  },
  currentPhase: {
    type: String,
    required: true,
  },
  isSelectionComplete: {
    type: Boolean,
    default: false,
  },
});

// Emits
const emit = defineEmits(['purchase-items-spells']);

// Store
const inGameStore = useInGameStore();
const authStore = useAuthStore();

// Reactive data
const selectedItem = ref(null);
const selectedSpell = ref(null);
const selectedItems = ref([]);
const selectedSpells = ref([]);

// Computed
const itemList = computed(() => inGameStore.itemList || []);
const spellList = computed(() => inGameStore.spellList || []);
const userCoin = computed(() => authStore.coin);

const timerMessage = computed(() => '아이템/스펠 구매 단계');
const isInteractionDisabled = computed(() => inGameStore.timerState.isCountdownActive || props.isSelectionComplete);

// 총 구매 비용 계산
const totalCost = computed(() => {
  let cost = 0;

  // 아이템 비용 계산
  selectedItems.value.forEach(item => {
    if (item) {
      cost += (item.cost || 0) * item.quantity;
    }
  });

  // 스펠 비용 계산
  selectedSpells.value.forEach(spell => {
    if (spell) {
      cost += (spell.cost || 0) * spell.quantity;
    }
  });

  return cost;
});

// 구매 가능 여부 확인 (선택된 아이템/스펠이 있고, 코인이 충분한지)
const canPurchase = computed(() => {
  const hasSelection = selectedItems.value.some(Boolean) || selectedSpells.value.some(Boolean);
  const hasEnoughCoin = userCoin.value >= totalCost.value;
  return hasSelection && hasEnoughCoin;
});

// 코인 부족 여부
const isInsufficientCoin = computed(() => totalCost.value > userCoin.value);

// Methods
const playButtonSound = () => {
  try {
    const audio = new Audio('/audio/interaction/chosen-sound.wav');
    audio.volume = 0.5;
    audio.play().catch(error => {
      console.warn('Audio play failed:', error);
    });
  } catch (error) {
    console.warn('Audio creation failed:', error);
  }
};

const handleItemSelect = item => {
  if (isInteractionDisabled.value) return;

  selectedItem.value = item;

  // 같은 아이템이 이미 선택되어 있는지 확인
  const existingSlotIndex = selectedItems.value.findIndex(slot => slot && slot.itemId === item.itemId);

  if (existingSlotIndex !== -1) {
    // 기존 슬롯의 수량 증가
    selectedItems.value[existingSlotIndex].quantity += 1;
  } else {
    // 새 아이템을 빈 슬롯에 추가 (왼쪽부터)
    const emptySlotIndex = selectedItems.value.findIndex(slot => !slot);
    if (emptySlotIndex !== -1) {
      selectedItems.value[emptySlotIndex] = {
        ...item,
        quantity: 1,
      };
    }
  }
};

const handleSpellSelect = spell => {
  if (isInteractionDisabled.value) return;

  selectedSpell.value = spell;

  // 같은 스펠이 이미 선택되어 있는지 확인
  const existingSlotIndex = selectedSpells.value.findIndex(slot => slot && slot.spellId === spell.spellId);

  if (existingSlotIndex !== -1) {
    // 기존 슬롯의 수량 증가
    selectedSpells.value[existingSlotIndex].quantity += 1;
  } else {
    // 새 스펠을 빈 슬롯에 추가 (왼쪽부터)
    const emptySlotIndex = selectedSpells.value.findIndex(slot => !slot);
    if (emptySlotIndex !== -1) {
      selectedSpells.value[emptySlotIndex] = {
        ...spell,
        quantity: 1,
      };
    }
  }
};

const removeItem = index => {
  if (isInteractionDisabled.value) return;

  if (selectedItems.value[index]) {
    // 수량이 1보다 크면 수량 감소, 1이면 슬롯 제거
    if (selectedItems.value[index].quantity > 1) {
      selectedItems.value[index].quantity -= 1;
    } else {
      selectedItems.value[index] = null;
    }
  }
};

const removeSpell = index => {
  if (isInteractionDisabled.value) return;

  if (selectedSpells.value[index]) {
    // 수량이 1보다 크면 수량 감소, 1이면 슬롯 제거
    if (selectedSpells.value[index].quantity > 1) {
      selectedSpells.value[index].quantity -= 1;
    } else {
      selectedSpells.value[index] = null;
    }
  }
};

const handlePurchaseConfirm = () => {
  if (isInteractionDisabled.value || !canPurchase.value) return;

  playButtonSound();

  const purchaseData = {
    items: selectedItems.value.filter(Boolean).map(item => ({
      itemId: item.itemId,
      quantity: item.quantity,
    })),
    spells: selectedSpells.value.filter(Boolean).map(spell => ({
      spellId: spell.spellId,
      quantity: spell.quantity,
    })),
  };

  emit('purchase-items-spells', purchaseData);
};

// 초기 슬롯 세팅
selectedItems.value = new Array(4).fill(null);
selectedSpells.value = new Array(2).fill(null);
</script>

<style lang="scss" scoped>
@use 'sass:color';

.item-spell-selection-panel {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;

  &__background {
    position: absolute;
    inset: 0;
    background: rgba($card-bg, 0.6);
    border: 0.2vh solid $neon-magenta;
    border-radius: 2vh;
    box-shadow:
      0vh 0.8vh 3.2vh 0vh rgba(0, 0, 0, 0.4),
      0vh 0.1vh 0vh 0vh rgba(255, 255, 255, 0.1);
  }

  &__header {
    position: relative;
    z-index: 2;
    padding: 2vh;
    flex-shrink: 0;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  &__content {
    position: relative;
    z-index: 2;
    flex: 1;
    display: flex;
    padding: 0 3vh;
    gap: 1vh;
  }

  &__actions {
    position: relative;
    z-index: 2;
    padding: 0vh 2vh 2vh 2vh;
    flex-shrink: 0;
    min-height: 10vh;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 1vh;
  }

  &__purchase-btn {
    background: linear-gradient(135deg, rgba($cyberpunk-yellow, 0.2) 0%, rgba($cyberpunk-yellow, 0.1) 50%, rgba($electric-blue, 0.1) 100%);
    border: 0.3vh solid $cyberpunk-yellow;
    border-radius: 1.5vh;
    color: $cyberpunk-yellow;
    font-family: $font-primary;
    font-size: 2.2vh;
    font-weight: 700;
    padding: 1.8vh 4vh;
    cursor: pointer;
    transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
    min-width: 16vh;
    min-height: 6vh;
    position: relative;
    overflow: hidden;
    text-transform: uppercase;
    letter-spacing: 0.1em;

    // 내부 글로우 효과
    &::before {
      content: '';
      position: absolute;
      inset: 0.2vh;
      background: linear-gradient(135deg, rgba($cyberpunk-yellow, 0.05) 0%, transparent 50%, rgba($electric-blue, 0.05) 100%);
      border-radius: 1.2vh;
      z-index: -1;
      transition: all 0.4s ease;
    }

    &:hover:not(:disabled) {
      @include neon-glow-strong($cyberpunk-yellow);
      transform: translateY(-0.3vh) scale(1.02);
      border-color: lighten($cyberpunk-yellow, 15%);
      color: lighten($cyberpunk-yellow, 20%);
      background: linear-gradient(
        135deg,
        rgba($cyberpunk-yellow, 0.3) 0%,
        rgba($cyberpunk-yellow, 0.2) 50%,
        rgba($electric-blue, 0.15) 100%
      );

      &::before {
        background: linear-gradient(
          135deg,
          rgba($cyberpunk-yellow, 0.1) 0%,
          rgba($electric-blue, 0.05) 50%,
          rgba($cyberpunk-yellow, 0.1) 100%
        );
      }
    }

    &:active:not(:disabled) {
      transform: translateY(-0.1vh) scale(1.01);
      transition: all 0.15s ease;
    }

    &:disabled {
      background: rgba($cyberpunk-yellow, 0.1);
      border-color: rgba($cyberpunk-yellow, 0.3);
      color: rgba($cyberpunk-yellow, 0.4);
      cursor: not-allowed;
      transform: none;

      &::before {
        background: rgba($cyberpunk-yellow, 0.02);
      }
    }
  }
}

// �� 9X ��|
.item-section,
.spell-section {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 0;
}

.section-title {
  font-family: $font-primary;
  font-size: 1.8vh;
  font-weight: 600;
  color: $electric-blue;
  text-align: center;
  margin-bottom: 1vh;
  padding-bottom: 0.5vh;
  border-bottom: 1px solid rgba($electric-blue, 0.3);
}

.item-list,
.spell-list {
  flex: 1;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  grid-auto-rows: 8vh;
  gap: 1vh;
  padding: 1vh 1vh;
  overflow-y: auto;
  min-height: 30vh;
  max-height: 30vh;
  align-content: start;

  &::-webkit-scrollbar {
    width: 0.4vh;
  }

  &::-webkit-scrollbar-track {
    background: rgba($primary-bg, 0.3);
    border-radius: 0.2vh;
  }

  &::-webkit-scrollbar-thumb {
    background: $electric-blue;
    border-radius: 0.2vh;

    &:hover {
      background: lighten($electric-blue, 10%);
    }
  }

  scrollbar-width: thin;
  scrollbar-color: $electric-blue rgba($primary-bg, 0.3);
}

.slots-container {
  margin-top: 0.5vh;
  margin-bottom: 1vh;
  flex-shrink: 0;
}

.slots-title {
  font-family: $font-primary;
  font-size: 1.4vh;
  color: $cyberpunk-pink;
  text-align: center;
  margin-bottom: 0.5vh;
}

.item-slots,
.spell-slots {
  display: flex;
  gap: 2vh;
  justify-content: space-between;
  width: 100%;
  padding: 1vh 1.5vh;
}

.slot {
  position: relative;
  flex: 1;
  height: 8vh;
  border-radius: 1vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  background: linear-gradient(135deg, rgba($primary-bg, 0.6) 0%, rgba($primary-bg, 0.4) 100%);
  border: 0.2vh dashed rgba($cyberpunk-pink, 0.4);
  overflow: visible;

  // 내부 패턴
  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: repeating-linear-gradient(
      45deg,
      transparent,
      transparent 3px,
      rgba($cyberpunk-pink, 0.02) 3px,
      rgba($cyberpunk-pink, 0.02) 6px
    );
    border-radius: 0.8vh;
    pointer-events: none;
    opacity: 0.5;
  }

  &.filled {
    border-style: solid;
    border-color: $cyberpunk-pink;
    background: linear-gradient(135deg, rgba($card-bg, 0.95) 0%, rgba($card-bg, 0.8) 50%, rgba($cyberpunk-pink, 0.1) 100%);
    @include neon-glow($cyberpunk-pink);
    transform: scale(1.02);

    &::before {
      opacity: 1;
    }

    .slot-name {
      color: lighten($text-primary, 10%);
      text-shadow: 0 0 0.3vh rgba($cyberpunk-pink, 0.5);
    }
  }

  .slot-remove-btn {
    position: absolute;
    top: -0.8vh;
    right: -0.8vh;
    width: 2.4vh;
    height: 2.4vh;
    border: 0.2vh solid #ff4757;
    border-radius: 50%;
    background: linear-gradient(135deg, #ff4757 0%, #ff3838 100%);
    color: $text-primary;
    font-size: 1.2vh;
    font-weight: bold;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 10;
    transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
    box-shadow: 0 0 1vh rgba(#ff4757, 0.6);

    &:hover {
      background: linear-gradient(135deg, lighten(#ff4757, 10%) 0%, #ff4757 100%);
      border-color: lighten(#ff4757, 20%);
      box-shadow: 0 0 1.5vh rgba(#ff4757, 0.8);
      transform: scale(1.15) rotate(90deg);
    }

    &:active {
      transform: scale(1.05) rotate(90deg);
    }
  }

  .slot-name {
    font-family: $font-primary;
    font-size: 1.4vh;
    font-weight: 600;
    color: $text-primary;
    text-align: center;
    line-height: 1.2;
    letter-spacing: 0.02em;
    text-transform: uppercase;
    transition: all 0.3s ease;
    z-index: 1;
    padding: 0 1vh;
    max-width: 90%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .slot-quantity {
    position: absolute;
    top: -0.8vh;
    left: -0.8vh;
    width: 2.4vh;
    height: 2.4vh;
    background: linear-gradient(135deg, $neon-green 0%, darken($neon-green, 10%) 100%);
    border: 0.2vh solid lighten($neon-green, 10%);
    border-radius: 50%;
    font-family: $font-primary;
    font-size: 1.4vh;
    font-weight: 900;
    color: darken($primary-bg, 50%);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 8;
    box-shadow: 0 0 1vh rgba($neon-green, 0.6);
    animation: quantity-pulse 2s ease-in-out infinite;
  }

  .slot-empty {
    font-family: $font-primary;
    font-size: 1.2vh;
    font-weight: 500;
    color: rgba($text-primary, 0.3);
    text-align: center;
    text-transform: uppercase;
    letter-spacing: 0.05em;
    z-index: 1;
  }
}

@keyframes quantity-pulse {
  0%,
  100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

// 코인 표시 스타일 - 사이버펑크 강화
.coin-display {
  position: absolute;
  top: 50%;
  right: 2vh;
  transform: translateY(-50%);
  display: flex;
  align-items: center;
  gap: 0.8vh;
  background: linear-gradient(135deg, rgba($neon-green, 0.15) 0%, rgba($neon-green, 0.08) 100%);
  border: 0.2vh solid $neon-green;
  border-radius: 2vh;
  padding: 1vh 2vh;
  overflow: hidden;
  @include neon-glow($neon-green);

  // 내부 글로우 효과
  &::before {
    content: '';
    position: absolute;
    inset: 0.1vh;
    background: linear-gradient(135deg, rgba($neon-green, 0.05) 0%, transparent 50%, rgba($neon-green, 0.03) 100%);
    border-radius: 1.8vh;
    z-index: -1;
  }

  .coin-icon {
    font-size: 2.2vh;
    filter: drop-shadow(0 0 0.5vh rgba($neon-green, 0.6));
  }

  .coin-amount {
    font-family: $font-primary;
    font-size: 1.8vh;
    font-weight: 700;
    color: lighten($neon-green, 15%);
    text-shadow: 0 0 0.5vh rgba($neon-green, 0.5);
    letter-spacing: 0.05em;
  }
}

// 구매 정보 스타일
.purchase-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5vh;
}

.cost-display {
  display: flex;
  align-items: center;
  gap: 1vh;
  font-family: $font-primary;

  .cost-label {
    font-size: 1.4vh;
    color: $text-primary;
  }

  .cost-amount {
    font-size: 1.6vh;
    font-weight: 600;
    color: $neon-green;
  }

  &.insufficient {
    .cost-amount {
      color: #ff4757;
    }
  }
}

.insufficient-warning {
  font-family: $font-primary;
  font-size: 1.2vh;
  color: #ff4757;
  text-align: center;
  //background: rgba(#ff4757, 0.1);
  border: 1px solid rgba(#ff4757, 0.3);
  border-radius: 0.5vh;
  padding: 0.5vh 1vh;
  animation: pulse-warning 1.5s ease-in-out infinite;
}

@keyframes pulse-warning {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.7;
  }
}
</style>
