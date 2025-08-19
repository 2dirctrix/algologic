<!-- src/pages/game/components/in-game/components/player-status/PlayerCard.vue -->
<template>
  <div
    class="player-card"
    :class="{
      'is-active': player.status === 'active',
      'drop-target': isDragOver,
      'drop-valid': isDragOver && canAcceptDrop,
    }"
    @dragover.prevent="handleDragOver"
    @dragleave="handleDragLeave"
    @drop="handleDrop"
  >
    <!-- WebRTC 전체 화면 -->
    <div class="webrtc-video-container">
      <video
        ref="videoRef"
        autoplay
        playsinline
        muted
        class="webrtc-video"
        :style="{ display: isVideoAttached ? 'block' : 'none' }"
      ></video>
      <div v-if="!isVideoAttached" class="webrtc-placeholder">
        <div class="placeholder-icon">👤</div>
        <div class="placeholder-text">연결 중...</div>
      </div>
    </div>

    <!-- 오버레이 사용자 정보 -->
    <div class="player-info-overlay">
      <div class="player-name">{{ player.name }}</div>
    </div>

    <!-- 상태 인디케이터 -->
    <div class="player-status-indicator" :class="`status-${player.status}`"></div>

    <!-- 타이핑 프로그레스 바 -->
    <div class="typing-progress-container" :class="{ 'is-typing': isTyping }">
      <div class="typing-progress-bar">
        <div class="typing-progress-fill" :class="{ active: isTyping }"></div>
      </div>
      <div v-if="isTyping" class="typing-label">입력 중...</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';
import { useItem } from '@/game/services/useItem.js';
import { useSpell } from '@/game/services/useSpell.js';

// Props
const props = defineProps({
  player: {
    type: Object,
    required: true,
  },
  isTyping: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(['itemDropped']);

// Refs
const videoRef = ref(null);
const isVideoAttached = ref(false);
const isDragOver = ref(false);

// Stores
// webRTCStore는 현재 사용되지 않음

// Composables
const { sendItemToOpponent } = useItem();
const { useSpellToMe } = useSpell();

// Computed
const canAcceptDrop = computed(() => {
  // 자기 자신에게는 아이템을 적용할 수 없다고 가정
  // 실제로는 비즈니스 로직에 따라 결정
  return true;
});

// Drag and Drop handlers
const handleDragOver = event => {
  event.preventDefault();
  isDragOver.value = true;
  event.dataTransfer.dropEffect = canAcceptDrop.value ? 'move' : 'none';
};

const handleDragLeave = event => {
  // relatedTarget이 자식 요소가 아닌 경우에만 drag leave 처리
  if (!event.currentTarget.contains(event.relatedTarget)) {
    isDragOver.value = false;
  }
};

const handleDrop = async event => {
  event.preventDefault();
  isDragOver.value = false;

  if (!canAcceptDrop.value) return;

  try {
    const rawData = event.dataTransfer.getData('application/json');

    // 빈 데이터 체크
    if (!rawData || rawData.trim() === '') {
      console.warn('[PlayerCard] 빈 드래그 데이터가 전달되었습니다.');
      return;
    }

    const dragData = JSON.parse(rawData);
    const targetMemberId = props.player.id;

    // 드래그 데이터 유효성 검사
    if (!dragData || typeof dragData !== 'object') {
      console.error('[PlayerCard] 유효하지 않은 드래그 데이터 형식:', dragData);
      return;
    }

    // 드래그 데이터에 따라 아이템 또는 스펠 처리
    if (dragData.itemId) {
      // 아이템 드롭 처리
      await handleItemDrop(dragData, targetMemberId);
    } else if (dragData.spellId) {
      // 스펠 드롭 처리
      await handleSpellDrop(dragData, targetMemberId);
    } else {
      console.error('[PlayerCard] 알 수 없는 드래그 데이터:', dragData);
    }
  } catch (error) {
    console.error('[PlayerCard] 드래그 데이터 파싱 실패:', error);
  }
};

// 아이템 드롭 처리
const handleItemDrop = async (dragData, targetMemberId) => {
  const itemId = dragData.itemId;

  try {
    await sendItemToOpponent(itemId, targetMemberId);

    // 성공 시 부모 컴포넌트에 알림
    emit('itemDropped', {
      type: 'item',
      from: 'inventory',
      to: targetMemberId,
      itemId: itemId,
      targetPlayer: props.player,
      success: true,
    });
  } catch (socketError) {
    console.error('[PlayerCard] 아이템 사용 요청 전송 실패:', socketError);

    // 실패 시에도 부모에게 알림
    emit('itemDropped', {
      type: 'item',
      from: 'inventory',
      to: targetMemberId,
      itemId: itemId,
      targetPlayer: props.player,
      success: false,
      error: socketError,
    });
  }
};

// 스펠 드롭 처리
const handleSpellDrop = async (dragData, targetMemberId) => {
  const spellId = dragData.spellId;

  // 스파이 스펠(spellId: 3)만 대상 지정 가능
  if (spellId === 3) {
    try {
      // 스파이 스펠은 특별히 targetMemberId 정보를 포함하여 전송해야 함
      // 하지만 현재 useSpellToMe는 targetMemberId를 받지 않으므로
      // 전역 변수로 targetMemberId를 설정하고 스펠을 사용
      window.currentSpyTarget = targetMemberId;

      await useSpellToMe(spellId);

      // 성공 시 부모 컴포넌트에 알림
      emit('itemDropped', {
        type: 'spell',
        from: 'inventory',
        to: targetMemberId,
        spellId: spellId,
        targetPlayer: props.player,
        success: true,
      });
    } catch (socketError) {
      console.error('[PlayerCard] 스파이 스펠 사용 요청 전송 실패:', socketError);

      // 실패 시에도 부모에게 알림
      emit('itemDropped', {
        type: 'spell',
        from: 'inventory',
        to: targetMemberId,
        spellId: spellId,
        targetPlayer: props.player,
        success: false,
        error: socketError,
      });
    }
  } else {
    console.warn('[PlayerCard] 대상 지정이 불가능한 스펠입니다:', spellId);

    emit('itemDropped', {
      type: 'spell',
      from: 'inventory',
      to: targetMemberId,
      spellId: spellId,
      targetPlayer: props.player,
      success: false,
      error: new Error('대상 지정이 불가능한 스펠입니다.'),
    });
  }
};

// 라이프사이클 훅
onMounted(() => {
  // 다른 참가자의 경우 participant 정보를 통해 remote track 연결
  const participant = props.player.participant;
  if (participant?.trackPublication?.videoTrack && videoRef.value) {
    try {
      participant.trackPublication.videoTrack.attach(videoRef.value);
      isVideoAttached.value = true;
    } catch (error) {
      console.error('[PlayerCard] RemoteTrack 비디오 연결 실패:', error);
    }
  }
});

onUnmounted(() => {
  // Remote track detach
  if (isVideoAttached.value && videoRef.value) {
    const participant = props.player.participant;
    if (participant?.trackPublication?.videoTrack) {
      try {
        participant.trackPublication.videoTrack.detach(videoRef.value);
      } catch (error) {
        console.error('[PlayerCard] RemoteTrack Detach 실패:', error);
      }
    }
  }
  isVideoAttached.value = false;
});
</script>

<style lang="scss" scoped>
.player-card {
  background: rgba($card-bg, 0.6);
  border: 0.1vh solid rgba($text-primary, 0.2);
  border-radius: 0.6vh;
  height: 100%;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;

  &.is-active {
    @include neon-glow($electric-blue);
  }

  &.drop-target {
    border-color: $cyberpunk-yellow;
    background: rgba($cyberpunk-yellow, 0.1);
    transform: scale(1.02);

    &.drop-valid {
      border-color: $neon-green;
      background: rgba($neon-green, 0.1);
      box-shadow: 0 0 2vh rgba($neon-green, 0.3);
    }
  }
}

.player-info-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  z-index: 10;
  //background: linear-gradient(180deg, rgba($primary-bg, 0.8) 0%, rgba($primary-bg, 0.4) 70%, transparent 100%);
  padding: 0.5vh;
  backdrop-filter: blur(0.1vh);
}

.player-name {
  font-family: $font-primary;
  font-size: 2.5vh;
  color: $text-gold;
  margin-bottom: 0.3vh;
  text-shadow: 0 0 0.5vh rgba($primary-bg, 0.8);
  font-weight: $font-weight-bold;
}

.webrtc-video-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 1;
}

.webrtc-video {
  width: 100%;
  height: 100%;
  object-fit: cover;
  background: rgba($primary-bg, 0.9);
  border-radius: 0.6vh;
}

.webrtc-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba($electric-blue, 0.05) 0%, rgba($cyberpunk-pink, 0.05) 100%);
  gap: 1vh;
}

.placeholder-icon {
  font-size: 3vh;
  opacity: 0.3;
}

.placeholder-text {
  font-family: $font-primary;
  font-size: 1.4vh;
  color: rgba($text-primary, 0.5);
}

.player-status-indicator {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 0.3vh;
  background: rgba($text-primary, 0.2);
  z-index: 20;
  border-bottom-left-radius: 0.6vh;
  border-bottom-right-radius: 0.6vh;

  &.status-active {
    background: $neon-green;
    box-shadow: 0 0 10px $neon-green;
  }

  &.status-thinking {
    background: $cyberpunk-yellow;
    box-shadow: 0 0 10px $cyberpunk-yellow;
  }

  &.status-submitted {
    background: $electric-blue;
    box-shadow: 0 0 10px $electric-blue;
  }
}

// 타이핑 프로그레스 바 컨테이너
.typing-progress-container {
  position: absolute;
  bottom: 0.3vh; // 기존 상태 인디케이터 위에 위치
  left: 0;
  right: 0;
  z-index: 25;
  height: 1.2vh; // 두꺼운 바
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease-in-out; // 빠른 나타남/사라짐

  &.is-typing {
    opacity: 1;
  }
}

.typing-progress-bar {
  width: 100%;
  height: 100%;
  background: rgba($primary-bg, 0.8);
  border-radius: 0 0 0.6vh 0.6vh;
  overflow: hidden;
  border: 0.1vh solid rgba($electric-blue, 0.3);
  position: relative;
}

.typing-progress-fill {
  height: 100%;
  width: 0%;
  background: linear-gradient(90deg, $electric-blue 0%, $cyberpunk-pink 50%, $neon-green 100%);
  border-radius: 0 0 0.5vh 0.5vh;
  transition: width 0.1s ease-out; // 훨씬 빠른 전환
  position: relative;
  overflow: hidden;

  &.active {
    width: 100%; // 즉시 100%로 설정
    animation: typing-pulse 0.8s ease-in-out infinite; // 빠른 펄스 애니메이션
  }

  &::after {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent 0%, rgba(255, 255, 255, 0.6) 50%, transparent 100%);
    animation: shimmer 0.8s ease-in-out infinite; // 더 빠른 shimmer
  }
}

.typing-label {
  position: absolute;
  top: -2.5vh;
  left: 50%;
  transform: translateX(-50%);
  font-family: $font-primary;
  font-size: 1vh;
  color: $electric-blue;
  font-weight: $font-weight-bold;
  background: rgba($primary-bg, 0.9);
  padding: 0.2vh 0.6vh;
  border-radius: 0.3vh;
  white-space: nowrap;
  border: 0.05vh solid $electric-blue;
  text-shadow: 0 0 0.3vh $electric-blue;
  animation: pulse-glow 1.5s ease-in-out infinite;
}

@keyframes typing-pulse {
  0% {
    opacity: 0.8;
    transform: scaleY(0.9);
  }
  50% {
    opacity: 1;
    transform: scaleY(1);
  }
  100% {
    opacity: 0.8;
    transform: scaleY(0.9);
  }
}

@keyframes shimmer {
  0% {
    left: -100%;
  }
  100% {
    left: 100%;
  }
}

@keyframes pulse-glow {
  0%,
  100% {
    box-shadow: 0 0 0.5vh rgba($electric-blue, 0.5);
  }
  50% {
    box-shadow: 0 0 1vh rgba($electric-blue, 0.8);
  }
}
</style>
