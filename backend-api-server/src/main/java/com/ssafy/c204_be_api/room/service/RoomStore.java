package com.ssafy.c204_be_api.room.service;

import com.ssafy.c204_be_api.room.domain.Participant;
import com.ssafy.c204_be_api.room.domain.Room;
import com.ssafy.c204_be_api.room.web.message.outbound.RoomSnapshot;
import com.ssafy.c204_be_api.room.web.message.outbound.RoomSummary;

import java.util.List;
import java.util.UUID;

public interface RoomStore {

    RoomSnapshot create(Room room);

    List<RoomSummary> list();

    RoomSnapshot get(UUID roomId);

    RoomStatusChange bookJoin(UUID roomId, Participant member);

    RoomStatusChange join(UUID roomId, Participant member);

    RoomStatusChange leave(UUID roomId, Long memberId);

    RoomStatusChange kick(UUID roomId, Long memberId, Long  targetId);

    void delete(UUID roomId);

    RoomSnapshot setReady(UUID roomId, Long memberId, boolean ready);

    RoomStatusChange enterGame(UUID roomId, Long memberId);

    RoomSnapshot disconnect(UUID roomId, Long memberId);

    void markClosed(UUID roomId);

    void exitGame(UUID roomId);

}