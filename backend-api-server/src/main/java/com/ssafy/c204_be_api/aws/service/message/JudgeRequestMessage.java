package com.ssafy.c204_be_api.aws.service.message;

import com.ssafy.c204_be_api.common.domain.ProgrammingLanguage;
import com.ssafy.c204_be_api.validation.exception.ClientException;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Objects;

@Builder
public record JudgeRequestMessage(
        Long playerId,
        Long memberId,
        Long problemId,
        String programmingLanguage,
        String sourceCode,
        Double timeLimit,
        Integer memoryLimit,
        LocalDateTime submittedAt
) {

    public JudgeRequestMessage {
        if (!ProgrammingLanguage.existsByDisplayName(programmingLanguage)) {
            throw new ClientException("programmingLanguage = %s 는 유효하지 않은 입력값 입니다.".formatted(programmingLanguage));
        }

        Objects.requireNonNull(submittedAt);
    }

}
