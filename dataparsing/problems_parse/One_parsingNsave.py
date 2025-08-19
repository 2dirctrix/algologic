from datasets import load_dataset
import re
import ast
import os

def parse_and_add_test_cases(example_dict):
    """하나의 문제 딕셔너리를 받아 파싱된 테스트 케이스를 추가하여 반환합니다."""
    # ... 이전과 동일 ...
    all_test_case_inputs, all_test_case_outputs = [], []
    if 'input_output' in example_dict and example_dict['input_output']:
        for test_case in example_dict['input_output']:
            input_str = test_case.get('input', '')
            output_str = test_case.get('output', '')
            if not input_str or not output_str: continue
            try:
                n_match = re.search(r'n\s*=\s*(-?\d+)', input_str)
                queries_match = re.search(r'queries\s*=\s*(\[.*\])', input_str)
                if n_match and queries_match:
                    n_val = int(n_match.group(1))
                    queries_val = ast.literal_eval(queries_match.group(1))
                    all_test_case_inputs.append([n_val, queries_val])
                    all_test_case_outputs.append(ast.literal_eval(output_str))
                else: continue
            except Exception: pass
    example_dict['parsed_test_inputs'] = all_test_case_inputs
    example_dict['parsed_test_outputs'] = all_test_case_outputs
    return example_dict

# --- 메인 실행 로직 ---
file_path = "test.json"
dataset = load_dataset('json', data_files=file_path, split='train')
list_of_problems = [problem for problem in dataset]
processed_data = [parse_and_add_test_cases(p) for p in list_of_problems]
print("✅ Parsing complete!")

# --- 최종 고도화된 파일 저장 로직 (쉼표 제거 버전) ---
base_output_dir = "testcases"
os.makedirs(base_output_dir, exist_ok=True)

for problem in processed_data:
    problem_id = problem.get('question_id')
    inputs = problem.get('parsed_test_inputs')
    outputs = problem.get('parsed_test_outputs')

    if not problem_id or not inputs or not outputs:
        print(f"⏩ Skipping a problem due to missing data (ID: {problem_id}).")
        continue

    problem_dir = os.path.join(base_output_dir, str(problem_id))
    os.makedirs(problem_dir, exist_ok=True)
    print(f"\n📁 Processing Problem ID: {problem_id} (saving to '{problem_dir}')")

    for i, (inp, outp) in enumerate(zip(inputs, outputs), 1):
        in_filepath = os.path.join(problem_dir, f"{i}.in")
        out_filepath = os.path.join(problem_dir, f"{i}.out")
        try:
            with open(in_filepath, 'w', encoding='utf-8') as f_in:
                n_val, queries_val = inp[0], inp[1]
                f_in.write(str(n_val) + '\n')
                for query_pair in queries_val:
                    f_in.write(" ".join(map(str, query_pair)) + '\n')

            with open(out_filepath, 'w', encoding='utf-8') as f_out:
                output_content = " ".join(map(str, outp))
                f_out.write(output_content)
            
            print(f"  - ✔️  Saved: {i}.in / {i}.out")
        except Exception as e:
            print(f"  - ❌ Failed to save case {i} for problem {problem_id}. Error: {e}")