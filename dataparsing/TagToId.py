from datasets import load_dataset
import json
from collections import defaultdict

def generate_category_to_problems_json(json_file_path, output_json_file):
    """
    JSON 파일에서 데이터를 읽어, 각 카테고리별로
    어떤 문제들이 속해 있는지 정리한 새로운 JSON 파일을 생성합니다.
    """
    
    # 1. 데이터를 담을 딕셔너리 생성
    # defaultdict를 사용하면 키가 없을 때 자동으로 빈 리스트를 생성해줘서 편리합니다.
    category_problem_map = defaultdict(list)
    
    # 데이터 로드
    dataset = load_dataset('json', data_files=json_file_path, split='train')
    list_of_problems = [problem for problem in dataset]
    
    print(f"✅ 총 {len(list_of_problems)}개의 문제를 파일에서 읽었습니다. 카테고리별로 분류를 시작합니다...")

    # 2. 각 문제를 순회하며 데이터 재구성
    for problem in list_of_problems:
        problem_id = problem.get('question_id')
        problem_name = problem.get('task_id', 'Untitled').replace('-', ' ').title()
        tags = problem.get('tags', [])

        if not problem_id or not tags:
            continue
            
        # 문제 정보를 담을 작은 딕셔너리
        problem_info = {
            "id": problem_id,
            "name": problem_name
        }
        
        # 3. 이 문제가 속한 모든 카테고리에 문제 정보를 추가
        for tag_name in tags:
            category_problem_map[tag_name].append(problem_info)

    print("📊 모든 문제 분류 완료. 가독성을 위해 정렬을 시작합니다...")

    # 4. 최종 결과물을 정렬
    # 4-1. 카테고리 이름을 알파벳 순으로 정렬
    sorted_keys = sorted(category_problem_map.keys())
    
    final_sorted_map = {}
    for category_name in sorted_keys:
        # 4-2. 각 카테고리 내의 문제 목록을 문제 ID 순으로 정렬
        problems_in_category = category_problem_map[category_name]
        sorted_problems = sorted(problems_in_category, key=lambda p: p['id'])
        final_sorted_map[category_name] = sorted_problems
        
    # 5. 정렬된 데이터를 JSON 파일로 저장
    with open(output_json_file, 'w', encoding='utf-8') as f:
        # indent=2 : 보기 좋게 2칸 들여쓰기
        # ensure_ascii=False : 한글 등 비-ASCII 문자 깨짐 방지
        json.dump(final_sorted_map, f, indent=2, ensure_ascii=False)
        
    print(f"\n🎉 성공! 모든 카테고리별 문제 목록을 '{output_json_file}' 파일에 저장했습니다.")

# --- 메인 실행 ---
if __name__ == "__main__":
    json_file_path = "converted_train.json"
    output_json_file = "categories_to_problems.json"
    generate_category_to_problems_json(json_file_path, output_json_file)