package com.ssafy.c204_be_api.common.web.message;

import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiMessage<T> {

    private final String type;   // "RESPONSE", "EVENT", "ERROR" 등
    private final int code;      // HTTP status-like code
    private final String message;
    private final T data;
    private final String reqId;
    private final Map<String, Object> meta;

    public static <T> ApiMessage<T> response(int code, String message, @Nullable T data) {
        return new ApiMessage<>("RESPONSE", code, message, data, null, null);
    }

    public static <T> ApiMessage<T> event(int code, String message,  @Nullable T data) {
        return new ApiMessage<>("EVENT", code, message, data, null, null);
    }

    public static <T> ApiMessage<T> sync(int code, String message,  @Nullable T data) {
        return new ApiMessage<>("SYNC", code, message, data, null, null);
    }

    public static <T> ApiMessage<T> error(int code, String message, String reqId, Map<String,Object> meta) {
        return new ApiMessage<>("ERROR", code, message, null, reqId, meta);
    }

    public static <T> ApiMessage<T> of(String type, int code, String message, @Nullable T data) {
        return new ApiMessage<>(type, code, message, data, null, null);
    }
}