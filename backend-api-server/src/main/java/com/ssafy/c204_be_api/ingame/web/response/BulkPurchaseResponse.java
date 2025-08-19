package com.ssafy.c204_be_api.ingame.web.response;

import com.ssafy.c204_be_api.ingame.message.outbound.sync.inner.PurchasedItem;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.inner.PurchasedSpell;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "배치 상품 구매 응답")
public class BulkPurchaseResponse {

    @Schema(description = "구매 처리 후 남은 코인")
    private final int remainingCoin;

    @Schema(description = "구매한 아이템 목록")
    private final List<PurchasedItem> purchasedItem;

    @Schema(description = "구매한 스펠 목록")
    private final List<PurchasedSpell> purchasedSpells;
}
