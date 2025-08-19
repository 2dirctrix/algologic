<template>
  <div class="user-profile">
    <div class="profile-menu">
      <span class="menu-item" @click="handleMyPage">마이페이지</span>
      <span class="menu-divider">|</span>
      <span class="menu-item" @click="handleLogout">로그아웃</span>
    </div>
    <div class="profile-avatar">
      <img v-if="authStore.profileImage" :src="authStore.profileImage" :alt="authStore.nickname" class="profile-image" />
    </div>
  </div>
</template>

<script setup>
import { inject } from 'vue';
import { useAuthStore } from '@/stores/auth/auth.js';
import { useAuth } from '@/composables/useAuth.js';

const authStore = useAuthStore();
const switchToMyPage = inject('switchToMyPage', null);

// 마이페이지 이동 처리
const handleMyPage = () => {
  if (switchToMyPage) {
    switchToMyPage();
  }
};

// 로그아웃 처리
const { logout } = useAuth();
const handleLogout = async () => {
  await logout();
};
</script>

<style lang="scss" scoped>
.user-profile {
  position: relative;
  display: flex;
  align-items: center;
  height: calc(4.8vh);
  background: rgba($cyberpunk-pink, 0.6);
  border: 1px solid $cyberpunk-pink;
  border-radius: calc(2.4vh);
  cursor: pointer;
  overflow: hidden;
  width: calc(5.1vh);

  transition:
    width 0.5s ease-in-out,
    box-shadow $transition-base;

  &:hover {
    width: calc(26vh);
    box-shadow: 0 0 10px rgba($cyberpunk-pink, 0.6);

    transition:
      width 1s cubic-bezier(0.23, 1, 0.32, 1),
      box-shadow $transition-base;

    .profile-menu {
      opacity: 1;
      visibility: visible;
      transform: translateX(0);

      transition:
        opacity 0.3s ease-out 0.3s,
        visibility 0.3s ease-out 0.3s,
        transform 0.3s ease-out 0.3s;
    }
  }
}

.profile-menu {
  position: absolute;
  left: calc(1vh);
  display: flex;
  align-items: center;
  gap: calc(1.5vh);
  opacity: 0;
  visibility: hidden;
  transform: translateX(-10px);
  white-space: nowrap;

  transition:
    opacity 0.2s ease-in,
    visibility 0.2s ease-in,
    transform 0.2s ease-in;
}

.menu-item {
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: calc(1.8vh);
  color: $primary-bg;
  cursor: pointer;
  padding: calc(0.5vh) calc(-12vh);
  border-radius: calc(0.5vh);
  transition: all 0.2s ease;

  &:hover {
    color: rgba($primary-bg, 0.8);
    background: rgba($primary-bg, 0.1);
  }
}

.menu-divider {
  font-family: $font-primary;
  font-weight: $font-weight-bold;
  font-size: calc(1.8vh);
  color: rgba($primary-bg, 0.6);
  margin: 0 calc(0.3vh);
}

.profile-avatar {
  position: absolute;
  right: 0;
  width: calc(5.1vh);
  height: calc(4.8vh);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: $cyberpunk-pink;
  z-index: 1;
  overflow: hidden;
}

.profile-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}
</style>
