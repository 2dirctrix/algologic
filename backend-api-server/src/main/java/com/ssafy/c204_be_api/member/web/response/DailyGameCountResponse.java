package com.ssafy.c204_be_api.member.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DailyGameCountResponse {
    private String date;
    private Integer gameCount;
}
