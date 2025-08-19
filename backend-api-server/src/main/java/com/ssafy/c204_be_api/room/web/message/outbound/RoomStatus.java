package com.ssafy.c204_be_api.room.web.message.outbound;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "방의 상태")
public enum RoomStatus {

    @Schema(description = "입장 가능 상태")
    WAITING("입장 가능"),

    @Schema(description = "방이 꽉 찬 상태")
    FULL("입장 불가"),

    @Schema(description = "게임중 상태")
    IN_GAME("게임중"),

    @Schema(description = "입장 비활성 상태")
    DISABLED("입장 비활성");

    private final String description;
}