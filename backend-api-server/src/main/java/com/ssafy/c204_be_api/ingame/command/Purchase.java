package com.ssafy.c204_be_api.ingame.command;

import com.ssafy.c204_be_api.ingame.domain.PurchaseType;
import com.ssafy.c204_be_api.ingame.web.request.PurchaseRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Purchase {

    private final PurchaseType purchaseType;

    private final Long purchaseTargetId;

    private final int quantity;

    public static Purchase of(PurchaseRequest request) {
        return Purchase.builder()
                .purchaseType(request.getPurchaseType())
                .purchaseTargetId(request.getPurchaseTargetId())
                .quantity(request.getQuantity())
                .build();
    }
}
