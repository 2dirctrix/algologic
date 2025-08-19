<template>
  <div class="login-section">
    <!-- 프로필 설정 로딩 상태 -->
    <div v-if="isProfileSetupLoading" class="login-section__content">
      <div class="setup-loading">
        <div class="setup-loading__spinner"></div>
        <p class="setup-loading__text">회원가입을 진행합니다</p>
      </div>
    </div>

    <!-- 프로필 설정 상태 -->
    <div v-else-if="showProfileSetupModal" class="login-section__content">
      <ProfileSetupModal @close="handleCloseSignup" />
    </div>

    <!-- 로그인 상태 -->
    <div v-else class="login-section__content">
      <!-- 로고 -->
      <div v-html="logoSvg" class="login-section__logo"></div>

      <!-- 로고와 버튼 사이 여백 -->
      <div class="login-section__spacer"></div>

      <!-- 설명 텍스트 -->
      <p class="login-section__description text-lg">{{ UI_TEXT.welcomeDescription }}</p>

      <!-- 소셜 로그인 버튼들 -->
      <div class="login-section__buttons">
        <SocialLoginButton
          v-for="(provider, key) in SOCIAL_PROVIDERS"
          :key="key"
          :provider="key"
          :label="provider.label"
          :bg-color="provider.bgColor"
          :text-color="provider.textColor"
        />
      </div>

      <!-- 로그인 처리 중 표시 -->
      <div v-if="isLoading" class="login-processing">
        <p class="text-base">로그인 처리 중...</p>
      </div>

      <!-- 하단 설명 -->
      <p class="login-section__footer text-sm text-secondary">
        {{ UI_TEXT.socialLoginDescription }}
      </p>
    </div>
  </div>
</template>

<script>
import { onMounted, computed, ref, watch } from 'vue';
import { UI_TEXT, SOCIAL_PROVIDERS } from '../constants/ui-constants.js';
import SocialLoginButton from './social-login/SocialLoginButton.vue';
import ProfileSetupModal from './profile-setup/ProfileSetupModal.vue';
import { extractAuthCodeFromURL, extractErrorFromURL } from '@/api/services/kakaoAuth.js';
import { useAuth } from '@/composables/useAuth.js';
import logoSvg from '@/assets/images/main-logo.svg?raw';

export default {
  name: 'LoginSection',
  components: {
    SocialLoginButton,
    ProfileSetupModal,
  },
  setup() {
    const { loginWithCode, isLoading, needSignup, closeSignupModal } = useAuth();

    const isProfileSetupLoading = ref(false);
    const showProfileSetupModal = ref(false);

    const showProfileSetup = computed(() => needSignup.value);

    // 프로필 설정 모달 표시를 위한 watcher
    watch(
      showProfileSetup,
      async newValue => {
        if (newValue) {
          isProfileSetupLoading.value = true;
          showProfileSetupModal.value = false;

          // 1초 후 모달 표시
          setTimeout(() => {
            isProfileSetupLoading.value = false;
            showProfileSetupModal.value = true;
          }, 1000);
        } else {
          isProfileSetupLoading.value = false;
          showProfileSetupModal.value = false;
        }
      },
      { immediate: true },
    );

    const handleCloseSignup = () => {
      closeSignupModal();
    };

    const handleAuthCallback = async () => {
      const authCode = extractAuthCodeFromURL();
      const { error, errorDescription } = extractErrorFromURL();
      if (error) {
        console.error('카카오 로그인 에러:', error, errorDescription);
        alert('로그인이 취소되었거나 실패했습니다.');
        cleanupURL();
        return;
      }

      if (authCode) {
        await processAuthCode(authCode);
      }
    };

    const processAuthCode = async authCode => {
      try {
        cleanupURL();
        await loginWithCode(authCode);
      } catch (error) {
        console.error('로그인 처리 실패:', error);
        alert(error.message);
      }
    };

    const cleanupURL = () => {
      const url = new URL(window.location);
      url.searchParams.delete('code');
      url.searchParams.delete('error');
      url.searchParams.delete('error_description');
      url.searchParams.delete('state');

      window.history.replaceState({}, document.title, url.pathname);
    };

    onMounted(() => {
      handleAuthCallback();
    });

    return {
      UI_TEXT,
      SOCIAL_PROVIDERS,
      logoSvg,
      isLoading,
      showProfileSetup,
      isProfileSetupLoading,
      showProfileSetupModal,
      handleCloseSignup,
    };
  },
};
</script>

<style scoped lang="scss">
.login-section {
  width: calc(70vh);
  height: calc(80vh);
  transition: all 0.3s ease;

  &__content {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    text-align: center;
    padding: calc(2vh);
  }

  &__logo {
    height: calc(24vh);
    width: auto;
    cursor: pointer;
    transition: all $transition-base;
    align-self: center;

    :deep(svg) {
      height: calc(24vh);
      width: auto;
      filter: drop-shadow(0 0 12px rgba($electric-blue, 0.8));
      transition: all $transition-base;

      path {
        fill: $electric-blue;
        transition: fill $transition-base;
      }
    }

    &:hover {
      :deep(svg) {
        filter: drop-shadow(0 0 16px rgba($electric-blue, 1));
        transform: scale(1.05);

        path {
          fill: $cyberpunk-pink;
        }
      }
    }
  }

  &__spacer {
    height: calc(8vh);
  }

  &__description {
    font-size: calc(1.8vh);
    line-height: 1.4;
    margin-bottom: calc(3vh);
  }

  &__buttons {
    display: flex;
    flex-direction: column;
    gap: calc(2vh);
    width: 100%;
    align-items: center;
    margin-bottom: calc(3vh);
  }

  &__footer {
    font-size: calc(1.4vh);
    line-height: 1.3;
  }
}

.login-processing {
  text-align: center;
  padding: calc(2vh);

  p {
    color: #00fff0;
    font-size: calc(1.6vh);
  }
}

.setup-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: calc(3vh);

  &__spinner {
    width: calc(8vh);
    height: calc(8vh);
    border: 3px solid rgba(255, 255, 255, 0.2);
    border-top: 3px solid $electric-blue;
    border-radius: 50%;
    animation: spin 1s linear infinite;
  }

  &__text {
    font-size: calc(1.8vh);
    color: $electric-blue;
    text-align: center;
  }
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
