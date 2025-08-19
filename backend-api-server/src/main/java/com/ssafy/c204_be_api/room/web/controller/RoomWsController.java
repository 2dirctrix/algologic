package com.ssafy.c204_be_api.room.web.controller;

import com.ssafy.c204_be_api.authentication.util.PrincipalUtils;
import com.ssafy.c204_be_api.member.domain.Member;
import com.ssafy.c204_be_api.room.domain.Participant;
import com.ssafy.c204_be_api.room.service.RoomService;
import com.ssafy.c204_be_api.room.web.message.inbound.KickRequest;
import com.ssafy.c204_be_api.room.web.message.inbound.ReadyRequest;
import com.ssafy.c204_be_api.websocket.session.SessionRegistry;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Slf4j
@Controller
@MessageMapping("/v1/rooms")
@RequiredArgsConstructor
public class RoomWsController {
    private final RoomService roomService;
    private final SessionRegistry sessionRegistry;

    @MessageMapping("/{roomId}/join")
    public void join(@DestinationVariable UUID roomId,
                     Principal principal,
                     @Header("simpSessionId") String sessionId
    ) {
        Member member = PrincipalUtils.extractMember(principal);

        roomService.join(roomId, Participant.fromEntity(member));
        sessionRegistry.register(sessionId, roomId, member.getId());
    }

    @MessageMapping("/{roomId}/ready")
    public void ready(@DestinationVariable UUID roomId,
                      Principal principal,
                      @Valid @Payload ReadyRequest request
    ) {
        Long memberId = PrincipalUtils.extractMember(principal).getId();
        roomService.ready(roomId, memberId, request.getReady());
    }

    @MessageMapping("/{roomId}/leave")
    public void leave(@DestinationVariable UUID roomId,
                      Principal principal,
                      @Header("simpSessionId") String sessionId
    ) {
        Long memberId = PrincipalUtils.extractMember(principal).getId();
        roomService.leave(roomId, memberId);
        sessionRegistry.unregister(sessionId);
    }

    @MessageMapping("/{roomId}/kick")
    public void kick(@DestinationVariable UUID roomId,
                     Principal principal,
                     @Header("simpSessionId") String sessionId,
                     @Valid @Payload KickRequest request
    ) {
        Long memberId = PrincipalUtils.extractMember(principal).getId();
        Long targetId = request.getTargetMemberId();
        roomService.kick(roomId, memberId, targetId);
        sessionRegistry.unregister(sessionRegistry.getSessionId(targetId));
    }

}
