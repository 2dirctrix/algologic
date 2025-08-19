// src/pages/game/components/in-game/constants/editor-config.js

/**
 * Monaco Editor 기본 설정
 * 모든 사용자에게 공통으로 적용되는 기본값
 */
export const editorConfig = {
  // 언어 설정 (기본값, 컴포넌트에서 변경 가능)
  language: 'java',

  // 테마 설정 (기본값, 변경 가능)
  theme: 'night-owl',

  // 에디터 옵션
  options: {
    // 폰트 설정
    fontSize: 14, // 기본 폰트 사이즈
    fontFamily: 'Fira Code, Menlo, Monaco, monospace',
    fontLigatures: true,

    // 줄번호 설정
    lineNumbers: 'on',
    lineNumbersMinChars: 3,

    // 스크롤 설정
    scrollBeyondLastLine: false,
    smoothScrolling: true,

    // 미니맵 활성화 (기본값)
    minimap: {
      enabled: true,
    },

    // 자동 완성 활성화 (기본값)
    quickSuggestions: true,
    suggestOnTriggerCharacters: true,
    acceptSuggestionOnCommitCharacter: true,

    // 들여쓰기 (기본 활성화)
    tabSize: 4,
    insertSpaces: true,
    detectIndentation: false,

    // 코드 접기 (기본 활성화)
    folding: true,
    foldingStrategy: 'indentation',

    // 선택 영역 (기본 활성화)
    selectionHighlight: true,
    occurrencesHighlight: true,

    // 괄호 매칭 (기본 활성화)
    matchBrackets: 'always',

    // 자동 포맷 (기본 활성화)
    formatOnPaste: true,
    formatOnType: false,

    // 읽기 전용 설정
    readOnly: false,

    // 기타 UI 설정
    cursorStyle: 'line',
    cursorBlinking: 'blink',
    renderWhitespace: 'selection',
    renderControlCharacters: false,
    renderIndentGuides: true,
    renderLineHighlight: 'all',

    // 워드랩
    wordWrap: 'off',

    // 에디터 패딩
    padding: {
      top: 10,
      bottom: 10,
    },

    // Undo/Redo 비활성화
    undoRedoService: false,
    disableMonacoTree: true,
  },
};

/**
 * 사용자 설정 가능한 옵션들의 기본값
 */
export const userSettings = {
  fontSize: 14, // 폰트 사이즈 조정 가능
  minimapEnabled: true, // 미니맵 활성화/비활성화 가능
  theme: 'monokai', // 테마 변경 가능

  // 토글 가능한 기능들
  indentation: true, // 들여쓰기
  folding: true, // 코드 접기
  selectionHighlight: true, // 선택 영역
  matchBrackets: true, // 괄호 매칭
  autoFormat: true, // 자동 포맷팅
};

/**
 * 사용자 설정을 반영한 에디터 옵션 생성 함수
 * @param {object} customSettings - 사용자 커스텀 설정
 * @returns {object} 에디터 옵션 객체
 */
export const createEditorOptions = (customSettings = {}) => {
  const settings = { ...userSettings, ...customSettings };

  return {
    ...editorConfig.options,
    fontSize: settings.fontSize,
    minimap: {
      enabled: settings.minimapEnabled,
    },
    folding: settings.folding,
    selectionHighlight: settings.selectionHighlight,
    occurrencesHighlight: settings.selectionHighlight,
    matchBrackets: settings.matchBrackets ? 'always' : 'never',
    formatOnPaste: settings.autoFormat,
    formatOnType: settings.autoFormat,
    tabSize: settings.indentation ? 4 : 2,
    insertSpaces: settings.indentation,
  };
};

/**
 * 지원하는 테마들을 명시적으로 import
 */
const themeMap = {
  monokai: () => import('monaco-themes/themes/Monokai.json'),
  'night-owl': () => import('monaco-themes/themes/Night Owl.json'),
  'github-dark': () => import('monaco-themes/themes/GitHub Dark.json'),
  'github-light': () => import('monaco-themes/themes/GitHub Light.json'),
  dracula: () => import('monaco-themes/themes/Dracula.json'),
  'solarized-dark': () => import('monaco-themes/themes/Solarized-dark.json'),
  'solarized-light': () => import('monaco-themes/themes/Solarized-light.json'),
  cobalt2: () => import('monaco-themes/themes/Cobalt2.json'),
  nord: () => import('monaco-themes/themes/Nord.json'),
};

/**
 * Monaco Editor 기본 테마들
 */
const builtInThemes = ['vs', 'vs-dark', 'hc-black', 'hc-light'];

/**
 * monaco-themes에서 테마 로드 및 등록
 * @param {object} monaco - Monaco Editor 인스턴스
 * @param {string} themeId - 테마 ID (예: 'monokai', 'night-owl')
 * @returns {Promise<boolean>} 성공 여부
 */
export const loadAndRegisterTheme = async (monaco, themeId) => {
  try {
    // Monaco Editor 기본 테마인 경우 별도 로드 없이 성공 반환
    if (builtInThemes.includes(themeId)) {
      return true;
    }

    const themeLoader = themeMap[themeId];
    if (!themeLoader) {
      console.warn(`지원하지 않는 테마: ${themeId}`);
      return false;
    }

    const themeModule = await themeLoader();
    const themeData = themeModule.default;

    if (!themeData) {
      console.warn(`테마 데이터가 없습니다: ${themeId}`);
      return false;
    }

    // 테마 데이터 구조 검증 및 보정
    const processedThemeData = {
      base: themeData.base || 'vs-dark',
      inherit: themeData.inherit !== undefined ? themeData.inherit : true,
      rules: themeData.rules || [],
      colors: themeData.colors || {},
    };

    monaco.editor.defineTheme(themeId, processedThemeData);
    return true;
  } catch (error) {
    console.error(`테마 로드 실패: ${themeId}`, error);
    return false;
  }
};

/**
 * 사용 가능한 테마 목록 (기본 테마 + 커스텀 테마)
 */
export const availableThemes = [...builtInThemes, ...Object.keys(themeMap)];

/**
 * 언어별 스켈레톤 코드
 */
export const skeletonCode = {
  python: '',

  java: `public class Main {
    public static void main(String[] args) {
        // 여기에 코드를 작성하세요

    }
}`,

  cpp: `#include <iostream>

using namespace std;

int main() {
    // 여기에 코드를 작성하세요

    return 0;
}`,

  c: `#include <stdio.h>

int main() {
    // 여기에 코드를 작성하세요

    return 0;
}`,

  javascript: `// 여기에 코드를 작성하세요
function solution() {

}`,
};

/**
 * 언어에 따른 초기 코드 반환
 * @param {string} language - 프로그래밍 언어
 * @returns {string} 해당 언어의 스켈레톤 코드
 */
export const getSkeletonCode = language => {
  return skeletonCode[language] || skeletonCode.python;
};
