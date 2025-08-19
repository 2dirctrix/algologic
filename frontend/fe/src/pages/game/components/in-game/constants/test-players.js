// src/pages/game/components/in-game/constants/test-players.js

/**
 * 인게임 플레이어 테스트 데이터
 * 최소 1명에서 최대 5명까지 동적으로 표시 가능
 */
export const testPlayers = [
  {
    id: 1,
    name: 'CodeMaster',
    tier: 'Diamond',
    rating: 2450,
    status: 'active',
    isCurrentPlayer: false,
  },
  {
    id: 2,
    name: 'AlgoGod',
    tier: 'Platinum',
    rating: 2100,
    status: 'active',
    isCurrentPlayer: false,
  },
  {
    id: 3,
    name: 'DPQueen',
    tier: 'Gold',
    rating: 1850,
    status: 'active',
    isCurrentPlayer: false,
  },
  {
    id: 4,
    name: 'BTreeKing',
    tier: 'Diamond',
    rating: 2380,
    status: 'active',
    isCurrentPlayer: false,
  },
  {
    id: 5,
    name: 'AlgoGod',
    tier: 'Platinum',
    rating: 2050,
    status: 'active',
    isCurrentPlayer: false,
  },
];

// 플레이어 수에 따른 테스트 시나리오
export const getTestPlayers = (count = 5) => {
  if (count < 1) return [testPlayers[0]];
  if (count > 5) return testPlayers;
  return testPlayers.slice(0, count);
};
