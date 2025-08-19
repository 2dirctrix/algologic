package com.ssafy.c204_be_api.websocket.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // CONNECT만 처리하고, 그 외는 skip
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            if (accessor.getUser() == null) {
                var attributes = accessor.getSessionAttributes();
                if (attributes == null) return message;

                Authentication auth = (Authentication) attributes.get("auth");

                if (auth != null) {
                    accessor.setUser(auth);  // WebSocketSession에 인증 주입
                } else {
                    throw new AuthenticationCredentialsNotFoundException("JWT 쿠키가 유효하지 않습니다.");
                }
            }
        }
        return message;
    }
}

