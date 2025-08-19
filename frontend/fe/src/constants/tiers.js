// src/constants/tiers.js

export const TIERS = [
  {
    name: 'BRONZE',
    divisions: [
      { division: 'V', min: 300, max: 399 },
      { division: 'IV', min: 400, max: 499 },
      { division: 'III', min: 500, max: 599 },
      { division: 'II', min: 600, max: 699 },
      { division: 'I', min: 700, max: 799 },
    ],
  },
  {
    name: 'SILVER',
    divisions: [
      { division: 'V', min: 800, max: 899 },
      { division: 'IV', min: 900, max: 999 },
      { division: 'III', min: 1000, max: 1099 },
      { division: 'II', min: 1100, max: 1199 },
      { division: 'I', min: 1200, max: 1299 },
    ],
  },
  {
    name: 'GOLD',
    divisions: [
      { division: 'V', min: 1300, max: 1399 },
      { division: 'IV', min: 1400, max: 1499 },
      { division: 'III', min: 1500, max: 1599 },
      { division: 'II', min: 1600, max: 1699 },
      { division: 'I', min: 1700, max: 1799 },
    ],
  },
  {
    name: 'PLATINUM',
    divisions: [
      { division: 'V', min: 1800, max: 1899 },
      { division: 'IV', min: 1900, max: 1999 },
      { division: 'III', min: 2000, max: 2099 },
      { division: 'II', min: 2100, max: 2199 },
      { division: 'I', min: 2200, max: 2299 },
    ],
  },
  {
    name: 'DIAMOND',
    divisions: [
      { division: 'V', min: 2300, max: 2399 },
      { division: 'IV', min: 2400, max: 2499 },
      { division: 'III', min: 2500, max: 2599 },
      { division: 'II', min: 2600, max: 2699 },
      { division: 'I', min: 2700, max: 2799 },
    ],
  },
  {
    name: 'MASTER',
    min: 2800,
    max: 2999,
  },
  {
    name: 'GRANDMASTER',
    min: 3000,
    max: 3199,
  },
  {
    name: 'CHALLENGER',
    min: 3200,
    max: Infinity,
  },
];

/**
 * 점수를 기반으로 티어 정보를 반환하는 함수
 * @param {number} score - 변환할 점수
 * @returns {Object} 티어 정보 { name, division }
 */
export const getScoreToTier = (score) => {
  // 점수가 유효하지 않은 경우
  if (typeof score !== 'number' || score < 0) {
    return { name: 'UNRANKED', division: null };
  }

  // 각 티어를 순회하면서 점수에 맞는 티어 찾기
  for (const tier of TIERS) {
    // MASTER, GRANDMASTER, CHALLENGER는 division이 없음
    if (!tier.divisions) {
      if (score >= tier.min && score <= tier.max) {
        return { name: tier.name, division: null };
      }
    } else {
      // BRONZE ~ DIAMOND는 division이 있음
      for (const div of tier.divisions) {
        if (score >= div.min && score <= div.max) {
          return { name: tier.name, division: div.division };
        }
      }
    }
  }

  // 300점 미만인 경우
  return { name: 'UNRANKED', division: null };
};
