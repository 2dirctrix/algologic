<template>
  <Teleport to="body">
    <Transition name="alert-fade">
      <div v-if="alertStore.isVisible" class="alert-overlay" @click="handleOverlayClick">
        <Transition name="alert-slide">
          <div v-if="alertStore.isVisible" class="alert-modal" :class="alertTypeClass" @click.stop>
            <!-- 헤더 -->
            <div class="alert-header">
              <span class="alert-header-text">NOTIFICATION</span>
            </div>

            <!-- 아이콘 -->
            <div class="alert-icon">
              <component :is="alertIcon" class="icon" />
            </div>

            <!-- 메시지 -->
            <div class="alert-content">
              <p class="alert-message">{{ alertStore.message }}</p>
            </div>

            <!-- 닫기 버튼 -->
            <button class="alert-close-btn" @click="alertStore.hideAlert">
              <span class="close-text">CLOSE</span>
            </button>
          </div>
        </Transition>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
import { computed } from 'vue';
import { useAlertStore } from '@/stores/alertStore';

const alertStore = useAlertStore();

// 알림 타입에 따른 CSS 클래스
const alertTypeClass = computed(() => {
  return `alert-${alertStore.type}`;
});

// 알림 타입에 따른 아이콘
const alertIcon = computed(() => {
  const icons = {
    success: 'CheckCircleIcon',
    error: 'XCircleIcon',
    warning: 'ExclamationTriangleIcon',
    info: 'InformationCircleIcon',
  };
  return icons[alertStore.type] || icons.info;
});

const handleOverlayClick = () => {
  alertStore.hideAlert();
};
</script>

<style lang="scss" scoped>
.alert-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(0.5vh);
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2vh;
}

.alert-modal {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  gap: 2vh;
  width: 45vh;
  height: 45vh;
  padding: 3vh 3vh 2vh 3vh;
  background: 
    linear-gradient(135deg, rgba($card-bg, 0.95) 0%, rgba($primary-bg, 0.85) 50%, rgba($card-bg, 0.9) 100%),
    repeating-linear-gradient(
      45deg,
      transparent,
      transparent 2px,
      rgba($electric-blue, 0.03) 2px,
      rgba($electric-blue, 0.03) 4px
    );
  border-radius: 1vh;
  border: 0.3vh solid rgba($electric-blue, 0.5);
  box-shadow:
    0 0 3vh rgba($electric-blue, 0.3),
    0 2vh 4vh rgba(0, 0, 0, 0.4),
    inset 0 0.1vh 0 rgba(255, 255, 255, 0.1),
    inset 0 0 2vh rgba($electric-blue, 0.05);
  backdrop-filter: blur(2vh);
  
  // 사이버펑크 코너 디테일
  &::before {
    content: '';
    position: absolute;
    top: -0.3vh;
    left: -0.3vh;
    width: 4vh;
    height: 4vh;
    border-top: 0.4vh solid $electric-blue;
    border-left: 0.4vh solid $electric-blue;
    border-radius: 0.5vh 0 0 0;
  }
  
  &::after {
    content: '';
    position: absolute;
    bottom: -0.3vh;
    right: -0.3vh;
    width: 4vh;
    height: 4vh;
    border-bottom: 0.4vh solid $electric-blue;
    border-right: 0.4vh solid $electric-blue;
    border-radius: 0 0 0.5vh 0;
  }

  // 타입별 스타일
  &.alert-success {
    border-color: rgba($neon-green, 0.7);
    box-shadow:
      0 0 4vh rgba($neon-green, 0.4),
      0 2vh 4vh rgba(0, 0, 0, 0.4),
      inset 0 0.1vh 0 rgba(255, 255, 255, 0.1),
      inset 0 0 3vh rgba($neon-green, 0.08);

    &::before {
      border-color: $neon-green;
      box-shadow: 0 0 1vh rgba($neon-green, 0.6);
    }
    
    &::after {
      border-color: $neon-green;
      box-shadow: 0 0 1vh rgba($neon-green, 0.6);
    }

    .alert-icon {
      color: $neon-green;
      background: 
        radial-gradient(circle, rgba($neon-green, 0.3) 0%, transparent 70%),
        linear-gradient(45deg, rgba($neon-green, 0.1) 0%, transparent 100%);
      border: 0.2vh solid rgba($neon-green, 0.4);
      box-shadow: 
        0 0 2vh rgba($neon-green, 0.3),
        inset 0 0 1vh rgba($neon-green, 0.1);
    }
  }

  &.alert-error {
    border-color: rgba(#ff1744, 0.7);
    box-shadow:
      0 0 4vh rgba(#ff1744, 0.4),
      0 2vh 4vh rgba(0, 0, 0, 0.4),
      inset 0 0.1vh 0 rgba(255, 255, 255, 0.1),
      inset 0 0 3vh rgba(#ff1744, 0.08);

    &::before {
      border-color: #ff1744;
      box-shadow: 0 0 1vh rgba(#ff1744, 0.6);
    }
    
    &::after {
      border-color: #ff1744;
      box-shadow: 0 0 1vh rgba(#ff1744, 0.6);
    }

    .alert-icon {
      color: #ff1744;
      background: 
        radial-gradient(circle, rgba(#ff1744, 0.3) 0%, transparent 70%),
        linear-gradient(45deg, rgba(#ff1744, 0.1) 0%, transparent 100%);
      border: 0.2vh solid rgba(#ff1744, 0.4);
      box-shadow: 
        0 0 2vh rgba(#ff1744, 0.3),
        inset 0 0 1vh rgba(#ff1744, 0.1);
    }
  }

  &.alert-warning {
    border-color: rgba($cyberpunk-yellow, 0.7);
    box-shadow:
      0 0 4vh rgba($cyberpunk-yellow, 0.4),
      0 2vh 4vh rgba(0, 0, 0, 0.4),
      inset 0 0.1vh 0 rgba(255, 255, 255, 0.1),
      inset 0 0 3vh rgba($cyberpunk-yellow, 0.08);

    &::before {
      border-color: $cyberpunk-yellow;
      box-shadow: 0 0 1vh rgba($cyberpunk-yellow, 0.6);
    }
    
    &::after {
      border-color: $cyberpunk-yellow;
      box-shadow: 0 0 1vh rgba($cyberpunk-yellow, 0.6);
    }

    .alert-icon {
      color: $cyberpunk-yellow;
      background: 
        radial-gradient(circle, rgba($cyberpunk-yellow, 0.3) 0%, transparent 70%),
        linear-gradient(45deg, rgba($cyberpunk-yellow, 0.1) 0%, transparent 100%);
      border: 0.2vh solid rgba($cyberpunk-yellow, 0.4);
      box-shadow: 
        0 0 2vh rgba($cyberpunk-yellow, 0.3),
        inset 0 0 1vh rgba($cyberpunk-yellow, 0.1);
    }
  }

  &.alert-info {
    border-color: rgba($electric-blue, 0.7);
    box-shadow:
      0 0 4vh rgba($electric-blue, 0.4),
      0 2vh 4vh rgba(0, 0, 0, 0.4),
      inset 0 0.1vh 0 rgba(255, 255, 255, 0.1),
      inset 0 0 3vh rgba($electric-blue, 0.08);

    &::before {
      border-color: $electric-blue;
      box-shadow: 0 0 1vh rgba($electric-blue, 0.6);
    }
    
    &::after {
      border-color: $electric-blue;
      box-shadow: 0 0 1vh rgba($electric-blue, 0.6);
    }

    .alert-icon {
      color: $electric-blue;
      background: 
        radial-gradient(circle, rgba($electric-blue, 0.3) 0%, transparent 70%),
        linear-gradient(45deg, rgba($electric-blue, 0.1) 0%, transparent 100%);
      border: 0.2vh solid rgba($electric-blue, 0.4);
      box-shadow: 
        0 0 2vh rgba($electric-blue, 0.3),
        inset 0 0 1vh rgba($electric-blue, 0.1);
    }
  }
}

.alert-icon {
  flex-shrink: 0;
  width: 6vh;
  height: 6vh;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;

  .icon {
    width: 3.5vh;
    height: 3.5vh;
    stroke-width: 2;
  }
}

.alert-content {
  flex: 1;
  text-align: center;
  max-width: 100%;

  .alert-message {
    font-family: $font-primary;
    font-size: 2.2vh;
    font-weight: $font-weight-medium;
    color: $text-primary;
    line-height: 1.5;
    margin: 0;
    word-break: keep-all;
    text-shadow: 0 0 0.5vh rgba($text-primary, 0.3);
    letter-spacing: 0.02em;
  }
}

.alert-header {
  width: 100%;
  text-align: center;
  flex-shrink: 0;
}

.alert-header-text {
  font-family: $font-accent;
  font-size: 1.8vh;
  font-weight: $font-weight-bold;
  color: $text-primary;
  text-transform: uppercase;
  letter-spacing: 0.3em;
  text-shadow: 0 0 1vh rgba($text-primary, 0.5);
  position: relative;
  
  &::before {
    content: '';
    position: absolute;
    left: -2vh;
    top: 50%;
    transform: translateY(-50%);
    width: 1.5vh;
    height: 0.2vh;
    background: linear-gradient(90deg, transparent 0%, $electric-blue 100%);
  }
  
  &::after {
    content: '';
    position: absolute;
    right: -2vh;
    top: 50%;
    transform: translateY(-50%);
    width: 1.5vh;
    height: 0.2vh;
    background: linear-gradient(90deg, $electric-blue 0%, transparent 100%);
  }
}

.alert-close-btn {
  width: calc(100% - 2vh);
  height: 4vh;
  background: linear-gradient(135deg, rgba($card-bg, 0.8) 0%, rgba($primary-bg, 0.6) 50%, rgba($card-bg, 0.8) 100%);
  border: 0.2vh solid rgba($text-primary, 0.3);
  border-radius: 0.5vh;
  color: $text-primary;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: $font-primary;
  font-size: 1.6vh;
  font-weight: $font-weight-semibold;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
  position: relative;
  overflow: hidden;
  flex-shrink: 0;
  
  &::before {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(90deg, transparent 0%, rgba($text-primary, 0.1) 50%, transparent 100%);
    opacity: 0;
    transform: translateX(-100%);
    transition: all 0.6s ease;
  }

  &:hover {
    background: linear-gradient(135deg, rgba($text-primary, 0.15) 0%, rgba($primary-bg, 0.8) 50%, rgba($text-primary, 0.15) 100%);
    border-color: rgba($text-primary, 0.6);
    color: lighten($text-primary, 20%);
    transform: translateY(-0.1vh);
    box-shadow: 
      0 0 1vh rgba($text-primary, 0.3),
      inset 0 0 1vh rgba($text-primary, 0.1);
    
    &::before {
      opacity: 1;
      transform: translateX(100%);
    }
  }

  &:active {
    transform: translateY(0) scale(0.98);
  }

  .close-text {
    position: relative;
    z-index: 1;
  }
}

// 아이콘 컴포넌트들 (SVG 아이콘)
.CheckCircleIcon {
  // 성공 아이콘
  &::before {
    content: '';
    display: block;
    width: 100%;
    height: 100%;
    background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' stroke='currentColor'%3E%3Cpath stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z'/%3E%3C/svg%3E")
      no-repeat center;
    background-size: contain;
  }
}

.XCircleIcon {
  // 에러 아이콘
  &::before {
    content: '';
    display: block;
    width: 100%;
    height: 100%;
    background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' stroke='currentColor'%3E%3Cpath stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z'/%3E%3C/svg%3E")
      no-repeat center;
    background-size: contain;
  }
}

.ExclamationTriangleIcon {
  // 경고 아이콘
  &::before {
    content: '';
    display: block;
    width: 100%;
    height: 100%;
    background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' stroke='currentColor'%3E%3Cpath stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5c-.77.833.192 2.5 1.732 2.5z'/%3E%3C/svg%3E")
      no-repeat center;
    background-size: contain;
  }
}

.InformationCircleIcon {
  // 정보 아이콘
  &::before {
    content: '';
    display: block;
    width: 100%;
    height: 100%;
    background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' stroke='currentColor'%3E%3Cpath stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z'/%3E%3C/svg%3E")
      no-repeat center;
    background-size: contain;
  }
}

// 애니메이션
.alert-fade-enter-active,
.alert-fade-leave-active {
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.alert-fade-enter-from,
.alert-fade-leave-to {
  opacity: 0;
}

.alert-slide-enter-active,
.alert-slide-leave-active {
  transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.alert-slide-enter-from {
  opacity: 0;
  transform: translateY(-3vh) scale(0.9);
}

.alert-slide-leave-to {
  opacity: 0;
  transform: translateY(3vh) scale(0.9);
}

// 반응형
@media (max-width: 768px) {
  .alert-modal {
    width: 80vw;
    height: 80vw;
    max-width: 50vh;
    max-height: 50vh;
    padding: 2vh;
    gap: 2vh;
    
    &::before, &::after {
      width: 3vh;
      height: 3vh;
      border-width: 0.3vh;
    }
  }

  .alert-icon {
    width: 5vh;
    height: 5vh;

    .icon {
      width: 3vh;
      height: 3vh;
    }
  }

  .alert-content .alert-message {
    font-size: 2vh;
  }
  
  .alert-header-text {
    font-size: 1.5vh;
    letter-spacing: 0.2em;
    
    &::before, &::after {
      width: 1vh;
      height: 0.15vh;
    }
    
    &::before {
      left: -1.5vh;
    }
    
    &::after {
      right: -1.5vh;
    }
  }
  
  .alert-close-btn {
    width: calc(100% - 1vh);
    height: 3.5vh;
    font-size: 1.4vh;
  }
}
</style>
