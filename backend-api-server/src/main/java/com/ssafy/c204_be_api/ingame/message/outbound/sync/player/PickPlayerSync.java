package com.ssafy.c204_be_api.ingame.message.outbound.sync.player;

import com.ssafy.c204_be_api.ingame.message.outbound.sync.base.BasePlayerSync;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


@Getter
@SuperBuilder
public class PickPlayerSync extends BasePlayerSync {

    private final Long pickedCategoryId;

    private final boolean chosen;

}
