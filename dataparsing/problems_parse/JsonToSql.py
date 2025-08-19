from datasets import load_dataset
import re
import json
import os
import google.generativeai as genai
import time

# ------------------- Gemini API 설정 -------------------
# 실제 발급받은 API 키를 이곳에 입력하세요.
# 경고: 이 키는 절대 코드에 하드코딩하여 외부에 노출하면 안 됩니다.
# 실제 운영 시에는 환경 변수 등을 사용하는 것이 안전합니다.
GEMINI_API_KEY = "YOUR_GEMINI_API_KEY"

try:
    genai.configure(api_key=GEMINI_API_KEY)
    model = genai.GenerativeModel('gemini-pro')
    print("✅ Gemini API configured successfully.")
except Exception as e:
    print(f"🚨 Failed to configure Gemini API. Translation will be skipped. Error: {e}")
    model = None

# 번역 캐시: 동일한 텍스트에 대한 반복적인 API 호출을 방지하여 속도와 비용을 절약합니다.
translation_cache = {}

def translate_text_with_gemini(text_to_translate):
    """Gemini API를 사용하여 텍스트를 한국어로 번역합니다."""
    if not model or not text_to_translate:
        return text_to_translate # API 설정 실패 또는 빈 텍스트는 원본 반환

    if text_to_translate in translation_cache:
        return translation_cache[text_to_translate]

    prompt = f"""다음 영문 텍스트를 자연스러운 한국어 문체로 번역해줘.
    결과에는 어떠한 추가 설명이나 마크다운 형식 없이, 오직 번역된 텍스트만 포함해줘.

    ---
    {text_to_translate}
    ---
    """
    
    try:
        response = model.generate_content(prompt)
        translated_text = response.text.strip()
        translation_cache[text_to_translate] = translated_text
        print(f"  - 🌐 Translating... -> '{translated_text[:30]}...'")
        time.sleep(1) # API 과부하 방지를 위한 간단한 딜레이
        return translated_text
    except Exception as e:
        print(f"  - ⚠️ Translation failed for text: '{text_to_translate[:30]}...'. Error: {e}")
        return text_to_translate # 번역 실패 시 원본 텍스트 반환

def extract_details(description_text):
    """문제 설명 텍스트에서 순수 설명과 제약 조건을 분리합니다."""
    parts = re.split(r'\n\s*Constraints:\s*\n', description_text, maxsplit=1)
    main_desc_with_examples = parts[0]
    constraints = parts[1].strip() if len(parts) > 1 else ""
    
    main_desc = re.split(r'\n\s*Example \d+:', main_desc_with_examples, maxsplit=1)[0].strip()
    
    main_desc = main_desc.replace('\xa0', ' ')
    constraints = constraints.replace('\xa0', ' ')
    
    # Follow-up 부분 제거 (필요 시)
    main_desc = re.split(r'\n\s*Follow-up:', main_desc, maxsplit=1)[0].strip()
    
    return main_desc, constraints


def generate_sql_from_json(json_file_path, output_sql_file):
    """JSON 데이터를 읽어 번역 후, SQL INSERT 문을 생성하고 파일에 저장합니다."""
    dataset = load_dataset('json', data_files=json_file_path, split='train')
    list_of_problems = [problem for problem in dataset]
    
    all_sql_queries = []

    for problem in list_of_problems:
        problem_id = problem.get('question_id')
        print(f"\n⚙️ Processing Problem ID: {problem_id}")

        # 데이터 추출
        problem_name = problem.get('task_id', 'Untitled').replace('-', ' ').title()
        problem_level = problem.get('difficulty', 'Medium').upper()
        
        # 설명 및 제약조건 분리 후 번역
        eng_desc, eng_constraint = extract_details(problem.get('problem_description', ''))
        kor_desc = translate_text_with_gemini(eng_desc)
        kor_constraint = translate_text_with_gemini(eng_constraint)
        
        # 예시 입출력 3개 추출 및 분리
        example_cases = problem.get('input_output', [])[:3]
        example_inputs = [case['input'] for case in example_cases]
        example_outputs = [case['output'] for case in example_cases]
        
        # JSON 문자열로 변환
        problem_example_input_json = json.dumps(example_inputs, ensure_ascii=False)
        problem_example_output_json = json.dumps(example_outputs, ensure_ascii=False)
        
        # 고정 값 설정
        time_limit_ms = 2000
        memory_limit_kb = 4096 # 4096000 bytes = 4096 KB

        # SQL 쿼리 생성
        sql_template = f"""
/** 알고리즘 문제: {problem_name} */
INSERT INTO problem (
    problem_id, problem_name, problem_level, problem_time_limit_ms, problem_memory_limit_kb,
    problem_description, problem_constraint, problem_example_input, problem_example_output,
    created_at, updated_at
) VALUES (
    {problem_id},
    '{problem_name.replace("'", "''")}',
    '{problem_level.replace("'", "''")}',
    {time_limit_ms},
    {memory_limit_kb},
    '{kor_desc.replace("'", "''")}',
    '{kor_constraint.replace("'", "''")}',
    '{problem_example_input_json.replace("'", "''")}',
    '{problem_example_output_json.replace("'", "''")}',
    NOW(),
    NOW()
);
"""
        all_sql_queries.append(sql_template)
        print(f"✅ Generated SQL for problem ID: {problem_id}")

    # 모든 쿼리를 하나의 파일에 저장
    with open(output_sql_file, 'w', encoding='utf-8') as f:
        f.write("\n".join(all_sql_queries))
    
    print(f"\n🎉 Successfully saved all SQL queries to '{output_sql_file}'")


# --- 메인 실행 ---
if __name__ == "__main__":
    json_file_path = "multi_problems.json"
    output_sql_file = "insert_problems_final.sql"
    generate_sql_from_json(json_file_path, output_sql_file)