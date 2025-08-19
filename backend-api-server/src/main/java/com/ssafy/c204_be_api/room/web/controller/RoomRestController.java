package com.ssafy.c204_be_api.room.web.controller;

import com.ssafy.c204_be_api.authentication.dto.CustomUserDetails;
import com.ssafy.c204_be_api.common.domain.ProgrammingLanguage;
import com.ssafy.c204_be_api.common.web.response.ApiResponse;
import com.ssafy.c204_be_api.game.domain.GameType;
import com.ssafy.c204_be_api.ingame.message.outbound.InGameSnapshot;
import com.ssafy.c204_be_api.room.domain.Participant;
import com.ssafy.c204_be_api.room.service.RoomService;
import com.ssafy.c204_be_api.room.web.message.outbound.RoomSnapshot;
import com.ssafy.c204_be_api.room.web.message.outbound.RoomSummary;
import com.ssafy.c204_be_api.room.web.request.RoomCreateRequest;
import com.ssafy.c204_be_api.room.web.response.GameEnterResponse;
import com.ssafy.c204_be_api.room.web.response.RoomCreateResponse;
import com.ssafy.c204_be_api.room.web.response.RoomListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name = "게임 대기 방 API")
@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomRestController {
    private final RoomService roomService;


    @Operation(
            summary = "게임 대기 방 생성 API",
            description = "방 정보를 입력 받아 새로운 게임을 생성합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "게임 대기 방 생성 성공"
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
    public ResponseEntity<ApiResponse<RoomCreateResponse>> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
                               @Valid @RequestBody RoomCreateRequest request
    ) {
        Participant host = Participant.fromEntity(userDetails.getMember());

        RoomSnapshot snap = roomService.create(request.toEntity(host));
        RoomCreateResponse response = RoomCreateResponse.builder().roomId(snap.getRoomId()).build();
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "방을 생성했습니다.", response));
    }


    @Operation(
            summary = "게임 대기 방 목록 조회 API",
            description = "필터링을 통해 게임 대기 방 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "게임 대기 방 목록 조회 성공"
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
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<RoomListResponse>> list(
            @RequestParam(required = false) String roomName,
            @RequestParam(required = false) String hostNickname,
            @RequestParam(required = false) ProgrammingLanguage programmingLanguage,
            @RequestParam(required = false) GameType gameType,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "8") int size
    ) {
        List<RoomSummary> all = roomService.list();

        List<RoomSummary> filtered = all.stream()
                .filter(r -> roomName == null || r.getRoomName().contains(roomName))
                .filter(r -> hostNickname == null || r.getHostNickname().contains(hostNickname))
                .filter(r -> programmingLanguage == null || r.getProgrammingLanguage().equals(programmingLanguage))
                .filter(r -> gameType == null || r.getGameType().equals(gameType))
                .toList();

        int total = filtered.size();

        RoomListResponse response = RoomListResponse.builder()
                .roomList(filtered.stream()
                        .skip((long) offset * size)
                        .limit(size)
                        .collect(Collectors.toList()))
                .page(offset)
                .size(size)
                .totalCount(total)
                .build();

        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "방 목록을 조회했습니다.", response));
    }


    @Operation(
            summary = "게임 대기방 참가 예약 요청",
            description = "게임 대기방에 참가하기 위해 예약합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "게임 대기 방 예약 성공"
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
    @PostMapping("/{roomId}/join")
    public ResponseEntity<ApiResponse<RoomCreateResponse>> bookJoin(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID roomId
    ) {
        Participant participant = Participant.fromEntity(userDetails.getMember());

        roomService.bookJoin(roomId, participant);
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "방 참가 예약이 승인되었습니다."));
    }

    @Operation(
            summary = "게임 대기 방 퇴장 API",
            description = "대기 방에서 퇴장합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "게임 대기 방 퇴장 성공"
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
    @PostMapping("/{roomId}/leave")
    public ResponseEntity<ApiResponse<Void>> leave(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID roomId
    ) {
        Participant participant = Participant.fromEntity(userDetails.getMember());

        roomService.leave(roomId, participant.getMemberId());
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "방에서 퇴장했습니다."));
    }


    @Operation(
            summary = "게임 입장 API",
            description = "게임방 참가자들이 게임에 입장합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "게임 입장 성공"
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
    @PostMapping("/{roomId}/enter-game")
    public ResponseEntity<ApiResponse<?>> enter(@PathVariable UUID roomId,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long memberId = userDetails.getMember().getId();

        InGameSnapshot snap = roomService.enterGame(roomId, memberId);
        GameEnterResponse response = GameEnterResponse.builder().gameId(snap.getGameId()).build();
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "게임에 입장했습니다.", response));
    }

}
