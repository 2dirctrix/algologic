package com.ssafy.c204_be_api.ingame.message.outbound;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InGamePlayerSnapshot {

    Long memberId;

    String nickname;

    String profileImageUrl;

    boolean connected;
}
