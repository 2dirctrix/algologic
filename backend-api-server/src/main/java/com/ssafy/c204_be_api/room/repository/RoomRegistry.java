package com.ssafy.c204_be_api.room.repository;

import com.ssafy.c204_be_api.common.web.message.Meta;
import com.ssafy.c204_be_api.validation.code.ErrorCode;
import com.ssafy.c204_be_api.validation.exception.RoomException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomRegistry {
    private final Map<Long, UUID> memberToRoom = new ConcurrentHashMap<>();

    public void register(UUID roomId, Long memberId) {
        memberToRoom.put(memberId, roomId);
    }

    public void unregister(Long memberId) {
        memberToRoom.remove(memberId);
    }

    public UUID getRoomId(Long memberId) {
        return memberToRoom.get(memberId);
    }

    public void validDuplicate(Long memberId, UUID roomId) {
        UUID existing = memberToRoom.get(memberId);
        if (existing != null && !existing.equals(roomId)) {
            throw new RoomException(
                    ErrorCode.DUPLICATE_JOIN, "이미 참가중인 방이 있습니다.",
                    null, Meta.of("roomId", roomId)
            );
        }
    }
}
