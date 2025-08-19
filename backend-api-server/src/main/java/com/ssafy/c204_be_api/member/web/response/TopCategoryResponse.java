package com.ssafy.c204_be_api.member.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopCategoryResponse {
    private Long problemCategoryId;
    private String problemCategoryName;
    private Integer solvedCount;
}
