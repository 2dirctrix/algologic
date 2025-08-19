package com.ssafy.c204_be_api.common.web.response;

import com.ssafy.c204_be_api.common.web.message.Meta;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private final Integer status;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> with(HttpStatus httpStatus, String message, @Nullable T data) {
        return new ApiResponse<>(httpStatus.value(), message, data);
    }

    public static <T> ApiResponse<T> with(HttpStatus httpStatus, String message) {
        return new ApiResponse<>(httpStatus.value(), message, null);
    }

    public static <T> ApiResponse<Object> with(Integer code, String message, Object data) {
        return new ApiResponse<>(code, message, data);
    }
}
