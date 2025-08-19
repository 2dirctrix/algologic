<template>
  <div class="room-name-input">
    <div class="input-header">
      <label class="input-label">방 이름</label>
      <span class="char-counter">{{ currentLength }} / 30</span>
    </div>
    <div class="input-wrapper">
      <input
        v-model="roomName"
        type="text"
        class="room-name-field"
        placeholder="배틀룸 이름을 입력하세요..."
        maxlength="30"
        @input="updateRoomName"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

const props = defineProps({
  modelValue: {
    type: String,
    default: '',
  },
});

const emit = defineEmits(['update:modelValue']);

const roomName = ref(props.modelValue);

const currentLength = computed(() => roomName.value.length);

const updateRoomName = () => {
  emit('update:modelValue', roomName.value);
};
</script>

<style scoped lang="scss">
.room-name-input {
  width: 100%;
  margin-bottom: calc(2vh);
}

.input-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: calc(1.5vh);
}

.input-label {
  font-family: $font-primary;
  font-weight: $font-weight-semibold;
  font-size: calc(1.7vh);
  color: $text-primary;
}

.char-counter {
  font-family: $font-primary;
  font-weight: $font-weight-regular;
  font-size: calc(1.2vh);
  color: rgba($text-primary, 0.6);
}

.input-wrapper {
  position: relative;
}

.room-name-field {
  width: 100%;
  height: calc(5.4vh);
  background: linear-gradient(135deg, 
    rgba($electric-blue, 0.05) 0%, 
    rgba($card-bg, 0.7) 50%, 
    rgba($electric-blue, 0.03) 100%
  );
  border: calc(0.2vh) solid rgba($electric-blue, 0.4);
  border-radius: calc(1.2vh);
  padding: calc(1.3vh) calc(3vh);
  font-family: $font-primary;
  font-weight: $font-weight-medium;
  font-size: calc(1.6vh);
  color: $text-primary;
  box-sizing: border-box;
  transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  position: relative;

  &::placeholder {
    color: rgba($text-primary, 0.5);
    transition: all 0.3s ease;
  }

  &:hover {
    background: linear-gradient(135deg, 
      rgba($electric-blue, 0.1) 0%, 
      rgba($card-bg, 0.8) 50%, 
      rgba($electric-blue, 0.05) 100%
    );
    border-color: rgba($electric-blue, 0.6);
    @include neon-glow($electric-blue);

    &::placeholder {
      color: rgba($text-primary, 0.7);
    }
  }

  &:focus {
    outline: none;
    background: linear-gradient(135deg, 
      rgba($cyberpunk-yellow, 0.08) 0%, 
      rgba($card-bg, 0.85) 50%, 
      rgba($cyberpunk-yellow, 0.05) 100%
    );
    border: calc(0.3vh) solid $cyberpunk-yellow;
    color: lighten($text-primary, 10%);
    @include neon-glow-strong($cyberpunk-yellow);
    transform: translateY(-0.1vh);

    &::placeholder {
      color: rgba($cyberpunk-yellow, 0.6);
      transform: translateY(-0.2vh);
    }
  }

  &:focus-within {
    box-shadow: 
      0 0 20px rgba($cyberpunk-yellow, 0.3),
      inset 0 0 10px rgba($cyberpunk-yellow, 0.1);
  }
}
</style>
