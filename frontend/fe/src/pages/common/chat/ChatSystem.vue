<template>
  <div class="chat-system">
    <!-- Chat Header -->
    <div class="chat-header">
      <h4 class="chat-title">{{ chatTitle }}</h4>
    </div>

    <!-- Chat Messages -->
    <div class="chat-messages" ref="messagesContainer">
      <ChatMessage
        v-for="message in messages"
        :key="message.id"
        :username="message.username"
        :content="message.content"
        :timestamp="message.timestamp"
      />
    </div>

    <!-- Chat Input -->
    <ChatInput :placeholder="inputPlaceholder" @send-message="handleSendMessage" />
  </div>
</template>

<script setup>
import { ref, computed, nextTick, watch, onUnmounted } from 'vue';
import ChatMessage from './components/ChatMessage.vue';
import ChatInput from './components/ChatInput.vue';
import { handleChatMessage } from './handler/chatHandler';
import { sendChatMessage } from './handler/chatSendHandler';
import { useWebSocketStore } from '@/stores/websocket/websocket.js';
import websocketService from '@/websocket/services/WebSocketService.js';
import { WS_ENDPOINTS } from '@/websocket/constants/endpoints.js';

const props = defineProps({
  chatType: {
    type: String,
    default: 'global', // 'global' | 'ingame'
    validator: value => ['global', 'ingame'].includes(value),
  },
  roomId: {
    type: [String, Number],
    default: null, // 인게임 채팅 시 필요
  },
});

// 채팅 제목 및 플레이스홀더
const chatTitle = computed(() => {
  return props.chatType === 'global' ? '전체 채팅' : '인게임 채팅';
});

const inputPlaceholder = computed(() => {
  return props.chatType === 'global' ? '메시지를 입력하세요...' : '팀원에게 메시지를 보내세요...';
});

// 메시지 상태
const messages = ref([]);
const messagesContainer = ref(null);
const webSocketStore = useWebSocketStore();
let subscriptionId = null; // 구독 ID 저장

// 채팅 메시지 사운드 재생
const playChatMessageSound = () => {
  try {
    const audio = new Audio('/audio/interaction/chat-message-sound.wav');
    audio.volume = 0.05;
    audio.play().catch(error => {
      console.warn('Chat message sound play failed:', error);
    });
  } catch (error) {
    console.warn('Chat message sound creation failed:', error);
  }
};

// 채팅 메시지 수신 핸들러
const handleMessageReceived = messageData => {
  try {
    const parsedMessage = typeof messageData === 'string' ? JSON.parse(messageData) : messageData;
    const processedMessage = handleChatMessage(parsedMessage);

    if (processedMessage) {
      const messageForTemplate = {
        id: processedMessage.id,
        username: processedMessage.nickname,
        content: processedMessage.content,
        timestamp: processedMessage.time,
      };

      messages.value.push(messageForTemplate);
      playChatMessageSound();
      scrollToBottom();
    }
  } catch (error) {
    console.error('채팅 메시지 처리 중 오류:', error);
  }
};

// 메시지 전송 핸들러
const handleSendMessage = async content => {
  try {
    let endpoint;

    if (props.chatType === 'global') {
      endpoint = WS_ENDPOINTS.CHAT.MAIN_SEND;
    } else if (props.chatType === 'ingame' && props.roomId) {
      endpoint = WS_ENDPOINTS.CHAT.ROOM_SEND(props.roomId);
    } else {
      console.error('유효하지 않은 채팅 타입 또는 방 ID가 누락되었습니다.');
      return;
    }

    await sendChatMessage(content, endpoint);
  } catch (error) {
    console.error('메시지 전송 중 오류:', error);
  }
};

// WebSocket 연결 상태를 watch하여 연결 완료 후 채팅 구독 시작
watch(
  () => webSocketStore.isConnected,
  isConnected => {
    if (isConnected && !subscriptionId) {
      try {
        let endpoint;

        if (props.chatType === 'global') {
          endpoint = WS_ENDPOINTS.CHAT.MAIN_SUBSCRIBE;
        } else if (props.chatType === 'ingame' && props.roomId) {
          endpoint = WS_ENDPOINTS.CHAT.ROOM_SUBSCRIBE(props.roomId);
        }

        if (endpoint) {
          subscriptionId = websocketService.subscribe(endpoint, handleMessageReceived);
        }
      } catch (error) {}
    }
  },
  { immediate: true },
);

// 컴포넌트 언마운트 시 구독 해제
onUnmounted(() => {
  if (subscriptionId) {
    try {
      websocketService.unsubscribe(subscriptionId);
    } catch (error) {
      console.error('[CHAT SYSTEM] 채팅 구독 해제 실패:', error);
    }
  }
});

const scrollToBottom = async () => {
  await nextTick();
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
  }
};
</script>

<style lang="scss" scoped>
.chat-system {
  width: 100%;
  background: rgba($card-bg, 0.6);
  //backdrop-filter: blur(1.5vh);
  border: calc(0.3vh) solid rgba($cyberpunk-yellow, 0.7);
  border-radius: calc(2vh);
  box-shadow: inset 0 0 calc(1.5vh) 0 rgba($cyberpunk-pink, 0.3);
  padding: calc(2vh) calc(3vh);
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 0;
  max-height: 100%;
  box-sizing: border-box;

  @include neon-glow($cyberpunk-yellow);
}

.chat-header {
  flex: 0 0 auto;
  margin-bottom: calc(0.8vh);
}

.chat-title {
  font-family: $font-primary;
  font-size: calc(1.5vh);
  font-weight: $font-weight-semibold;
  color: $text-primary;
  margin: 0;
}

.chat-messages {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  margin-bottom: calc(2vh);

  &::-webkit-scrollbar {
    width: calc(0.5vh);
  }

  &::-webkit-scrollbar-track {
    background: rgba($text-primary, $opacity-minimal);
    border-radius: calc(0.3vh);
  }

  &::-webkit-scrollbar-thumb {
    background: rgba($cyberpunk-yellow, 0.6);
    border-radius: calc(0.3vh);

    &:hover {
      background: rgba($cyberpunk-yellow, 0.8);
    }
  }
}
</style>
