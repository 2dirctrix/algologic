package com.ssafy.c204_be_api.problem.web.controller;

import com.ssafy.c204_be_api.authentication.dto.CustomUserDetails;
import com.ssafy.c204_be_api.common.domain.ProgrammingLanguage;
import com.ssafy.c204_be_api.common.domain.TestcaseCount;
import com.ssafy.c204_be_api.common.web.response.ApiResponse;
import com.ssafy.c204_be_api.problem.domain.ProblemLevel;
import com.ssafy.c204_be_api.problem.service.ProblemCategorySearchService;
import com.ssafy.c204_be_api.problem.service.ProblemSearchService;
import com.ssafy.c204_be_api.problem.service.ProblemSubmitService;
import com.ssafy.c204_be_api.problem.web.message.JudgeResultMessage;
import com.ssafy.c204_be_api.problem.web.request.ProblemCategorySearchCondition;
import com.ssafy.c204_be_api.problem.web.request.ProblemSubmitRequest;
import com.ssafy.c204_be_api.problem.web.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Tag(name = "알고리즘 문제 API")
@RequestMapping("/api/v1/problems")
@RequiredArgsConstructor
@RestController
public class ProblemController {

    private final ProblemSearchService problemSearchService;
    private final ProblemCategorySearchService problemCategorySearchService;
    private final ProblemSubmitService problemSubmitService;

    @GetMapping("/{problemId}")
    @Operation(summary = "알고리즘 문제 조회", description = "문제번호를 기반으로 알고리즘 문제를 조회합니다.")
    public ResponseEntity<ApiResponse<ProblemResponse>> searchProblem(
            @Parameter(description = "알고리즘 문제 번호", required = true)
            @PathVariable Long problemId
    ) {
        ProblemResponse response = problemSearchService.search(problemId);
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "문제 조회에 성공하였습니다.", response));
    }

    @GetMapping("/{problemId}/category")
    @Operation(summary = "알고리즘 문제 카테고리 조회", description = "문제번호에 해당하는 알고리즘 문제의 카테고리를 조회합니다.")
    public ResponseEntity<ApiResponse<SimpleProblemCategoryListResponse>> searchSimpleProblemCategory(
            @Parameter(description = "알고리즘 문제 번호", required = true)
            @PathVariable Long problemId
    ) {
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "문제 조회에 성공하였습니다.", getSimpleProblemCategoryResponse()));
    }

    @GetMapping("/category")
    @Operation(summary = "알고리즘 문제 카테고리 전체 조회", description = "알고리즘 문제의 모든 카테고리를 조회합니다.")
    public ResponseEntity<ApiResponse<ProblemCategoryListResponse>> searchProblemCategory(
        @ModelAttribute ProblemCategorySearchCondition searchCondition
    ) {
        ProblemCategoryListResponse response = ProblemCategoryListResponse.from(problemCategorySearchService.searchAll());
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "문제 카테고리 조회에 성공하였습니다.", response));
    }

    @PostMapping("/{problemId}/submit")
    @Operation(summary = "알고리즘 문제 제출", description = "알고리즘 문제를 푼 후 제출합니다.")
    public ResponseEntity<ApiResponse<TestcaseCountResponse>> submitProblem(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long problemId, @Validated @RequestBody ProblemSubmitRequest request) {
        problemSubmitService.submit(problemId, userDetails.getMember().getId(), request);

        TestcaseCountResponse response = TestcaseCountResponse.builder()
                .count(TestcaseCount.valueOf("T" + problemId).getCount())
                .build();
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "문제 제출에 성공하였습니다.", response));
    }

    @PostMapping("/judge-result/{memberId}")
    @Operation(summary = "채점 결과 전송 및 저장", description = "채점 서버가 호출하는 API")
    public ResponseEntity<ApiResponse<Void>> saveAndNotifyJudgeResult(
            @PathVariable Long memberId,
            @RequestBody JudgeResultMessage message) {
        problemSubmitService.saveAndNotifyJudgeResult(memberId, message);
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "채점 결과 전송에 성공하였습니다."));
    }

    @GetMapping("/submits/{submitId}")
    @Operation(summary = "제출 결과 단건 조회", description = "내가 제출한 문제의 채점 결과 중 특정 채점 결과의 세부사항을 확인합니다.")
    public ResponseEntity<ApiResponse<ProblemJudgementResponse>> getJudgementResult(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long submitId) {
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "제출 결과 조회에 성공하였습니다.", getProblemJudgementResponse()));
    }

    @GetMapping("/{problemId}/submits")
    @Operation(summary = "제출 결과 다건 조회", description = "내가 제출한 문제의 모든 채점 결과를 확인합니다.")
    public ResponseEntity<ApiResponse<Page<ProblemJudgementResponse>>> getAllJudgementResult(
        @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long problemId, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "제출 결과 조회에 성공하였습니다.", new PageImpl<>(getProblemJudgementListResponse())));
    }

    @GetMapping("/category/rank")
    @Operation(summary = "알고리즘 문제 카테고리 밴픽 랭킹 조회", description = "알고리즘 카테고리의 밴픽 랭킹을 확인합니다.")
    public ResponseEntity<ApiResponse<ProblemCategoryRankResponse>> searchProblemCategoryRank() {
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "알고리즘 문제 카테고리 밴픽 랭킹 조회에 성공하였습니다.",
                problemCategorySearchService.getTopPickedAndBanned()
        ));
    }

    private SimpleProblemCategoryListResponse getSimpleProblemCategoryResponse() {
        return SimpleProblemCategoryListResponse.builder()
                .category(List.of("BFS", "DFS", "GRAPH"))
                .build();
    }

    private ProblemJudgementResponse getProblemJudgementResponse() {
        return ProblemJudgementResponse.builder()
                .submitId(1L)
                .problem(SimpleProblemResponse.builder()
                        .id(1L)
                        .name("두 수의 합")
                        .level(ProblemLevel.EASY.getDescription())
                        .category(List.of("BFS", "DFS", "GRAPH"))
                        .build()
                ).programmingLanguage(ProgrammingLanguage.JAVA.getDisplayName())
                .runningTime(1232)
                .memory(32142)
                .isCorrect(true)
                .submittedAt(LocalDateTime.now())
                .build();
    }

    private List<ProblemJudgementResponse> getProblemJudgementListResponse() {
        return List.of(
                ProblemJudgementResponse.builder()
                        .submitId(1L)
                        .problem(SimpleProblemResponse.builder()
                                .id(1L)
                                .name("two-sum")
                                .level("쉬움")
                                .category(List.of("ARRAY", "HASH"))
                                .build())
                        .programmingLanguage(ProgrammingLanguage.JAVA.getDisplayName())
                        .runningTime(1232)
                        .memory(32142)
                        .isCorrect(true)
                        .submittedAt(LocalDateTime.parse("2025-07-22T14:30:00"))
                        .build(),

                    ProblemJudgementResponse.builder()
                        .submitId(2L)
                        .problem(SimpleProblemResponse.builder()
                            .id(1L)
                            .name("two-sum")
                            .level("쉬움")
                            .category(List.of("ARRAY", "HASH"))
                            .build())
                        .programmingLanguage(ProgrammingLanguage.JAVA.getDisplayName())
                        .runningTime(1232)
                        .memory(32142)
                        .isCorrect(true)
                        .submittedAt(LocalDateTime.parse("2025-07-22T14:30:00"))
                        .build(),

                    ProblemJudgementResponse.builder()
                        .submitId(3L)
                        .problem(SimpleProblemResponse.builder()
                            .id(1L)
                            .name("two-sum")
                            .level("쉬움")
                            .category(List.of("ARRAY", "HASH"))
                            .build())
                        .programmingLanguage(ProgrammingLanguage.JAVA.getDisplayName())
                        .runningTime(1232)
                        .memory(32142)
                        .isCorrect(true)
                        .submittedAt(LocalDateTime.parse("2025-07-22T14:30:00"))
                        .build()
        );
    }
}
