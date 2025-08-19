<template>
  <div class="game-type-toggle">
    <button :class="['type-button', 'rank-button', { active: selectedType === 'RANKED' }]" @click="selectType('RANKED')">랭크 게임</button>
    <button :class="['type-button', 'normal-button', { active: selectedType === 'NORMAL' }]" @click="selectType('NORMAL')">
      일반 게임
    </button>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// Props & Emits
const props = defineProps({
  modelValue: {
    type: String,
    default: 'RANKED',
  },
});

const emit = defineEmits(['update:modelValue']);

// 반응형 데이터
const selectedType = ref(props.modelValue);

// 메서드
const selectType = type => {
  selectedType.value = type;
  emit('update:modelValue', type);
};
</script>

<style scoped lang="scss">
.game-type-toggle {
  display: flex;
  gap: calc(2vh);
  margin-bottom: calc(2vh);
}

.type-button {
  flex: 1;
  height: calc(5vh);
  border-radius: calc(1.2vh);
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: calc(1.6vh);
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  border: calc(0.2vh) solid transparent;
  position: relative;
  overflow: hidden;
  text-transform: uppercase;
  letter-spacing: 0.05em;

  display: flex;
  align-items: center;
  justify-content: center;

  // 내부 글로우 효과
  &::before {
    content: '';
    position: absolute;
    inset: 0.15vh;
    border-radius: calc(1vh);
    transition: all 0.4s ease;
    z-index: -1;
  }
}

.rank-button {
  background: linear-gradient(135deg, 
    rgba($cyberpunk-pink, 0.1) 0%, 
    rgba($card-bg, 0.7) 50%, 
    rgba($cyberpunk-pink, 0.05) 100%
  );
  border-color: rgba($cyberpunk-pink, 0.4);
  color: $cyberpunk-pink;

  &::before {
    background: linear-gradient(135deg, 
      rgba($cyberpunk-pink, 0.03) 0%, 
      transparent 50%, 
      rgba($cyberpunk-pink, 0.03) 100%
    );
  }

  &.active {
    background: linear-gradient(135deg, 
      rgba($cyberpunk-pink, 0.3) 0%, 
      rgba($cyberpunk-pink, 0.2) 50%, 
      rgba($electric-blue, 0.1) 100%
    );
    border: calc(0.3vh) solid $cyberpunk-pink;
    color: lighten($cyberpunk-pink, 15%);
    transform: scale(1.05);
    @include neon-glow-strong($cyberpunk-pink);

    &::before {
      background: linear-gradient(135deg, 
        rgba($cyberpunk-pink, 0.1) 0%, 
        transparent 50%, 
        rgba($electric-blue, 0.05) 100%
      );
    }
  }

  &:not(.active) {
    &:hover {
      background: linear-gradient(135deg, 
        rgba($cyberpunk-pink, 0.2) 0%, 
        rgba($card-bg, 0.8) 50%, 
        rgba($cyberpunk-pink, 0.1) 100%
      );
      border-color: rgba($cyberpunk-pink, 0.6);
      color: lighten($cyberpunk-pink, 10%);
      transform: translateY(-0.1vh) scale(1.02);
      @include neon-glow($cyberpunk-pink);

      &::before {
        background: linear-gradient(135deg, 
          rgba($cyberpunk-pink, 0.08) 0%, 
          rgba($cyberpunk-pink, 0.03) 50%, 
          rgba($cyberpunk-pink, 0.08) 100%
        );
      }
    }

    &:active {
      transform: translateY(0) scale(1.01);
      transition: all 0.15s ease;
    }
  }
}

.normal-button {
  background: linear-gradient(135deg, 
    rgba($cyberpunk-yellow, 0.1) 0%, 
    rgba($card-bg, 0.7) 50%, 
    rgba($cyberpunk-yellow, 0.05) 100%
  );
  border-color: rgba($cyberpunk-yellow, 0.4);
  color: $cyberpunk-yellow;

  &::before {
    background: linear-gradient(135deg, 
      rgba($cyberpunk-yellow, 0.03) 0%, 
      transparent 50%, 
      rgba($cyberpunk-yellow, 0.03) 100%
    );
  }

  &.active {
    background: linear-gradient(135deg, 
      rgba($cyberpunk-yellow, 0.3) 0%, 
      rgba($cyberpunk-yellow, 0.2) 50%, 
      rgba($electric-blue, 0.1) 100%
    );
    border: calc(0.3vh) solid $cyberpunk-yellow;
    color: lighten($cyberpunk-yellow, 15%);
    transform: scale(1.05);
    @include neon-glow-strong($cyberpunk-yellow);

    &::before {
      background: linear-gradient(135deg, 
        rgba($cyberpunk-yellow, 0.1) 0%, 
        transparent 50%, 
        rgba($electric-blue, 0.05) 100%
      );
    }
  }

  &:not(.active) {
    &:hover {
      background: linear-gradient(135deg, 
        rgba($cyberpunk-yellow, 0.2) 0%, 
        rgba($card-bg, 0.8) 50%, 
        rgba($cyberpunk-yellow, 0.1) 100%
      );
      border-color: rgba($cyberpunk-yellow, 0.6);
      color: lighten($cyberpunk-yellow, 10%);
      transform: translateY(-0.1vh) scale(1.02);
      @include neon-glow($cyberpunk-yellow);

      &::before {
        background: linear-gradient(135deg, 
          rgba($cyberpunk-yellow, 0.08) 0%, 
          rgba($cyberpunk-yellow, 0.03) 50%, 
          rgba($cyberpunk-yellow, 0.08) 100%
        );
      }
    }

    &:active {
      transform: translateY(0) scale(1.01);
      transition: all 0.15s ease;
    }
  }
}
</style>
