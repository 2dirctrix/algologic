package com.ssafy.c204_be_api.player.web.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlayerResultResponse {

    private Long playerResultId;
    private Long memberId;
    private String nickname;
    private String profileImageUrl;
    private Integer score;
    private Integer rank;
    private Integer earnedScore;
    private Integer earnedCoin;
    private String bannedProblemCategoryName;
    private String pickedProblemCategoryName;
    private Integer solveDuration;  // sec 단위
    private Integer runningTime;     // ms 단위
    private Integer memory;         // kb 단위
}
