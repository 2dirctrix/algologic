package com.ssafy.c204_be_api.ingame.message.outbound.sync.inner;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivatedItem {
    private UUID activationId;
    private long itemId;
    private long from;
    private int duration;  // 초 단위
    private long activatedUnix;
    private String activatedIso;
    private long expiresUnix;
    private String expiresIso;
}
