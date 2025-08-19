package com.ssafy.c204_be_api.problem.web.response;

import lombok.Builder;

@Builder
public record TestcaseCountResponse (
        int count
) {
}
