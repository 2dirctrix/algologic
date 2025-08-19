package com.ssafy.c204_be_api.ingame.web.controller;

import com.ssafy.c204_be_api.authentication.util.PrincipalUtils;
import com.ssafy.c204_be_api.common.web.event.Event;
import com.ssafy.c204_be_api.common.web.message.ApiMessage;
import com.ssafy.c204_be_api.ingame.event.InGameEvent;
import com.ssafy.c204_be_api.ingame.message.inbound.BanChoiceRequest;
import com.ssafy.c204_be_api.ingame.message.inbound.PickChoiceRequest;
import com.ssafy.c204_be_api.ingame.message.inbound.UseItemRequest;
import com.ssafy.c204_be_api.ingame.message.inbound.UseSpellRequest;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.SyncResponse;
import com.ssafy.c204_be_api.ingame.service.InGameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Slf4j
@Controller
@MessageMapping("/v1/ingame")
@RequiredArgsConstructor
public class InGameWsController {

    private final InGameService inGameService;

    /** 알고리즘 벤 선택 */
    @MessageMapping("/{roomId}/ban-choice")
    public void banChoice(
            @DestinationVariable UUID roomId,
            @Valid @Payload BanChoiceRequest request,
            Principal principal
    ) {
        long memberId = PrincipalUtils.extractMember(principal).getId();
        inGameService.banChoice(roomId, memberId, request.getProblemCategoryId());
    }

    /** 알고리즘 픽 선택 */
    @MessageMapping("/{roomId}/pick-choice")
    public void pickChoice(
            @DestinationVariable UUID roomId,
            @Valid @Payload PickChoiceRequest request,
            Principal principal
    ) {
        long memberId = PrincipalUtils.extractMember(principal).getId();
        inGameService.pickChoice(roomId, memberId, request.getProblemCategoryId());
    }

    /** 아이템 사용 */
    @MessageMapping("/{roomId}/item-use")
    public void useItem(
            @DestinationVariable UUID roomId,
            @Valid @Payload UseItemRequest request,
            Principal principal
    ) {
        long memberId = PrincipalUtils.extractMember(principal).getId();
        inGameService.useItem(roomId, memberId, request.getItemId(), request.getTargetMemberId());
    }

    /** 스펠 사용 */
    @MessageMapping("/{roomId}/spell-use")
    public void useSpell(
            @DestinationVariable UUID roomId,
            @Valid @Payload UseSpellRequest request,
            Principal principal
    ) {
        long memberId = PrincipalUtils.extractMember(principal).getId();
        inGameService.useSpell(roomId, memberId, request.getSpellId());
    }

    /** 항복 요청 */
    @MessageMapping("/{roomId}/surrender")
    public void surrender(
            @DestinationVariable UUID roomId,
            Principal principal
    ) {
        long memberId = PrincipalUtils.extractMember(principal).getId();
        inGameService.surrender(roomId, memberId);
    }

    /** 상태 동기화 요청 */
    @MessageMapping("/{roomId}/sync")
    @SendToUser("/queue/ingame/{roomId}/sync")
    public ApiMessage<Event> sync(
            @DestinationVariable UUID roomId,
            Principal principal
    ) {
        long memberId = PrincipalUtils.extractMember(principal).getId();
        SyncResponse sync = inGameService.sync(roomId, memberId);
        return ApiMessage.response(
                HttpStatus.OK.value(),
                "게임의 현재 상태를 전달합니다.",
                Event.of(InGameEvent.SYNC, sync)
        );
    }

}
