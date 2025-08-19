package com.ssafy.c204_be_api.player.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class PlayerResultRequest {

    @NotNull
    @Schema(description = "플레이어 ID")
    private Long playerId;

    @NotNull
    @Schema(description = "획득한 점수")
    private Integer earnedScore;

    @NotNull
    @Schema(description = "획득한 코인")
    private Integer earnedCoin;

    @NotNull
    @Schema(description = "순위")
    private Integer ranks;

//    @NotNull
    @Schema(description = "밴된 문제 카테고리 ID")
    private Long bannedProblemCategoryId;

//    @NotNull
    @Schema(description = "선택된 문제 카테고리 ID")
    private Long pickedProblemCategoryId;

    @Schema(description = "문제 제출 ID")
    private Long submitId;

    @Schema(description = "풀이 시간")
    private Integer solveDuration;
}
