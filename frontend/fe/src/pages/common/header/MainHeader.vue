<template>
  <header class="main-header">
    <div class="header-container">
      <div class="logo-section">
        <div v-html="logoSvg" class="main-logo" @click="navigateToMain"></div>
      </div>

      <div class="header-right">
        <CoinInfo />
        <UserProfile />
      </div>
    </div>
  </header>
</template>

<script setup>
import { inject } from 'vue';
import { useRouter } from 'vue-router';
import CoinInfo from './CoinInfo.vue';
import UserProfile from './UserProfile.vue';
import logoSvg from '@/assets/images/main-logo.svg?raw';

const router = useRouter();
const resetToHome = inject('resetToHome', null);

const navigateToMain = () => {
  const currentRoute = router.currentRoute.value;

  if (currentRoute.path === '/main') {
    // 이미 /main 페이지에 있다면 홈 뷰로 리셋
    if (resetToHome) {
      resetToHome();
    }
  } else {
    // 다른 페이지에 있다면 /main으로 이동
    router.push('/main');
  }
};
</script>

<style lang="scss" scoped>
.main-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: $z-dropdown;
  width: 100%;
  height: calc(12vh);
  background: linear-gradient(rgba($card-bg, 0.9), rgba($card-bg, 0.1));
}

.header-container {
  position: relative;
  width: 80%;
  height: 100%;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 calc(4vh);
}

.logo-section {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);

  .main-logo {
    height: calc(6vh);
    width: auto;
    cursor: pointer;
    transition: all $transition-base;

    :deep(svg) {
      height: calc(10vh);
      width: auto;
      filter: drop-shadow(0 0 8px rgba($electric-blue, 0.6));
      transition: all $transition-base;

      path {
        fill: $electric-blue;
        transition: fill $transition-base;
      }
    }

    &:hover {
      :deep(svg) {
        filter: drop-shadow(0 0 12px rgba($electric-blue, 0.8));
        transform: scale(1.02);

        path {
          fill: $cyberpunk-pink;
        }
      }
    }
  }
}

.header-right {
  position: absolute;
  right: calc(4vh);
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  align-items: center;
  gap: calc(2vh);
}

</style>
