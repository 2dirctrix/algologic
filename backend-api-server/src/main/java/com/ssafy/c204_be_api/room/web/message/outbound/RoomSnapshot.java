package com.ssafy.c204_be_api.room.web.message.outbound;

import com.ssafy.c204_be_api.common.domain.ProgrammingLanguage;
import com.ssafy.c204_be_api.game.domain.Game;
import com.ssafy.c204_be_api.game.domain.GameType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "게임방 목록 응답 객체")
public class RoomSnapshot {

    @Schema(description = "방의 고유 ID")
    private UUID roomId;

    @Schema(description = "방의 이름")
    private String roomName;

    @Schema(description = "방장의 닉네임")
    private String hostNickname;

    @Schema(description = "방의 게임 유형")
    private GameType gameType;

    @Schema(description = "방의 게임 프로그래밍 언어")
    private ProgrammingLanguage programmingLanguage;

    @Schema(description = "방의 현재 인원 수")
    private int size;

    @Schema(description = "방의 최대 인원 수")
    private int maxSize;

    @Schema(description = "방의 게임 제한 시간")
    private int timelimit;

    @Schema(description = "방의 현재 상태")
    private RoomStatus status;

    @Schema(description = "참가자 목록")
    private List<ParticipantSnapshot> participants;

    @Schema(description = "방 상태의 버전 (이벤트 동기화용)")
    private Long version;

    public Game toEntity() {
        return Game.builder()
                .name(roomName)
                .maxPlayers(maxSize)
                .gameType(gameType)
                .programmingLanguage(programmingLanguage)
                .timeLimit(timelimit)
                .build();
    }

}