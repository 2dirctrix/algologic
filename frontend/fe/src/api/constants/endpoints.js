/**
 * API 엔드포인트 상수 관리
 */

// BASE URL
const API_PREFIX = '/api/v1';

// MEMBER
const MEMBERS_PREFIX = `${API_PREFIX}/members`;

// INGAME
const ROOMS_PREFIX = `${API_PREFIX}/rooms`;
const PROBLEMS_PREFIX = `${API_PREFIX}/problems`;
const BANPICKBUY_PREFIX = `${API_PREFIX}/ingame`;

// WEBRTC
const WEBRTC_PREFIX = `${API_PREFIX}/webrtc`;

export const API_ENDPOINTS = {
  AUTH: {
    KAKAO_LOGIN: `${API_PREFIX}/auth/kakao/access-token`,
    SIGNUP: `${API_PREFIX}/auth/signup`,
    LOGOUT: `${API_PREFIX}/auth/logout`,
    REFRESH: `${API_PREFIX}/auth/refresh-token`,
  },
  USER: {
    INFO: `${MEMBERS_PREFIX}/me`,
    DETAIL: `${MEMBERS_PREFIX}/me/detail`,
    UPDATE: `${MEMBERS_PREFIX}/me`,
    RANKERS: `${MEMBERS_PREFIX}/rankers`,
    ALGORITHM_STATS: `${MEMBERS_PREFIX}/me/solved-problems/categories/top`,
    DAILY_GAMES: `${MEMBERS_PREFIX}/me/games/daily`,
    GAME_RESULTS: `${MEMBERS_PREFIX}/me/game-results`,
  },
  ROOM: {
    LIST: `${ROOMS_PREFIX}/list`,
    CREATE: `${ROOMS_PREFIX}`,
    ENTER_GAME: roomId => `${ROOMS_PREFIX}/${roomId}/enter-game`,
    JOIN: roomId => `${ROOMS_PREFIX}/${roomId}/join`,
    BAN: `${BANPICKBUY_PREFIX}/ban`,
    PICK: `${BANPICKBUY_PREFIX}/pick`,
    GET_ITEM: `${BANPICKBUY_PREFIX}/items`,
    GET_SPELL: `${BANPICKBUY_PREFIX}/spells`,
    BUY: `${BANPICKBUY_PREFIX}/purchase/bulk`,
    LEAVE: roomId => `${ROOMS_PREFIX}/${roomId}/leave`,
  },
  PROBLEM: {
    CATEGORY: `${PROBLEMS_PREFIX}/category`,
    GET: problemId => `${PROBLEMS_PREFIX}/${problemId}`,
    SUBMIT: problemId => `${PROBLEMS_PREFIX}/${problemId}/submit`,
    RANK: `${API_PREFIX}/problems/category/rank`,
  },
  BATTLE: {
    GAME_RESULT_PREFIX: gameResultId => `${API_PREFIX}/game-results/${gameResultId}`,
  },
  WEBRTC: {
    DISPLAY: `${WEBRTC_PREFIX}/token`, // token 발급
  },
};
