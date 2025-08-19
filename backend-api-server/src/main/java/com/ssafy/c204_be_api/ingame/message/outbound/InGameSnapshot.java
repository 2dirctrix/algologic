package com.ssafy.c204_be_api.ingame.message.outbound;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class InGameSnapshot {
    Long gameId;

    List<InGamePlayerSnapshot> players;
}
