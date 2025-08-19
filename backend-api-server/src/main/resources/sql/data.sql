/** 회원 */
INSERT INTO member (member_id, email, nickname, provider, profile_image_url, programming_language,
                    coin, score, is_deleted, created_at, updated_at)
VALUES (1, 'alice@example.com', 'Alice', 'KAKAO', 'https://picsum.photos/id/1011/200', 'JAVA', 1000, 1000, false, NOW(),
        NOW()),
       (2, 'bob@example.com', 'Bob', 'KAKAO', 'https://picsum.photos/id/1012/200', 'CPP', 1500, 1000, false, NOW(),
        NOW()),
       (3, 'charlie@example.com', 'Charlie', 'KAKAO', 'https://picsum.photos/id/1013/200', 'PYTHON', 2000, 1000, false,
        NOW(), NOW()),
       (4, 'david@example.com', 'David', 'KAKAO', 'https://picsum.photos/id/1014/200', 'JAVA', 500, 1000, false, NOW(),
        NOW()),
       (5, 'eve@example.com', 'Eve', 'KAKAO', 'https://picsum.photos/id/1015/200', 'CPP', 1200, 1000, false, NOW(),
        NOW()),
       (6, 'frank@example.com', 'Frank', 'GOOGLE', 'https://picsum.photos/id/1016/200', 'PYTHON', 700, 1000, false, NOW(),
        NOW()),
       (7, 'grace@example.com', 'Grace', 'GOOGLE', 'https://picsum.photos/id/1017/200', 'JAVA', 900, 1000, false, NOW(),
        NOW()),
       (8, 'henry@example.com', 'Henry', 'GOOGLE', 'https://picsum.photos/id/1018/200', 'CPP', 1300, 1000, false, NOW(),
        NOW()),
       (9, 'irene@example.com', 'Irene', 'GOOGLE', 'https://picsum.photos/id/1019/200', 'PYTHON', 1100, 1000, false,
        NOW(), NOW()),
       (10, 'jack@example.com', 'Jack', 'GOOGLE', 'https://picsum.photos/id/1020/200', 'JAVA', 1000, 1000, true, NOW(),
        NOW()),
       (11, 'skwpqjq@gmail.com', 'jebeop', 'KAKAO', 'https://picsum.photos/id/1011/200', 'JAVA', 1000, 1000, false,
        NOW(), NOW()),
       (21, 'shenlianxiu@example.com', 'Yeonsu', 'KAKAO', 'https://picsum.photos/id/1011/200', 'JAVA', 1000, 1000, false,
        NOW(), NOW()),
       (13, 'kihwan@example.com', 'Kiwhan', 'KAKAO', 'https://picsum.photos/id/1011/200', 'JAVA', 1000, 1000, false, NOW(),
        NOW()),
       (14, 'rudrn0110@naver.com', 'gyeong', 'KAKAO', 'https://picsum.photos/id/1011/200', 'JAVA', 1000, 1000, false,
        NOW(), NOW()),
       (15, 'dbswjddms517@naver.com', 'jjjj', 'KAKAO', 'https://picsum.photos/id/1011/200', 'JAVA', 1000, 1000, false,
        NOW(), NOW()),
       (16, 'realKiwhan@naver.com', 'real Kiwhan', 'KAKAO', 'https://picsum.photos/id/1011/200', 'JAVA', 1000, 1000, false,
        NOW(), NOW());
        -- !!!!!! memberId=17 쓰지 마세요. 기환오빠 껍니다 !!!!!

/** 알고리즘 카테고리 */
INSERT INTO problem_category (problem_category_id, problem_category_name, banned_count, picked_count, created_at, updated_at)
VALUES
    (1, 'Breadth-First Search', 99, 121, '2025-08-10 05:09:37', '2025-08-10 05:09:37'),
    (2, 'Depth-First Search', 176, 33, '2025-08-10 05:09:37', '2025-08-10 05:09:37'),
    (3, 'Graph', 233, 10, '2025-08-10 05:09:37', '2025-08-10 05:09:37'),
    (4, 'Dynamic Programming', 144, 16, '2025-08-10 05:09:37', '2025-08-10 05:09:37'),
    (5, 'Array', 212, 22, '2025-08-10 05:09:37', '2025-08-10 05:09:37'),
    (6, 'Stack', 123, 43, '2025-08-10 05:09:37', '2025-08-10 05:09:37'),
    (7, 'Queue', 122, 43, '2025-08-10 05:09:37', '2025-08-10 05:09:37'),
    (8, 'Union Find', 123, 55, '2025-08-10 05:09:37', '2025-08-10 05:09:37'),
    (9, 'Backtracking', 126, 55, '2025-08-10 05:09:37', '2025-08-10 05:09:37'),
    (10, 'Recursion', 123, 32, '2025-08-10 05:09:37', '2025-08-10 05:09:37'),
    (11, 'Binary Search', 133, 12, '2025-08-10 05:09:37', '2025-08-10 05:09:37'),
    (12, 'Sorting', 134, 67, '2025-08-10 05:09:37', '2025-08-10 05:09:37'),
    (13, 'Greedy', 121, 54, '2025-08-10 05:09:37', '2025-08-10 05:09:37'),
    (14, 'Two Pointers', 120, 67, '2025-08-10 05:09:37', '2025-08-10 05:09:37'),
    (15, 'Bit Manipulation', 111, 34, '2025-08-10 05:09:37', '2025-08-10 05:09:37'),
    (16, 'Hashing', 128, 44, '2025-08-10 05:09:37', '2025-08-10 05:09:37'),
    (17, 'Sliding Window', 132, 47, '2025-08-10 05:09:37', '2025-08-10 05:09:37'),
    (18, 'Math', 122, 49, '2025-08-10 05:09:37', '2025-08-10 05:09:37'),
    (19, 'String', 182, 57, '2025-08-10 05:09:37', '2025-08-10 05:09:37'),
    (20, 'Tree', 123, 69, '2025-08-10 05:09:37', '2025-08-10 05:09:37');


/** 알고리즘 문제 */
INSERT INTO problem (problem_id, problem_name, problem_level, time_limit_sec, memory_limit_kb, problem_description, problem_constraint, problem_example,
                     created_at, updated_at
) VALUES (3243, '도로를 추가하면서 최단 거리 구하기 I', 'MEDIUM',
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
             '정수 배열 nums와 정수 target이 주어지면, 두 숫자의 합이 target이 되는 두 숫자의 인덱스를 반환하세요.
         각 입력값은 정확히 하나의 솔루션을 가지며, 동일한 요소를 두 번 사용할 수 없다고 가정합니다.
         정답은 어떤 순서로든 반환할 수 있습니다.',
             'nums 배열의 길이는 2 이상 10,000 이하입니다.
         nums 배열의 각 요소는 -10^9 이상 10^9 이하입니다.
         target 값은 -10^9 이상 10^9 이하입니다.
         정답은 단 하나만 존재합니다.

         추가 질문: O(n^2)보다 낮은 시간 복잡도를 가진 알고리즘을 생각해낼 수 있습니까?',
             '[{\"input\": \"nums = [3,3], target = 6\", \"output\": \"[0, 1]\"}, {\"input\": \"nums = [-1,-2,-3,-4], target = -8\", \"output\": \"None\"}, {\"input\": \"nums = [1000000000, 1000000000], target = 2000000000\", \"output\": \"[0, 1]\"}]',
             NOW(),
             NOW()
         );

INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES (3243, 1, NOW(), NOW()),
       (3243, 3, NOW(), NOW()),
       (3243, 5, NOW(), NOW()),
       (1, 1, NOW(), NOW()),
       (1, 2, NOW(), NOW()),
       (1, 3, NOW(), NOW()),
       (1, 4, NOW(), NOW()),
       (1, 5, NOW(), NOW()),
       (1, 6, NOW(), NOW()),
       (1, 7, NOW(), NOW()),
       (1, 8, NOW(), NOW()),
       (1, 9, NOW(), NOW()),
       (1, 10, NOW(), NOW()),
       (1, 11, NOW(), NOW()),
       (1, 12, NOW(), NOW()),
       (1, 13, NOW(), NOW()),
       (1, 14, NOW(), NOW()),
       (1, 15, NOW(), NOW()),
       (1, 16, NOW(), NOW()),
       (1, 17, NOW(), NOW()),
       (1, 18, NOW(), NOW()),
       (1, 19, NOW(), NOW()),
       (1, 20, NOW(), NOW());

INSERT INTO game (game_id, game_name, game_type, programming_language, max_players, time_limit, created_at, updated_at)
VALUES
    (1, '알고리즘 배틀 A', 'RANKED', 'JAVA', 2, 3600, '2025-08-06 09:00:00', '2025-08-06 09:00:00'),
    (2, '알고리즘 배틀 B', 'NORMAL', 'PYTHON', 2, 3600, '2025-08-06 09:10:00', '2025-08-06 09:10:00'),
    (3, '알고리즘 배틀 C', 'RANKED', 'CPP', 4, 3000, '2025-08-06 09:20:00', '2025-08-06 09:20:00'),
    (4, '기환을 위한 테스트방', 'NORMAL', 'PYTHON', 2, 3600, '2025-08-06 09:10:00', '2025-08-06 09:10:00');

INSERT INTO player (player_id, game_id, member_id, created_at, updated_at)
VALUES
    (1, 1, 11, '2025-08-06 09:01:00', '2025-08-06 09:01:00'),
    (2, 1, 2, '2025-08-06 09:01:10', '2025-08-06 09:01:10'),
    (3, 2, 11, '2025-08-06 09:11:00', '2025-08-06 09:11:00'),
    (4, 2, 2, '2025-08-06 09:11:05', '2025-08-06 09:11:05'),
    (5, 3, 4, '2025-08-06 09:21:00', '2025-08-06 09:21:00'),
    (6, 3, 11, '2025-08-06 09:21:02', '2025-08-06 09:21:02'),
    (7, 3, 7, '2025-08-06 09:21:03', '2025-08-06 09:21:03'),
    (8, 3, 7, '2025-08-06 09:21:03', '2025-08-06 09:21:03'),
    (9, 4, 9, '2025-08-06 09:21:04', '2025-08-06 09:21:04'),
    (10, 4, 17, '2025-08-06 09:21:04', '2025-08-06 09:21:04');


INSERT INTO game_result (
    game_id, problem_id, started_at, finished_at, created_at, updated_at
) VALUES
      (1, 3243, '2025-07-29 09:00:00', '2025-07-29 09:15:32', NOW(), NOW()),
      (2, 3243, '2025-07-29 09:00:00', '2025-07-29 09:15:32', NOW(), NOW()),
      (3, 3243, '2025-07-29 09:00:00', '2025-07-29 09:15:32', NOW(), NOW()),
      (4, 3243, '2025-07-29 09:00:00', '2025-07-29 09:15:32', NOW(), NOW());

INSERT INTO player_result (
    game_result_id, player_id, ranks, earned_score, earned_coin,
    banned_problem_category_id, picked_problem_category_id, submit_id, solve_duration, created_at, updated_at
) VALUES
      (1, 1, 1, 150, 20, 5, 2, null, null, NOW(), NOW()),
      (1, 2, 2, 120, 15, 3, 1, null, null, NOW(), NOW()),
      (2, 3, 1, 180, 22, 1, 5, null, null, NOW(), NOW()),
      (2, 4, 2, 140, 16, 2, 3, null, null, NOW(), NOW()),
      (3, 5, 1, 200, 30, 3, 4, null, null, NOW(), NOW()),
      (3, 6, 2, 160, 24, 5, 1, null, null, NOW(), NOW()),
      (3, 7, 3, 130, 19, 4, 5, null, null, NOW(), NOW()),
      (3, 8, 4, 90, 12, 2, 3, null, null, NOW(), NOW()),
      (4, 9, 2, 0, 1000, 4, 5, 1, 1700, NOW(), NOW()),
      (4, 10, 1, 0, 2000, 2, 3, 2, 2000, NOW(), NOW());

INSERT INTO `item` (item_name, item_description, item_duration, item_cost) VALUES
    ('해킹', '상대방의 물리 키보드를 박살내고, 화상 키보드 코딩을 강제합니다.', 10, 500),
    ('월식', '상대방 화면에 암전 효과를 줍니다.', 10, 500),
    ('탈진', '상대방의 타이핑 입력속도를 지연시킵니다.', 10, 500),
    ('지진', '상대방 화면에 지진을 일으킵니다.', 5, 500),
    ('점화', '상대방 에디터에 불을 질러 코드를 태웁니다.', 10, 500);

INSERT INTO `spell` (spell_name, spell_description, spell_duration, spell_cost) VALUES
    ('보호막', '5분 간 상대방의 아이템 효과를 1회 막아주는 보호막을 생성합니다.', 300, 500),
    ('정화', '현재 나에게 적용되어 있는 모든 아이템 효과를 제거합니다.', 60, 800),
    ('감시자', '상대방의 실시간 화면을 일정시간 감시할 수 있습니다.', 60, 500);


/** 게임 결과 저장 테스트용 코드 제출 내역 **/
INSERT INTO problem_submit (
    problem_submit_id, problem_id, player_id, running_time, memory, source_code, programming_language, is_correct, submitted_at, created_at, updated_at
) VALUES
    (1, 3243, 9, 0.4, 25600, 'public class Main{public static void main(String[]a){System.out.println("try1");}}', 'JAVA', 1, NOW(), NOW(), NOW()),
    (2, 3243, 10, 0.3, 25600, 'public class Main{public static void main(String[]a){System.out.println("try1");}}', 'JAVA', 1, NOW(), NOW(), NOW()),

    (3, 3243, 11, 0.4, 23500, 'public class Main{public static void main(String[]a){System.out.println("try1");}}', 'JAVA', 1, NOW(), NOW(), NOW()),
    (4, 3243, 11, 0.4, 23500, 'public class Main{public static void main(String[]a){System.out.println("try2");}}', 'JAVA', 1, NOW(), NOW(), NOW()),
    (5, 3243, 11, null, null, 'print("try3")', 'JAVA', 0, NOW(), NOW(), NOW()),

    (6, 3243, 12, null, null, 'print("try1")', 'JAVA', 0, NOW(), NOW(), NOW()),
    (7, 3243, 12, 0.2, 23500, 'public class Main{public static void main(String[]a){System.out.println("try2");}}', 'JAVA', 1, NOW(), NOW(), NOW()),
    (8, 3243, 12, 0.5, null, 'public class Main{public static void main(String[]a){System.out.println("try2");}}', 'JAVA', 1, NOW(), NOW(), NOW());
