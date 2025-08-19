package com.ssafy.c204_be_api.common.web.message;

import java.util.*;

/** 공용 메타데이터 유틸. null key/value는 무시, 입력 순서를 보존, 불변 Map 반환 */
public final class Meta {
    private Meta() {}

    /** 간단 버전: (key, value) ... 가변 인자 */
    public static Map<String, Object> of(Object... kv) {
        LinkedHashMap<String, Object> m = new LinkedHashMap<>();
        for (int i = 0; i + 1 < kv.length; i += 2) {
            Object k = kv[i];
            Object v = kv[i + 1];
            if (k != null && v != null) m.put(String.valueOf(k), v);
        }
        return Collections.unmodifiableMap(m);
    }

    /** 빌더 시작 */
    public static Builder builder() { return new Builder(); }

    /*  // TODO. reqId 처리에 Correlation 도입
    public static Map<String, Object> withReqId(Map<String, Object> base) {
        String id = Correlation.get();
        if (id == null || base.containsKey("reqId")) return base;
        LinkedHashMap<String, Object> copy = new LinkedHashMap<>(base);
        copy.put("reqId", id);
        return Collections.unmodifiableMap(copy);
    }
    */

    /** 플루언트 빌더 */
    public static final class Builder {
        private final LinkedHashMap<String, Object> m = new LinkedHashMap<>();

        public Builder put(String key, Object value) {
            if (key != null && value != null) m.put(key, value);
            return this;
        }
        public Builder putIf(String key, Object value, boolean condition) {
            if (condition) put(key, value);
            return this;
        }
        public Builder putAllNonNull(Map<String, ?> other) {
            if (other == null) return this;
            other.forEach((k, v) -> { if (k != null && v != null) m.put(k, v); });
            return this;
        }
        public Map<String, Object> build() {
            return Collections.unmodifiableMap(new LinkedHashMap<>(m));
        }
    }
}
