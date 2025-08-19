package com.ssafy.c204_be_api.problem.web.message;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Builder
public record JudgeResultMessage(
        Long playerId,
        Long problemId,
        String programmingLanguage,
        Boolean isSolved,
        @Nullable Double maxRunningTime,
        @Nullable Integer maxMemoryUsage,
        LocalDateTime submittedAt,
        @Nullable List<TestcaseResult> testcaseResults,
        @Nullable Failure failure
) {
    public JudgeResultMessage {
        if (Objects.isNull(testcaseResults)) {
            Objects.requireNonNull(failure);
        }
    }
}
record TestcaseResult(
        Integer testcaseNumber,
        boolean isSolved, //문제를 맞추었는지 여부
        Double runningTime,
        Integer memoryUsage,
        Failure failure //테스트케이스 실행이 실패한 원인
) {
}

@Builder
record Failure (
        String cause
) {
}
