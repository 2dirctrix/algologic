package com.ssafy.c204_be_api.ingame.message.outbound.sync.player;

import com.ssafy.c204_be_api.ingame.message.outbound.sync.inner.PurchasedItem;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.inner.PurchasedSpell;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.base.BasePlayerSync;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class PurchasePlayerSync extends BasePlayerSync {

    private final List<PurchasedItem> purchasedItems;

    private final List<PurchasedSpell> purchasedSpells;

    private boolean purchased;

}
