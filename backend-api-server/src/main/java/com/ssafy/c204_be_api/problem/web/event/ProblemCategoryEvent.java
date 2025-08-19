package com.ssafy.c204_be_api.problem.web.event;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum ProblemCategoryEvent {
    PROBLEM_CATEGORY_RANK_UPDATED,
}
