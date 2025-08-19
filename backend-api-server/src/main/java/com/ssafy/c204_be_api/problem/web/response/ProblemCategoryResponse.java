package com.ssafy.c204_be_api.problem.web.response;

import com.ssafy.c204_be_api.problem.domain.ProblemCategory;
import lombok.Builder;

@Builder
public record ProblemCategoryResponse (Long categoryId, String category, Integer pickedCount, Integer bannedCount) {

    public static ProblemCategoryResponse from(ProblemCategory category) {
        return ProblemCategoryResponse.builder()
                .categoryId(category.getId())
                .category(category.getName())
                .pickedCount(category.getPickedCount())
                .bannedCount(category.getBannedCount())
                .build();
    }

}