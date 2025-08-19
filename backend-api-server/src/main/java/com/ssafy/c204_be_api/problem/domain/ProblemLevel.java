package com.ssafy.c204_be_api.problem.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProblemLevel {
    EASY("쉬움"),
    MEDIUM("중간"),
    HARD("어려움");

    private final String description;
}
