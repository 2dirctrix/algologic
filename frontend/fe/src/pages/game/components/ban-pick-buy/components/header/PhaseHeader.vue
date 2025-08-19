<template>
  <div class="phase-header">
    <PhaseIndicator
      v-for="(phaseData, index) in phasesData"
      :key="phaseData.id"
      :phase="phaseData.id"
      :status="phaseData.status"
      :total-duration="phaseData.totalDuration"
      :remaining-time="phaseData.remainingTime"
      :position="getPhasePosition(index)"
      :banned-algorithm-name="bannedAlgorithmName"
    />
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useInGameStore } from '@/pages/game/store/useInGameStore.js';
import PhaseIndicator from './PhaseIndicator.vue';
import { PHASE_STATES, INDICATOR_STATUS } from '../../constants/phase-status.js';

const inGameStore = useInGameStore();

// useInGameStore에서 직접 데이터 가져오기
const currentPhase = computed(() => {
  return inGameStore.gameSync.phase || PHASE_STATES.BAN;
});

const remainingTime = computed(() => {
  return inGameStore.gameSync.remainingTime || 0;
});

const totalDuration = computed(() => {
  return inGameStore.gameSync.totalDuration || 30;
});

// 밴된 알고리즘 이름 조회
const bannedAlgorithmName = computed(() => {
  const bannedId = inGameStore.banPickBuyResults.bannedAlgorithm;
  if (!bannedId || !inGameStore.algorithmCategories.length) {
    return null;
  }

  const bannedCategory = inGameStore.algorithmCategories.find(category => category.categoryId === bannedId);
  return bannedCategory?.category || null;
});

const phasesData = computed(() => {
  const phaseOrder = [PHASE_STATES.BAN, PHASE_STATES.PICK, PHASE_STATES.BUY];
  const currentIndex = phaseOrder.indexOf(currentPhase.value);

  return phaseOrder.map((phaseId, index) => {
    // 상태 결정
    let status, phaseRemainingTime;
    if (index < currentIndex) {
      status = INDICATOR_STATUS.COMPLETED;
      phaseRemainingTime = 0;
    } else if (index === currentIndex) {
      status = INDICATOR_STATUS.ACTIVE;
      phaseRemainingTime = remainingTime.value;
    } else {
      status = INDICATOR_STATUS.WAITING;
      phaseRemainingTime = totalDuration.value;
    }

    return {
      id: phaseId,
      name: phaseId === PHASE_STATES.BAN ? '알고리즘 밴' : phaseId === PHASE_STATES.PICK ? '알고리즘 픽' : '아이템 / 스펠 구매',
      status,
      totalDuration: totalDuration.value,
      remainingTime: phaseRemainingTime,
    };
  });
});

const getPhasePosition = index => {
  // 0: first (우측만 대각선), 1: middle (양쪽 대각선), 2: last (좌측만 대각선)
  if (index === 0) return 'first';
  if (index === phasesData.value.length - 1) return 'last';
  return 'middle';
};
</script>

<style lang="scss" scoped>
.phase-header {
  display: flex;
  width: 100%;
  height: 8vh;
  margin-bottom: 1vh;
  position: relative;
  gap: 0.3vh;
}
</style>
