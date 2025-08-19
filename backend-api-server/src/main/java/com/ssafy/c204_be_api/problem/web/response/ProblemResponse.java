package com.ssafy.c204_be_api.problem.web.response;

import com.ssafy.c204_be_api.problem.domain.Problem;
import com.ssafy.c204_be_api.problem.domain.ProblemCategory;
import lombok.Builder;

import java.util.List;

@Builder
public record ProblemResponse (
        Long id,
        String name,
        List<String> category,
        String level,
        String description,
        String constraint,
        List<ProblemExampleResponse> examples,
        Double timeLimit,
        Integer memoryLimit
) {

    public static ProblemResponse of(Problem problem, List<ProblemCategory> categories) {
        return ProblemResponse.builder()
                .id(problem.getId())
                .name(problem.getName())
                .category(categories.stream().map(ProblemCategory::getName).toList())
                .level(problem.getLevel().getDescription())
                .description(problem.getDescription())
                .constraint(problem.getConstraint())
                .examples(problem.getExample().stream()
                        .map(ProblemExampleResponse::from)
                        .toList())
                .timeLimit(problem.getTimeLimitSec())
                .memoryLimit(problem.getMemoryLimitKb())
                .build();
    }

}
