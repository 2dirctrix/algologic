package com.ssafy.c204_be_api.common.web.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Event<T> {
    private T event;

    private Object payload;  // JSON 구조 전송 고려 (다양한 DTO 가능)

    public static <T> Event<T> of(T event, Object payload) {
        return new Event<>(event, payload);
    }
}
