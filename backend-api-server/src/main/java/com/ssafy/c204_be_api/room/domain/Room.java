package com.ssafy.c204_be_api.room.domain;

import com.ssafy.c204_be_api.common.domain.ProgrammingLanguage;
import com.ssafy.c204_be_api.game.domain.GameType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Room {

//    private UUID roomId;

    private Participant host;

    private String roomName;

    private GameType gameType;

    private Integer maxSize;

    private ProgrammingLanguage programmingLanguage;

    private Integer timelimit;

}