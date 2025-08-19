package com.ssafy.c204_be_api.ingame.web.controller;

import com.ssafy.c204_be_api.authentication.dto.CustomUserDetails;
import com.ssafy.c204_be_api.common.web.response.ApiResponse;
import com.ssafy.c204_be_api.ingame.command.BulkPurchaseCommand;
import com.ssafy.c204_be_api.ingame.command.Purchase;
import com.ssafy.c204_be_api.ingame.command.PurchaseCommand;
import com.ssafy.c204_be_api.ingame.domain.Item;
import com.ssafy.c204_be_api.ingame.domain.Spell;
import com.ssafy.c204_be_api.ingame.message.inbound.BanChoiceRequest;
import com.ssafy.c204_be_api.ingame.message.inbound.PickChoiceRequest;
import com.ssafy.c204_be_api.ingame.service.InGameService;
import com.ssafy.c204_be_api.ingame.service.InGameShopService;
import com.ssafy.c204_be_api.ingame.web.request.BulkPurchaseRequest;
import com.ssafy.c204_be_api.ingame.web.request.PurchaseRequest;
import com.ssafy.c204_be_api.ingame.web.response.BulkPurchaseResponse;
import com.ssafy.c204_be_api.ingame.web.response.ItemListResponse;
import com.ssafy.c204_be_api.ingame.web.response.PurchaseResponse;
import com.ssafy.c204_be_api.ingame.web.response.SpellListResponse;
import com.ssafy.c204_be_api.member.domain.Member;
import com.ssafy.c204_be_api.websocket.session.SessionRegistry;
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

@RestController
@Tag(name = "인 게임 API", description = "인게임 관련 API")
@RequestMapping("/api/v1/ingame")
@RequiredArgsConstructor
public class InGameRestController {

    private final InGameShopService inGameShopService;
    private final InGameService inGameService;
    private final SessionRegistry sessionRegistry;

    @Operation(
            summary = "벤 선택",
            description = "플레이어가 지정한 문제 카테고리를 벤(Ban)합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "벤 선택 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "벤 선택 요청 실패",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @PostMapping("/ban")
    public ResponseEntity<ApiResponse<Void>> choiceBan(
            @Valid @RequestBody BanChoiceRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Member member = customUserDetails.getMember();
        UUID roomId = getRoomId(member.getId());

        inGameService.banChoice(roomId, member.getId(), request.getProblemCategoryId());
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "벤 선택이 완료되었습니다."));
    }

    @Operation(
            summary = "픽 선택",
            description = "플레이어가 지정한 문제 카테고리를 픽(Pick)합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "픽 선택 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "픽 선택 요청 실패",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @PostMapping("/pick")
    public ResponseEntity<ApiResponse<Void>> choicePick(
            @Valid @RequestBody PickChoiceRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Member member = customUserDetails.getMember();
        UUID roomId = getRoomId(member.getId());

        inGameService.pickChoice(roomId, member.getId(), request.getProblemCategoryId());
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "픽 선택이 완료되었습니다."));
    }


    @Operation(
            summary = "아이템 목록 조회",
            description = "게임에서 사용 가능한 모든 아이템의 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "아이템 목록 조회 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @GetMapping("/items")
    public ResponseEntity<ApiResponse<ItemListResponse>> getItemList() {
        List<Item> items = inGameShopService.getAllItems();
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "아이템 목록을 조회했습니다.",
                ItemListResponse.fromEntity(items)));
    }

    @Operation(
            summary = "스펠 목록 조회",
            description = "게임에서 사용 가능한 모든 스펠의 목록을 조회합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "스펠 목록 조회 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @GetMapping("/spells")
    public ResponseEntity<ApiResponse<SpellListResponse>> getSpellList() {
        List<Spell> spells = inGameShopService.getAllSpells();
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "스펠 목록을 조회했습니다.",
                SpellListResponse.fromEntity(spells)));
    }

    @Operation(
            summary = "물품 구매",
            description = "선택한 물품을 구매합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "구매 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "구매 요청 실패",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    })
    @PostMapping("/purchase")
    public ResponseEntity<ApiResponse<PurchaseResponse>> purchase(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @Valid @RequestBody PurchaseRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Member member = customUserDetails.getMember();
        UUID roomId = getRoomId(member.getId());
        PurchaseCommand command = PurchaseCommand.of(request, member.getId(), roomId, idempotencyKey);

        PurchaseResponse response = inGameShopService.purchase(command);
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "구매가 완료되었습니다.", response));
    }

    @Operation(
            summary = "물품 일괄 구매",
            description = "여러 물품/스펠을 한 번에 구매합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "구매 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "구매 요청 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    @PostMapping("/purchase/bulk")
    public ResponseEntity<ApiResponse<BulkPurchaseResponse>> bulkPurchase(
            @Valid @RequestBody BulkPurchaseRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        Member member = customUserDetails.getMember();
        UUID roomId = getRoomId(member.getId());

        BulkPurchaseCommand command = BulkPurchaseCommand.builder()
                .memberId(member.getId())
                .roomId(roomId)
                .purchases(
                        request.getPurchases().stream()
                                .map(Purchase::of)
                                .collect(Collectors.toList())
                )
                .build();

        BulkPurchaseResponse response = inGameShopService.bulkPurchase(command);
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "일괄 구매가 완료되었습니다.", response));
    }


    /***** HELPER *****/

    private UUID getRoomId(Long memberId) {
        String sessionId =  sessionRegistry.getSessionId(memberId);
        if (sessionId == null) {
            throw new IllegalStateException("세션에 연결되지 않았습니다.");
        }
        UUID roomId = sessionRegistry.getRoomId(sessionId);
        if (roomId == null) {
            throw new IllegalStateException("게임방에 입장하지 않았습니다.");
        }
        return roomId;
    }
}
