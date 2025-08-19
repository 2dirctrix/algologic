from datasets import load_dataset
import json

def generate_mapping_sql(json_file_path, output_sql_file):
    """
    JSON 파일에서 문제별 태그를 읽어와,
    problem_category_map 테이블에 INSERT하는 SQL문을 생성합니다.
    """
    
    # --- 1. 완성된 전체 카테고리 목록 (이름: ID) ---
    category_to_id = {
        'Breadth-First Search': 1, 'Depth-First Search': 2, 'Graph': 3,
        'Dynamic Programming': 4, 'Array': 5, 'Stack': 6, 'Queue': 7,
        'Union Find': 8, 'Backtracking': 9, 'Recursion': 10,
        'Binary Search': 11, 'Sorting': 12, 'Greedy': 13, 'Two Pointers': 14,
        'Bit Manipulation': 15, 'Hashing': 16, 'Sliding Window': 17,
        'Math': 18, 'String': 19, 'Tree': 20, 'Binary Indexed Tree': 21,
        'Binary Search Tree': 22, 'Binary Tree': 23, 'Bitmask': 24,
        'Brainteaser': 25, 'Bucket Sort': 26, 'Combinatorics': 27,
        'Counting': 28, 'Counting Sort': 29, 'Divide and Conquer': 30,
        'Enumeration': 31, 'Eulerian Circuit': 32, 'Game Theory': 33,
        'Geometry': 34, 'Hash Function': 35, 'Hash Table': 36,
        'Heap (Priority Queue)': 37, 'Interactive': 38, 'Line Sweep': 39,
        'Linked List': 40, 'Matrix': 41, 'Memoization': 42,
        'Merge Sort': 43, 'Monotonic Queue': 44, 'Monotonic Stack': 45,
        'Number Theory': 46, 'Ordered Set': 47, 'Prefix Sum': 48,
        'Quickselect': 49, 'Radix Sort': 50, 'Rolling Hash': 51,
        'Segment Tree': 52, 'Simulation': 53, 'String Matching': 54,
        'Topological Sort': 55, 'Trie': 56
    }
    
    # 데이터 로드
    dataset = load_dataset('json', data_files=json_file_path, split='train')
    list_of_problems = [problem for problem in dataset]
    
    all_map_queries = []

    # 2. 각 문제를 순회하며 매핑 SQL 생성
    for problem in list_of_problems:
        problem_id = problem.get('question_id')
        tags = problem.get('tags', [])

        # 문제 ID나 태그가 없으면 건너뜀
        if not problem_id or not tags:
            continue
            
        value_clauses = []
        # 3. 각 태그에 대한 (problem_id, category_id) 쌍 생성
        for tag_name in sorted(tags): # 태그 이름을 정렬하여 일관성 유지
            if tag_name in category_to_id:
                category_id = category_to_id[tag_name]
                clause = f"({problem_id}, {category_id}, NOW(), NOW())"
                value_clauses.append(clause)
            else:
                # 마스터 카테고리 목록에 없는 태그가 있을 경우 경고
                print(f"⚠️ 경고: 문제 ID {problem_id}의 태그 '{tag_name}'를 카테고리 목록에서 찾을 수 없습니다. 건너뜁니다.")
        
        # 4. 문제별로 INSERT 문 그룹화
        if value_clauses:
            # VALUES (...) , (...) , (...) 형식으로 만들기
            values_string = ",\n       ".join(value_clauses)
            
            sql = f"""/** 문제 ID {problem_id} 카테고리 매핑 */
INSERT INTO problem_category_map(problem_id, problem_category_id, created_at, updated_at)
VALUES {values_string};"""
            all_map_queries.append(sql)

    # 5. 모든 SQL문을 파일에 저장
    if all_map_queries:
        with open(output_sql_file, 'w', encoding='utf-8') as f:
            f.write("/**\n * ==========================================\n *      문제-카테고리 매핑 데이터\n * ==========================================\n */\n\n")
            f.write("\n\n".join(all_map_queries)) # 각 INSERT 문 사이에 두 줄 띄우기
        print(f"\n🎉 성공적으로 모든 매핑 SQL을 '{output_sql_file}' 파일에 저장했습니다.")
    else:
        print("\nℹ️ 생성할 매핑 데이터가 없습니다.")

# --- 메인 실행 ---
if __name__ == "__main__":
    json_file_path = "converted_train.json"
    output_sql_file = "mappings.sql"
    generate_mapping_sql(json_file_path, output_sql_file)