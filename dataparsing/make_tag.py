from datasets import load_dataset
import json

def generate_new_category_sql(json_file_path, output_sql_file):
    """
    JSON 파일에서 모든 태그를 읽어와, 기존 목록에 없는
    새로운 카테고리에 대한 INSERT SQL문을 생성합니다.
    """
    
    # 1. 기존에 정의된 카테고리 목록 (빠른 조회를 위해 set 사용)
    existing_categories = {
        'Breadth-First Search', 'Depth-First Search', 'Graph',
        'Dynamic Programming', 'Array', 'Stack', 'Queue',
        'Union Find', 'Backtracking', 'Recursion',
        'Binary Search', 'Sorting', 'Greedy', 'Two Pointers',
        'Bit Manipulation', 'Hashing', 'Sliding Window',
        'Math', 'String', 'Tree'
    }
    None
    # 데이터 로드
    dataset = load_dataset('json', data_files=json_file_path, split='train')
    list_of_problems = [problem for problem in dataset]
    
    # 2. JSON 파일에서 모든 태그를 중복 없이 수집
    all_tags_from_file = set()
    for problem in list_of_problems:
        tags = problem.get('tags', [])
        if tags:  # tags가 None이거나 비어있지 않은 경우
            all_tags_from_file.update(tags)
            
    print(f"✅ 총 {len(all_tags_from_file)}개의 고유한 태그를 파일에서 찾았습니다.")
    print(f"   - {list(all_tags_from_file)}")
    
    # 3. 기존 목록에 없는 '신규' 카테고리만 필터링
    new_categories = all_tags_from_file - existing_categories
    
    if not new_categories:
        print("\n🎉 새로운 카테고리가 없습니다. 모든 카테고리가 이미 목록에 존재합니다.")
        # 파일에 내용이 없음을 명시
        with open(output_sql_file, 'w', encoding='utf-8') as f:
            f.write("-- No new categories found to insert. --\n")
        return

    print(f"\n✨ {len(new_categories)}개의 새로운 카테고리를 찾았습니다: {list(new_categories)}")
    
    # 4. 신규 카테고리에 대한 SQL INSERT 문 생성
    # ID는 기존 20개 다음인 21번부터 시작
    next_category_id = 21
    new_category_queries = []
    
    # 가나다순으로 정렬하여 INSERT 문 생성
    for category_name in sorted(list(new_categories)):
        # SQL 인젝션 방지를 위해 작은따옴표 이스케이프
        escaped_name = category_name.replace("'", "''")
        
        sql = f"INSERT INTO problem_category (problem_category_id, problem_category_name, created_at, updated_at) VALUES ({next_category_id}, '{escaped_name}', NOW(), NOW());"
        new_category_queries.append(sql)
        next_category_id += 1
        
    # 5. 생성된 SQL문을 파일에 저장
    with open(output_sql_file, 'w', encoding='utf-8') as f:
        f.write("/**\n * --------------------------\n *  신규 알고리즘 카테고리 INSERT\n * --------------------------\n */\n")
        f.write("\n".join(new_category_queries))
    
    print(f"\n💾 성공적으로 신규 카테고리 SQL을 '{output_sql_file}' 파일에 저장했습니다.")

# --- 메인 실행 ---
if __name__ == "__main__":
    json_file_path = "converted_train.json"  # 분석할 JSON 파일 경로
    output_sql_file = "new_categories.sql" # 결과 SQL 파일
    generate_new_category_sql(json_file_path, output_sql_file)