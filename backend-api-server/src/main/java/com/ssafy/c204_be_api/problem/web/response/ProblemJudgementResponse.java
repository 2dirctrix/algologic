package com.ssafy.c204_be_api.problem.web.response;

import com.ssafy.c204_be_api.common.domain.ProgrammingLanguage;
import com.ssafy.c204_be_api.validation.exception.ClientException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProblemJudgementResponse (
    @Min(1)
    @NotNull
    Long submitId,

    SimpleProblemResponse problem,

    @NotBlank
    String programmingLanguage,

    @Min(0)
    @NotNull
    Integer runningTime,

    @Min(0)
    @NotNull
    Integer memory,

    @NotNull
    Boolean isCorrect,

    @NotNull
    LocalDateTime submittedAt
) {
    public ProblemJudgementResponse {
        if (!ProgrammingLanguage.existsByDisplayName(programmingLanguage)) {
            throw new ClientException("programmingLanguage = %s 는 유효하지 않은 입력값 입니다.".formatted(programmingLanguage));
        }
    }
}
