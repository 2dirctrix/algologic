import json

def convert_jsonl_to_json_safe(input_path, output_path):
    """
    대용량 JSONL 파일을 JSON 배열 파일로 안전하게 변환합니다.
    각 줄을 읽어 유효한 JSON인지 확인하고, 유효한 경우에만 파일에 씁니다.
    """
    json_objects = []
    print("파일을 읽는 중...")
    try:
        with open(input_path, 'r', encoding='utf-8') as infile:
            for line in infile:
                stripped_line = line.strip()
                if not stripped_line:
                    continue
                try:
                    # 각 줄이 유효한 JSON인지 파싱 시도
                    json_obj = json.loads(stripped_line)
                    json_objects.append(json_obj)
                except json.JSONDecodeError:
                    print(f"⚠️ 경고: 잘못된 JSON 형식의 줄을 건너뜁니다: {stripped_line[:100]}...")

        print(f"총 {len(json_objects)}개의 유효한 객체를 찾았습니다. JSON 파일로 저장합니다...")

        with open(output_path, 'w', encoding='utf-8') as outfile:
            # indent=2 옵션으로 사람이 읽기 좋게 만듭니다. (파일이 커지므로 원치 않으면 제거)
            json.dump(json_objects, outfile, ensure_ascii=False, indent=2)

        print(f"✅ 변환 완료: '{input_path}' -> '{output_path}'")

    except FileNotFoundError:
        print(f"❌ 오류: 입력 파일 '{input_path}'을(를) 찾을 수 없습니다.")
    except Exception as e:
        print(f"❌ 변환 중 오류 발생: {e}")


# --- 사용 방법 ---
if __name__ == "__main__":
    # 1. 여기에 파일 경로를 수정하세요.
    input_file_path = 'train.jsonl'  # 원본 JSONL 파일 경로
    output_file_path = 'converted_train.json'   # 저장될 JSON 파일 경로

    # 2. 스크립트를 실행하면 변환이 시작됩니다.
    # ※ 주의: 이 방법은 모든 데이터를 메모리에 올리므로 파일이 매우 크면 문제가 될 수 있습니다.
    #          메모리가 부족할 경우 '방법 1'을 사용하세요.
    convert_jsonl_to_json_safe(input_file_path, output_file_path)