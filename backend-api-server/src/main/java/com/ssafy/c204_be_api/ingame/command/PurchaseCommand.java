package com.ssafy.c204_be_api.ingame.command;

import com.ssafy.c204_be_api.ingame.domain.PurchaseHistory;
import com.ssafy.c204_be_api.ingame.domain.PurchaseType;
import com.ssafy.c204_be_api.ingame.web.request.PurchaseRequest;
import com.ssafy.c204_be_api.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class PurchaseCommand {

    private final Long memberId;

    private final UUID roomId;

    private final PurchaseType purchaseType;

    private final Long purchaseTargetId;

    private final int quantity;

    private final String idempotencyKey;

    public static PurchaseCommand of(PurchaseRequest request, Long memberId, UUID roomId, String idempotencyKey) {
        return PurchaseCommand.builder()
                .memberId(memberId)
                .roomId(roomId)
                .idempotencyKey(idempotencyKey)
                .purchaseType(request.getPurchaseType())
                .purchaseTargetId(request.getPurchaseTargetId())
                .quantity(request.getQuantity())
                .build();
    }

    public PurchaseHistory toHistory(Member member, Long playerId) {
        return PurchaseHistory.builder()
                .member(member)
                .playerId(playerId)
                .purchaseType(purchaseType)
                .purchaseTargetId(purchaseTargetId)
                .idempotencyKey(idempotencyKey)
                .quantity(quantity)
                .build();
    }

}
