package com.ssafy.c204_be_api.room.service;

import com.ssafy.c204_be_api.room.web.message.outbound.RoomSnapshot;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomStatusChange {
    boolean changed;
    RoomSnapshot snapshot;

    public static RoomStatusChange of(boolean changed, RoomSnapshot snapshot) {
        return new RoomStatusChange(changed, snapshot);
    }
}
