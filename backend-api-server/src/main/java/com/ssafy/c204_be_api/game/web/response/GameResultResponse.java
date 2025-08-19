package com.ssafy.c204_be_api.game.web.response;

import com.ssafy.c204_be_api.player.web.response.PlayerResultResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class GameResultResponse {
    private Long gameResultId;
    private String gameName;
    private Integer maxPlayers;
    private String problemName;
    private String problemLevel;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private List<PlayerResultResponse> playerResults;
}
