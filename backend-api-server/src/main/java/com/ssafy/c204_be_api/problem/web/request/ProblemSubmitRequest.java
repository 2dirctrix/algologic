package com.ssafy.c204_be_api.problem.web.request;

import com.ssafy.c204_be_api.common.domain.ProgrammingLanguage;
import com.ssafy.c204_be_api.validation.exception.ClientException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProblemSubmitRequest(
        @NotNull
        Long playerId,

        @NotBlank
        String programmingLanguage,

        @NotBlank
        String sourceCode
) {
    public ProblemSubmitRequest {
        if (!ProgrammingLanguage.existsByDisplayName(programmingLanguage)) {
            throw new ClientException("programmingLanguage = %s 는 유효하지 않은 입력값 입니다.".formatted(programmingLanguage));
        }
    }
}
