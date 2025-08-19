package com.ssafy.c204_be_api.ingame.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PurchaseType {
    ITEM("아이템"),
    SPELL("스펠");

    private final String description;
}
