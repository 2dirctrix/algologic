package com.ssafy.c204_be_api.ingame.message.outbound.sync.base;

import com.ssafy.c204_be_api.ingame.message.outbound.sync.PlayerSync;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * 모든 Phase별 PlayerSync DTO가 상속하는 공통 필드 정의 클래스입니다.
 */
@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class BasePlayerSync implements PlayerSync {

    private final long memberId;

}