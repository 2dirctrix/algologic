<template>
  <div class="modal-action-buttons">
    <button class="cancel-button" @click="handleCancel">취소</button>

    <button class="create-button" @click="handleCreate" :disabled="!canCreate">배틀룸 생성</button>
  </div>
</template>

<script setup>
const props = defineProps({
  canCreate: {
    type: Boolean,
    default: true,
  },
});

const emit = defineEmits(['cancel', 'create']);

const playClickSound = () => {
  try {
    const audio = new Audio('/audio/interaction/common-click-sound.wav');
    audio.volume = 0.6;
    audio.play().catch(error => {
      console.warn('Audio play failed:', error);
    });
  } catch (error) {
    console.warn('Audio creation failed:', error);
  }
};

const handleCancel = () => {
  playClickSound();
  emit('cancel');
};

const handleCreate = () => {
  if (props.canCreate) {
    playClickSound();
    emit('create');
  }
};
</script>

<style scoped lang="scss">
.modal-action-buttons {
  display: flex;
  gap: calc(2vh);
  width: 100%;
  margin-top: auto;
}

.cancel-button,
.create-button {
  flex: 1;
  height: calc(5.4vh);
  border-radius: calc(1.2vh);
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: calc(2vh);
  line-height: 1.2;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  position: relative;
  overflow: hidden;
  text-transform: uppercase;
  letter-spacing: 0.1em;

  display: flex;
  align-items: center;
  justify-content: center;

  // 내부 글로우 효과
  &::before {
    content: '';
    position: absolute;
    inset: 0.2vh;
    border-radius: calc(1vh);
    transition: all 0.4s ease;
    z-index: -1;
  }
}

.cancel-button {
  background: linear-gradient(135deg, 
    rgba($electric-blue, 0.15) 0%, 
    rgba($card-bg, 0.8) 50%, 
    rgba($electric-blue, 0.1) 100%
  );
  border: calc(0.3vh) solid rgba($electric-blue, 0.5);
  color: $electric-blue;

  &::before {
    background: linear-gradient(135deg, 
      rgba($electric-blue, 0.05) 0%, 
      transparent 50%, 
      rgba($electric-blue, 0.05) 100%
    );
  }

  &:hover {
    background: linear-gradient(135deg, 
      rgba($electric-blue, 0.25) 0%, 
      rgba($card-bg, 0.9) 50%, 
      rgba($electric-blue, 0.2) 100%
    );
    border-color: rgba($electric-blue, 0.8);
    color: lighten($electric-blue, 15%);
    transform: translateY(-0.2vh) scale(1.02);
    @include neon-glow($electric-blue);

    &::before {
      background: linear-gradient(135deg, 
        rgba($electric-blue, 0.1) 0%, 
        rgba($electric-blue, 0.05) 50%, 
        rgba($electric-blue, 0.1) 100%
      );
    }
  }

  &:active {
    transform: translateY(-0.1vh) scale(1.01);
    transition: all 0.15s ease;
  }
}

.create-button {
  background: linear-gradient(135deg, 
    rgba($cyberpunk-yellow, 0.3) 0%, 
    rgba($cyberpunk-yellow, 0.2) 50%, 
    rgba($hot-pink, 0.2) 100%
  );
  border: calc(0.3vh) solid $cyberpunk-yellow;
  color: $cyberpunk-yellow;
  box-shadow: 
    0px calc(0.4vh) calc(1.5vh) 0px rgba($cyberpunk-yellow, 0.3),
    inset 0 0 8px rgba($cyberpunk-yellow, 0.1);

  &::before {
    background: linear-gradient(135deg, 
      rgba($cyberpunk-yellow, 0.1) 0%, 
      transparent 50%, 
      rgba($hot-pink, 0.05) 100%
    );
  }

  &:hover:not(:disabled) {
    background: linear-gradient(135deg, 
      rgba($cyberpunk-yellow, 0.4) 0%, 
      rgba($cyberpunk-yellow, 0.3) 50%, 
      rgba($hot-pink, 0.25) 100%
    );
    border-color: lighten($cyberpunk-yellow, 15%);
    color: lighten($cyberpunk-yellow, 20%);
    transform: translateY(-0.3vh) scale(1.02);
    @include neon-glow-strong($cyberpunk-yellow);
    box-shadow: 
      0px calc(0.6vh) calc(2vh) 0px rgba($cyberpunk-yellow, 0.5),
      inset 0 0 12px rgba($cyberpunk-yellow, 0.2);

    &::before {
      background: linear-gradient(135deg, 
        rgba($cyberpunk-yellow, 0.15) 0%, 
        rgba($hot-pink, 0.05) 50%, 
        rgba($cyberpunk-yellow, 0.1) 100%
      );
    }
  }

  &:active:not(:disabled) {
    transform: translateY(-0.1vh) scale(1.01);
    transition: all 0.15s ease;
  }

  &:disabled {
    background: linear-gradient(135deg, 
      rgba($cyberpunk-yellow, 0.1) 0%, 
      rgba($card-bg, 0.5) 50%, 
      rgba($cyberpunk-yellow, 0.05) 100%
    );
    border-color: rgba($cyberpunk-yellow, 0.3);
    color: rgba($cyberpunk-yellow, 0.4);
    cursor: not-allowed;
    transform: none;
    box-shadow: none;

    &::before {
      background: rgba($cyberpunk-yellow, 0.02);
    }
  }
}
</style>
