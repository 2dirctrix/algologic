from datasets import load_dataset
import re
import json
import os
import requests
import time

# ------------------- GMS API 설정 -------------------
GMS_API_KEY = "S13P11C204-59a4ebfd-2e77-4809-8d10-dd4bfaea59d9"
GMS_API_URL = f"https://gms.ssafy.io/gmsapi/generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-lite:generateContent?key={GMS_API_KEY}"

# (번역 및 정제 함수는 이전과 동일)
translation_cache = {}
def translate_text_with_gms(text_to_translate):
    if not all([GMS_API_KEY, GMS_API_URL, text_to_translate]): return text_to_translate
    if text_to_translate in translation_cache: return translation_cache[text_to_translate]
    headers = {'Content-Type': 'application/json'}
    prompt_text = f"다음 영문 텍스트를 자연스러운 한국어 문체로 번역해줘. 결과에는 어떠한 추가 설명이나 마크다운 형식 없이, 오직 번역된 텍스트만 포함해줘.\n---\n{text_to_translate}\n---"
    data = {"contents": [{"parts": [{"text": prompt_text}]}]}
    try:
        response = requests.post(GMS_API_URL, headers=headers, json=data)
        response.raise_for_status()
        translated_text = response.json()['candidates'][0]['content']['parts'][0]['text'].strip()
        translation_cache[text_to_translate] = translated_text
        print(f"  - 🌐(GMS) Translating... -> '{translated_text[:30]}...'")
        time.sleep(1)
        return translated_text
    except Exception as e:
        print(f"  - ⚠️(GMS) Translation failed. Error: {e}")
        return text_to_translate

def extract_details(description_text):
    parts = re.split(r'\n\s*Constraints:\s*\n', description_text, maxsplit=1)
    main_desc_with_examples = parts[0]
    constraints = parts[1].strip() if len(parts) > 1 else ""
    main_desc = re.split(r'\n\s*Example \d+:', main_desc_with_examples, maxsplit=1)[0].strip()
    main_desc = main_desc.replace('\xa0', ' ')
    constraints = constraints.replace('\xa0', ' ')
    main_desc = re.split(r'\n\s*Follow-up:', main_desc, maxsplit=1)[0].strip()
    return main_desc, constraints

# --- MySQL을 위한 최종 이스케이프 함수 (요청사항 반영) ---
def escape_for_mysql(value):
    """MySQL INSERT 문에 들어갈 문자열을 안전하게 이스케이프합니다."""
    if value is None:
        return "NULL"
    # 백슬래시, 작은따옴표, 큰따옴표 모두 이스케이프 처리
    return value.replace('\\', '\\\\').replace("'", "\\'").replace('"', '\\"')

def generate_sql_from_json(json_file_path, output_sql_file):
    """JSON 데이터를 읽어 번역 후, 요청사항에 맞게 데이터를 수정하여 SQL INSERT 문을 생성합니다."""
    dataset = load_dataset('json', data_files=json_file_path, split='train')
    list_of_problems = [problem for problem in dataset]
    all_sql_queries = []

    for problem in list_of_problems:
        problem_id = problem.get('question_id')
        print(f"\n⚙️ Processing Problem ID: {problem_id}")

        problem_name = problem.get('task_id', 'Untitled').replace('-', ' ').title()
        problem_level = problem.get('difficulty', 'Medium').upper()
        
        eng_desc, eng_constraint = extract_details(problem.get('problem_description', ''))
        kor_desc = translate_text_with_gms(eng_desc)
        kor_constraint = translate_text_with_gms(eng_constraint)
        
        # --- 1단계: 원본 데이터에서 'input' 값 내부의 큰따옴표 제거 ---
        example_cases_raw = problem.get('input_output', [])[:3]
        modified_example_cases = []
        for case in example_cases_raw:
            modified_case = {
                "input": case.get('input', '').replace('"', ''),
                "output": case.get('output', '')
            }
            modified_example_cases.append(modified_case)
            
        # 2단계: 수정된 데이터를 JSON 문자열로 변환
        problem_example_json = json.dumps(modified_example_cases, ensure_ascii=False)
        
        time_limit_sec = 2
        memory_limit_byte = 4096000

        # --- 3단계: 모든 문자열 필드에 최종 이스케이프 함수 적용 ---
        sql_template = f"""
/** 알고리즘 문제: {problem_name} */
INSERT INTO problem (
    problem_id, problem_name, problem_level, time_limit_sec, memory_limit_byte,
    problem_description, problem_constraint, problem_example,
    created_at, updated_at
) VALUES (
    {problem_id},
    '{escape_for_mysql(problem_name)}',
    '{escape_for_mysql(problem_level)}',
    {time_limit_sec},
    {memory_limit_byte},
    '{escape_for_mysql(kor_desc)}',
    '{escape_for_mysql(kor_constraint)}',
    '{escape_for_mysql(problem_example_json)}',
    NOW(),
    NOW()
);
"""
        all_sql_queries.append(sql_template)
        print(f"✅ Generated SQL for problem ID: {problem_id}")

    with open(output_sql_file, 'w', encoding='utf-8') as f:
        f.write("\n".join(all_sql_queries))
    
    print(f"\n🎉 Successfully saved all SQL queries to '{output_sql_file}'")

# --- 메인 실행 ---
if __name__ == "__main__":
    json_file_path = "converted_train.json"
    output_sql_file = "insert_problems_mysql2.sql"
    generate_sql_from_json(json_file_path, output_sql_file)