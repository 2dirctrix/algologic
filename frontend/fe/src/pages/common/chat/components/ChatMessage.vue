<template>
  <div class="chat-message">
    <div class="message-row">
      <span class="message-username">{{ username }}</span>
      <span class="message-content">{{ content }}</span>
      <span class="message-timestamp">{{ timestamp }}</span>
    </div>
  </div>
</template>

<script setup>
defineProps({
  username: {
    type: String,
    required: true,
  },
  content: {
    type: String,
    required: true,
  },
  timestamp: {
    type: String,
    required: true,
  },
});
</script>

<style lang="scss" scoped>
.chat-message {
  margin-bottom: calc(1.5vh);

  &:last-child {
    margin-bottom: 0;
  }
}

.message-row {
  display: flex;
  align-items: flex-end;
  gap: calc(1vh);
  line-height: 1.3;
}

.message-username {
  font-family: $font-primary;
  font-size: 1.8vh;
  font-weight: $font-weight-medium;
  color: $text-gold;
  flex-shrink: 0;
}

.message-content {
  font-family: $font-primary;
  font-size: 2vh;
  font-weight: $font-weight-regular;
  color: $text-primary;
  opacity: $opacity-text-secondary;
  word-wrap: break-word;
  flex: 1;
}

.message-timestamp {
  font-family: $font-primary;
  font-size: 1.6vh;
  font-weight: $font-weight-regular;
  color: $text-primary;
  opacity: $opacity-placeholder;
  flex-shrink: 0;
  align-self: flex-end;
}
</style>
