package com.ssafy.c204_be_api.ingame.message.outbound.sync.response;

import com.ssafy.c204_be_api.ingame.message.outbound.sync.base.BaseSyncResponse;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.player.PurchasePlayerSync;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class PurchaseSyncResponse extends BaseSyncResponse<PurchasePlayerSync> {
}
