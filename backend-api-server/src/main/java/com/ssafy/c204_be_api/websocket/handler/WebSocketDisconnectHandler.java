package com.ssafy.c204_be_api.websocket.handler;

import com.ssafy.c204_be_api.room.service.RoomService;
import com.ssafy.c204_be_api.websocket.session.SessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketDisconnectHandler {

    private final RoomService roomService;
    private final SessionRegistry sessionRegistry;

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        String sessionId = StompHeaderAccessor.wrap(event.getMessage()).getSessionId();

        UUID roomId = sessionRegistry.getRoomId(sessionId);
        Long memberId = sessionRegistry.getMemberId(sessionId);

        if (roomId != null && memberId != null) {
            sessionRegistry.unregister(sessionId);
            roomService.disconnect(roomId, memberId);
        }
    }
}