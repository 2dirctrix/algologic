package com.ssafy.c204_be_api.ingame.web.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "배치 상품 구매 요청")
public class BulkPurchaseRequest {

    @Valid
//    @Size(min = 1, message = "최소 1개 이상의 구매 항목이 필요합니다.")
    @ArraySchema(arraySchema = @Schema(description = "구매 항목 목록"))
    private List<PurchaseRequest> purchases;

}
