package com.ssafy.c204_be_api.member.web.response;

import com.ssafy.c204_be_api.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "회원 상세 정보 응답 객체")
public class MemberDetailResponse {
    @Schema(description = "소셜 로그인 제공자명")
    private String provider;

    @Schema(description = "생성 일자")
    private LocalDateTime createdAt;

    @Schema(description = "전체 게임 수")
    private Integer totalGameCount;

    @Schema(description = "전체 승리 수")
    private Long totalWinCount;

    @Schema(description = "승률")
    private Double winRate;

    @Schema(description = "평균 순위")
    private Double avgRanking;

    @Schema(description = "문제 해결률")
    private Double problemSolvedRate;

}
