<template>
  <div id="app" @click="initBgm" @keydown="initBgm">
    <router-view />
    <!-- 전역 알림창 -->
    <AlertModal />
  </div>
  <audio ref="bgm" autoplay>
    <source src="/audio/main-sound.mp3" type="audio/mp3" />
    브라우저가 오디오를 지원하지 않습니다.
  </audio>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useBgmStore } from '@/stores/bgm.js';
import AlertModal from '@/components/common/AlertModal.vue';

const bgm = ref(null);
const bgmStore = useBgmStore();

let started = false;

const initBgm = () => {
  if (started) return;
  started = true;
  bgm.value.currentTime = 0;
  bgm.value.play();
};

onMounted(() => {
  if (bgm.value) {
    bgm.value.volume = 0.1;
  }
});

// BGM 상태 변경 감시
watch(
  () => bgmStore.isGlobalBgmEnabled,
  isEnabled => {
    if (bgm.value) {
      if (isEnabled) {
        bgm.value.play().catch(() => {
          // Ignore play errors
        });
      } else {
        bgm.value.pause();
      }
    }
  },
);
</script>

<style>
#app {
  width: 100%;
  height: 100vh;
}
</style>
