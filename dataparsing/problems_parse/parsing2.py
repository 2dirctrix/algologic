# ... (상단의 import 및 함수들은 모두 이전과 동일)

# --- 파일 저장 로직 (all.in, all.out 추가) ---
base_output_dir = "testcases"
os.makedirs(base_output_dir, exist_ok=True)

for problem in processed_data:
    problem_id = problem.get('question_id')
    inputs = problem.get('parsed_test_inputs')
    outputs = problem.get('parsed_test_outputs')

    if not problem_id or not inputs or not outputs:
        print(f"⏩ Skipping problem {problem.get('task_id', 'N/A')} due to missing parsed test cases.")
        continue

    problem_dir = os.path.join(base_output_dir, str(problem_id))
    os.makedirs(problem_dir, exist_ok=True)
    print(f"\n📁 Processing Problem ID: {problem_id} (saving to '{problem_dir}')")

    # --- 1. 종합 파일 내용을 담을 리스트 초기화 ---
    all_in_contents = []
    all_out_contents = []
    num_test_cases = len(inputs)

    # 2. 각 테스트 케이스를 개별 파일로 저장하며, 내용도 수집
    for i, (inp, outp) in enumerate(zip(inputs, outputs), 1):
        in_filepath = os.path.join(problem_dir, f"{i}.in")
        out_filepath = os.path.join(problem_dir, f"{i}.out")
        
        try:
            # .in 파일 내용 생성 및 수집
            formatted_inputs = [format_arg_to_boj_style(arg) for arg in inp]
            input_content = "\n".join(formatted_inputs)
            all_in_contents.append(input_content)
            
            # .out 파일 내용 생성 및 수집
            if outp is None:
                output_content = "None"
            else:
                output_content = format_arg_to_boj_style(outp)
            all_out_contents.append(output_content)

            # 개별 파일 쓰기
            with open(in_filepath, 'w', encoding='utf-8') as f_in:
                f_in.write(input_content)
            with open(out_filepath, 'w', encoding='utf-8') as f_out:
                f_out.write(output_content)
            
            print(f"  - ✔️  Saved: {i}.in / {i}.out")
        except Exception as e:
            print(f"  - ❌ Failed to save case {i} for problem {problem_id}. Error: {e}")

    # --- 3. 모든 개별 파일 저장이 끝나면 종합 파일 생성 ---
    try:
        # all.in 파일 생성
        all_in_filepath = os.path.join(problem_dir, "all.in")
        with open(all_in_filepath, 'w', encoding='utf-8') as f_all_in:
            f_all_in.write(str(num_test_cases) + '\n') # 첫 줄에 테스트 케이스 개수(T)
            # 각 테스트 케이스를 줄바꿈으로 구분하여 합침
            f_all_in.write("\n".join(all_in_contents))
        
        # all.out 파일 생성
        all_out_filepath = os.path.join(problem_dir, "all.out")
        with open(all_out_filepath, 'w', encoding='utf-8') as f_all_out:
            # 각 테스트 케이스 출력을 줄바꿈으로 구분하여 합침
            f_all_out.write("\n".join(all_out_contents))
            
        print(f"  - ⭐ ✔️  Saved: all.in / all.out")
    except Exception as e:
        print(f"  - ❌ Failed to save summary files for problem {problem_id}. Error: {e}")