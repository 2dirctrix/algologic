package com.ssafy.c204_be_api.websocket.interceptor;

import com.ssafy.c204_be_api.authentication.util.PrincipalUtils;
import com.ssafy.c204_be_api.member.domain.Member;
import com.ssafy.c204_be_api.websocket.session.SessionRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketConnectionInterceptor implements ChannelInterceptor {

    private final SessionRegistry sessionRegistry;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String sessionId = accessor.getSessionId();
            Member member = PrincipalUtils.extractMember(accessor.getUser());

            Long memberId = member.getId();

            sessionRegistry.validDuplicate(memberId, sessionId);  // 중복 연결 검증
        }
        return message;
    }
}
