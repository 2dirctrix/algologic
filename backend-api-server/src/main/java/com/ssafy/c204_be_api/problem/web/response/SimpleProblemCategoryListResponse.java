package com.ssafy.c204_be_api.problem.web.response;

import lombok.Builder;

import java.util.List;

@Builder
public record SimpleProblemCategoryListResponse (List<String> category) {
}
