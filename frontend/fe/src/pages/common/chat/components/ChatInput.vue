<template>
  <div class="chat-input">
    <div class="input-container">
      <input
        v-model="message"
        type="text"
        :placeholder="props.placeholder"
        class="message-input"
        @keyup.enter="sendMessage"
        @keydown.enter.prevent
        maxlength="200"
      />
    </div>

    <button class="send-button" @click="sendMessage" :disabled="!message.trim()">
      <span class="send-icon">></span>
    </button>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const props = defineProps({
  placeholder: {
    type: String,
    default: '메시지를 입력하세요...',
  },
});

const emit = defineEmits(['send-message']);

const message = ref('');

const sendMessage = () => {
  if (message.value.trim()) {
    emit('send-message', message.value.trim());
    message.value = '';
  }
};
</script>

<style lang="scss" scoped>
.chat-input {
  display: flex;
  gap: calc(1.5vh);
  align-items: center;
  width: 100%;
  box-sizing: border-box;
  height: calc(4vh);
}

.input-container {
  flex: 1;
  min-width: 0;
  height: 100%;
  display: flex;
  align-items: center;
}

.message-input {
  width: 100%;
  height: calc(4vh);
  background: rgba($text-primary, $opacity-minimal);
  border: calc(0.3vh) solid $electric-blue;
  border-radius: $radius-button;
  padding: calc(1vh) calc(2vh);
  font-family: $font-primary;
  font-size: calc(1.5vh);
  font-weight: $font-weight-regular;
  color: $text-primary;
  line-height: 1.4;
  box-sizing: border-box;

  &::placeholder {
    color: $text-primary;
    opacity: $opacity-placeholder;
    font-size: calc(1.2vh);
  }

  &:focus {
    outline: none;
    @include neon-glow($electric-blue);
  }
}

.send-button {
  width: calc(5vh);
  height: calc(4vh);
  background: rgba($electric-blue, $opacity-active);
  border: calc(0.3vh) solid $electric-blue;
  border-radius: $radius-button;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all $transition-base;
  box-sizing: border-box;
  flex-shrink: 0;

  @include neon-glow($electric-blue);

  &:hover:not(:disabled) {
    background: rgba($electric-blue, 0.9);
    border-color: $electric-blue;
  }

  &:disabled {
    opacity: $opacity-placeholder;
    cursor: not-allowed;
    background: rgba($text-primary, $opacity-minimal);
    border-color: rgba($electric-blue, 0.3);
  }
}

.send-icon {
  font-family: $font-primary;
  font-size: calc(2vh);
  font-weight: $font-weight-bold;
  color: $text-primary;
  line-height: 1;
}
</style>
