package com.ssafy.c204_be_api.room.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GameEnterResponse {

    @Schema(description = "게임의 고유 ID")
    private Long gameId;

}