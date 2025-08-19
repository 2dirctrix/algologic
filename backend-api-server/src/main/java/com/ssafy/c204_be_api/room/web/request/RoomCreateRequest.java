package com.ssafy.c204_be_api.room.web.request;

import com.ssafy.c204_be_api.common.domain.ProgrammingLanguage;
import com.ssafy.c204_be_api.game.domain.GameType;
import com.ssafy.c204_be_api.room.domain.Participant;
import com.ssafy.c204_be_api.room.domain.Room;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomCreateRequest {

    @NotNull
    private String roomName;

    @Min(2)
    @Max(6)
    @NotNull
    private Integer maxSize;

    @NotNull
    private GameType gameType;

    @NotNull
    private ProgrammingLanguage programmingLanguage;

    @Min(0)
    @Max(14400)  // 4시간
    private Integer timelimit;

    public Room toEntity(Participant host) {
        return Room.builder()
                .host(host)
                .roomName(roomName)
                .maxSize(maxSize)
                .gameType(gameType)
                .programmingLanguage(programmingLanguage)
                .timelimit(timelimit != null ? timelimit : 3600)
                .build();
    }
}
