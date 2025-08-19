import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useAlertStore = defineStore('alert', () => {
  const isVisible = ref(false);
  const message = ref('');
  const type = ref('info'); // 'info', 'success', 'warning', 'error'
  const duration = ref(3000); // 기본 3초
  
  let timeoutId = null;

  const playErrorSound = () => {
    try {
      const audio = new Audio('/audio/interaction/error-sound.wav');
      audio.volume = 0.6;
      audio.play().catch(error => {
        console.warn('Error sound play failed:', error);
      });
    } catch (error) {
      console.warn('Error sound creation failed:', error);
    }
  };

  const showAlert = (alertMessage, alertType = 'info', alertDuration = 3000) => {
    // 기존 타이머가 있으면 제거
    if (timeoutId) {
      clearTimeout(timeoutId);
    }

    message.value = alertMessage;
    type.value = alertType;
    duration.value = alertDuration;
    isVisible.value = true;

    // 에러 타입일 때 사운드 재생
    if (alertType === 'error') {
      playErrorSound();
    }

    // 자동 숨김 (duration이 0이면 자동 숨김 안함)
    if (alertDuration > 0) {
      timeoutId = setTimeout(() => {
        hideAlert();
      }, alertDuration);
    }
  };

  const hideAlert = () => {
    isVisible.value = false;
    
    // 애니메이션 완료 후 상태 초기화
    setTimeout(() => {
      if (!isVisible.value) {
        message.value = '';
        type.value = 'info';
      }
    }, 300);
  };

  // 편의 메소드들
  const showSuccess = (message, duration = 3000) => {
    showAlert(message, 'success', duration);
  };

  const showError = (message, duration = 5000) => {
    showAlert(message, 'error', duration);
  };

  const showWarning = (message, duration = 4000) => {
    showAlert(message, 'warning', duration);
  };

  const showInfo = (message, duration = 3000) => {
    showAlert(message, 'info', duration);
  };

  return {
    isVisible,
    message,
    type,
    duration,
    showAlert,
    hideAlert,
    showSuccess,
    showError,
    showWarning,
    showInfo,
  };
});