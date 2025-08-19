// ===================================
// KAKAO SDK 동적 로딩 및 초기화
// ===================================

let isInitialized = false;
let isLoading = false;

// Kakao SDK 스크립트 동적 로딩
export const loadKakaoSDK = () => {
  if (window.Kakao || isLoading) {
    return Promise.resolve();
  }

  isLoading = true;

  return new Promise((resolve, reject) => {
    const script = document.createElement('script');
    script.src = 'https://t1.kakaocdn.net/kakao_js_sdk/2.7.5/kakao.min.js';
    script.integrity = 'sha384-dok87au0gKqJdxs7msEdBPNnKSRT+/mhTVzq+qOhcL464zXwvcrpjeWvyj1kCdq6';
    script.crossOrigin = 'anonymous';

    script.onload = () => {
      isLoading = false;
      resolve();
    };

    script.onerror = () => {
      isLoading = false;
      reject(new Error('카카오 SDK 로드 실패'));
    };

    document.head.appendChild(script);
  });
};

export const initKakaoSDK = async () => {
  if (isInitialized) {
    return Promise.resolve();
  }

  // SDK 로드 후 초기화
  await loadKakaoSDK();

  return new Promise((resolve, reject) => {
    if (window.Kakao) {
      if (!window.Kakao.isInitialized()) {
        const jsKey = import.meta.env.VITE_KAKAO_JAVASCRIPT_KEY;

        if (!jsKey) {
          const errorMsg = `카카오 JavaScript 키가 설정되지 않았습니다.`;
          reject(new Error(errorMsg));
          return;
        }

        try {
          window.Kakao.init(jsKey);
          isInitialized = true;
        } catch (error) {
          reject(error);
          return;
        }
      }
      resolve();
    } else {
      reject(new Error('카카오 SDK가 로드되지 않았습니다'));
    }
  });
};

export const isKakaoSDKReady = () => {
  return window.Kakao && window.Kakao.isInitialized();
};
