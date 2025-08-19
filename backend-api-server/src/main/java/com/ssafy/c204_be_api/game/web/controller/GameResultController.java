package com.ssafy.c204_be_api.game.web.controller;

import com.ssafy.c204_be_api.common.web.response.ApiResponse;
import com.ssafy.c204_be_api.game.service.GameResultService;
import com.ssafy.c204_be_api.game.web.request.GameResultCreateRequest;
import com.ssafy.c204_be_api.game.web.response.GameResultCreateResponse;
import com.ssafy.c204_be_api.game.web.response.GameResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/game-results")
@RequiredArgsConstructor
public class GameResultController {
    private final GameResultService gameResultService;

    @Operation(
            summary = "게임 생성 API",
            description = "게임 결과를 입력 받아 저장합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "게임 결과 저장 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "요청 유효성 검사 실패",
                    content = @Content(
//                mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<GameResultCreateResponse>> createGameResult(
            @Valid @RequestBody GameResultCreateRequest request
    ) {
        Long gameResultId = gameResultService.saveGameResult(request);

        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "게임 결과가 저장되었습니다.",
                GameResultCreateResponse.builder()
                        .gameResultId(gameResultId)
                        .build()));
    }


    @Operation(
            summary = "게임 결과 조회 API",
            description = "게임 결과 ID를 받아 게임 결과를 반환합니다."
    )
    @GetMapping("/{gameResultId}")
    public ResponseEntity<ApiResponse<GameResultResponse>> getGameResult(
            @PathVariable Long gameResultId
    ) {
        GameResultResponse gameResult = gameResultService.getGameResult(gameResultId);

        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "게임 결과 조회에 성공했습니다.",
                gameResult));
    }
}
