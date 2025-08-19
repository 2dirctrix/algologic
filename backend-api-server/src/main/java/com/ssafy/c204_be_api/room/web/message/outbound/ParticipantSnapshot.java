package com.ssafy.c204_be_api.room.web.message.outbound;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "게임방 참가자 목록 응답 객체")
public class ParticipantSnapshot {

    @Schema(description = "회원의 고유 ID")
    private Long memberId;

    @Schema(description = "회원의 닉네임")
    private String nickname;

    @Schema(description = "회원의 티어 점수")
    private Integer score;

    @Schema(description = "회원의 프로필 이미지 URL")
    private String profileImageUrl;

    @Schema(description = "회원의 준비 상태")
    private boolean ready;

    @Schema(description = "회원의 접속 상태")
    private boolean connected;

}