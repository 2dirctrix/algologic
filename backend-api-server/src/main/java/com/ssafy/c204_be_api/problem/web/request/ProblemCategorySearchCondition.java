package com.ssafy.c204_be_api.problem.web.request;

import com.ssafy.c204_be_api.common.web.request.SortDirection;
import jakarta.annotation.Nullable;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

@Builder
public record ProblemCategorySearchCondition (
    @Nullable String sort,
    @Nullable String order
) {
    public ProblemCategorySearchCondition {
        if (StringUtils.isNoneBlank(sort) && !SortDirection.existsByDirection(order)) {
            order = "asc";
        }
    }
}
