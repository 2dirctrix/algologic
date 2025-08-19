package com.ssafy.c204_be_api.member.web.controller;

import com.ssafy.c204_be_api.authentication.dto.CustomUserDetails;
import com.ssafy.c204_be_api.aws.service.S3Service;
import com.ssafy.c204_be_api.common.web.response.ApiResponse;
import com.ssafy.c204_be_api.game.service.GameResultService;
import com.ssafy.c204_be_api.game.web.response.GameResultResponse;
import com.ssafy.c204_be_api.member.service.MemberService;
import com.ssafy.c204_be_api.member.web.request.MemberUpdateRequest;
import com.ssafy.c204_be_api.member.web.response.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final S3Service s3Service;
    private final GameResultService gameResultService;

    @GetMapping("/me")
    @Operation(summary = "회원 정보 조회", description = "현재 로그인된 사용자의 정보 반환")
    public ResponseEntity<ApiResponse<MemberResponse>> findMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MemberResponse myInfoResponse = memberService.findById(userDetails.getMember().getId());
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "회원 조회에 성공하였습니다.", myInfoResponse));
    }

    @GetMapping("/me/detail")
    @Operation(summary = "회원 상세 정보 조회", description = "현재 로그인된 사용자의 상세 정보 반환")
    public ResponseEntity<ApiResponse<MemberDetailResponse>> findMyDetail(@AuthenticationPrincipal CustomUserDetails userDetails) {
        MemberDetailResponse myInfoResponse = memberService.getMyDetail(userDetails.getMember().getId());
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "회원 상세 조회에 성공하였습니다.", myInfoResponse));
    }

    @GetMapping()
    @Operation(summary = "다건 회원 정보 조회", description = "전체 사용자 정보 반환")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> findAllMembers() {
        List<MemberResponse> memberResponseList = memberService.findAll();
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "전체 회원 조회에 성공하였습니다.", memberResponseList));
    }

    @GetMapping("/rankers")
    @Operation(summary = "상위 랭커 정보 조회", description = "상위 랭커 정보 반환")
    public ResponseEntity<ApiResponse<List<RankerResponse>>> findTopMembers() {
        List<RankerResponse> topMembers = memberService.findTopMembers();
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "상위 회원 10명 조회에 성공하였습니다.", topMembers));
    }

    @PutMapping("/me")
    @Operation(summary = "회원 정보 수정", description = "현재 로그인된 사용자의 정보를 수정")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                    @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
                                                                    @RequestPart(value = "request") @Valid MemberUpdateRequest request) {
        if (memberService.isNicknameDuplicate(request.getNickname()) && !userDetails.getMember().getNickname().equals(request.getNickname())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.with(HttpStatus.BAD_REQUEST, "이미 사용 중인 닉네임입니다."));
        }

        String uploadUrl = uploadImageIfPresent(profileImage);
        memberService.updateProfile(request, uploadUrl, userDetails.getMember().getId());

        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "회원 정보 수정 성공하였습니다."));
    }

    @GetMapping("/me/game-results")
    @Operation(summary = "회원 게임 결과 조회", description = "현재 로그인된 사용자의 게임 결과 조회")
    public ResponseEntity<ApiResponse<List<GameResultResponse>>> getMyGameResults(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<GameResultResponse> gameResults = gameResultService.getGameResultsByMemberId(userDetails.getMember().getId());
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "회원 게임 결과 조회에 성공하였습니다.", gameResults));
    }

    @GetMapping("/me/games/daily")
    @Operation(summary = "회원의 데일리 게임 카운트 조회", description = "현재 로그인된 사용자의 데일리로 게임한 횟수 조회")
    public ResponseEntity<ApiResponse<List<DailyGameCountResponse>>> getMonthlySolvedProblems(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<DailyGameCountResponse> monthlySolvedProblems = memberService.getMyDailyGameCount(userDetails.getMember().getId());

        if (userDetails.getMember().getId() == 11) {
            monthlySolvedProblems = getDummyDataWhenIdEqual11();
        }

        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "회원의 데일리 게임 카운트 조회에 성공하였습니다.", monthlySolvedProblems));
    }

    private static List<DailyGameCountResponse> getDummyDataWhenIdEqual11() {
        LocalDate start = LocalDate.of(2025, 7, 1);
        LocalDate end = LocalDate.of(2025, 8, 18);

        return Stream.iterate(start, date -> !date.isAfter(end), date -> date.plusDays(1))
                .map(date -> DailyGameCountResponse.builder()
                        .date(date.toString())
                        .gameCount(ThreadLocalRandom.current().nextInt(1, 6))
                        .build())
                .toList();
    }

    @GetMapping("/me/solved-problems/categories/top")
    @Operation(summary = "회원이 많이 푼 알고리즘 유형 조회", description = "현재 로그인된 사용자의 많이 푼 알고리즘 유형 조회")
    public ResponseEntity<ApiResponse<List<TopCategoryResponse>>> getMyTopCategories(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<TopCategoryResponse> topCategoryList = memberService.getMyTopSolvedCategories(userDetails.getMember().getId());
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "회원이 많이 푼 알고리즘 유형 조회에 성공하였습니다.", topCategoryList));
    }


    private String uploadImageIfPresent(MultipartFile image) {
        String uploadUrl = null;
        if (Objects.nonNull(image)) {
            uploadUrl = s3Service.uploadImage(image);
        }
        return uploadUrl;
    }
}
