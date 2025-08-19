<!-- AuthPage.vue -->
<template>
  <div class="auth-page">
    <div class="auth-page__container">
      <LoginSection />
    </div>
  </div>
</template>

<script>
import { onMounted } from 'vue';
import LoginSection from './components/LoginSection.vue';
import { initKakaoSDK } from '@/utils/kakaoSDK.js';

export default {
  name: 'AuthPage',
  components: {
    LoginSection,
  },
  setup() {
    // AuthPage 마운트 시점에 Kakao SDK 로드 및 초기화
    onMounted(async () => {
      try {
        await initKakaoSDK();
      } catch (error) {
        console.error('AuthPage에서 Kakao SDK 초기화 실패:', error);
      }
    });

    return {};
  },
};
</script>

<style scoped lang="scss">
.auth-page {
  height: 100vh;
  background-color: $primary-bg;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: calc(2vh);

  background:
    linear-gradient(rgba(0, 0, 0, 0.4), rgba(0, 0, 0, 0.4)),
    url('@/pages/auth/assets/images/background-image.png') no-repeat center center;
  background-size: cover;

  &__container {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
  }

}
</style>
