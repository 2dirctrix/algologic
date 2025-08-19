package com.ssafy.c204_be_api.problem.web.response;

import com.ssafy.c204_be_api.problem.domain.ProblemCategory;
import lombok.Builder;

import java.util.List;

@Builder
public record ProblemCategoryListResponse (List<ProblemCategoryResponse> category) {

    public static ProblemCategoryListResponse from(List<ProblemCategory> categories) {
        return ProblemCategoryListResponse.builder()
                .category(categories.stream()
                        .map(ProblemCategoryResponse::from)
                        .toList())
                .build();
    }

}
