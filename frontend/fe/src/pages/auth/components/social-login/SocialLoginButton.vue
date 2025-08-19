<template>
  <button class="social-login-button" :data-provider="provider.toLowerCase()" @click="handleClick" @mouseenter="handleMouseEnter" :disabled="isLoading || isGoogleProvider">
    <div class="social-login-button__icon rounded-full">
      <img :src="iconPath" :alt="`${provider} icon`" class="social-login-button__icon-image" />
    </div>
    <span class="social-login-button__text text-base text-bold">
      {{ isLoading ? '로그인 중...' : label }}
    </span>
  </button>
</template>

<script>
import googleIcon from '../../assets/icon/google-icon.svg';
import kakaoIcon from '../../assets/icon/kakao-icon.svg';
import { startKakaoLogin } from '@/api/services/kakaoAuth.js';

export default {
  name: 'SocialLoginButton',
  props: {
    provider: {
      type: String,
      required: true,
    },
    label: {
      type: String,
      required: true,
    },
    bgColor: {
      type: String,
      required: true,
    },
    textColor: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      isLoading: false,
    };
  },
  computed: {
    iconPath() {
      const iconMap = {
        google: googleIcon,
        kakao: kakaoIcon,
      };
      return iconMap[this.provider.toLowerCase()] || '';
    },
    isGoogleProvider() {
      return this.provider.toLowerCase() === 'google';
    },
  },
  methods: {
    playHoverSound() {
      try {
        const audio = new Audio('/audio/interaction/button-hover-sound.wav');
        audio.volume = 0.3;
        audio.play().catch(error => {
          console.warn('Audio play failed:', error);
        });
      } catch (error) {
        console.warn('Audio creation failed:', error);
      }
    },

    playClickSound() {
      try {
        const audio = new Audio('/audio/interaction/chosen-sound.wav');
        audio.volume = 0.6;
        audio.play().catch(error => {
          console.warn('Audio play failed:', error);
        });
      } catch (error) {
        console.warn('Audio creation failed:', error);
      }
    },

    handleMouseEnter() {
      if (!this.isLoading && !this.isGoogleProvider) {
        this.playHoverSound();
      }
    },

    async handleClick() {
      if (this.isLoading) return;

      // 클릭 사운드 재생
      if (!this.isGoogleProvider) {
        this.playClickSound();
      }

      try {
        this.isLoading = true;

        if (this.provider.toLowerCase() === 'kakao') {
          await this.handleKakaoLogin();
        } else if (this.provider.toLowerCase() === 'google') {
          // console.log('구글 로그인은 아직 구현되지 않았습니다')
        } else {
          // console.log(`${this.provider} 로그인 클릭`)
        }
      } catch (error) {
        // console.error('소셜 로그인 실패:', error);
        alert('로그인에 실패했습니다. 다시 시도해주세요.');
      } finally {
        this.isLoading = false;
      }
    },

    async handleKakaoLogin() {
      try {
        await startKakaoLogin();
      } catch (error) {
        throw new Error(`카카오 로그인 실패: ${error.message}`);
      }
    },
  },
};
</script>

<style scoped lang="scss">
.social-login-button {
  width: 100%;
  height: calc(6vh);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: calc(1.5vh);
  padding: 0 calc(2vh);
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;

  // 네온 테두리와 배경색 적용
  border: 2px solid;

  // 각 제공자별 색상 스타일
  &[data-provider='kakao'] {
    border-color: #fbe300;
    color: #fbe300;
    background: rgba(251, 227, 0, 0.85);

    &:hover:not(:disabled) {
      background: rgba(251, 227, 0, 0.8);
      box-shadow:
        0 0 10px rgba(251, 227, 0, 0.5),
        inset 0 0 10px rgba(251, 227, 0, 0.1);
    }
  }

  &[data-provider='google'] {
    border-color: #ff00ff;
    color: #ff00ff;
    background: rgba(255, 0, 255, 0.85);

    &:hover:not(:disabled) {
      background: rgba(255, 0, 255, 0.8);
      box-shadow:
        0 0 10px rgba(255, 0, 255, 0.5),
        inset 0 0 10px rgba(255, 0, 255, 0.1);
    }
  }

  &:hover:not(:disabled) {
    transform: translateY(-1px);
  }

  &:disabled {
    cursor: not-allowed;
    opacity: 0.5;
  }

  &__icon {
    width: calc(3vh);
    height: calc(3vh);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    background: rgba(255, 255, 255, 0.1);
  }

  &__icon-image {
    width: calc(2.2vh);
    height: calc(2.2vh);
    object-fit: contain;
  }

  &__text {
    font-size: calc(1.8vh);
    font-weight: 600;
    letter-spacing: 0.5px;
  }
}
</style>
