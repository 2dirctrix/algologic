package com.ssafy.c204_be_api.ingame.message.outbound.finished;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class BanFinishedPayload {

    private final List<BanChoice> banList;
    private final Long bannedProblemCategoryId;
    private final int totalChoiceCount;

}
