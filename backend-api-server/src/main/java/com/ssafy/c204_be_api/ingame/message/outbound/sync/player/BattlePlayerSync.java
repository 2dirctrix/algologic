package com.ssafy.c204_be_api.ingame.message.outbound.sync.player;

import com.ssafy.c204_be_api.ingame.message.outbound.sync.inner.ActivatedItem;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.inner.ActivatedSpell;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.inner.PurchasedItem;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.inner.PurchasedSpell;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.base.BasePlayerSync;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class BattlePlayerSync extends BasePlayerSync {

    private final Boolean surrender;

    private final Long playerId;    // 문제 채점 요청을 위한 필드

    private final List<PurchasedItem> purchasedItems;

    private final List<PurchasedSpell> purchasedSpells;

    private final List<ActivatedItem> activatedItems;

    private final List<ActivatedSpell> activatedSpells;

}
