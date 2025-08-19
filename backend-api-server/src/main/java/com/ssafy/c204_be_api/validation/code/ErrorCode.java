package com.ssafy.c204_be_api.validation.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ===== COMMON (40xxx) =====
    INTERNAL_ERROR(40000, "일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
    UNAUTHORIZED(40001, "인증이 필요합니다."),
    FORBIDDEN(40002, "권한이 없습니다."),
    BAD_REQUEST(40003, "잘못된 요청입니다."),

    // ===== ROOM (41xxx) =====
    ROOM_NOT_FOUND(41001, "방을 찾을 수 없습니다."),
    NOT_ROOM_HOST(41002, "방장만 수행할 수 있는 작업입니다."),
    INVALID_ACTION(41003, "허용되지 않는 작업니다."),

    DUPLICATE_SESSION(41004, "이미 연결중인 세션이 있습니다."),
    DUPLICATE_JOIN(41005, "이미 참가중인 방이 있습니다."),

    ROOM_CLOSED(41006, "이미 종료(폐쇄)된 방입니다."),
    ROOM_NOT_OPEN(41007, "방이 아직 오픈되지 않았습니다."),
    ROOM_ALREADY_STARTED(41008, "이미 게임이 시작되었습니다."),

    ROOM_FULL(41009, "방 인원이 가득 찼습니다."),
    ROOM_NOT_FULL(41010, "정원이 가득 차지 않았습니다."),
    ROOM_NOT_ALL_READY(41011, "모든 참가자가 준비되지 않았습니다."),
    ROOM_NOT_JOINED(41012, "방에 참가하지 않은 사용자입니다."),
    ROOM_STATE_NULL(41013, "유효하지 않은 방 상태입니다."),
    ROOM_KICKED(41014, "강퇴당한 방입니다."),


    // ===== IN-GAME (42xxx) =====
    INGAME_SESSION_ALREADY_EXISTS(42001, "게임 세션이 이미 존재합니다."),
    INGAME_PHASE_NOT_STARTED(42002, "해당 단계가 아직 시작되지 않았습니다."),
    INVALID_GAME_STATE(42003, "현재 게임 상태에서는 허용되지 않습니다."),
    INGAME_WRONG_PHASE(42004, "잘못된 단계에서 호출되었습니다."),

    INGAME_SESSION_NOT_FOUND(42005, "게임 세션을 찾을 수 없습니다."),
    INGAME_MEMBER_NOT_FOUND(42006, "회원 정보를 찾을 수 없습니다."),
    INGAME_PLAYER_NOT_FOUND(42007, "플레이어를 찾을 수 없습니다."),
    ITEM_NOT_FOUND(42008, "아이템을 찾을 수 없습니다."),
    SPELL_NOT_FOUND(42009, "스펠을 찾을 수 없습니다."),

    CATEGORY_BANNED(42010, "밴된 알고리즘 유형입니다."),
    PICK_ALREADY_DONE(42011, "이미 픽 선택을 완료했습니다."),
    BAN_ALREADY_DONE(42012, "이미 밴 선택을 완료했습니다."),

    PURCHASE_TYPE_UNSUPPORTED(42013, "지원하지 않는 구매 유형입니다."),
    PURCHASE_ALREADY_PROCESSED(42014, "이미 처리된 구매 요청입니다."),
    INVALID_PURCHASE_QUANTITY(42015, "구매 수량이 올바르지 않습니다."),
    INSUFFICIENT_COIN(42016, "코인이 부족합니다."),

    ITEM_NOT_OWNED(42017, "보유하지 않은 아이템입니다."),
    INVALID_ITEM_USE(42018, "해당 아이템은 지금 사용할 수 없습니다."),
    ITEM_NOT_PURCHASED(42019, "구매하지 않은 아이템입니다."),
    ITEM_OUT_OF_STOCK(42020, "사용 가능한 아이템 수량이 없습니다."),
    SPELL_NOT_PURCHASED(42021, "구매하지 않은 스펠입니다."),
    SPELL_OUT_OF_STOCK(42022, "사용 가능한 스펠 수량이 없습니다.");

    private final int code;
    private final String defaultMessage;
}
