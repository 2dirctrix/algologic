package com.ssafy.c204_be_api.game.web.controller;

import com.ssafy.c204_be_api.common.web.response.ApiResponse;
import com.ssafy.c204_be_api.game.web.request.GameCreateRequest;
import com.ssafy.c204_be_api.game.web.response.GameCreateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {

    @Operation(
        summary = "게임 생성 API",
        description = "게임 정보를 입력 받아 새로운 게임을 생성합니다."
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "게임 생성 성공"
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
    public ResponseEntity<ApiResponse<GameCreateResponse>> createGame(
        @Valid @RequestBody GameCreateRequest request
    ) {
        Long gameId = 1L;
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "게임이 생성되었습니다.",
            GameCreateResponse.builder().gameId(gameId).build()));
    }
}