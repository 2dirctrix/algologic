
/** 알고리즘 문제: Two Sum */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_byte,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
    1,
    'Two Sum',
    'EASY',
    2,
    4096000,
    '정수 배열 nums와 정수 target이 주어지면, 두 숫자의 합이 target이 되는 인덱스를 반환하세요.
각 입력값은 정확히 하나의 해를 가지며, 같은 요소를 두 번 사용할 수 없습니다.
정답의 순서는 상관없습니다.',
    'nums의 길이는 2 이상 10,000 이하입니다.
nums[i]는 -1,000,000,000 이상 1,000,000,000 이하입니다.
target은 -1,000,000,000 이상 1,000,000,000 이하입니다.
유효한 정답은 하나만 존재합니다.

후속 질문: O(n2)보다 적은 시간 복잡도를 가진 알고리즘을 생각해낼 수 있습니까?',
    '[{"input": "nums = [3,3], target = 6", "output": "[0, 1]"}, {"input": "nums = [-1,-2,-3,-4], target = -8", "output": "None"}, {"input": "nums = [1000000000, 1000000000], target = 2000000000", "output": "[0, 1]"}]',
    NOW(),
    NOW()
);


/** 알고리즘 문제: Add Two Numbers */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_byte,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
    2,
    'Add Two Numbers',
    'MEDIUM',
    2,
    4096000,
    '두 개의 0이 아닌 연결 리스트가 주어지는데, 이들은 두 개의 음이 아닌 정수를 나타냅니다. 각 숫자는 역순으로 저장되어 있으며, 각 노드는 한 자리 숫자를 포함합니다. 두 숫자를 더하고 합계를 연결 리스트로 반환하세요. 두 숫자는 0 자체를 제외하고는 선행 0을 포함하지 않는다고 가정합니다.',
    '각 연결 리스트의 노드 수는 1에서 100 사이입니다.
각 노드의 값은 0 이상 9 이하입니다.
리스트는 0으로 시작하지 않는 숫자를 나타내는 것으로 보장됩니다.',
    '[{"input": "l1 = [9,8,7], l2 = [1,2,3]", "output": "[0, 1, 1, 1]"}, {"input": "l1 = [1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0], l2 = [5,6,4]", "output": "[6, 6, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]"}, {"input": "l1 = [1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1], l2 = [5,6,4]", "output": "[6, 6, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1]"}]',
    NOW(),
    NOW()
);


/** 알고리즘 문제: Longest Substring Without Repeating Characters */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_byte,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
    3,
    'Longest Substring Without Repeating Characters',
    'MEDIUM',
    2,
    4096000,
    '문자열 s가 주어졌을 때, 반복되는 문자가 없는 가장 긴 부분 문자열의 길이를 구하시오.',
    's의 길이는 0 이상 50,000 이하입니다.
s는 영어 대문자, 소문자, 숫자, 기호 및 공백으로 구성됩니다.',
    '[{"input": "s = abcabcbb", "output": "3"}, {"input": "s = bbbbb", "output": "1"}, {"input": "s = pwwkew", "output": "3"}]',
    NOW(),
    NOW()
);


/** 알고리즘 문제: Median Of Two Sorted Arrays */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_byte,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
    4,
    'Median Of Two Sorted Arrays',
    'HARD',
    2,
    4096000,
    '정렬된 두 배열 nums1과 nums2가 주어지고, 각 배열의 크기는 m과 n입니다. 두 정렬된 배열의 중앙값을 반환하세요. 전체 실행 시간 복잡도는 O(log(m+n))이어야 합니다.',
    'nums1의 길이는 m과 같다.
nums2의 길이는 n과 같다.
0 <= m <= 1000
0 <= n <= 1000
1 <= m + n <= 2000
-106 <= nums1[i], nums2[i] <= 106',
    '[{"input": "nums1 = [100,200,300], nums2 = [150,250,350]", "output": "225.0"}, {"input": "nums1 = [2], nums2 = []", "output": "2.0"}, {"input": "nums1 = [1,3], nums2 = [2]", "output": "2.0"}]',
    NOW(),
    NOW()
);


/** 알고리즘 문제: Longest Palindromic Substring */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_byte,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
    5,
    'Longest Palindromic Substring',
    'MEDIUM',
    2,
    4096000,
    '문자열 s가 주어졌을 때, s의 가장 긴 팰린드롬 부분 문자열을 반환하시오.',
    '1 <= s의 길이 <= 1000
s는 숫자와 영문자로만 구성됩니다.',
    '[{"input": "s = abba", "output": "abba"}, {"input": "s = aaaa", "output": "aaaa"}, {"input": "s = abacdfgdcaba", "output": "aba"}]',
    NOW(),
    NOW()
);


/** 알고리즘 문제: Zigzag Conversion */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_byte,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
    6,
    'Zigzag Conversion',
    'MEDIUM',
    2,
    4096000,
    '문자열 "PAYPALISHIRING"을 주어진 행 개수에 따라 지그재그 패턴으로 다음과 같이 씁니다. (가독성을 위해 고정 글꼴로 표시하는 것이 좋습니다.)

P   A   H   N
A P L S I I G
Y   I   R

그런 다음 줄별로 읽습니다: "PAHNAPLSIIGYIR"
문자열과 행 개수가 주어졌을 때, 이러한 변환을 수행하는 코드를 작성하세요:

string convert(string s, int numRows);',
    '1 <= s의 길이 <= 1000
s는 영어 대소문자, \',\'와 \'.\'로 구성됩니다.
1 <= numRows <= 1000',
    '[{"input": "s = PAYPALISHIRING, numRows = 4", "output": "PINALSIGYAHRPI"}, {"input": "s = ABCDEFGHI, numRows = 3", "output": "AEIBDFHCG"}, {"input": "s = A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z, numRows = 5", "output": "AEIMQUY,,,,,,,,,,,,,BDFHJLNPRTVXZ,,,,,,,,,,,,CGKOSW"}]',
    NOW(),
    NOW()
);


/** 알고리즘 문제: Reverse Integer */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_byte,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
    7,
    'Reverse Integer',
    'MEDIUM',
    2,
    4096000,
    '32비트 부호 있는 정수 x가 주어졌을 때, x의 숫자를 뒤집어 반환하세요. x를 뒤집는 과정에서 값이 부호 있는 32비트 정수 범위 [-2^31, 2^31 - 1]을 벗어나면 0을 반환합니다.
환경은 64비트 정수(부호 있거나 없는)를 저장하는 것을 허용하지 않는다고 가정합니다.',
    '-231 이상 x, 231 - 1 이하',
    '[{"input": "x = -2147483412", "output": "-2143847412"}, {"input": "x = 2147483647", "output": "0"}, {"input": "x = 120", "output": "21"}]',
    NOW(),
    NOW()
);
