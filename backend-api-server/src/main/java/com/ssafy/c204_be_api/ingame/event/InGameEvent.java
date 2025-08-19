package com.ssafy.c204_be_api.ingame.event;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum InGameEvent {

    PHASE_STARTED,
    SYNC,

    BAN_FINISHED,
    BAN_CHOSEN,

    PICK_FINISHED,
    PICK_CHOSEN,

    PURCHASED,
    PURCHASE_FINISHED,

    BATTLE_FINISHED,

    ITEM_SENT,
    ITEM_RECEIVED,
    SPELL_ACTIVATED,

    SURRENDERED,

}
