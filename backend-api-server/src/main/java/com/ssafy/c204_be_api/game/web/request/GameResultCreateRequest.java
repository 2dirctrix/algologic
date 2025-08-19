package com.ssafy.c204_be_api.game.web.request;

import com.ssafy.c204_be_api.player.web.request.PlayerResultRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "게임 결과 저장 요청 객체")
public class GameResultCreateRequest {

    @NotNull
    @Schema(description = "게임 ID")
    private Long gameId;

    @NotNull
    @Schema(description = "문제 ID")
    private Long problemId;

    @NotNull
    @Schema(description = "게임 시작 시간")
    private LocalDateTime startedAt;

    @NotNull
    @Schema(description = "게임 종료 시간")
    private LocalDateTime finishedAt;

    @NotNull
    @Size(min = 2)
    @Valid
    @Schema(description = "참가자 개별 결과 목록 (최소 2개 이상)")
    private List<PlayerResultRequest> playerResults;
}
