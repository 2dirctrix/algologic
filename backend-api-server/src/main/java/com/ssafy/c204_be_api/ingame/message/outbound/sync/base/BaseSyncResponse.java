package com.ssafy.c204_be_api.ingame.message.outbound.sync.base;

import com.ssafy.c204_be_api.ingame.message.outbound.sync.SyncResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 모든 Phase SyncResponse 의 공통 필드를 정의하는 추상 클래스입니다.
 * P 는 BasePlayerSync 의 자손 타입입니다.
 */
@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class BaseSyncResponse<P extends BasePlayerSync> implements SyncResponse {

    private final String phase;

    private final long currentUnix;

    private final String currentIso;

    private final long startUnix;

    private final String startIso;

    private final long deadlineUnix;

    private final String deadlineIso;

    private final P me;

    private final List<P> players;

    private final int version;

}
