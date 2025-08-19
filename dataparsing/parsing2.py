from datasets import load_dataset
import re
import ast
import os

def format_arg_to_boj_style(arg):
    """
    하나의 인자(정수, 리스트, 2D 리스트 등)를 받아
    백준 스타일의 텍스트 형식으로 변환합니다.
    """
    if isinstance(arg, list) and (not arg or all(not isinstance(i, list) for i in arg)):
        return f"{len(arg)}\n" + " ".join(map(str, arg))
    
    if isinstance(arg, list) and all(isinstance(i, list) for i in arg):
        num_rows = len(arg)
        rows_str = "\n".join(" ".join(map(str, row)) for row in arg)
        return f"{num_rows}\n" + rows_str
        
    return str(arg)

def parse_and_add_test_cases(example_dict):
    """
    하나의 문제 딕셔너리를 받아, 함수 시그니처를 동적으로 분석하여
    테스트 케이스를 파싱하고 새로운 키로 추가하여 반환합니다. (전처리 기능 강화)
    """
    all_test_case_inputs = []
    all_test_case_outputs = []
    
    try:
        starter_code = example_dict.get('starter_code', '')
        match = re.search(r'def\s+\w+\((.*?)\)', starter_code)
        if not match: return example_dict

        params_str = match.group(1)
        arg_names = [p.split(':')[0].strip() for p in params_str.split(',')][1:]

    except Exception as e:
        print(f"🚨 Error parsing function signature for problem {example_dict.get('question_id')}: {e}")
        return example_dict

    if 'input_output' in example_dict and example_dict['input_output'] is not None:
        for test_case in example_dict['input_output']:
            input_str = test_case.get('input', '')
            output_str = test_case.get('output', '')
            if not input_str or not output_str: continue
            
            try:
                # 1. 'null'을 파이썬의 'None'으로 치환
                processed_input_str = input_str.replace('null', 'None')
                # 2. 여러 할당문을 분리하기 위해, 리스트/튜플 바깥의 쉼표를 줄바꿈으로 치환
                processed_input_str = re.sub(r',(?![^\[]*\])(?![^\(]*\))', '\n', processed_input_str)

                local_vars = {}
                exec(processed_input_str, {}, local_vars)
                
                parsed_inputs = [local_vars[name] for name in arg_names]
                all_test_case_inputs.append(parsed_inputs)
                
                # 출력 문자열도 'null'을 'None'으로 바꿔서 파싱
                processed_output_str = output_str.replace('null', 'None')
                all_test_case_outputs.append(ast.literal_eval(processed_output_str))

            except Exception as e:
                problem_id_for_log = example_dict.get('question_id', 'N/A')
                print(f"  - ⚠️  [Problem ID: {problem_id_for_log}] Skipping test case due to error.")
                print(f"      - Original Input: '{input_str}'")
                print(f"      - Error: {e}")

    example_dict['parsed_test_inputs'] = all_test_case_inputs
    example_dict['parsed_test_outputs'] = all_test_case_outputs
    return example_dict

# --- 메인 실행 로직 ---
# 👇 여기에 새로 다운로드한 정확한 파일 이름을 넣어주세요!
file_path = "train.jsonl" 
dataset = load_dataset('json', data_files=file_path, split='train')
list_of_problems = [problem for problem in dataset]
processed_data = [parse_and_add_test_cases(p) for p in list_of_problems]
print(f"✅ {len(processed_data)} problems parsed successfully!")

# --- 파일 저장 로직 ---
base_output_dir = "testcases"
os.makedirs(base_output_dir, exist_ok=True)

for problem in processed_data:
    problem_id = problem.get('question_id')
    inputs = problem.get('parsed_test_inputs')
    outputs = problem.get('parsed_test_outputs')

    if not problem_id or not inputs or not outputs:
        print(f"⏩ Skipping problem folder creation for ID {problem_id} because no test cases were successfully parsed.")
        continue

    problem_dir = os.path.join(base_output_dir, str(problem_id))
    os.makedirs(problem_dir, exist_ok=True)
    print(f"\n📁 Processing Problem ID: {problem_id} (saving to '{problem_dir}')")
    
    all_in_contents = []
    all_out_contents = []
    num_test_cases = len(inputs)
    for i, (inp, outp) in enumerate(zip(inputs, outputs), 1):
        in_filepath = os.path.join(problem_dir, f"{i}.in")
        out_filepath = os.path.join(problem_dir, f"{i}.out")
        try:
            formatted_inputs = [format_arg_to_boj_style(arg) for arg in inp]
            input_content = "\n".join(formatted_inputs)
            all_in_contents.append(input_content)
            if outp is None:
                output_content = "None"
            else:
                output_content = format_arg_to_boj_style(outp)
            all_out_contents.append(output_content)
            with open(in_filepath, 'w', encoding='utf-8') as f_in: f_in.write(input_content)
            with open(out_filepath, 'w', encoding='utf-8') as f_out: f_out.write(output_content)
            print(f"  - ✔️  Saved: {i}.in / {i}.out")
        except Exception as e:
            print(f"  - ❌ Failed to save case {i} for problem {problem_id}. Error: {e}")

    try:
        all_in_filepath = os.path.join(problem_dir, "all.in")
        with open(all_in_filepath, 'w', encoding='utf-8') as f_all_in:
            f_all_in.write(str(num_test_cases) + '\n')
            f_all_in.write("\n".join(all_in_contents))
        all_out_filepath = os.path.join(problem_dir, "all.out")
        with open(all_out_filepath, 'w', encoding='utf-8') as f_all_out:
            f_all_out.write("\n".join(all_out_contents))
        print(f"  - ⭐ ✔️  Saved: all.in / all.out")
    except Exception as e:
        print(f"  - ❌ Failed to save summary files. Error: {e}")