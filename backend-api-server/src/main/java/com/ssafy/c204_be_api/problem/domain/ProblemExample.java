package com.ssafy.c204_be_api.problem.domain;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class ProblemExample {
    private String input;
    private String output;
}
