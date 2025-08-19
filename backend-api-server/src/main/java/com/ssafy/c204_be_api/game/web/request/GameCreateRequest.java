package com.ssafy.c204_be_api.game.web.request;

import com.ssafy.c204_be_api.common.domain.ProgrammingLanguage;
import com.ssafy.c204_be_api.game.domain.GameType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "게임 생성 요청 객체")
public class GameCreateRequest {

    @NotNull
    @Valid
    @Schema(description = "게임 설정 정보")
    private Game game;

    @NotNull
    @Size(min = 2)
    @Valid
    @Schema(description = "참여하는 유저 리스트 (최소 2명 이상)")
    private List<Player> players;

    @Getter
    @Schema(description = "게임 생성 세부 정보")
    private static class Game {

        @NotNull
        @Schema(description = "방 제목 (한글, 영문, 숫자 허용, 최대 30자)")
        private String gameName;

        @NotNull
        @Schema(description = "게임 유형: NORMAL | RANKED")
        private GameType gameType;

        @NotNull
        @Schema(description = "사용 언어: JAVA | CPP | PYTHON")
        private ProgrammingLanguage programmingLanguage;

        @Min(2)
        @Max(6)
        @NotNull
        @Schema(description = "최대 참가 인원 수 (2~6명)")
        private Integer maxPlayers;

        @Schema(description = "게임 제한 시간 (초 단위), 기본값은 3600")
        private Integer timeLimit;
    }

    @Getter
    @Schema(description = "참가자 정보")
    private static class Player {

        @NotNull
        @Schema(description = "게임에 참가한 회원의 고유 ID")
        private Long memberId;
    }
}
