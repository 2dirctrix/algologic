package com.ssafy.c204_be_api.validation.exception;

import com.ssafy.c204_be_api.validation.code.ErrorCode;
import lombok.Getter;

import java.util.Map;

/**
 * 방/대기실 도메인에서 의도적으로 던지는 비즈니스 예외.
 */
@Getter
public class RoomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> meta; // ex) { "max": 6, "current": 6 }
    private final String reqId;             // 클라이언트 상관관계 ID (선택)

    public RoomException(ErrorCode code) {
        this(code, code.getDefaultMessage(), null, Map.of());
    }

    public RoomException(ErrorCode code, String message) {
        this(code, message, null, Map.of());
    }

    public RoomException(ErrorCode code, String message, String reqId) {
        this(code, message, reqId, Map.of());
    }

    public RoomException(ErrorCode code, String message, String reqId, Map<String, Object> meta) {
        super(message);
        this.errorCode = code;
        this.reqId = reqId;
        this.meta = meta == null ? Map.of() : Map.copyOf(meta);
    }
}