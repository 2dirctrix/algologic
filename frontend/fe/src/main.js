import { createApp } from 'vue';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';
import { createPinia } from 'pinia';
import { install as VueMonacoEditorPlugin } from '@guolao/vue-monaco-editor';

import App from './App.vue';
import router from './router';

import './assets/styles/main.scss';

import '@/api/utils/interceptors.js';
import { watchAuthAndConnect } from '@/stores/auth/auth.js';

const app = createApp(App);

const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);

app.use(pinia);
app.use(router);

// Monaco Editor CDN 설정
app.use(VueMonacoEditorPlugin, {
  paths: {
    vs: 'https://cdn.jsdelivr.net/npm/monaco-editor@0.52.2/min/vs',
  },
});

// 인증 상태 변화에 따른 WebSocket 연결 자동 관리
watchAuthAndConnect(pinia);

app.mount('#app');
