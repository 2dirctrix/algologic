package com.ssafy.c204_be_api.ingame.message.outbound.sync.inner;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchasedItem {
    private long itemId;
    private int quantity;
    private Integer remainingCount;
    private Integer usedCount;
}