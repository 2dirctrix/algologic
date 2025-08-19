package com.ssafy.c204_be_api.validation.exception;

import com.ssafy.c204_be_api.validation.code.ErrorCode;
import lombok.Getter;

import java.util.Map;

/**
 * 인게임 도메인(픽/샵/아이템/스펠/전투)에서 던지는 비즈니스 예외.
 */
@Getter
public class InGameException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> meta;
    private final String reqId;

    public InGameException(ErrorCode code) {
        this(code, code.getDefaultMessage(), null, Map.of());
    }

    public InGameException(ErrorCode code, String message) {
        this(code, message, null, Map.of());
    }

    public InGameException(ErrorCode code, String message, String reqId) {
        this(code, message, reqId, Map.of());
    }

    public InGameException(ErrorCode code, String message, String reqId, Map<String, Object> meta) {
        super(message);
        this.errorCode = code;
        this.reqId = reqId;
        this.meta = meta == null ? Map.of() : Map.copyOf(meta);
    }
}
