// ===================================
// KAKAO 로그인 핵심 로직
// ===================================

import { initKakaoSDK, isKakaoSDKReady } from '@/utils/kakaoSDK.js';
// 1. 카카오 로그인 페이지로 이동
export const startKakaoLogin = async () => {
  try {
    await initKakaoSDK();

    if (!isKakaoSDKReady()) {
      throw new Error('카카오 SDK가 준비되지 않았습니다');
    }

    // 현재 페이지를 리다이렉트 URI로 설정 (회원가입/로그인 페이지)
    const redirectUri = window.location.origin + window.location.pathname;
    window.Kakao.Auth.authorize({
      redirectUri: redirectUri,
    });
  } catch (error) {
    console.error('카카오 로그인 시작 실패:', error);
    throw error;
  }
};

// function : URL에서 인가 코드 추출
export const extractAuthCodeFromURL = () => {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams.get('code');
};

// funciton : URL에서 에러 코드 추출
export const extractErrorFromURL = () => {
  const urlParams = new URLSearchParams(window.location.search);
  return {
    error: urlParams.get('error'),
    errorDescription: urlParams.get('error_description'),
  };
};
