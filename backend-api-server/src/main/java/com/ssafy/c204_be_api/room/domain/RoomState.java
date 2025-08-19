package com.ssafy.c204_be_api.room.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoomState {

    OPENED("입장 가능 상태"),

    FULL("입장 불가능 상태"),

    PENDED("참가자가 없는 상태"),

    STARTED("게임 시작됨"),

    CLOSED("게임 종료 또는 방 닫힘");

    private final String description;
}
