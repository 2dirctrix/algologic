package com.ssafy.c204_be_api.room.web.message.outbound;

import com.ssafy.c204_be_api.common.domain.ProgrammingLanguage;
import com.ssafy.c204_be_api.game.domain.GameType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@Schema(description = "게임방 목록 응답 객체")
public class RoomSummary {

    @Schema(description = "게임방의 고유 ID")
    private UUID roomId;

    @Schema(description = "게임방의 방장 닉네임")
    private String hostNickname;

    @Schema(description = "게임방 이름")
    private String roomName;

    @Schema(description = "게임방의 게임 유형")
    private GameType gameType;

    @Schema(description = "게임방의 프로그래밍 언어")
    private ProgrammingLanguage programmingLanguage;

    @Schema(description = "게임방의 참가자 수")
    private int size;

    @Schema(description = "게임방의 최대 수용 인원 수")
    private int maxSize;

    @Schema(description = "게임방의 게임 제한 시간")
    private int timelimit;

    @Schema(description = "게임방의 상태")
    private RoomStatus status;

}
