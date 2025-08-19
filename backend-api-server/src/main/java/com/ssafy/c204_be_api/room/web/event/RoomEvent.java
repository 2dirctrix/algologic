package com.ssafy.c204_be_api.room.web.event;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum RoomEvent{
    JOINED,
    READY_CHANGED,
    LEFT,
    DISCONNECTED,
    ENTERED,
    CLOSED,
    REFRESHED,
    KICKED,
}