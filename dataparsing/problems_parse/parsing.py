from datasets import load_dataset
import re
import ast

def parse_and_add_test_cases(example_dict):
    """
    하나의 문제 딕셔너리(example_dict)를 받아
    파싱된 테스트 케이스를 새로운 키로 추가하여 반환합니다.
    """
    all_test_case_inputs = []
    all_test_case_outputs = []

    if 'input_output' in example_dict and example_dict['input_output'] is not None:
        for test_case in example_dict['input_output']:
            input_str = test_case.get('input', '')
            output_str = test_case.get('output', '')
            
            # 입력 또는 출력이 비어있으면 건너뜁니다.
            if not input_str or not output_str:
                continue

            try:
                # --- 입력 파싱 ---
                n_match = re.search(r'n\s*=\s*(-?\d+)', input_str)
                queries_match = re.search(r'queries\s*=\s*(\[.*\])', input_str)

                if n_match and queries_match:
                    n_val = int(n_match.group(1))
                    queries_val = ast.literal_eval(queries_match.group(1))
                    parsed_inputs = [n_val, queries_val]
                    all_test_case_inputs.append(parsed_inputs)
                else:
                    # 다른 형식의 입력일 수 있으므로, 일단 건너뜁니다.
                    # print(f"Skipping due to unmatched format: {input_str}")
                    continue

                # --- 출력 파싱 ---
                parsed_output = ast.literal_eval(output_str)
                all_test_case_outputs.append(parsed_output)
                
            except Exception as e:
                # 파싱 중 에러가 발생하면 해당 테스트 케이스는 건너뜁니다.
                # print(f"Warning: Could not parse test case. Input: '{input_str}', Error: {e}")
                pass
    
    # 원본 딕셔너리에 새로운 키를 추가합니다.
    example_dict['parsed_test_inputs'] = all_test_case_inputs
    example_dict['parsed_test_outputs'] = all_test_case_outputs
    
    return example_dict

# --- 메인 실행 로직 ---
file_path = "test.json"

# 1. 데이터셋 로드
dataset = load_dataset('json', data_files=file_path, split='train')
print("Dataset loaded successfully.")

# 2. Dataset 객체를 일반 파이썬 리스트로 변환 (Arrow 에러 회피)
list_of_problems = [problem for problem in dataset]
print(f"Converted dataset to a list with {len(list_of_problems)} problem(s).")

# 3. for 루프를 사용하여 각 문제를 직접 파싱
processed_data = []
for problem_dict in list_of_problems:
    # 각 딕셔너리에 파싱 함수를 적용
    processed_dict = parse_and_add_test_cases(problem_dict)
    processed_data.append(processed_dict)

print("Parsing complete!")

# 4. 결과 확인 (이제 processed_data는 파싱된 정보를 담은 리스트입니다)
if processed_data:
    first_problem = processed_data[0]
    print("\n--- 파싱 결과 확인 (첫 번째 문제) ---")
    print(f"문제 ID: {first_problem.get('question_id')}")
    print(f"함수 이름: {first_problem.get('entry_point')}")

    if first_problem.get('parsed_test_inputs'):
        print("\n첫 번째 테스트 케이스:")
        print(f"  - 입력 (파싱됨): {first_problem['parsed_test_inputs'][0]}")
        print(f"  - 예상 출력 (파싱됨): {first_problem['parsed_test_outputs'][0]}")
        
        print("\n전체 파싱된 테스트 케이스 리스트 (첫 5개만 출력):")
        print("입력 리스트:")
        print(first_problem.get('parsed_test_inputs', [])[:5])
        print("출력 리스트:")
        print(first_problem.get('parsed_test_outputs', [])[:5])
    else:
        print("파싱된 테스트 케이스를 찾을 수 없습니다.")
else:
    print("No data was processed.")