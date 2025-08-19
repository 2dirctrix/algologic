package com.ssafy.c204_be_api.ingame.command;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class BulkPurchaseCommand {

    private final Long memberId;

    private final UUID roomId;

    private final List<Purchase> purchases;

}