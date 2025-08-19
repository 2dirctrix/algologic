package com.ssafy.c204_be_api.ingame.service;

import com.ssafy.c204_be_api.ingame.domain.InGamePhase;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class PhaseTransitionResult<T> {
    private final InGamePhase from;
    private final InGamePhase to;
    private final T payload;
}