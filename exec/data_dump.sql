
/** 회원 */
INSERT INTO member (coin, is_deleted, score, created_at, member_id, updated_at, email, nickname, profile_image_url, programming_language, provider)
VALUES
    (28000, false, 3000, '2025-08-13 00:23:32.000000', 1, '2025-08-17 02:31:51.366065', 'daol@example.com', 'daol', 'https://picsum.photos/id/1011/200', 'JAVA', 'KAKAO'),
    (1500, false, 600, '2025-08-13 00:23:32.000000', 2, '2025-08-13 00:23:32.000000', 'bob@example.com', '추지웅추앙해', 'https://picsum.photos/id/1012/200', 'CPP', 'KAKAO'),
    (2000, false, 1300, '2025-08-13 00:23:32.000000', 3, '2025-08-13 00:23:32.000000', 'charlie@example.com', '갱갱갱', 'https://picsum.photos/id/1013/200', 'PYTHON', 'KAKAO'),
    (500, false, 500, '2025-08-13 00:23:32.000000', 4, '2025-08-13 00:23:32.000000', 'david@example.com', '연뚜', 'https://picsum.photos/id/1014/200', 'JAVA', 'KAKAO'),
    (1200, false, 500, '2025-08-13 00:23:32.000000', 5, '2025-08-13 00:23:32.000000', 'eve@example.com', '난제법', 'https://picsum.photos/id/1015/200', 'CPP', 'KAKAO'),
    (700, false, 500, '2025-08-13 00:23:32.000000', 6, '2025-08-13 00:23:32.000000', 'frank@example.com', '동이', 'https://picsum.photos/id/1016/200', 'PYTHON', 'KAKAO'),
    (1000000, false, 500, '2025-08-13 00:23:32.000000', 7, '2025-08-13 00:23:32.000000', 'grace@example.com', 'kihwan', 'https://picsum.photos/id/1017/200', 'JAVA', 'KAKAO'),
    (1000000, false, 500, '2025-08-13 00:23:32.000000', 8, '2025-08-13 00:23:32.000000', 'henry@example.com', '준선', 'https://picsum.photos/id/1018/200', 'CPP', 'KAKAO'),
    (1100, false, 500, '2025-08-13 00:23:32.000000', 9, '2025-08-13 00:23:32.000000', 'irene@example.com', 'jungeun', 'https://picsum.photos/id/1019/200', 'PYTHON', 'KAKAO'),
    (1000, false, 500, '2025-08-13 00:23:32.000000', 10, '2025-08-13 00:23:32.000000', 'jack@example.com', 'ㄱㄱㄱ', 'https://picsum.photos/id/1020/200', 'JAVA', 'KAKAO');


/** 알고리즘 카테고리 */
INSERT INTO problem_category (problem_category_id, problem_category_name, created_at, updated_at, banned_count, picked_count)
VALUES (1, '너비 우선 탐색', NOW(), NOW(), 4, 6),
       (3, '그래프', NOW(), NOW(), 9, 2),
       (4, '다이나믹 프로그래밍', NOW(), NOW(), 40, 9),
       (5, '배열', NOW(), NOW(), 3, 2),
       (6, '스택', NOW(), NOW(), 2, 3),
       (8, '유니온 파인드', NOW(), NOW(), 7, 5),
       (9, '백트래킹', NOW(), NOW(), 2, 5),
       (10, '재귀', NOW(), NOW(), 2, 6),
       (11, '이진 탐색', NOW(), NOW(), 1, 1),
       (12, '정렬', NOW(), NOW(), 1, 5),
       (13, '그리디 알고리즘', NOW(), NOW(), 5, 4),
       (14, '투 포인터', NOW(), NOW(), 1, 1),
       (15, '비트 연산자', NOW(), NOW(), 0, 2),
       (17, '슬라이딩 윈도우', NOW(), NOW(), 2, 0),
       (18, '수학', NOW(), NOW(), 0, 4),
       (19, '문자열', NOW(), NOW(), 0, 1),
       (20, '트리', NOW(), NOW(), 0, 0),
       (21, '바이너리 인덱스 트리', NOW(), NOW(), 0, 0),
       (22, '이진 검색 트리', NOW(), NOW(), 0, 0),
       (23, '이진 트리', NOW(), NOW(), 0, 0),
       (26, '버킷 정렬', NOW(), NOW(), 0, 0),
       (28, '개수', NOW(), NOW(), 0, 0),
       (30, '분할 정복', NOW(), NOW(), 1, 0),
       (31, '열거', NOW(), NOW(), 0, 0),
       (34, '기하학', NOW(), NOW(), 0, 1),
       (35, '해시 함수', NOW(), NOW(), 0, 0),
       (36, '해시 테이블', NOW(), NOW(), 0, 0),
       (37, '우선순위 큐', NOW(), NOW(), 2, 1),
       (39, '라인 스위프', NOW(), NOW(), 0, 0),
       (41, '행렬', NOW(), NOW(), 0, 0),
       (42, '메모이제이션', NOW(), NOW(), 1, 0),
       (45, '단조 스택', NOW(), NOW(), 0, 0),
       (46, '정수론', NOW(), NOW(), 0, 0),
       (47, '정렬된 집합', NOW(), NOW(), 1, 0),
       (48, '누적 합', NOW(), NOW(), 0, 1),
       (50, '기수 정렬', NOW(), NOW(), 0, 0),
       (51, '롤링 해시', NOW(), NOW(), 0, 0),
       (52, '세그먼트 트리', NOW(), NOW(), 1, 0),
       (53, '시뮬레이션', NOW(), NOW(), 0, 0);

/** 알고리즘 문제 */
    INSERT INTO problem (problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb, problem_description,
                         problem_constraint, problem_example,
                         created_at, updated_at)
VALUES (3243, '도로를 추가하면서 최단 거리 구하기 I', 'MEDIUM',
    2, 4096000,
    '정수 n과 2차원 정수 배열 queries가 주어집니다.

- 도시는 0번부터 n-1번까지 총 n개 존재합니다.
- 초기에는 도시 i에서 i+1로 가는 단방향 도로가 존재합니다. (즉, 0 → 1 → 2 → ... → n-1)
- queries[i] = [ui, vi]는 도시 ui에서 도시 vi로 가는 새로운 단방향 도로를 추가함을 의미합니다.

각 쿼리가 처리된 후마다, 도시 0에서 도시 n-1까지의 최단 경로 길이를 계산하여 기록합니다.',
    '- 3 <= n <= 500
- 1 <= queries.length <= 500
- queries[i].length == 2
- 0 <= queries[i][0] < queries[i][1] < n
- queries[i][1] - queries[i][0] > 1 (즉, 인접 노드는 아님)
- 중복된 쿼리는 없음',
    '[{
      \"input\": \"n = 7, queries = [[0,5],[1,6],[2,4]]\",
      \"output\": \"[2, 2, 2]\"
    }, {
      \"input\": \"n = 8, queries = [[1,5],[2,6],[3,7],[0,4],[0,6],[0,7]]\",
      \"output\": \"[4, 4, 4, 4, 2, 1]\"
    }]', NOW(), NOW());


/** 알고리즘 문제: Two Sum */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             1,
             'Two Sum',
             'EASY',
             2,
             4096000,
             '
정수 배열 nums와 정수 target이 주어집니다.
이때, nums 배열에서 두 숫자의 합이 target이 되는 두 숫자의 index를 반환해주세요.
각 테스트케이스에 대해서는 정확히 하나의 해를 가지며, 동일한 요소를 두 번 사용할 수 없다고 가정합니다.

입력으로는 첫째 줄에 nums 배열, 두번째 줄에 정수 target이 주어집니다.
정답이 존재하는 경우 오직 하나의 답만 존재합니다.
만약 정답이 없는 경우 None을 출력하세요.',
             '
nums 배열의 길이는 2 이상 10,000 이하입니다.
nums 배열의 각 요소는 -10^9 이상 10^9 이하입니다.
target 값은 -10^9 이상 10^9 이하입니다.

더 생각해보기: O(n^2)보다 낮은 시간 복잡도를 가진 알고리즘을 생각해볼까요?',
             '[{\"input\": \"3 3\\n6\", \"output\": \"0 1\"}, {\"input\": \"-1 -2 -3 -4\\n-8\", \"output\": \"None\"}, {\"input\": \"1000000000 1000000000\\n2000000000\", \"output\": \"0 1\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Longest Substring Without Repeating Characters */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             3,
             'Longest Substring Without Repeating Characters',
             'MEDIUM',
             2,
             4096000,
             '문자열 s가 주어졌을 때, 반복되는 문자가 없는 가장 긴 부분 문자열의 길이를 구하세요.',
             '입력(Input): 문자열 s (길이 0~50,000). 공백·기호·숫자·영문 대소문자 모두 가능, 대소문자 구분.
         출력(Output): s에서 중복 문자가 없는 가장 긴 “연속” 부분문자열의 길이(정수).',
             '[{\"input\": \"abcabcbb\", \"output\": \"3\"}, {\"input\": \"bbbbb\", \"output\": \"1\"}, {\"input\": \"pwwkew\", \"output\": \"3\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Reverse Integer */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             7,
             'Reverse Integer',
             'MEDIUM',
             2,
             4096000,
             '32비트 부호 있는 정수 x가 주어졌을 때, x의 숫자를 뒤집어 반환하세요. x를 뒤집는 과정에서 값이 32비트 부호 있는 정수 범위 [-2<sup>31</sup>, 2<sup>31</sup> - 1]을 벗어나면 0을 반환합니다. 환경은 64비트 정수 (부호 있는 또는 없는)를 저장하는 것을 허용하지 않는다고 가정합니다.',
             '입력(Input): -2^31 이상 x 이하 2^31 - 1
             출력(Output) x의 자릿수를 뒤집은 정수.',
             '[{\"input\": \"-2147483412\", \"output\": \"-2143847412\"}, {\"input\": \"2147483647\", \"output\": \"0\"}, {\"input\": \"120\", \"output\": \"21\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: String To Integer Atoi */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             8,
             'String To Integer Atoi',
             'MEDIUM',
             2,
             4096000,
             'myAtoi(문자열 s) 함수를 구현하세요. 이 함수는 문자열을 32비트 부호 있는 정수로 변환합니다.

         myAtoi(문자열 s)의 알고리즘은 다음과 같습니다.

         공백 처리: 선행 공백 (\" \")을 모두 무시합니다.
         부호 결정: 다음 문자가 \'-\' 또는 \'+\'인지 확인하여 부호를 결정합니다. 둘 다 없는 경우 양수로 간주합니다.
변환: 선행 0을 건너뛰고, 숫자 아닌 문자를 만나거나 문자열 끝에 도달할 때까지 정수를 읽습니다. 숫자가 읽히지 않은 경우 결과는 0입니다.
범위 처리: 정수가 32비트 부호 있는 정수 범위 [-2^31, 2^31 - 1]를 벗어나는 경우, 범위를 벗어나지 않도록 정수를 조정합니다. 구체적으로, -2^31 미만의 정수는 -2^31로, 2^31 - 1 초과의 정수는 2^31 - 1로 조정합니다.

최종 결과를 정수로 반환합니다.',
             '0 <= s의 길이 <= 200

         입력(Input): 문자열 s (길이 0~200). 문자 구성: 영문자, 숫자, 공백 ' ', +, -, .
출력(Output): 변환된 정수 (32비트 부호 있는 정수). 범위를 벗어나는 경우 -2^31 또는 2^31 - 1로 조정합니다.',
             '[{\"input\": \"2147483647\", \"output\": \"2147483647\"}, {\"input\": \"42 with words\", \"output\": \"42\"}, {\"input\": \"20000000000000000000000000000000000000000\", \"output\": \"2147483647\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Palindrome Number */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             9,
             'Palindrome Number',
             'EASY',
             2,
             4096000,
             '정수 x가 회문(palindrome)이면 참을, 그렇지 않으면 거짓을 반환하십시오.',
             '입력(Input): -2^31 <= x <= 2^31 - 1
         출력(Output): x가 회문이면 True, 그렇지 않으면 False
         후속 질문: 정수를 문자열로 변환하지 않고 풀 수 있나요?',
             '[{\"input\": \"1221\", \"output\": \"True\"}, {\"input\": \"10\", \"output\": \"False\"}, {\"input\": \"123421\", \"output\": \"False\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Container With Most Water */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             11,
             'Container With Most Water',
             'MEDIUM',
             2,
             4096000,
             '길이가 n인 정수 배열 height가 주어집니다. n개의 수직선이 있는데, i번째 선의 두 끝점은 (i, 0)과 (i, height[i])입니다.
         x축과 함께 컨테이너를 형성하여 가장 많은 물을 담을 수 있는 두 선을 찾으세요.
         컨테이너가 담을 수 있는 최대 물의 양을 반환하세요.
         컨테이너를 기울일 수는 없습니다.',
             'n은 height 배열의 길이와 같습니다.
         입력(Input): 첫번째 줄에 n값, 두번째 줄에 height 배열이 주어집니다. 2 ≤ n ≤ 10^5 0 ≤ height[i] ≤ 10^4
         출력(Output): 최대 물의 양 (정수).',
             '[{\"input\": \"2\\n1 1\", \"output\": \"1\"}, {\"input\": \"5\\n4 3 2 1 4\", \"output\": \"16\"}, {\"input\": \"10\\n8 10 14 0 13 10 9 9 8 9\", \"output\": \"72\"}]',
             NOW(),
             NOW()
         );



/** 알고리즘 문제: Roman To Integer */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             13,
             'Roman To Integer',
             'EASY',
             2,
             4096000,
             '로마 숫자는 다음과 같은 일곱 개의 기호로 표현됩니다: I, V, X, L, C, D, M.

         기호       값
         I             1
         V             5
         X             10
         L             50
         C             100
         D             500
         M             1000
         예를 들어, 2는 로마 숫자 II로 표기되는데, 이는 두 개의 1을 더한 것입니다. 12는 XII로 표기되는데, 이는 X + II입니다. 숫자 27은 XXVII로 표기되는데, 이는 XX + V + II입니다.

         로마 숫자는 일반적으로 왼쪽에서 오른쪽으로 큰 숫자부터 작은 숫자 순으로 표기됩니다. 하지만 4는 IIII로 표기되지 않습니다. 대신 4는 IV로 표기됩니다. 1이 5 앞에 오면 1을 빼서 4를 만드는 것입니다. 9도 마찬가지로 IX로 표기됩니다. 빼기를 사용하는 경우는 다음과 같이 여섯 가지가 있습니다.

         I는 V (5)와 X (10) 앞에 와서 4와 9를 만듭니다.
         X는 L (50)과 C (100) 앞에 와서 40과 90을 만듭니다.
         C는 D (500)와 M (1000) 앞에 와서 400과 900을 만듭니다.

         로마 숫자가 주어지면 정수로 변환하십시오.',
             '문자열 s의 길이는 1 이상 15 이하입니다.
         s는 \'I\', \'V\', \'X\', \'L\', \'C\', \'D\', \'M\' 문자만 포함합니다.
s는 1부터 3999 사이의 유효한 로마 숫자임이 보장됩니다.',
             '[{\"input\": \"XCIX\", \"output\": \"99\"}, {\"input\": \"MMCMXCIX\", \"output\": \"2999\"}, {\"input\": \"MMMCMXCIX\", \"output\": \"3999\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: 3Sum */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             15,
             '3Sum',
             'MEDIUM',
             2,
             4096000,
             '정수 배열 nums가 주어졌을 때, i != j, i != k, j != k 이고 nums[i] + nums[j] + nums[k] == 0을 만족하는 모든 세 개의 숫자 묶음 [nums[i], nums[j], nums[k]]을 반환하세요.
         주의할 점은, 결과에 중복된 세 개의 숫자 묶음이 포함되어서는 안 됩니다.',
             'nums의 길이는 3 이상 3000 이하입니다.
         nums[i]는 -10^5 이상 10^5 이하입니다.
         입력(Input): 첫 번째 줄에 배열의 길이 n값이 주어집니다. 두 번째 줄에 nums 배열이 주어집니다.
         출력(Output): 세 개의 숫자 묶음의 개수 k와 각 묶음의 요소를 공백으로 구분하여 출력합니다.',
             '[{\"input\": \"5\\n-2 0 0 2 2]\", \"output\": \"1\\n-2 0 2\"}, {\"input\": \"3\\n0 0 0\", \"output\": \"1\\n0 0 0\"}, {\"input\": \"6\\n-1 0 1 2 -1 -4\", \"output\": \"2\\n-1 -1 2\\n-1 0 1\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Letter Combinations Of A Phone Number */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             17,
             'Letter Combinations Of A Phone Number',
             'MEDIUM',
             2,
             4096000,
             '2에서 9까지의 숫자로 이루어진 문자열이 주어졌을 때, 숫자가 나타낼 수 있는 모든 가능한 문자 조합을 반환하세요. 반환 순서는 상관없습니다.
         숫자와 문자의 매핑은 아래와 같습니다 (전화기 버튼과 같음). 참고로, 1은 어떤 문자와도 매핑되지 않습니다.
         2: abc 3: def 4: ghi 5: jkl 6: mno 7: pqrs 8: tuv 9: wxyz',
             'digits 배열의 길이는 0 이상 4 이하입니다.
         digits[i]는 \'2\'부터 \'9\' 사이의 숫자입니다.

입력(Input): 첫 번째 줄에 digits 배열이 주어집니다.
출력(Output): 가능한 문자 조합의 개수 k와 각 조합을 공백으로 구분하여 출력합니다.',
             '[{\"input\": \"5678\", \"output\": \"jmpt jmpu jmpv jmqt jmqu jmqv jmrt jmru jmrv jmst jmsu jmsv jnpt jnpu jnpv jnqt jnqu jnqv jnrt jnru jnrv jnst jnsu jnsv jopt jopu jopv joqt joqu joqv jort joru jorv jost josu josv kmpt kmpu kmpv kmqt kmqu kmqv kmrt kmru kmrv kmst kmsu kmsv knpt knpu knpv knqt knqu knqv knrt knru knrv knst knsu knsv kopt kopu kopv koqt koqu koqv kort koru korv kost kosu kosv lmpt lmpu lmpv lmqt lmqu lmqv lmrt lmru lmrv lmst lmsu lmsv lnpt lnpu lnpv lnqt lnqu lnqv lnrt lnru lnrv lnst lnsu lnsv lopt lopu lopv loqt loqu loqv lort loru lorv lost losu losv\"}, {\"input\": \"2\", \"output\": \"3\\na b c\"}, {\"input\": \"3\", \"output\": \"3\\nd e f\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Valid Parentheses */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             20,
             'Valid Parentheses',
             'EASY',
             2,
             4096000,
             '문자열 s가 \'(\' , \')\', \'{\', \'}\', \'[\' , \']\' 문자로만 이루어져 있을 때, 입력된 문자열이 유효한지 판단하세요.

입력된 문자열이 유효하려면 다음 조건을 만족해야 합니다.

열린 괄호는 동일한 유형의 괄호로 닫혀야 합니다.
열린 괄호는 올바른 순서로 닫혀야 합니다.
모든 닫힌 괄호는 동일한 유형의 열린 괄호에 해당해야 합니다.',
             '1 <= s의 길이 <= 10^4
         s는 괄호 \'()[]{}\'로만 구성됩니다.',
    '[{\"input\": \"((((((()))))))\", \"output\": \"True\"}, {\"input\": \"{[()]}\", \"output\": \"True\"}, {\"input\": \"[[[[[[{{{{}}}}]]]]]]\", \"output\": \"True\"}]',
    NOW(),
    NOW()
);


/** 알고리즘 문제: Generate Parentheses */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             22,
             'Generate Parentheses',
             'MEDIUM',
             2,
             4096000,
             'n 쌍의 괄호가 주어졌을 때, 올바른 괄호 쌍 조합을 모두 생성하는 함수를 작성하세요.',
             '1 이상 n 이하',
             '[{\"input\": \"2\", \"output\": \"2\\n(()) ()()\"}, {\"input\": \"3\", \"output\": \"5\\n((())) (()()) (())() ()(()) ()()()\"}, {\"input\": \"4\", \"output\": \"14\\n(((()))) ((()())) ((())()) ((()))() (()(())) (()()()) (()())() (())(()) (())()() ()((())) ()(()()) ()(())() ()()(()) ()()()()\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Remove Duplicates From Sorted Array */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             26,
             'Remove Duplicates From Sorted Array',
             'EASY',
             2,
             4096000,
             '정렬된 정수 배열 nums가 주어지면, 중복된 값을 제자리에서 제거하여 각 고유한 요소가 한 번만 나타나도록 합니다. 요소들의 상대적인 순서는 동일하게 유지해야 합니다. 그런 다음 nums에서 고유한 요소의 수를 반환합니다.

         nums의 고유한 요소의 수를 k라고 가정하면, 답을 맞히기 위해 다음을 수행해야 합니다.

         nums 배열을 변경하여 nums의 처음 k개 요소에 원래 nums에 존재했던 순서대로 고유한 요소가 포함되도록 합니다. nums의 나머지 요소는 중요하지 않으며 nums의 크기도 중요하지 않습니다.
         k를 반환합니다.

         맞춤형 심사관:
         심사관은 다음 코드로 당신의 솔루션을 테스트합니다.

         int[] nums = [...]; // 입력 배열
         int[] expectedNums = [...]; // 올바른 길이의 예상 답

         int k = removeDuplicates(nums); // 당신의 구현을 호출합니다.

         assert k == expectedNums.length;
         for (int i = 0; i < k; i++) {
             assert nums[i] == expectedNums[i];
         }

         모든 어서트가 통과하면, 당신의 솔루션이 허용됩니다.',
             'nums 배열의 길이는 1 이상 3 * 10^4 이하입니다.
         nums 배열의 각 요소는 -100 이상 100 이하입니다.
         nums 배열은 오름차순으로 정렬되어 있습니다.

         입력(Input): 첫 번째 줄에 배열 크기 n, 두 번째 줄에 nums 배열이 주어집니다.',
             '[{\"input\": \"10\\n0 0 0 0 0 0 0 0 0 0\", \"output\": \"1\"}, {\"input\": \"3\\n-100 0 100]\", \"output\": \"3\"}, {\"input\": \"8\\n1 2 2 3 4 4 4 5]\", \"output\": \"5\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Longest Valid Parentheses */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             32,
             'Longest Valid Parentheses',
             'HARD',
             2,
             4096000,
             '\'(\'와 \')\' 문자로만 이루어진 문자열이 주어졌을 때, 가장 긴 유효한(올바르게 구성된) 괄호 부분 문자열의 길이를 반환하세요.',
             '0 <= s의 길이 <= 3 * 10^4
         s[i]는 \'(\' 또는 \')\'입니다.',
             '[{\"input\": \"(()())\", \"output\": \"6\"}, {\"input\": \"())\", \"output\": \"2\"}, {\"input\": \"())(()\", \"output\": \"2\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Valid Sudoku */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             36,
             'Valid Sudoku',
             'MEDIUM',
             2,
             4096000,
             '9x9 스도쿠 보드의 유효성을 확인하세요. 다음 규칙에 따라 채워진 셀만 유효성을 검사하면 됩니다.

         각 행은 1부터 9까지의 숫자를 중복 없이 포함해야 합니다.
         각 열은 1부터 9까지의 숫자를 중복 없이 포함해야 합니다.
         그리드의 아홉 개의 3x3 서브 박스 각각은 1부터 9까지의 숫자를 중복 없이 포함해야 합니다.

         참고:

         스도쿠 보드 (부분적으로 채워짐)는 유효할 수 있지만 반드시 풀 수 있는 것은 아닙니다.
         언급된 규칙에 따라 채워진 셀만 유효성을 검사하면 됩니다.',
             '보드의 길이는 9입니다.
         board[i]의 길이는 9입니다.
         board[i][j]는 숫자 1부터 9까지 또는 \'.\'입니다.

입력(Input): 첫 번째 줄에 보드의 길이 n이 주어집니다. 두 번째 줄부터 n+1번째 줄까지 보드의 각 행이 주어집니다.',
             '[{\"input\": \"9\\n8 3 . . 7 . . . .\\n6 . . 1 9 5 . . .\\n. 9 8 . . . . 6 .\\n8 . . . 6 . . . 3\\n4 . . 8 . 3 . . 1\\n7 . . . 2 . . . 6\\n. 6 . . . . 2 8 .\\n. . . 4 1 9 . . 5\\n. . . . 8 . . 7 9\", \"output\": \"False\"}, {\"input\": \"9\\n5 3 . . 7 . . . .\\n6 . . 1 9 5 . . .\\n. 9 8 . . . . 6 .\\n8 . . . 6 . . . 3\\n4 . . 8 . 3 . . 1\\n7 . . . 2 . . . 6\\n. 6 . . . . 2 8 .\\n. . . 4 1 9 . . 5\\n. . . . 8 . . 7 9\", \"output\": \"True\"}, {\"input\": \"9\\n. . . . . . 5 1 9\\n. 9 . . 5 . . . .\\n. . . 2 . 4 . . .\\n4 . . . . . 1 . .\\n. . . . . . 7 . 3\\n. . 2 . . . . . 6\\n. 6 . . . . . 8 .\\n. . . 4 1 9 . . 5\\n. . . . 8 . . 7 9\", \"output\": \"False\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Count And Say */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             38,
             'Count And Say',
             'MEDIUM',
             2,
             4096000,
             '카운트 앤드 세이 수열은 다음과 같은 재귀적 공식을 사용하여 정의되는 숫자 문자열 수열입니다.

         countAndSay(1) = \"1\"
         countAndSay(n)은 countAndSay(n - 1)의 런 길이 인코딩입니다.

         런 길이 인코딩(RLE)은 연속된 동일한 문자(2번 이상 반복)를 해당 문자와 문자 개수(런 길이)를 나타내는 숫자를 연결한 것으로 대체하는 문자열 압축 방법입니다. 예를 들어, \"3322251\" 문자열을 압축하려면 \"33\"을 \"23\"으로, \"222\"를 \"32\"로, \"5\"를 \"15\"로, \"1\"을 \"11\"로 바꿉니다. 따라서 압축된 문자열은 \"23321511\"이 됩니다.

         양의 정수 n이 주어졌을 때, 카운트 앤드 세이 수열의 n번째 요소를 반환하세요.',
             '1 이상 30 이하

         후속 질문: 반복적으로 해결할 수 있습니까?',
             '[{\"input\": \"8\", \"output\": \"1113213211\"}, {\"input\": \"3\", \"output\": \"21\"}, {\"input\": \"30\", \"output\": \"311311222113111231131112132112311321322112111312211312111322212311322113212221\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: First Missing Positive */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             41,
             'First Missing Positive',
             'HARD',
             2,
             4096000,
             '정렬되지 않은 정수 배열 nums가 주어졌을 때, nums에 없는 가장 작은 양의 정수를 반환하시오.
         알고리즘은 O(n) 시간 복잡도와 O(1) 보조 공간을 사용하여 구현해야 합니다.',
             '1 <= nums 배열의 길이 <= 10^5
         -2^31 <= nums[i] <= 2^31 - 1

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.',
             '[{\"input\": \"3\\n1000000 -1000000 500000\", \"output\": \"1\"}, {\"input\": \"3\\n1000000 -1000000 1\", \"output\": \"2\"}, {\"input\": \"4\\n1 1 1 1\", \"output\": \"2\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Trapping Rain Water */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             42,
             'Trapping Rain Water',
             'HARD',
             2,
             4096000,
             '너비가 1인 막대 그래프로 표현되는 고도 맵이 주어졌을 때, 비가 온 후 얼마나 많은 물을 가둘 수 있는지 계산하시오.',
             'n은 height의 길이와 같다.
         1 ≤ n ≤ 2 * 10^4
         0 ≤ height[i] ≤ 10^5

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 height 배열이 주어집니다.',
             '[{\"input\": \"9\\n3 1 2 1 4 3 2 1 5\", \"output\": \"11\"}, {\"input\": \"7\\n3 0 1 3 0 1 3\", \"output\": \"10\"}, {\"input\": \"5\\n5 4 3 2 1\", \"output\": \"0\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Jump Game Ii */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             45,
             'Jump Game Ii',
             'MEDIUM',
             2,
             4096000,
             '길이 n의 정수 배열 nums가 0부터 시작하는 인덱스로 주어집니다. 처음 위치는 nums[0]입니다.
         각 요소 nums[i]는 인덱스 i에서 앞으로 점프할 수 있는 최대 거리를 나타냅니다. 즉, nums[i]에 있다면 다음 조건을 만족하는 모든 nums[i + j]로 점프할 수 있습니다.

         0 <= j <= nums[i] 이고
         i + j < n

         nums[n - 1]에 도달하기 위한 최소 점프 횟수를 반환하세요. 테스트 케이스는 nums[n - 1]에 도달할 수 있도록 생성됩니다.',
             'nums의 길이는 1 이상 10,000 이하입니다.
         nums[i]는 0 이상 1000 이하입니다.
         nums[n - 1]에 도달할 수 있음이 보장됩니다.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.',
             '[{\"input\": \"211\\n5 6 4 4 6 9 4 4 7 4 4 8 2 6 8 1 5 9 6 5 2 7 9 7 9 6 9 4 1 6 8 8 4 4 2 0 7 1 3 8 0 1 2 1 2 4 3 2 0 7 1 2 7 0 6 8 0 6 5 9 9 7 4 6 6 5 8 9 3 4 3 7 0 4 9 0 9 8 4 3 0 7 7 1 9 1 9 4 9 0 1 9 5 7 7 9 5 6 6 4 6 8 3 1 2 1 3 4 6 0 7 1 9 8 0 4 3 0 4 3 0 8 6 1 1 6 8 7 5 3 1 1 5 0 7 3 7 0 9 1 2 1 8 8 7 2 9 7 9 7 9 6 9 4 1 6 8 8 4 4 2 0 7 1 3 8 0 1 2 1 2 4 3 2 0 7 1 2 7 0 6 8 0 6 5 9 9 7 4 6 6 5 8 9 3 4 3 7 0 4 9 0 9 8 4 3 0 7 7 1 9\", \"output\": \"32\"}, {\"input\": \"10\\n1 1 1 1 1 1 1 1 1 1\", \"output\": \"9\"}, {\"input\": \"5\\n2 3 0 1 4\", \"output\": \"2\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Permutations */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             46,
             'Permutations',
             'MEDIUM',
             2,
             4096000,
             '고유한 정수로 이루어진 배열 nums가 주어지면, 가능한 모든 순열을 반환하세요. 답은 어떤 순서로 반환해도 좋습니다.',
             'nums의 길이는 1 이상 6 이하입니다.
         nums[i]는 -10 이상 10 이하입니다.
         nums의 모든 정수는 중복되지 않습니다.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.',
             '[{\"input\": \"1\\n1\", \"output\": \"1\\n1\"}, {\"input\": \"4\\n4 5 6 7\", \"output\": \"24\\n4 5 6 7\\n4 5 7 6\\n4 6 5 7\\n4 6 7 5\\n4 7 5 6\\n4 7 6 5\\n5 4 6 7\\n5 4 7 6\\n5 6 4 7\\n5 6 7 4\\n5 7 4 6\\n5 7 6 4\\n6 4 5 7\\n6 4 7 5\\n6 5 4 7\\n6 5 7 4\\n6 7 4 5\\n6 7 5 4\\n7 4 5 6\\n7 4 6 5\\n7 5 4 6\\n7 5 6 4\\n7 6 4 5\\n7 6 5 4\"}, {\"input\": \"3\\n4 5 6\", \"output\": \"6\\n4 5 6\\n4 6 5\\n5 4 6\\n5 6 4\\n6 4 5\\n6 5 4\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Permutations Ii */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             47,
             'Permutations Ii',
             'MEDIUM',
             2,
             4096000,
             '중복된 숫자를 포함할 수 있는 숫자 모음 nums가 주어지면, 가능한 모든 고유 순열을 어떤 순서로든 반환하세요.',
             'nums 배열의 길이는 1 이상 8 이하입니다.
         nums 배열의 각 요소 값은 -10 이상 10 이하입니다.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.',
             '[{\"input\": \"5\\n0 1 0 0 9\", \"output\": \"20\\n0 0 0 1 9\\n0 0 0 9 1\\n0 0 1 0 9\\n0 0 1 9 0\\n0 0 9 0 1\\n0 0 9 1 0\\n0 1 0 0 9\\n0 1 0 9 0\\n0 1 9 0 0\\n0 9 0 0 1\\n0 9 0 1 0\\n0 9 1 0 0\\n1 0 0 0 9\\n1 0 0 9 0\\n1 0 9 0 0\\n1 9 0 0 0\\n9 0 0 0 1\\n9 0 0 1 0\\n9 0 1 0 0\\n9 1 0 0 0\"}, {\"input\": \"4\\n1 1 1 1\", \"output\": \"4\\n1 1 1 1\"}, {\"input\": \"3\\n1 1 1\", \"output\": \"1\\n1 1 1\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Group Anagrams */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             49,
             'Group Anagrams',
             'MEDIUM',
             2,
             4096000,
             '문자열 배열 strs가 주어졌을 때, 애너그램끼리 묶으세요. 반환 순서는 상관없습니다.',
             'strs 배열의 길이는 1 이상 10,000 이하입니다.
         strs[i] 문자열의 길이는 0 이상 100 이하입니다.
         strs[i]는 소문자 영어 알파벳으로 구성됩니다.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄부터 n개 알파벳이 주어집니다.',
             '[{\"input\": \"5\\na b c d e\", \"output\": \"5\\na\\nb\\nc\\nd\\ne\"}, {\"input\": \"6\\nabc bca cab xyz zyx yxz\", \"output\": \"2\\nabc bca cab\\nxyz zyx yxz\"}, {\"input\": \"4\\nabc def ghi jkl\", \"output\": \"4\\nabc\\ndef\\nghi\\njkl\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: N Queens */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             51,
             'N Queens',
             'HARD',
             2,
             4096000,
             'n-퀸 퍼즐은 n x n 체스판에 퀸 n개를 서로 공격하지 않도록 배치하는 문제입니다.

         정수 n이 주어지면, n-퀸 퍼즐의 서로 다른 모든 해답을 반환하세요. 반환 순서는 상관없습니다.

         각 해답은 n-퀸 배치의 서로 다른 보드 구성을 포함하며, 여기서 \'Q\'는 퀸, \'.\'은 빈 공간을 나타냅니다.',
             '1 이상 9 이하',
             '[{\"input\": \"4\", \"output\": \"2\\n.Q.. ...Q Q... ..Q.\\n..Q. Q... ...Q .Q..\"}, {\"input\": \"2\", \"output\": \"1\\nQ\"}, {\"input\": \"6\", \"output\": \"0\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: N Queens Ii */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             52,
             'N Queens Ii',
             'HARD',
             2,
             4096000,
             'N-퀸 문제는 n x n 체스판에 서로 공격할 수 없는 n개의 퀸을 놓는 문제입니다. 정수 n이 주어지면, N-퀸 문제의 서로 다른 해의 개수를 반환하세요.',
             '1 이상 9 이하',
             '[{\"input\": \"8\", \"output\": \"92\"}, {\"input\": \"3\", \"output\": \"0\"}, {\"input\": \"4\", \"output\": \"2\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Maximum Subarray */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             53,
             'Maximum Subarray',
             'MEDIUM',
             2,
             4096000,
             '정수 배열 nums가 주어졌을 때, 합이 가장 큰 부분 배열을 찾아 그 합을 반환하세요.',
             'nums 배열의 길이는 1 이상 10^5 이하입니다.
         nums[i]는 -10^4 이상 10^4 이하입니다.

         추가 질문: O(n) 솔루션을 찾았다면, 분할 정복 방식을 사용하여 더욱 까다로운 다른 솔루션을 코딩해 보세요.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.',
             '[{\"input\": \"6\\n0 1 2 3 4 5\", \"output\": \"15\"}, {\"input\": \"10\\n-2 1 -3 4 -1 2 1 -5 4 7\", \"output\": \"12\"}, {\"input\": \"5\\n10000 -10000 10000 -10000 10000\", \"output\": \"10000\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Jump Game */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             55,
             'Jump Game',
             'MEDIUM',
             2,
             4096000,
             '정수 배열 nums가 주어집니다. 배열의 첫 번째 인덱스에서 시작하며, 배열의 각 요소는 해당 위치에서 점프할 수 있는 최대 거리를 나타냅니다.
         마지막 인덱스에 도달할 수 있으면 True, 그렇지 않으면 False를 반환하세요.',
             'nums 배열의 길이는 1 이상 10,000 이하입니다.
         nums 배열의 각 요소 값은 0 이상 100,000 이하입니다.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.',
             '[{\"input\": \"10\\n1 1 1 1 1 1 1 1 1 1\", \"output\": \"True\"}, {\"input\": \"4\\n2 5 0 0\", \"output\": \"True\"}, {\"input\": \"5\\n1 1 1 1 1\", \"output\": \"True\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Merge Intervals */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             56,
             'Merge Intervals',
             'MEDIUM',
             2,
             4096000,
             '구간 배열 intervals가 주어졌을 때, intervals[i] = [starti, endi]로 표현되는 겹치는 구간들을 모두 병합하여, 입력된 모든 구간을 포함하는 겹치지 않는 구간들의 배열을 반환하세요.',
             '1 <= intervals 배열의 길이 <= 10,000
         intervals[i] 배열의 길이 == 2
         0 <= 시작 값 <= 종료 값 <= 10,000

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄부터 n개의 구간이 주어집니다.
         출력(Output): 병합된 구간의 길이 m, 두 번째 줄부터 m개의 병합된 구간이 출력합니다.',
             '[{\"input\": \"4\\n1 2\\n2 3\\n3 4\\n4 5\", \"output\": \"1\\n1 5\"}, {\"input\": \"6\\n1 2\\n3 5\\n6 7\\n8 10\\n12 16\\n4 9\", \"output\": \"3\\n1 2\\n3 10\\n12 16\"}, {\"input\": \"4\\n1 3\\n2 6\\n8 10\\n15 18\", \"output\": \"3\\n1 6\\n8 10\\n15 18\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Length Of Last Word */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             58,
             'Length Of Last Word',
             'EASY',
             2,
             4096000,
             '단어와 공백으로 구성된 문자열 s가 주어지면, 문자열의 마지막 단어 길이를 반환하세요. 단어는 공백 문자가 아닌 문자로만 이루어진 최대 부분 문자열입니다.',
             's의 길이는 1 이상 10^4 이하입니다.
         s는 영어 알파벳과 공백 \' \'으로만 구성됩니다.
s에는 적어도 한 개의 단어가 존재합니다.',
             '[{\"input\": \"Hello World\", \"output\": \"5\"}, {\"input\": \"   fly me   to   the moon  \", \"output\": \"4\"}, {\"input\": \"luffy is still joyboy\", \"output\": \"6\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Spiral Matrix Ii */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             59,
             'Spiral Matrix Ii',
             'MEDIUM',
             2,
             4096000,
             '양의 정수 n이 주어지면, 1부터 n²까지의 숫자를 나선형 순서로 채운 n x n 행렬을 생성하세요.',
             '1 이상 n 이하

             입력(Input): 첫 번째 줄에 n이 주어집니다.
             출력(Output): 첫 번째 줄에 n을 작성합니다. 두 번째 줄부터 n + 1번째 줄까지 n x n 행렬을 출력합니다.',
             '[{\"input\": \"3\", \"output\": \"3\\n1 2 3\\n8 9 4\\n7 6 5\"}, {\"input\": \"4\", \"output\": \"4\\n1 2 3 4\\n12 13 14 5\\n11 16 15 6\\n10 9 8 7\"}, {\"input\": \"2\", \"output\": \"2\\n1 2\\n4 3\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Valid Number */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             65,
             'Valid Number',
             'HARD',
             2,
             4096000,
             '문자열 s가 유효한 숫자인지 여부를 반환하세요.

         예를 들어, \"2\", \"0089\", \"-0.1\", \"+3.14\", \"4.\", \"-.9\", \"2e10\", \"-90E3\", \"3e+7\", \"+6e-1\", \"53.5e93\", \"-123.456e789\"는 모두 유효한 숫자입니다. 반면 \"abc\", \"1a\", \"1e\", \"e3\", \"99e2.5\", \"--6\", \"-+3\", \"95a54e53\"은 유효하지 않습니다.

         정확히 말하면, 유효한 숫자는 다음 정의 중 하나를 사용하여 정의됩니다.

         선택적인 지수가 뒤따르는 정수.
         선택적인 지수가 뒤따르는 십진수.

         정수는 선택적인 부호 \'-\' 또는 \'+\' 다음에 숫자가 오는 것으로 정의됩니다.
십진수는 선택적인 부호 \'-\' 또는 \'+\' 다음에 다음 정의 중 하나가 오는 것으로 정의됩니다.

숫자 다음에 점 \'.\'이 오는 경우.
숫자 다음에 점 \'.\'이 오고 숫자가 오는 경우.
점 \'.\' 다음에 숫자가 오는 경우.

지수는 지수 표기법 \'e\' 또는 \'E\' 다음에 정수가 오는 것으로 정의됩니다.
숫자는 하나 이상의 숫자로 정의됩니다.',
             's의 길이는 1 이상 20 이하입니다.
         s는 영어 대소문자, 숫자 (0-9), 더하기 \'+\', 빼기 \'-\', 또는 점 \'.\'으로만 구성됩니다.',
             '[{\"input\": \"0089\", \"output\": \"True\"}, {\"input\": \"-123.456e789\", \"output\": \"True\"}, {\"input\": \"95a54e53\", \"output\": \"False\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Plus One */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             66,
             'Plus One',
             'EASY',
             2,
             4096000,
             '정수 배열 digits로 표현된 큰 정수가 주어집니다. digits[i]는 정수의 i번째 자릿수를 나타내며, 가장 중요한 자릿수부터 가장 덜 중요한 자릿수 순으로 왼쪽에서 오른쪽으로 정렬되어 있습니다. 큰 정수에는 선행 0이 없습니다.

         큰 정수에 1을 더하고, 결과로 얻은 자릿수 배열을 반환하세요.',
             'digits 배열의 길이는 1 이상 100 이하입니다.
         digits 배열의 각 요소는 0 이상 9 이하입니다.
         digits 배열은 0으로 시작하지 않습니다.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 digits 배열이 주어집니다.
         출력(Output): 첫 번째 줄에 배열의 길이 m, 두 번째 줄부터 m개의 자릿수가 출력됩니다.',
             '[{\"input\": \"5\\n5 8 9 9 9\", \"output\": \"5\\n5 9 0 0 0\"}, {\"input\": \"5\\n2 0 0 0 0\", \"output\": \"5\\n2 0 0 0 1\"}, {\"input\": \"4\\n1 0 0 0\", \"output\": \"4\\n1 0 0 1\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Sqrtx */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             69,
             'Sqrtx',
             'EASY',
             2,
             4096000,
             '0보다 크거나 같은 정수 x가 주어지면, x의 제곱근을 구하여 가장 가까운 정수로 내림합니다. 반환되는 정수 역시 0보다 크거나 같아야 합니다.

         내장된 지수 함수나 연산자를 사용해서는 안 됩니다.

         예를 들어, C++에서는 pow(x, 0.5)를, 파이썬에서는 x ** 0.5를 사용해서는 안 됩니다.',
             '0 이상 2^31 - 1 이하

             입력(Input): 첫 번째 줄에 x가 주어집니다.
             출력(Output): x의 제곱근을 가장 가까운 정수로 내림한 값을 출력합니다.',
             '[{\"input\": \"2147483647\", \"output\": \"46340\"}, {\"input\": \"26\", \"output\": \"5\"}, {\"input\": \"4\", \"output\": \"2\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Climbing Stairs */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             70,
             'Climbing Stairs',
             'EASY',
             2,
             4096000,
             '계단을 오르고 있습니다. 정상에 도달하려면 n개의 계단을 올라야 합니다.
         한 번에 1계단 또는 2계단을 오를 수 있습니다. 정상까지 오르는 서로 다른 방법은 몇 가지입니까?',
             '1 이상 n 이하',
             '[{\"input\": \"3\", \"output\": \"3\"}, {\"input\": \"45\", \"output\": \"1836311903\"}, {\"input\": \"4\", \"output\": \"5\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Remove Duplicates From Sorted Array Ii */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             80,
             'Remove Duplicates From Sorted Array Ii',
             'MEDIUM',
             2,
             4096000,
             '정렬된 정수 배열 nums가 주어지면, 각 고유 요소가 최대 두 번 나타나도록 중복된 요소를 제자리에서 제거하세요. 요소들의 상대적인 순서는 동일하게 유지되어야 합니다.

         일부 언어에서는 배열의 길이를 변경하는 것이 불가능하므로, 결과는 배열 nums의 앞부분에 배치되어야 합니다. 더 구체적으로, 중복을 제거한 후 k개의 요소가 있다면, nums의 처음 k개 요소가 최종 결과를 담아야 합니다. 처음 k개 요소를 넘어서 남겨진 값은 중요하지 않습니다.

         최종 결과를 nums의 처음 k개 슬롯에 배치한 후 k를 반환하세요.

         추가 배열에 대한 여유 공간을 할당하지 마세요. 입력 배열을 제자리에서 수정하여 O(1)의 추가 메모리만 사용해야 합니다.

         커스텀 심사:

         심사자는 다음과 같은 코드로 여러분의 솔루션을 테스트합니다.

         int[] nums = [...]; // 입력 배열
         int[] expectedNums = [...]; // 올바른 길이의 예상 답

         int k = removeDuplicates(nums); // 여러분의 구현 호출

         assert k == expectedNums.length;
         for (int i = 0; i < k; i++) {
             assert nums[i] == expectedNums[i];
         }

         모든 어서트가 통과하면 여러분의 솔루션이 허용됩니다.',
             'nums의 길이는 1 이상 30,000 이하입니다.
         nums의 각 원소는 -10,000 이상 10,000 이하입니다.
         nums는 오름차순으로 정렬되어 있습니다.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.',
             '[{\"input\": \"10\\n0 0 0 0 0 0 0 0 0 0\", \"output\": \"2\"}, {\"input\": \"3\\n1 1 1\", \"output\": \"2\"}, {\"input\": \"4\\n1 1 1 1\", \"output\": \"2\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Largest Rectangle In Histogram */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             84,
             'Largest Rectangle In Histogram',
             'HARD',
             2,
             4096000,
             '각 막대의 너비가 1인 히스토그램의 막대 높이를 나타내는 정수 배열 heights가 주어졌을 때, 히스토그램에서 가장 큰 직사각형의 면적을 반환하세요.',
             '1 <= 높이.길이 <= 10^5
         0 <= 높이[i] <= 10^4

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 heights 배열이 주어집니다.',
             '[{\"input\": \"1\\n1\", \"output\": \"1\"}, {\"input\": \"5\\n1 0 1 0 1\", \"output\": \"1\"}, {\"input\": \"5\\n10000 10000 10000 10000 10000\", \"output\": \"50000\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Gray Code */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             89,
             'Gray Code',
             'MEDIUM',
             2,
             4096000,
             'n 비트 그레이 코드 시퀀스는 다음 조건을 만족하는 2n 개의 정수 시퀀스입니다.

         *   모든 정수는 [0, 2n - 1] 범위 내에 있습니다.
         *   첫 번째 정수는 0입니다.
         *   시퀀스에 정수는 한 번만 나타납니다.
         *   인접한 모든 정수 쌍의 이진 표현은 정확히 한 비트만 다릅니다.
         *   첫 번째 정수와 마지막 정수의 이진 표현은 정확히 한 비트만 다릅니다.

         정수 n이 주어지면, 유효한 n 비트 그레이 코드 시퀀스를 아무거나 반환하세요.',
             '1 이상 16 이하
             입력(Input): 첫 번째 줄에 n이 주어집니다.
             출력(Output): 첫 번째 줄에 시퀀스의 길이 m, 두 번째 줄부터 m개의 정수가 출력됩니다.',
             '[{\"input\": \"3\", \"output\": \"8\\n0 1 3 2 6 7 5 4\"}, {\"input\": \"15\", \"output\": \"16\\n0 1 3 2 6 7 5 4 12 13 15 14 10 11 9 8\"}, {\"input\": \"4\", \"output\": \"4\\n0 1 3 2\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Subsets Ii */
-- INSERT INTO problem (
--     problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
--     problem_description, problem_constraint, problem_example,
--     created_at, updated_at
-- ) VALUES (
--     90,
--     'Subsets Ii',
--     'MEDIUM',
--     2,
--     4096000,
--     '중복된 숫자를 포함할 수 있는 정수 배열 nums가 주어질 때, 가능한 모든 부분 집합(멱집합)을 반환하세요.
-- 해결책은 중복된 부분 집합을 포함해서는 안 됩니다. 반환 순서는 상관없습니다.',
--     'nums 배열의 길이는 1 이상 10 이하입니다.
-- nums[i]의 값은 -10 이상 10 이하입니다.
-- 입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.
-- 출력(Output): 첫 번째 줄에 부분 집합의 개수 m, 두 번째 줄부터 m개의 부분 집합이 출력됩니다.',
--     '[{\"input\": \"4\\n-1 1 -1 1\", \"output\": \"9\\n-1 -1 1 1\\n-1 -1 1\\n-1 -1\\n-1 1 1\\n-1 1\\n-1\\n1 1\\n1\"}, {\"input\": \"4\\n1 2 2 3\", \"output\": \"12\\n1 2 2 3\\n1 2 2\\n1 2 3\\n1 2\\n1 3\\n1\\n2 2 3\\n2 2\\n2 3\\n2\\n3\"}, {\"input\": \"4\\n1 1 2 2\", \"output\": \"9\\n1 1 2 2\\n1 1 2\\n1 1\\n1 2 2\\n1 2\\n1\\n2 2\\n2\"}]',
--     NOW(),
--     NOW()
-- );


/** 알고리즘 문제: Decode Ways */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             91,
             'Decode Ways',
             'MEDIUM',
             2,
             4096000,
             '당신은 숫자 문자열로 암호화된 비밀 메시지를 가로챘습니다. 이 메시지는 다음과 같은 매핑을 통해 해독됩니다.

         \"1\" -> \'A\'
\"2\" -> \'B\'
...
\"25\" -> \'Y\'
\"26\" -> \'Z\'

하지만 메시지를 해독하는 과정에서, 일부 코드가 다른 코드에 포함되기 때문에 (\"2\"와 \"5\" 대 \"25\") 메시지를 해독할 수 있는 다양한 방법이 있다는 것을 깨달았습니다.

예를 들어, \"11106\"은 다음과 같이 해독될 수 있습니다.

\"AAJF\" (1, 1, 10, 6)
\"KJF\" (11, 10, 6)
(1, 11, 06)의 그룹화는 유효하지 않습니다. \"06\"은 유효한 코드 (\"6\"만 유효)가 아니기 때문입니다.

참고: 해독할 수 없는 문자열이 있을 수 있습니다.

숫자만 포함하는 문자열 s가 주어졌을 때, 이를 해독하는 방법의 수를 반환하십시오. 문자열 전체를 유효한 방법으로 해독할 수 없는 경우 0을 반환하십시오.

테스트 케이스는 답이 32비트 정수에 맞도록 생성됩니다.',
             '문자열 s의 길이는 1 이상 100 이하입니다.
         s는 숫자만 포함하며, 선행 0을 가질 수 있습니다.

         입력(Input): 첫 번째 줄에 문자열 s가 주어집니다.
         출력(Output): 해독 방법의 수를 출력합니다.',
             '[{\"input\": \"100100100\", \"output\": \"0\"}, {\"input\": \"101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010101010\", \"output\": \"1\"}, {\"input\": \"1111001111001111001111001111001111001111001111001111001111\", \"output\": \"0\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Restore Ip Addresses */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             93,
             'Restore Ip Addresses',
             'MEDIUM',
             2,
             4096000,
             '유효한 IP 주소는 점 하나로 구분된 정확히 네 개의 정수로 구성됩니다. 각 정수는 0에서 255 사이(0과 255 포함)여야 하며, 선행 0을 가질 수 없습니다.

         예를 들어 \"0.1.2.201\"과 \"192.168.1.1\"은 유효한 IP 주소이지만, \"0.011.255.245\", \"192.168.1.312\" 및 \"192.168@1.1\"은 유효하지 않습니다.

         숫자로만 구성된 문자열 s가 주어졌을 때, s에 점을 삽입하여 만들 수 있는 모든 유효한 IP 주소를 반환하세요. s의 숫자를 재정렬하거나 제거할 수 없습니다. 유효한 IP 주소는 어떤 순서로든 반환할 수 있습니다.',
             's의 길이는 1 이상 20 이하입니다.
         s는 숫자(digits)로만 이루어져 있습니다.
         입력(Input): 첫 번째 줄에 문자열 s가 주어집니다.
         출력(Output): 첫 번째 줄에 유효한 IP 주소의 개수 n, 두 번째 줄부터 n개의 유효한 IP 주소가 출력됩니다.',
             '[{\"input\": \"101023\", \"output\": \"5\\n1.0.10.23 1.0.102.3 10.1.0.23 10.10.2.3 101.0.2.3\"}, {\"input\": \"1111\", \"output\": \"1\\n1.1.1.1\"}, {\"input\": \"9876543210\", \"output\": \"0\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Unique Binary Search Trees */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             96,
             'Unique Binary Search Trees',
             'MEDIUM',
             2,
             4096000,
             '정수 n이 주어졌을 때, 1부터 n까지의 고유한 값을 갖는 노드 n개를 가진 구조적으로 고유한 BST(이진 탐색 트리)의 개수를 반환하세요.',
             '1 ≤ n ≤ 19

             입력(Input): 첫 번째 줄에 n이 주어집니다.
             출력(Output): 구조적으로 고유한 BST의 개수를 출력합니다.',
             '[{\"input\": \"3\", \"output\": \"5\"}, {\"input\": \"4\", \"output\": \"14\"}, {\"input\": \"19\", \"output\": \"1767263190\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Pascals Triangle */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             118,
             'Pascals Triangle',
             'EASY',
             2,
             4096000,
             '정수 numRows가 주어지면 파스칼의 삼각형에서 처음 numRows 행을 반환하세요. 파스칼의 삼각형에서 각 숫자는 바로 위에 있는 두 숫자의 합입니다.',
             '1 이상 30 이하

             입력(Input): 첫 번째 줄에 numRows가 주어집니다.
             출력(Output): 첫 번째 줄에 numRows를 작성하고, 두 번째 줄부터 numRows개의 행을 출력합니다.',
             '[{\"input\": \"3\", \"output\": \"3\\n1\\n1 1\\n1 2 1\"}, {\"input\": \"1\", \"output\": \"1\\n1\"}, {\"input\": \"5\", \"output\": \"5\\n1\\n1 1\\n1 2 1\\n1 3 3 1\\n1 4 6 4 1\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Pascals Triangle Ii */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             119,
             'Pascals Triangle Ii',
             'EASY',
             2,
             4096000,
             '정수 rowIndex가 주어지면, 파스칼의 삼각형에서 rowIndex번째(0부터 시작) 행을 반환하세요.
         파스칼의 삼각형에서 각 숫자는 바로 위에 있는 두 숫자의 합으로 이루어집니다.',
             '0 <= rowIndex <= 33

         후속 질문: O(rowIndex)의 추가 공간만을 사용하여 알고리즘을 최적화할 수 있습니까?

             입력(Input): 첫 번째 줄에 rowIndex가 주어집니다.
             출력(Output): rowIndex번째 행의 길이를 작성하고, 두 번째 줄에 rowIndex번째 행의 숫자를 출력합니다.',
             '[{\"input\": \"0\", \"output\": \"1\\n1\"}, {\"input\": \"10\", \"output\": \"11\\n1 10 45 120 210 252 210 120 45 10 1\"}, {\"input\": \"15\", \"output\": \"16\\n1 15 105 455 1365 3003 5005 6435 6435 5005 3003 1365 455 105 15 1\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Triangle */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             120,
             'Triangle',
             'MEDIUM',
             2,
             4096000,
             '삼각형 배열이 주어졌을 때, 맨 위에서 맨 아래까지의 최소 경로 합을 반환하세요.
         각 단계에서 바로 아래 행의 인접한 숫자로 이동할 수 있습니다. 좀 더 구체적으로 말하면, 현재 행의 인덱스 i에 있다면 다음 행의 인덱스 i 또는 i + 1로 이동할 수 있습니다.',
             '1 <= triangle의 길이 <= 200
         triangle[0]의 길이 == 1
         triangle[i]의 길이 == triangle[i - 1]의 길이 + 1
         -10^4 <= triangle[i][j] <= 10^4

         추가 질문: 삼각형의 총 행 수를 n이라고 할 때, O(n)의 추가 공간만을 사용하여 이 문제를 해결할 수 있습니까?

             입력(Input): 첫 번째 줄에 삼각형의 행 수 n, 두 번째 줄부터 n개의 행이 주어집니다.
             출력(Output): 최소 경로 합을 출력합니다.',
             '[{\"input\": \"1\\n-10\", \"output\": \"-10\"}, {\"input\": \"4\\n-1\\n2 3\\n1 -1 -3\\n-2 1 -1 -2\", \"output\": \"-3\"}, {\"input\": \"4\\n0\\n1 2\\n3 4 5\\n6 7 8 9\", \"output\": \"10\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Best Time To Buy And Sell Stock */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             121,
             'Best Time To Buy And Sell Stock',
             'EASY',
             2,
             4096000,
             '주어진 배열 prices에서 prices[i]는 i번째 날의 주식 가격을 나타냅니다.
         주식 한 주를 매수할 날짜와 미래의 다른 날짜에 매도할 날짜를 선택하여 이익을 최대화하려고 합니다.
         이 거래에서 얻을 수 있는 최대 이익을 반환하세요. 만약 이익을 얻을 수 없다면 0을 반환하세요.',
             '가격 배열의 길이는 1 이상 10^5 이하입니다.
         각 가격은 0 이상 10^4 이하입니다.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 prices 배열이 주어집니다.
         출력(Output): 최대 이익을 출력합니다.',
             '[{\"input\": \"4\\n10 9 8 2\", \"output\": \"0\"}, {\"input\": \"1\\n1\", \"output\": \"0\"}, {\"input\": \"7\\n2 1 2 1 0 1 2\", \"output\": \"2\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Best Time To Buy And Sell Stock Ii */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             122,
             'Best Time To Buy And Sell Stock Ii',
             'MEDIUM',
             2,
             4096000,
             '주어진 정수 배열 prices에서 prices[i]는 i번째 날의 특정 주식 가격을 나타냅니다.

         각 날짜에, 주식을 사거나 팔거나 결정할 수 있습니다. 동시에 최대 한 주만 보유할 수 있습니다. 하지만, 주식을 산 후 같은 날 즉시 팔 수도 있습니다.

         얻을 수 있는 최대 이익을 찾아 반환하세요.',
             '가격 배열의 길이는 1 이상 30,000 이하입니다.
         각 가격은 0 이상 10,000 이하입니다.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 prices 배열이 주어집니다.
         출력(Output): 최대 이익을 출력합니다.',
             '[{\"input\": \"7\\n1 1 1 1 1 1 1\", \"output\": \"0\"}, {\"input\": \"7\\n1 2 2 3 4 4 5\", \"output\": \"4\"}, {\"input\": \"10\\n10 9 8 7 6 5 4 3 2 1\", \"output\": \"0\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Best Time To Buy And Sell Stock Iii */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             123,
             'Best Time To Buy And Sell Stock Iii',
             'HARD',
             2,
             4096000,
             '주어진 배열 prices는 i번째 날의 주식 가격을 나타냅니다.
         최대 이익을 구하세요. 최대 두 번의 거래를 완료할 수 있습니다.
         참고: 여러 거래를 동시에 할 수 없습니다 (즉, 다시 구매하기 전에 주식을 매도해야 합니다).',
             '1 <= 가격 배열의 길이 <= 10^5
         0 <= 각 가격 값 <= 10^5

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 prices 배열이 주어집니다.
         출력(Output): 최대 이익을 출력합니다.',
             '[{\"input\": \"8\\n1 5 1 5 1 5 1 5\", \"output\": \"8\"}, {\"input\": \"7\\n2 1 4 5 2 9 7\", \"output\": \"11\"}, {\"input\": \"6\\n10 22 5 75 65 80\", \"output\": \"87\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Valid Palindrome */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             125,
             'Valid Palindrome',
             'EASY',
             2,
             4096000,
             '문자열 s가 회문인지 여부를 판단하는 문제입니다. 회문이란, 모든 대문자를 소문자로 변환하고 영문자와 숫자 외의 문자를 모두 제거한 후, 앞에서 읽으나 뒤에서 읽으나 동일한 문자열을 의미합니다. 영문자와 숫자를 포함한 문자를 영숫자 문자라고 합니다. 문자열 s가 회문이면 true, 아니면 false를 반환합니다.',
             '1 <= s의 길이 <= 2 * 10^5
         s는 출력 가능한 ASCII 문자로만 구성됩니다.

         입력(Input): 첫 번째 줄에 문자열 s가 주어집니다.
         출력(Output): s가 회문이면 True, 아니면 False를 출력합니다.',
             '[{\"input\": \"race a car\", \"output\": \"True\"}, {\"input\": \" ", \"output\": \"True\"}, {\"input\": \"Able was I ere I saw Elba\", \"output\": \"True\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Longest Consecutive Sequence */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             128,
             'Longest Consecutive Sequence',
             'MEDIUM',
             2,
             4096000,
             '정렬되지 않은 정수 배열 nums가 주어질 때, 가장 긴 연속된 요소 시퀀스의 길이를 반환하세요.
         O(n) 시간 안에 실행되는 알고리즘을 작성해야 합니다.',
             'nums의 길이는 0 이상 10^5 이하입니다.
         nums[i]는 -10^9 이상 10^9 이하입니다.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.
         출력(Output): 가장 긴 연속된 요소 시퀀스의 길이를 출력합니다',
             '[{\"input\": \"4\\n-1 -2 -3 -4\", \"output\": \"4\"}, {\"input\": \"8\\n10 5 12 3 9 7 8 11\", \"output\": \"6\"}, {\"input\": \"7\\n1 9 3 10 4 20 2\", \"output\": \"4\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Palindrome Partitioning */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             131,
             'Palindrome Partitioning',
             'MEDIUM',
             2,
             4096000,
             '문자열 s가 주어졌을 때, s를 분할하되 각 부분 문자열이 회문이 되도록 하세요. s의 모든 가능한 회문 분할을 반환하세요.',
             '1 <= s의 길이 <= 16

         s는 소문자 영어 알파벳만 포함합니다.

         입력(Input): 첫 번째 줄에 문자열 s가 주어집니다.
         출력(Output): 첫 번째 줄에 분할의 개수 n, 두 번째 줄부터 n + 1번째 줄까지 n개의 분할이 출력됩니다.',
             '[{\"input\": \"madam\", \"output\": \"3\\nm a d a m\\nm ada m\\nmadam\"}, {\"input\": \"deified\", \"output\": \"4\\nd e i f i e d\\nd e ifi e d\\nd eifie d\\ndeified\"}, {\"input\": \"repaper\", \"output\": \"4\\nr e p a p e r\\nr e pap e r\\nr epape r\\nrepaper\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Palindrome Partitioning Ii */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             132,
             'Palindrome Partitioning Ii',
             'HARD',
             2,
             4096000,
             '문자열 s가 주어졌을 때, s를 분할하여 각 부분 문자열이 회문이 되도록 하세요.
         s를 회문으로 분할하기 위해 필요한 최소 분할 횟수를 반환하세요.',
             '1 <= s의 길이 <= 2000
         s는 소문자 영어 알파벳으로만 구성됩니다.

         입력(Input): 첫 번째 줄에 문자열 s가 주어집니다.
         출력(Output): s를 회문으로 분할하기 위해 필요한 최소 분할 횟수를 출력합니다.',
             '[{\"input\": \"abcba\", \"output\": \"0\"}, {\"input\": \"aabbaa\", \"output\": \"0\"}, {\"input\": \"abcdcba\", \"output\": \"0\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Candy */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             135,
             'Candy',
             'HARD',
             2,
             4096000,
             'n명의 아이들이 일렬로 서 있습니다. 각 아이는 정수 배열 ratings에 주어진 평점을 받습니다.

         다음 요구 사항에 따라 이 아이들에게 사탕을 나누어 주려고 합니다.

         각 아이는 최소한 하나의 사탕을 받아야 합니다.
         평점이 높은 아이는 이웃보다 더 많은 사탕을 받아야 합니다.

         아이들에게 사탕을 나누어주기 위해 필요한 최소 사탕의 개수를 반환하세요.',
             'n은 ratings의 길이와 같다.
         1 <= n <= 2 * 10^4
         0 <= ratings[i] <= 2 * 10^4

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 ratings 배열이 주어집니다.
         출력(Output): 필요한 최소 사탕의 개수를 출력합니다.',
             '[{\"input\": \"5\\n50 40 30 20 10\", \"output\": \"15\"}, {\"input\": \"1\\n1\", \"output\": \"1\"}, {\"input\": \"5\\n1 3 4 5 2\", \"output\": \"11\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Single Number */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             136,
             'Single Number',
             'EASY',
             2,
             4096000,
             '정수 배열 nums가 주어졌는데, 모든 요소는 두 번씩 나타나고 딱 하나의 요소만 한 번 나타납니다. 이 한 번 나타나는 숫자를 찾아야 합니다.
         선형 시간 복잡도로 해결해야 하며, 추가적인 공간은 상수 공간만 사용해야 합니다.',
             '1 <= nums 배열의 길이 <= 3 * 10^4
         -3 * 10^4 <= nums[i] <= 3 * 10^4
         배열의 각 요소는 두 번씩 나타나고, 단 하나의 요소만 한 번 나타납니다.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.
         출력(Output): 한 번 나타나는 숫자를 출력합니다.',
             '[{\"input\": \"5\\n-1 2 -1 -2 2\", \"output\": \"-2\"}, {\"input\": \"1\\n1\", \"output\": \"1\"}, {\"input\": \"5\\n10 1 10 2 2\", \"output\": \"1\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Single Number Ii */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             137,
             'Single Number Ii',
             'MEDIUM',
             2,
             4096000,
             '정수 배열 nums가 주어집니다. 모든 요소는 세 번 나타나고, 딱 한 번만 나타나는 요소가 하나 있습니다. 이 단일 요소를 찾아 반환하세요.

         선형 시간 복잡도로 해결해야 하며, 상수 공간만을 사용해야 합니다.',
             '1 <= nums 배열의 길이 <= 3 * 10^4
         -2^31 <= nums[i] <= 2^31 - 1
         nums의 각 요소는 정확히 세 번 나타나고, 한 요소만 한 번 나타납니다.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.
         출력(Output): 한 번 나타나는 숫자를 출력합니다.',
             '[{\"input\": \"4\\n1000000000 1000000000 1000000000 7\", \"output\": \"7\"}, {\"input\": \"4\\n-1 -1 -1 100\", \"output\": \"100\"}, {\"input\": \"4\\n-2 -2 -2 1\", \"output\": \"1\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Max Points On A Line */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             149,
             'Max Points On A Line',
             'HARD',
             2,
             4096000,
             'X-Y 평면 상의 점들을 나타내는 points[i] = [xi, yi] 배열이 주어졌을 때, 같은 직선 위에 놓인 최대 점의 개수를 반환하세요.',
             '1 <= points의 길이 <= 300
         points[i]의 길이 == 2
         -10^4 <= xi, yi <= 10^4
         모든 점은 서로 다르다.

         입력(Input): 첫 번째 줄에 점의 개수 n, 두 번째 줄부터 n개의 점이 주어집니다.
         출력(Output): 같은 직선 위에 놓인 최대 점의 개수를 출력합니다.',
             '[{\"input\": \"6\\n0 0\\n1 1\\n2 2\\n3 3\\n4 4\\n5 5\", \"output\": \"6\"}, {\"input\": \"4\\n0 0\\n1 1\\n0 1\\n1 0\", \"output\": \"2\"}, {\"input\": \"6\\n1 2\\n2 3\\n3 4\\n4 5\\n5 6\\n6 7\", \"output\": \"6\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Evaluate Reverse Polish Notation */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             150,
             'Evaluate Reverse Polish Notation',
             'MEDIUM',
             2,
             4096000,
             '역 폴란드 표기법으로 표현된 산술 표현식을 나타내는 문자열 배열 tokens이 주어집니다.
         이 표현식을 계산하고 그 값을 나타내는 정수를 반환하세요.
         참고:

         유효한 연산자는 \'+\', \'-\', \'*\', \'/\'입니다.
각 피연산자는 정수이거나 다른 표현식일 수 있습니다.
두 정수 간의 나눗셈은 항상 0 방향으로 절삭됩니다.
0으로 나누는 경우는 없습니다.
입력은 역 폴란드 표기법의 유효한 산술 표현식을 나타냅니다.
답과 모든 중간 계산은 32비트 정수로 표현될 수 있습니다.',
             '토큰 배열의 길이는 1 이상 104 이하입니다.
         tokens[i]는 연산자 \"+\", \"-\", \"*\", \"/\"이거나, -200에서 200 사이의 정수입니다.

         입력(Input): 첫 번째 줄에 tokens 배열의 길이 n, 두 번째 줄에 n개의 토큰이 주어집니다.
         출력(Output): 계산된 값을 출력합니다.',
             '[{\"input\": \"5\\n4 13 5 / +\", \"output\": \"6\"}, {\"input\": \"5\\n2 1 + 3 *\", \"output\": \"9\"}, {\"input\": \"13\\n10 6 9 3 + -11 * / * 17 + 5 +\", \"output\": \"22\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Reverse Words In A String */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             151,
             'Reverse Words In A String',
             'MEDIUM',
             2,
             4096000,
             '입력 문자열 s가 주어지면, 단어의 순서를 뒤집으세요.
         단어는 공백 문자가 아닌 문자의 연속으로 정의됩니다. s의 단어는 하나 이상의 공백으로 구분됩니다.
         단어를 단일 공백으로 연결하여 단어의 순서를 뒤집은 문자열을 반환하세요.
         s는 앞뒤 공백이나 두 단어 사이의 여러 공백을 포함할 수 있습니다. 반환된 문자열은 단어 사이에 단일 공백만 있어야 합니다. 여분의 공백은 포함하지 마세요.',
             '1 <= s의 길이 <= 10^4
         s는 영어 대문자, 소문자, 숫자, 그리고 공백 \' \'을 포함합니다.
s에는 적어도 한 단어가 존재합니다.

추가 질문: 문자열 데이터 타입이 해당 언어에서 변경 가능하다면, O(1)의 추가 공간으로 제자리에서 문제를 해결할 수 있습니까?

입력(Input): 첫 번째 줄에 문자열 s가 주어집니다.
출력(Output): 단어의 순서를 뒤집은 문자열을 출력합니다.',
             '[{\"input\": \"the sky is blue\", \"output\": \"blue is sky the\"}, {\"input\": \"  hello world  \", \"output\": \"world hello\"}, {\"input\": \"a good   example\", \"output\": \"example good a\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Maximum Product Subarray */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             152,
             'Maximum Product Subarray',
             'MEDIUM',
             2,
             4096000,
             '정수 배열 nums가 주어졌을 때, 곱이 가장 큰 부분 배열을 찾아 그 곱을 반환하세요.
         테스트 케이스는 정답이 32비트 정수로 표현될 수 있도록 생성됩니다.',
             '1 ≤ nums 배열의 길이 ≤ 2 * 10^4
         -10 ≤ nums[i] ≤ 10
         nums의 어떤 부분 배열의 곱도 32비트 정수에 들어갈 수 있도록 보장됩니다.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.
         출력(Output): 곱이 가장 큰 부분 배열의 곱을 출력합니다.',
             '[{\"input\": \"5\\n2 -5 -2 -4 3\", \"output\": \"24\"}, {\"input\": \"1\\n1\", \"output\": \"1\"}, {\"input\": \"3\\n-2 3 -4\", \"output\": \"24\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Find Minimum In Rotated Sorted Array */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             153,
             'Find Minimum In Rotated Sorted Array',
             'MEDIUM',
             2,
             4096000,
             '오름차순으로 정렬된 길이 n인 배열이 1번에서 n번 사이에서 회전되었다고 가정해 봅시다. 예를 들어, nums = [0,1,2,4,5,6,7] 배열은 다음과 같이 변할 수 있습니다.

         * 4번 회전: [4,5,6,7,0,1,2]
         * 7번 회전: [0,1,2,4,5,6,7]

         배열 [a[0], a[1], a[2], ..., a[n-1]]을 1번 회전하면 [a[n-1], a[0], a[1], a[2], ..., a[n-2]]가 됩니다.

         고유한 요소로 이루어진 정렬된 회전 배열 nums가 주어졌을 때, 이 배열의 최소값을 반환하세요.

         O(log n) 시간 안에 실행되는 알고리즘을 작성해야 합니다.',
             'n은 nums의 길이와 같다.
         1 ≤ n ≤ 5000
         -5000 ≤ nums[i] ≤ 5000
         nums의 모든 정수는 고유하다.
         nums는 정렬되어 있으며 1번에서 n번 사이로 회전되었다.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.
         출력(Output): 최소값을 출력합니다.',
             '[{\"input\": \"7\\n4 5 6 7 0 1 2\", \"output\": \"0\"}, {\"input\": \"1\\n1\", \"output\": \"1\"}, {\"input\": \"10\\n2 3 4 5 6 7 8 9 1 0\", \"output\": \"1\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Find Minimum In Rotated Sorted Array Ii */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             154,
             'Find Minimum In Rotated Sorted Array Ii',
             'HARD',
             2,
             4096000,
             '오름차순으로 정렬된 길이가 n인 배열이 1회에서 n회 사이로 회전되었다고 가정해 봅시다. 예를 들어, nums = [0,1,4,4,5,6,7] 배열은 다음과 같이 변할 수 있습니다.

         4번 회전: [4,5,6,7,0,1,4]
         7번 회전: [0,1,4,4,5,6,7]

         배열 [a[0], a[1], a[2], ..., a[n-1]]을 1회 회전시키면 [a[n-1], a[0], a[1], a[2], ..., a[n-2]] 배열이 됩니다.

         중복된 값을 포함할 수 있는 정렬된 회전 배열 nums가 주어졌을 때, 이 배열의 최소값을 반환하십시오.

         전체 연산 단계를 가능한 한 줄여야 합니다.',
             'n은 nums의 길이와 같다.
         1 <= n <= 5000
         -5000 <= nums[i] <= 5000
         nums는 정렬된 배열이 1번에서 n번 사이로 회전된 상태이다.

         추가 질문: 이 문제는 회전 정렬된 배열에서 최소값 찾기와 유사하지만, nums에 중복된 값이 있을 수 있습니다. 이것이 런타임 복잡도에 영향을 미칠까요? 그렇다면 어떻게, 왜 그런가요?

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.
         출력(Output): 최소값을 출력합니다.',
             '[{\"input\": \"10\\n0 0 1 1 2 2 3 3 4 4\", \"output\": \"0\"}, {\"input\": \"3\\n1 3 5\", \"output\": \"1\"}, {\"input\": \"5\\n2 2 2 0 1\", \"output\": \"0\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Longest Substring With At Most Two Distinct Characters */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             159,
             'Longest Substring With At Most Two Distinct Characters',
             'MEDIUM',
             2,
             4096000,
             '문자열 s가 주어졌을 때, 서로 다른 문자를 최대 두 개 포함하는 가장 긴 부분 문자열의 길이를 반환하세요.',
             '1 <= s의 길이 <= 10^5
         s는 영어 알파벳으로 구성되어 있습니다.

         입력(Input): 첫 번째 줄에 문자열 s가 주어집니다.
         출력(Output): 서로 다른 문자를 최대 두 개 포함하는 가장 긴 부분 문자열의 길이를 출력합니다.',
             '[{\"input\": \"abcdefghijklmnopqrstuvwxyz\", \"output\": \"2\"}, {\"input\": \"ababccababcc\", \"output\": \"4\"}, {\"input\": \"aabacbebebe\", \"output\": \"6\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Find Peak Element */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             162,
             'Find Peak Element',
             'MEDIUM',
             2,
             4096000,
             '피크 요소란 주변 이웃보다 엄격하게 큰 요소를 말합니다.
         0으로 시작하는 정수 배열 nums가 주어졌을 때, 피크 요소를 찾아 그 인덱스를 반환하세요. 배열에 여러 개의 피크가 있는 경우, 어떤 피크의 인덱스든 반환하면 됩니다.
         nums[-1] = nums[n] = -∞라고 가정해도 좋습니다. 즉, 배열 바깥에 있는 이웃보다 항상 엄격하게 큰 요소로 간주됩니다.
         O(log n) 시간에 실행되는 알고리즘을 작성해야 합니다.',
             '1 <= nums 배열의 길이 <= 1000
         -2^31 <= nums[i] <= 2^31 - 1
         유효한 i에 대해, nums[i] != nums[i + 1]

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.
         출력(Output): 피크 요소의 인덱스를 출력합니다.',
             '[{\"input\": \"4\\n1 2 3 1\", \"output\": \"2\"}, {\"input\": \"5\\n1 2 2 3 1\", \"output\": \"3\"}, {\"input\": \"11\\n0 1 2 3 4 5 6 7 8 9 10\", \"output\": \"10\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Maximum Gap */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             164,
             'Maximum Gap',
             'MEDIUM',
             2,
             4096000,
             '정수 배열 nums가 주어지면, 정렬된 형태에서 연속된 두 요소 간의 최대 차이를 반환합니다. 배열에 두 개 미만의 요소가 포함된 경우 0을 반환합니다.
         선형 시간 안에 실행되고 선형 추가 공간을 사용하는 알고리즘을 작성해야 합니다.',
             '1 <= nums 배열의 길이 <= 10^5
         0 <= nums[i] <= 10^9

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.
         출력(Output): 정렬된 형태에서 연속된 두 요소 간의 최대 차이를 출력합니다.',
             '[{\"input\": \"2\\n1 1000000000\", \"output\": \"999999999\"}, {\"input\": \"11\\n8 10 58 59 9 29 90 1 7 2 45\", \"output\": \"31\"}, {\"input\": \"10\\n10 9 8 7 6 5 4 3 2 1\", \"output\": \"1\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Majority Element */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             169,
             'Majority Element',
             'EASY',
             2,
             4096000,
             '크기가 n인 배열 nums가 주어졌을 때, 과반수 요소를 반환하세요.
         과반수 요소는 ⌊n / 2⌋번 이상 나타나는 요소입니다. 과반수 요소는 항상 배열에 존재한다고 가정해도 좋습니다.',
             'n은 nums의 길이와 같다.
         1 <= n <= 5 * 10^4
         -10^9 <= nums[i] <= 10^9

         추가 질문: 선형 시간 복잡도와 O(1) 공간 복잡도로 문제를 해결할 수 있습니까?

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.
         출력(Output): 과반수 요소를 출력합니다.',
             '[{\"input\": \"7\\n1 1 2 2 2 2 2\", \"output\": \"2\"}, {\"input\": \"7\\n1 1 1 1 2 2 3\", \"output\": \"1\"}, {\"input\": \"7\\n2 2 1 1 1 2 2\", \"output\": \"2\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Excel Sheet Column Number */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             171,
             'Excel Sheet Column Number',
             'EASY',
             2,
             4096000,
             '엑셀 시트의 열 제목을 나타내는 문자열 columnTitle이 주어지면, 해당 열 번호를 반환하세요.

         예를 들어:

         A -> 1
         B -> 2
         C -> 3
         ...
         Z -> 26
         AA -> 27
         AB -> 28
         ...',
             '열 제목의 길이는 1 이상 7 이하입니다.
         열 제목은 대문자 영문 알파벳으로만 구성됩니다.
         열 제목은 \"A\"에서 \"FXSHRXW\" 범위 내에 있습니다.

         입력(Input): 첫 번째 줄에 문자열 columnTitle이 주어집니다.
         출력(Output): 해당 열 번호를 출력합니다.',
             '[{\"input\": \"MAD\", \"output\": \"8818\"}, {\"input\": \"AAA\", \"output\": \"703\"}, {\"input\": \"ZY\", \"output\": \"701\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Factorial Trailing Zeroes */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             172,
             'Factorial Trailing Zeroes',
             'MEDIUM',
             2,
             4096000,
             '정수 n이 주어졌을 때, n!의 뒤에 붙는 0의 개수를 반환하세요.
         참고로 n! = n * (n - 1) * (n - 2) * ... * 3 * 2 * 1 입니다.',
             '0 ≤ n ≤ 10,000

         추가 질문: 로그 시간 복잡도로 작동하는 해결책을 작성할 수 있습니까?

         입력(Input): 첫 번째 줄에 정수 n이 주어집니다.
         출력(Output): n!의 뒤에 붙는 0의 개수를 출력합니다.',
             '[{\"input\": \"0\", \"output\": \"0\"}, {\"input\": \"3\", \"output\": \"0\"}, {\"input\": \"10000\", \"output\": \"2499\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Largest Number */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             179,
             'Largest Number',
             'MEDIUM',
             2,
             4096000,
             '0 이상의 정수 목록 nums가 주어지면, 이들을 조합하여 가장 큰 수를 만들고 문자열로 반환하세요. 결과가 매우 클 수 있으므로 정수가 아닌 문자열로 반환해야 합니다.',
             'nums 배열의 길이는 1 이상 100 이하입니다.
         nums 배열의 각 요소 nums[i]는 0 이상 10억 이하입니다.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.
         출력(Output): 가장 큰 수를 문자열로 반환합니다.',
             '[{\"input\": \"2\\n0 0\", \"output\": \"0\"}, {\"input\": \"1\\n1\", \"output\": \"1\"}, {\"input\": \"5\\n3 30 34 5 9\", \"output\": \"9534330\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Repeated Dna Sequences */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             187,
             'Repeated Dna Sequences',
             'MEDIUM',
             2,
             4096000,
             'DNA 염기 서열은 \'A\', \'C\', \'G\', \'T\'로 축약된 일련의 뉴클레오타이드로 구성됩니다.

예를 들어, \"ACGAATTCCG\"는 DNA 염기 서열입니다.

DNA를 연구할 때, DNA 내에서 반복되는 서열을 식별하는 것이 유용합니다.

DNA 염기 서열을 나타내는 문자열 s가 주어졌을 때, DNA 분자 내에서 두 번 이상 나타나는 모든 10글자 길이의 서열(부분 문자열)을 반환하세요. 답은 어떤 순서로 반환해도 좋습니다.',
             's의 길이는 1 이상 10^5 이하입니다.
         s[i]는 \'A\', \'C\', \'G\', 또는 \'T\'입니다.

입력(Input): 첫 번째 줄에 문자열 s가 주어집니다.
출력(Output): 첫 번째 줄에 부분 문자열의 수를 출력하고, 두 번째 줄부터 부분 문자열을 출력합니다.',
             '[{\"input\": \"ACGACGACGACGACGACG\", \"output\": \"3\\nACGACGACGA CGACGACGAC GACGACGACG\"}, {\"input\": \"AAAAAAAAAAAAA\", \"output\": \"1\\nAAAAAAAAAA\"}, {\"input\": \"AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT\", \"output\": \"2\\nAAAAACCCCC CCCCCAAAAA\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Reverse Bits */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             190,
             'Reverse Bits',
             'EASY',
             2,
             4096000,
             '주어진 32비트 부호 없는 정수의 비트 순서를 반전시키세요.

         참고:

         Java와 같은 일부 언어에는 부호 없는 정수 타입이 없습니다. 이 경우 입력과 출력 모두 부호 있는 정수 타입으로 주어집니다. 정수의 내부 이진 표현은 부호가 있든 없든 동일하므로, 이는 구현에 영향을 미치지 않습니다.

         Java에서 컴파일러는 2의 보수 표기법을 사용하여 부호 있는 정수를 표현합니다. 따라서 위의 예시 2에서 입력은 부호 있는 정수 -3을 나타내고, 출력은 부호 있는 정수 -1073741825를 나타냅니다.',
             '입력은 길이가 32인 이진 문자열이어야 합니다.

         추가 질문: 이 함수를 여러 번 호출해야 한다면, 어떻게 최적화할 수 있을까요?

         입력(Input): 첫 번째 줄에 32비트 부호 없는 정수 n이 주어집니다.
         출력(Output): n의 비트를 반전시킨 32비트 부호 없는 정수를 출력합니다.',
             '[{\"input\": \"0\", \"output\": \"0\"}, {\"input\": \"11111111111111111111111111111111\", \"output\": \"3817748707\"}, {\"input\": \"10000000000000000000000000000000\", \"output\": \"1\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Number Of 1 Bits */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             191,
             'Number Of 1 Bits',
             'EASY',
             2,
             4096000,
             '양의 정수 n이 주어졌을 때, 이진 표현에서 켜져 있는 비트의 개수(해밍 무게라고도 함)를 반환하는 함수를 작성하세요.',
             '1 ≤ n ≤ 2^31 - 1

         추가 질문: 이 함수가 여러 번 호출될 경우, 어떻게 최적화할 수 있을까요?

         입력(Input): 첫 번째 줄에 양의 정수 n이 주어집니다.
         출력(Output): n의 이진 표현에서 켜져 있는 비트의 개수를 출력합니다.',
             '[{\"input\": \"0\", \"output\": \"0\"}, {\"input\": \"4095\", \"output\": \"12\"}, {\"input\": \"11\", \"output\": \"3\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: House Robber */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             198,
             'House Robber',
             'MEDIUM',
             2,
             4096000,
             '당신은 한 거리를 따라 있는 집들을 털 계획을 세운 전문 강도입니다. 각 집에는 일정 금액의 돈이 숨겨져 있는데, 당신이 모든 집을 털지 못하게 하는 유일한 제약은 인접한 집들이 보안 시스템으로 연결되어 있어, 같은 날 밤에 두 개의 인접한 집을 털 경우 자동으로 경찰에 신고된다는 것입니다.

         각 집의 돈의 액수를 나타내는 정수 배열 nums가 주어질 때, 경찰에 알리지 않고 오늘 밤 털 수 있는 최대 금액을 반환하십시오.',
             'nums의 길이는 1 이상 100 이하이다.
         nums[i]는 0 이상 400 이하이다.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.
         출력(Output): 경찰에 알리지 않고 오늘 밤 털 수 있는 최대 금액을 출력합니다.',
             '[{\"input\": \"1\\n1\", \"output\": \"1\"}, {\"input\": \"4\\n1 2 3 1\", \"output\": \"4\"}, {\"input\": \"4\\n2 1 1 2\", \"output\": \"4\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Happy Number */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             202,
             'Happy Number',
             'EASY',
             2,
             4096000,
             '어떤 숫자 n이 해피 넘버인지 판별하는 알고리즘을 작성하세요.

         해피 넘버는 다음 과정을 통해 정의됩니다.

         임의의 양의 정수에서 시작하여, 숫자를 각 자릿수의 제곱의 합으로 바꿉니다.
         이 과정을 숫자가 1이 될 때까지 반복합니다 (1이 되면 그대로 유지됩니다). 또는 1을 포함하지 않는 사이클에서 무한히 반복됩니다.
         이 과정을 통해 1로 끝나는 숫자를 해피 넘버라고 합니다.

         n이 해피 넘버이면 true를 반환하고, 그렇지 않으면 false를 반환하세요.',
             '1 ≤ n ≤ 2³¹ - 1

             입력(Input): 첫 번째 줄에 양의 정수 n이 주어집니다.
             출력(Output): n이 해피 넘버이면 True, 그렇지 않으면 False를 반환합니다.',
             '[{\"input\": \"100\", \"output\": \"True\"}, {\"input\": \"4\", \"output\": \"False\"}, {\"input\": \"20\", \"output\": \"False\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Count Primes */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             204,
             'Count Primes',
             'MEDIUM',
             2,
             4096000,
             '정수 n이 주어졌을 때, n보다 작은 소수의 개수를 반환하세요.',
             '0 ≤ n ≤ 5 * 10⁶

             입력(Input): 첫 번째 줄에 정수 n이 주어집니다.
             출력(Output): n보다 작은 소수의 개수를 출력합니다.',
             '[{\"input\": \"0\", \"output\": \"0\"}, {\"input\": \"5000000\", \"output\": \"348513\"}, {\"input\": \"1000\", \"output\": \"168\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: House Robber Ii */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             213,
             'House Robber Ii',
             'MEDIUM',
             2,
             4096000,
             '당신은 전문 강도로, 길을 따라 늘어선 집들을 털 계획을 세우고 있습니다. 각 집에는 일정 금액의 돈이 숨겨져 있습니다. 이 동네의 모든 집들은 원형으로 배치되어 있습니다. 즉, 첫 번째 집은 마지막 집의 이웃입니다. 한편, 인접한 집들은 보안 시스템으로 연결되어 있어, 같은 날 밤에 두 인접한 집이 털리면 자동으로 경찰에 연락합니다.

         각 집의 돈을 나타내는 정수 배열 nums가 주어졌을 때, 경찰에 알리지 않고 오늘 밤 털 수 있는 최대 금액을 반환하세요.',
             'nums 배열의 길이는 1 이상 100 이하입니다.
         nums 배열의 각 요소 값은 0 이상 1000 이하입니다.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.
         출력(Output): 경찰에 알리지 않고 오늘 밤 털 수 있는 최대 금액을 출력합니다.',
             '[{\"input\": \"4\\n1 2 3 1\", \"output\": \"4\"}, {\"input\": \"5\\n1 0 1 0 1\", \"output\": \"2\"}, {\"input\": \"6\\n5 1 2 4 7 8\", \"output\": \"14\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Contains Duplicate */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             217,
             'Contains Duplicate',
             'EASY',
             2,
             4096000,
             '정수 배열 nums가 주어지면, 배열 내에 중복된 값이 있는지 확인하여 있으면 True를, 모든 요소가 고유하면 False를 반환하세요.',
             'nums의 길이는 1 이상 10^5 이하입니다.
         -10^9 이상 nums[i]는 10^9 이하입니다.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.
         출력(Output): 배열 내에 중복된 값이 있으면 True, 그렇지 않으면 False를 반환합니다.',
             '[{\"input\": \"1\\n1\", \"output\": \"False\"}, {\"input\": \"4\\n1 2 3 1\", \"output\": \"True\"}, {\"input\": \"10\\n1 1 1 3 3 4 3 2 4 2\", \"output\": \"True\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: The Skyline Problem */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             218,
             'The Skyline Problem',
             'HARD',
             2,
             4096000,
             '도시의 스카이라인은 도시의 모든 건물들을 멀리서 봤을 때 형성되는 외곽선입니다. 모든 건물들의 위치와 높이가 주어졌을 때, 이 건물들이 함께 만들어내는 스카이라인을 반환하세요.

         각 건물의 기하학적 정보는 buildings 배열에 buildings[i] = [lefti, righti, heighti] 형태로 주어집니다.

         *   lefti는 i번째 건물의 왼쪽 가장자리의 x 좌표입니다.
         *   righti는 i번째 건물의 오른쪽 가장자리의 x 좌표입니다.
         *   heighti는 i번째 건물의 높이입니다.

         모든 건물은 높이 0의 절대 평평한 표면에 위치한 완벽한 직사각형이라고 가정합니다.

         스카이라인은 [[x1, y1], [x2, y2], ...] 형식으로 x 좌표를 기준으로 정렬된 \"키 포인트\" 목록으로 표현되어야 합니다. 각 키 포인트는 스카이라인에서 어떤 수평 선분의 왼쪽 끝점입니다. 마지막 점을 제외하고는 항상 y 좌표가 0이며, 이는 가장 오른쪽에 있는 건물이 끝나는 스카이라인의 종료 지점을 표시하는 데 사용됩니다. 가장 왼쪽 건물과 가장 오른쪽 건물 사이의 모든 땅은 스카이라인의 윤곽의 일부여야 합니다.

         참고: 출력 스카이라인에는 동일한 높이의 연속적인 수평선이 없어야 합니다. 예를 들어, [..., [2, 3], [4, 5], [7, 5], [11, 5], [12, 7], ...]은 허용되지 않습니다. 높이 5의 세 선은 최종 출력에서 다음과 같이 하나로 병합되어야 합니다: [..., [2, 3], [4, 5], [12, 7], ...]',
             '건물 배열의 길이는 1 이상 10,000 이하입니다.
         각 건물의 왼쪽 좌표는 0 이상 2^31 - 1 이하이며, 오른쪽 좌표는 0 이상 2^31 - 1 이하입니다.
         각 건물의 높이는 1 이상 2^31 - 1 이하입니다.
         buildings 배열은 왼쪽 좌표(lefti)를 기준으로 오름차순 정렬되어 있습니다.

         입력(Input): 첫 번째 줄에 건물의 개수 n, 두 번째 줄에 buildings 배열이 주어집니다.
         출력(Output): 첫 번째 줄에 스카이라인의 키 포인트 개수를 출력하고, 두 번째 줄부터 각 키 포인트를 [x, y] 형식으로 출력합니다.',
             '[{\"input\": \"7\\n0 5 7\\n5 10 3\\n5 10 12\\n10 15 15\\n15 20 10\\n15 20 10\\n20 25 10\", \"output\": \"5\\n0 7\\n5 12\\n10 15\\n15 10\\n25 0\"}, {\"input\": \"3\\n1 2 1\\n1 2 2\\n1 2 3\", \"output\": \"2\\n1 3\\n2 0\"}, {\"input\": \"3\\n0 3 3\\n1 5 3\\n2 4 3\", \"output\": \"2\\n0 3\\n5 0\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Basic Calculator */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             224,
             'Basic Calculator',
             'HARD',
             2,
             4096000,
             '유효한 수식을 나타내는 문자열 s가 주어졌을 때, 기본 계산기를 구현하여 수식을 계산하고 결과를 반환하세요.
         참고: eval()과 같이 문자열을 수학적 표현식으로 평가하는 내장 함수는 사용할 수 없습니다.',
             '1 <= s의 길이 <= 3 * 105
         s는 숫자, \'+\', \'-\', \'(\', \')\', \' \'로 구성됩니다.
s는 유효한 수식을 나타냅니다.
\'+\'는 단항 연산자로 사용되지 않습니다 (예: \"+1\" 및 \"+(2 + 3)\"은 유효하지 않습니다).
\'-\'는 단항 연산자로 사용될 수 있습니다 (예: \"-1\" 및 \"-(2 + 3)\"은 유효합니다).
입력에는 두 개의 연산자가 연속으로 나타나지 않습니다.
모든 숫자와 실행 중인 계산은 부호 있는 32비트 정수에 적합합니다.

입력(Input): 첫 번째 줄에 문자열 s가 주어집니다.
출력(Output): s의 계산 결과를 정수로 반환합니다.',
             '[{\"input\": \"30 - (5 + (10 - 15) + 20)\", \"output\": \"10\"}, {\"input\": \"2147483647\", \"output\": \"2147483647\"}, {\"input\": \"-2147483647\", \"output\": \"-2147483647\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Basic Calculator Ii */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             227,
             'Basic Calculator Ii',
             'MEDIUM',
             2,
             4096000,
             '문자열 s가 주어지면, 이 표현식을 계산하여 그 값을 반환하세요. 정수 나눗셈은 0 방향으로 잘라냅니다. 주어진 표현식은 항상 유효하다고 가정합니다. 모든 중간 결과는 [-231, 231 - 1] 범위 내에 있습니다. 참고: eval()과 같이 문자열을 수학적 표현식으로 평가하는 내장 함수는 사용할 수 없습니다.',
             's의 길이는 1 이상 3 * 10^5 이하입니다.
         s는 공백으로 구분된 정수와 연산자 (\'+\', \'-\', \'*\', \'/\')로 구성됩니다.
s는 유효한 수식을 나타냅니다.
수식의 모든 정수는 0 이상 2^31 - 1 이하의 범위에 있는 비음의 정수입니다.
답은 32비트 정수 안에 들어갈 것으로 보장됩니다.

입력(Input): 첫 번째 줄에 문자열 s가 주어집니다.
출력(Output): s의 계산 결과를 정수로 반환합니다.',
             '[{\"input\": \"2 - 3 + 4\", \"output\": \"3\"}, {\"input\": \"1000000000 - 500000000 + 250000000\", \"output\": \"750000000\"}, {\"input\": \"30 + 2 * 6 / (3 - 1)\", \"output\": \"33\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Summary Ranges */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             228,
             'Summary Ranges',
             'EASY',
             2,
             4096000,
             '정렬된 고유 정수 배열 nums가 주어집니다.
         범위 [a, b]는 a부터 b까지의 모든 정수의 집합(a와 b 포함)입니다.
         배열의 모든 숫자를 정확히 포함하는 가장 작은 정렬된 범위 목록을 반환하십시오. 즉, nums의 각 요소는 정확히 하나의 범위에 포함되며, x가 범위 중 하나에 있지만 nums에 없는 정수 x는 없습니다.
         목록의 각 범위 [a, b]는 다음과 같이 출력되어야 합니다.

         a != b인 경우 \"a->b\"
         a == b인 경우 \"a\"',
             'nums의 길이는 0 이상 20 이하입니다.
         nums[i]의 값은 -2^31 이상 2^31 - 1 이하입니다.
         nums의 모든 값은 고유합니다.
         nums는 오름차순으로 정렬되어 있습니다.

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.
         출력(Output): 첫 번째 줄에 범위의 개수를 출력하고, 두 번째에 각 범위를 \"a->b\" 또는 \"a\" 형식으로 출력합니다.',
             '[{\"input\": \"6\\n0 1 2 3 4 5\", \"output\": \"1\\n0->5\"}, {\"input\": \"10\\n-1 0 1 2 3 5 6 7 8 10\", \"output\": \"3\\n-1->3 5->8 10\"}, {\"input\": \"3\\n-2147483648 -2147483647 -2147483646\", \"output\": \"1\\n-2147483648->-2147483646\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Majority Element Ii */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             229,
             'Majority Element Ii',
             'MEDIUM',
             2,
             4096000,
             '크기가 n인 정수 배열이 주어졌을 때, ⌊ n/3 ⌋번 이상 나타나는 모든 원소를 찾아라.',
             '1 <= nums 배열의 길이 <= 5 * 10^4
         -10^9 <= nums[i] <= 10^9

         추가 질문: 선형 시간 복잡도와 O(1) 공간 복잡도로 문제를 해결할 수 있습니까?

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.
         출력(Output): 첫 번째 줄에 ⌊ n/3 ⌋번 이상 나타나는 원소의 수를 나타내고, 두 번째 줄에 ⌊ n/3 ⌋번 이상 나타나는 모든 원소를 오름차순으로 출력합니다.',
             '[{\"input\": \"3\\n3 2 3\", \"output\": \"1\\n3\"}, {\"input\": \"1\\n1\", \"output\": \"1\\n1\"}, {\"input\": \"2\\n1 2\", \"output\": \"2\\n2 1\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Power Of Two */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             231,
             'Power Of Two',
             'EASY',
             2,
             4096000,
             '정수 n이 주어졌을 때, 2의 거듭제곱수이면 True을 반환하고, 그렇지 않으면 False를 반환하세요. 정수 n이 2의 거듭제곱수라는 것은 n == 2^x를 만족하는 정수 x가 존재한다는 의미입니다.',
             '-2^31 이상 2^31 - 1 이하

         추가 질문: 반복문이나 재귀 없이 해결할 수 있나요?

         입력(Input): 첫 번째 줄에 정수 n이 주어집니다.
         출력(Output): n이 2의 거듭제곱수이면 True, 그렇지 않으면 False를 반환합니다.',
             '[{\"input\": \"3\", \"output\": \"False\"}, {\"input\": \"2097152\", \"output\": \"True\"}, {\"input\": \"-16\", \"output\": \"False\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Number Of Digit One */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             233,
             'Number Of Digit One',
             'HARD',
             2,
             4096000,
             '정수 n이 주어졌을 때, 0부터 n까지의 모든 정수에서 숫자 1이 나타나는 총 횟수를 세세요.',
             '0 이상 n 이하

             입력(Input): 첫 번째 줄에 정수 n이 주어집니다.
             출력(Output): 0부터 n까지의 모든 정수에서 숫자 1이 나타나는 총 횟수를 출력합니다.',
             '[{\"input\": \"0\", \"output\": \"0\"}, {\"input\": \"987654321\", \"output\": \"891632373\"}, {\"input\": \"100000\", \"output\": \"50001\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Product Of Array Except Self */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             238,
             'Product Of Array Except Self',
             'MEDIUM',
             2,
             4096000,
             '정수 배열 nums가 주어졌을 때, answer[i]가 nums[i]를 제외한 나머지 모든 nums 요소의 곱과 같도록 배열 answer를 반환하세요.

         nums의 모든 접두사 또는 접미사의 곱은 32비트 정수에 적합하다고 보장됩니다.

         나눗셈 연산을 사용하지 않고 O(n) 시간에 실행되는 알고리즘을 작성해야 합니다.',
             'nums 배열의 길이는 2 이상 10^5 이하입니다.
         nums[i]는 -30 이상 30 이하입니다.
         정답 배열 answer[i]는 32비트 정수 안에 들어갈 수 있도록 입력이 생성됩니다.

         추가 질문: O(1)의 추가 공간 복잡도로 문제를 해결할 수 있습니까? (출력 배열은 공간 복잡도 분석에서 추가 공간으로 간주하지 않습니다.)

         입력(Input): 첫 번째 줄에 배열의 길이 n, 두 번째 줄에 nums 배열이 주어집니다.
         출력(Output): 첫 번째 줄에 answer 배열의 길이를 출력하고, 두 번째 줄에 answer 배열을 출력합니다.',
             '[{\"input\": \"5\\n5 3 0 2 1\", \"output\": \"5\\n0 0 30 0 0\"}, {\"input\": \"5\\n1 0 -1 2 -2\", \"output\": \"5\\n0 4 0 0 0\"}, {\"input\": \"6\\n1 -1 2 -2 3 -3\", \"output\": \"6\\n-36 36 -18 18 -12 12\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Strobogrammatic Number */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             246,
             'Strobogrammatic Number',
             'EASY',
             2,
             4096000,
             '정수를 나타내는 문자열 num이 주어졌을 때, num이 스토보그램마틱 숫자이면 True, 아닐 경우 False를 반환하세요. 스토보그램마틱 숫자는 180도 회전했을 때 (거꾸로 보았을 때) 똑같이 보이는 숫자입니다.',
             'num의 길이는 1 이상 50 이하입니다.
         num은 숫자만으로 구성되어 있습니다.
         num은 0을 제외하고는 선행 제로를 포함하지 않습니다.

         입력(Input): 첫 번째 줄에 문자열 num이 주어집니다.
         출력(Output): num이 스토보그램마틱 숫자이면 True, 그렇지 않으면 False를 반환합니다.',
             '[{\"input\": \"9\", \"output\": \"False\"}, {\"input\": \"69\", \"output\": \"True\"}, {\"input\": \"808\", \"output\": \"True\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Strobogrammatic Number Ii */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             247,
             'Strobogrammatic Number Ii',
             'MEDIUM',
             2,
             4096000,
             '정수 n이 주어지면, 길이가 n인 모든 스트로보그램 숫자를 반환하세요. 반환 순서는 상관없습니다. 스트로보그램 숫자는 180도 회전했을 때 (거꾸로 보았을 때) 똑같이 보이는 숫자입니다.',
             '1 이상 14 이하

             입력(Input): 첫 번째 줄에 정수 n이 주어집니다.
             출력(Output): 첫 번째 줄에 스트로보그램 숫자의 개수를 출력하고, 두 번째 줄부터 각 스트로보그램 숫자를 출력합니다.',
             '[{\"input\": \"3\", \"output\": \"12\\n101 808 609 906 111 818 619 916 181 888 689 986\"}, {\"input\": \"2\", \"output\": \"4\\n11 88 69 96\"}, {\"input\": \"1\", \"output\": \"3\\n0 1 8\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Meeting Rooms */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             252,
             'Meeting Rooms',
             'EASY',
             2,
             4096000,
             '회의 시간 간격 배열 intervals[i] = [starti, endi]가 주어졌을 때, 한 사람이 모든 회의에 참석할 수 있는지 여부를 결정하세요.',
             '0 <= intervals 배열의 길이 <= 10^4
         intervals[i]의 길이 == 2
         0 <= start_i < end_i <= 10^6',
             '[{\"input\": \"intervals = [[1,5],[2,3]]\", \"output\": \"False\"}, {\"input\": \"intervals = [[1,2]]\", \"output\": \"True\"}, {\"input\": \"intervals = [[1,2],[2,3]]\", \"output\": \"True\"}]',
             NOW(),
             NOW()
         );


/** 알고리즘 문제: Meeting Rooms Ii */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
             253,
             'Meeting Rooms Ii',
             'MEDIUM',
             2,
             4096000,
             '회의 시간 간격 배열 intervals가 주어졌을 때, intervals[i] = [starti, endi]이며, 필요한 최소 회의실 수를 반환하세요.',
             '1 <= intervals 배열의 길이 <= 104
         0 <= starti < endi <= 106',
             '[{\"input\": \"4\\n1 2\\n2 3\\n3 4\\n4 5\", \"output\": \"1\"}, {\"input\": \"4\\n1 3\\n2 6\\n8 10\\n15 18\", \"output\": \"2\"}, {\"input\": \"4\\n1 13\\n15 24\\n8 18\\n3 19\", \"output\": \"3\"}]',
             NOW(),
             NOW()
         );


/** 문제 ID 1 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (1, 5, NOW(), NOW()),
       (1, 36, NOW(), NOW());

/** 문제 ID 3 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (3, 36, NOW(), NOW()),
       (3, 17, NOW(), NOW()),
       (3, 19, NOW(), NOW());

/** 문제 ID 7 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (7, 18, NOW(), NOW());

/** 문제 ID 8 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (8, 19, NOW(), NOW());

/** 문제 ID 9 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (9, 18, NOW(), NOW());

/** 문제 ID 11 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (11, 5, NOW(), NOW()),
       (11, 13, NOW(), NOW()),
       (11, 14, NOW(), NOW());

/** 문제 ID 13 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (13, 36, NOW(), NOW()),
       (13, 18, NOW(), NOW()),
       (13, 19, NOW(), NOW());

/** 문제 ID 15 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (15, 5, NOW(), NOW()),
       (15, 12, NOW(), NOW()),
       (15, 14, NOW(), NOW());

/** 문제 ID 17 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (17, 9, NOW(), NOW()),
       (17, 36, NOW(), NOW()),
       (17, 19, NOW(), NOW());

/** 문제 ID 20 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (20, 6, NOW(), NOW()),
       (20, 19, NOW(), NOW());

/** 문제 ID 22 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (22, 9, NOW(), NOW()),
       (22, 4, NOW(), NOW()),
       (22, 19, NOW(), NOW());

/** 문제 ID 26 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (26, 5, NOW(), NOW()),
       (26, 14, NOW(), NOW());

/** 문제 ID 32 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (32, 4, NOW(), NOW()),
       (32, 6, NOW(), NOW()),
       (32, 19, NOW(), NOW());

/** 문제 ID 36 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (36, 5, NOW(), NOW()),
       (36, 36, NOW(), NOW()),
       (36, 41, NOW(), NOW());

/** 문제 ID 38 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (38, 19, NOW(), NOW());

/** 문제 ID 41 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (41, 5, NOW(), NOW()),
       (41, 36, NOW(), NOW());

/** 문제 ID 42 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (42, 5, NOW(), NOW()),
       (42, 4, NOW(), NOW()),
       (42, 45, NOW(), NOW()),
       (42, 6, NOW(), NOW()),
       (42, 14, NOW(), NOW());

/** 문제 ID 45 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (45, 5, NOW(), NOW()),
       (45, 4, NOW(), NOW()),
       (45, 13, NOW(), NOW());

/** 문제 ID 46 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (46, 5, NOW(), NOW()),
       (46, 9, NOW(), NOW());

/** 문제 ID 47 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (47, 5, NOW(), NOW()),
       (47, 9, NOW(), NOW()),
       (47, 12, NOW(), NOW());

/** 문제 ID 49 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (49, 5, NOW(), NOW()),
       (49, 36, NOW(), NOW()),
       (49, 12, NOW(), NOW()),
       (49, 19, NOW(), NOW());

/** 문제 ID 51 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (51, 5, NOW(), NOW()),
       (51, 9, NOW(), NOW());

/** 문제 ID 52 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (52, 9, NOW(), NOW());

/** 문제 ID 53 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (53, 5, NOW(), NOW()),
       (53, 30, NOW(), NOW()),
       (53, 4, NOW(), NOW());

/** 문제 ID 55 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (55, 5, NOW(), NOW()),
       (55, 4, NOW(), NOW()),
       (55, 13, NOW(), NOW());

/** 문제 ID 56 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (56, 5, NOW(), NOW()),
       (56, 12, NOW(), NOW());

/** 문제 ID 58 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (58, 19, NOW(), NOW());

/** 문제 ID 59 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (59, 5, NOW(), NOW()),
       (59, 41, NOW(), NOW()),
       (59, 53, NOW(), NOW());

/** 문제 ID 65 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (65, 19, NOW(), NOW());

/** 문제 ID 66 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (66, 5, NOW(), NOW()),
       (66, 18, NOW(), NOW());

/** 문제 ID 69 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (69, 11, NOW(), NOW()),
       (69, 18, NOW(), NOW());

/** 문제 ID 70 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (70, 4, NOW(), NOW()),
       (70, 18, NOW(), NOW()),
       (70, 42, NOW(), NOW());

/** 문제 ID 80 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (80, 5, NOW(), NOW()),
       (80, 14, NOW(), NOW());

/** 문제 ID 84 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (84, 5, NOW(), NOW()),
       (84, 45, NOW(), NOW()),
       (84, 6, NOW(), NOW());

/** 문제 ID 89 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (89, 9, NOW(), NOW()),
       (89, 15, NOW(), NOW()),
       (89, 18, NOW(), NOW());

/** 문제 ID 91 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (91, 4, NOW(), NOW()),
       (91, 19, NOW(), NOW());

/** 문제 ID 93 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (93, 9, NOW(), NOW()),
       (93, 19, NOW(), NOW());

/** 문제 ID 96 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (96, 22, NOW(), NOW()),
       (96, 23, NOW(), NOW()),
       (96, 4, NOW(), NOW()),
       (96, 18, NOW(), NOW()),
       (96, 20, NOW(), NOW());

/** 문제 ID 118 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (118, 5, NOW(), NOW()),
       (118, 4, NOW(), NOW());

/** 문제 ID 119 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (119, 5, NOW(), NOW()),
       (119, 4, NOW(), NOW());

/** 문제 ID 120 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (120, 5, NOW(), NOW()),
       (120, 4, NOW(), NOW());

/** 문제 ID 121 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (121, 5, NOW(), NOW()),
       (121, 4, NOW(), NOW());

/** 문제 ID 122 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (122, 5, NOW(), NOW()),
       (122, 4, NOW(), NOW()),
       (122, 13, NOW(), NOW());

/** 문제 ID 123 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (123, 5, NOW(), NOW()),
       (123, 4, NOW(), NOW());

/** 문제 ID 125 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (125, 19, NOW(), NOW()),
       (125, 14, NOW(), NOW());

/** 문제 ID 128 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (128, 5, NOW(), NOW()),
       (128, 36, NOW(), NOW()),
       (128, 8, NOW(), NOW());

/** 문제 ID 131 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (131, 9, NOW(), NOW()),
       (131, 4, NOW(), NOW()),
       (131, 19, NOW(), NOW());

/** 문제 ID 132 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (132, 4, NOW(), NOW()),
       (132, 19, NOW(), NOW());

/** 문제 ID 135 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (135, 5, NOW(), NOW()),
       (135, 13, NOW(), NOW());

/** 문제 ID 136 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (136, 5, NOW(), NOW()),
       (136, 15, NOW(), NOW());

/** 문제 ID 137 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (137, 5, NOW(), NOW()),
       (137, 15, NOW(), NOW());

/** 문제 ID 149 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (149, 5, NOW(), NOW()),
       (149, 34, NOW(), NOW()),
       (149, 36, NOW(), NOW()),
       (149, 18, NOW(), NOW());

/** 문제 ID 150 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (150, 5, NOW(), NOW()),
       (150, 18, NOW(), NOW()),
       (150, 6, NOW(), NOW());

/** 문제 ID 151 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (151, 19, NOW(), NOW()),
       (151, 14, NOW(), NOW());

/** 문제 ID 152 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (152, 5, NOW(), NOW()),
       (152, 4, NOW(), NOW());

/** 문제 ID 153 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (153, 5, NOW(), NOW()),
       (153, 11, NOW(), NOW());

/** 문제 ID 154 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (154, 5, NOW(), NOW()),
       (154, 11, NOW(), NOW());

/** 문제 ID 159 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (159, 36, NOW(), NOW()),
       (159, 17, NOW(), NOW()),
       (159, 19, NOW(), NOW());

/** 문제 ID 162 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (162, 5, NOW(), NOW()),
       (162, 11, NOW(), NOW());

/** 문제 ID 164 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (164, 5, NOW(), NOW()),
       (164, 26, NOW(), NOW()),
       (164, 50, NOW(), NOW()),
       (164, 12, NOW(), NOW());

/** 문제 ID 169 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (169, 5, NOW(), NOW()),
       (169, 28, NOW(), NOW()),
       (169, 30, NOW(), NOW()),
       (169, 36, NOW(), NOW()),
       (169, 12, NOW(), NOW());

/** 문제 ID 171 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (171, 18, NOW(), NOW()),
       (171, 19, NOW(), NOW());

/** 문제 ID 172 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (172, 18, NOW(), NOW());

/** 문제 ID 179 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (179, 5, NOW(), NOW()),
       (179, 13, NOW(), NOW()),
       (179, 12, NOW(), NOW()),
       (179, 19, NOW(), NOW());

/** 문제 ID 187 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (187, 15, NOW(), NOW()),
       (187, 35, NOW(), NOW()),
       (187, 36, NOW(), NOW()),
       (187, 51, NOW(), NOW()),
       (187, 17, NOW(), NOW()),
       (187, 19, NOW(), NOW());

/** 문제 ID 190 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (190, 15, NOW(), NOW()),
       (190, 30, NOW(), NOW());

/** 문제 ID 191 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (191, 15, NOW(), NOW()),
       (191, 30, NOW(), NOW());

/** 문제 ID 198 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (198, 5, NOW(), NOW()),
       (198, 4, NOW(), NOW());

/** 문제 ID 202 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (202, 36, NOW(), NOW()),
       (202, 18, NOW(), NOW()),
       (202, 14, NOW(), NOW());

/** 문제 ID 204 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (204, 5, NOW(), NOW()),
       (204, 31, NOW(), NOW()),
       (204, 18, NOW(), NOW()),
       (204, 46, NOW(), NOW());

/** 문제 ID 213 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (213, 5, NOW(), NOW()),
       (213, 4, NOW(), NOW());

/** 문제 ID 217 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (217, 5, NOW(), NOW()),
       (217, 36, NOW(), NOW()),
       (217, 12, NOW(), NOW());

/** 문제 ID 218 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (218, 5, NOW(), NOW()),
       (218, 21, NOW(), NOW()),
       (218, 30, NOW(), NOW()),
       (218, 37, NOW(), NOW()),
       (218, 39, NOW(), NOW()),
       (218, 47, NOW(), NOW()),
       (218, 52, NOW(), NOW());

/** 문제 ID 224 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (224, 18, NOW(), NOW()),
       (224, 10, NOW(), NOW()),
       (224, 6, NOW(), NOW()),
       (224, 19, NOW(), NOW());

/** 문제 ID 227 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (227, 18, NOW(), NOW()),
       (227, 6, NOW(), NOW()),
       (227, 19, NOW(), NOW());

/** 문제 ID 228 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (228, 5, NOW(), NOW());

/** 문제 ID 229 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (229, 5, NOW(), NOW()),
       (229, 28, NOW(), NOW()),
       (229, 36, NOW(), NOW()),
       (229, 12, NOW(), NOW());

/** 문제 ID 231 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (231, 15, NOW(), NOW()),
       (231, 18, NOW(), NOW()),
       (231, 10, NOW(), NOW());

/** 문제 ID 233 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (233, 4, NOW(), NOW()),
       (233, 18, NOW(), NOW()),
       (233, 10, NOW(), NOW());

/** 문제 ID 238 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (238, 5, NOW(), NOW()),
       (238, 48, NOW(), NOW());

/** 문제 ID 246 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (246, 36, NOW(), NOW()),
       (246, 19, NOW(), NOW()),
       (246, 14, NOW(), NOW());

/** 문제 ID 247 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (247, 5, NOW(), NOW()),
       (247, 10, NOW(), NOW()),
       (247, 19, NOW(), NOW());

/** 아이템 정보 */
INSERT INTO `item` (item_cost, item_duration, item_id, item_name, item_description, item_image_url)
VALUES (500, 10, 1, '해킹', '상대방의 물리 키보드를 해킹하여 가상 키보드 코딩을 강제합니다.', 'https://picsum.photos/200'),
       (500, 10, 2, '월식', '상대방 화면에 암전 효과를 줍니다.', 'https://picsum.photos/200'),
       (500, 10, 3, '탈진', '상대방의 타이핑 입력속도를 지연시킵니다.', 'https://picsum.photos/200'),
       (500, 5, 4, '지진', '상대방 화면에 지진을 일으킵니다.', 'https://picsum.photos/200'),
       (500, 10, 5, '점화', '상대방 에디터에 불을 질러 코드를 태웁니다.', 'https://picsum.photos/200');

/** 스펠 정보 */
INSERT INTO `spell` (spell_cost, spell_duration, spell_id, spell_name, spell_description, spell_image_url)
VALUES (500, 300, 1, '보호막', '5분 간 상대방의 아이템 효과를 1회 막아주는 보호막을 생성합니다.', 'https://picsum.photos/200'),
       (800, 60, 2, '정화', '현재 나에게 적용되어 있는 모든 아이템 효과를 제거합니다.', 'https://picsum.photos/200'),
       (500, 60, 3, '감시자', '상대방의 실시간 화면을 일정시간 감시할 수 있습니다.', 'https://picsum.photos/200');