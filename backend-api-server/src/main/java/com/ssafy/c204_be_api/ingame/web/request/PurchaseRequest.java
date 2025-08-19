package com.ssafy.c204_be_api.ingame.web.request;

import com.ssafy.c204_be_api.ingame.command.PurchaseCommand;
import com.ssafy.c204_be_api.ingame.domain.PurchaseType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "상품 구매 요청 객체")
public class PurchaseRequest {

    @Schema(description = "구매할 상품의 타입")
    @NotNull
    private PurchaseType purchaseType;

    @Schema(description = "구매할 상품의 아이디")
    @NotNull
    private Long purchaseTargetId;

    @Schema(description = "구매할 스펠 수량")
    @Min(1)
    private int quantity = 1;

}
