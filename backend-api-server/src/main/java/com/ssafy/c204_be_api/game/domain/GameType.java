package com.ssafy.c204_be_api.game.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameType {
    NORMAL("일반전"),
    RANKED("랭크전");

    private final String description;
}
