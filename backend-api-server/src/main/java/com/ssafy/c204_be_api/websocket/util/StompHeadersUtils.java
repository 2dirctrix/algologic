package com.ssafy.c204_be_api.websocket.util;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public final class StompHeadersUtils {
    private StompHeadersUtils() {}

    public static String getCorrelationId(SimpMessageHeaderAccessor accessor) {
        if (accessor == null) return null;
        Object v = accessor.getNativeHeader("x-req-id") != null
                ? accessor.getNativeHeader("x-req-id").stream().findFirst().orElse(null)
                : null;
        return v instanceof String s && !s.isBlank() ? s : null;
    }
}
