package com.ssafy.c204_be_api.ingame.web.response;

import com.ssafy.c204_be_api.ingame.domain.Item;
import com.ssafy.c204_be_api.ingame.domain.PurchaseType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "아이템 구매 응답 객체")
public class PurchaseResponse {

    @Schema(description = "구매 후 남은 코인", example = "1200")
    private int remainingCoin;

    @Schema(description = "구매 타입 (ITEM or SPELL)", example = "ITEM")
    private PurchaseType purchasedType;

    @Schema(description = "구매 대상 PK (itemId 또는 spellId)", example = "1")
    private Long purchasedTargetId;

    @Schema(description = "누적 구매 수량", example = "2")
    private int totalPurchasedQuantity;

}
