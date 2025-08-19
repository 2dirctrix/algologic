package com.ssafy.c204_be_api.member.web.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GameDailyCountResponse {
    private List<DailyGameCountResponse> dailySolvedCounts;
}
