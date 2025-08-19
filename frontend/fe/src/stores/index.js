/**
 * Pinia Store 설정
 * persistedState 플러그인 등록
 */

import { createPinia } from 'pinia';
import piniaPluginPersistence from 'pinia-plugin-persistedstate';

export const pinia = createPinia();
pinia.use(piniaPluginPersistence);
export default pinia;
