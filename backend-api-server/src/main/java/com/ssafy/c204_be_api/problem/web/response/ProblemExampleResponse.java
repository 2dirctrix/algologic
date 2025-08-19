package com.ssafy.c204_be_api.problem.web.response;

import com.ssafy.c204_be_api.problem.domain.ProblemExample;
import lombok.Builder;

@Builder
public record ProblemExampleResponse (String input, String output) {

    public static ProblemExampleResponse from(ProblemExample example) {
        return ProblemExampleResponse.builder()
                .input(example.getInput())
                .output(example.getOutput())
                .build();
    }

}
