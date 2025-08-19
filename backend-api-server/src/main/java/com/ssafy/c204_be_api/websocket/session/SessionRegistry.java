package com.ssafy.c204_be_api.websocket.session;

import com.ssafy.c204_be_api.common.web.message.Meta;
import com.ssafy.c204_be_api.validation.code.ErrorCode;
import com.ssafy.c204_be_api.validation.exception.RoomException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionRegistry {

    private final Map<String, UUID> sessionToRoom = new ConcurrentHashMap<>();
    private final Map<String, Long> sessionToMember = new ConcurrentHashMap<>();
    private final Map<Long, String> memberToSession = new ConcurrentHashMap<>();

    public void register(String sessionId, UUID roomId, Long memberId) {
        sessionToRoom.put(sessionId, roomId);
        sessionToMember.put(sessionId, memberId);
        memberToSession.put(memberId, sessionId);
    }

    public void unregister(String sessionId) {
        sessionToRoom.remove(sessionId);
        Long memberId = sessionToMember.remove(sessionId);
        if (memberId != null) {
            memberToSession.remove(memberId);
        }
    }

    public UUID getRoomId(String sessionId) {
        return sessionToRoom.get(sessionId);
    }

    public Long getMemberId(String sessionId) {
        return sessionToMember.get(sessionId);
    }

    public String getSessionId(Long memberId) {
        return memberToSession.get(memberId);
    }

    public void validDuplicate(Long memberId, String sessionId) {
        String existing = getSessionId(memberId);
        if (existing != null && !existing.equals(sessionId)) {
            throw new RoomException(
                    ErrorCode.DUPLICATE_SESSION, "중복 연결이 감지되어 연결이 거부되었습니다.",
                    null, Meta.of("roomId", getRoomId(existing), "sessionId", existing)
            );
        }
    }
}
