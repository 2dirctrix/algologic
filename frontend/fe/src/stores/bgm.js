import { defineStore } from 'pinia';

export const useBgmStore = defineStore('bgm', {
  state: () => ({
    isGlobalBgmEnabled: true, // 전역 BGM 활성화 여부
  }),

  actions: {
    // 전역 BGM 비활성화
    disableGlobalBgm() {
      this.isGlobalBgmEnabled = false;
    },

    // 전역 BGM 활성화
    enableGlobalBgm() {
      this.isGlobalBgmEnabled = true;
    },

    // 전역 BGM 토글
    toggleGlobalBgm() {
      this.isGlobalBgmEnabled = !this.isGlobalBgmEnabled;
    },
  },
});