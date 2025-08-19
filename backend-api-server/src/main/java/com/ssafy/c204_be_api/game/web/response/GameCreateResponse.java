package com.ssafy.c204_be_api.game.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "게임 생성 응답 객체")
public class GameCreateResponse {
    @Schema(description = "게임의 고유 ID")
    private Long gameId;
}