package com.ssafy.c204_be_api.ingame.message.outbound.finished;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BanChoice {
    private final Long problemCategoryId;
    private final Integer choiceCount;
}
