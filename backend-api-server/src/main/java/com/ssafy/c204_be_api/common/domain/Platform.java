package com.ssafy.c204_be_api.common.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Platform {
    KAKAO("카카오"),
    GOOGLE("구글");

    private final String description;
}
