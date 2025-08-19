package com.ssafy.c204_be_api.room.web.message.constant;

import java.util.UUID;

public class RoomTopic {

    private static final String BASE = "/topic/rooms";
    private static final String USER = "/queue/rooms";
    private static final String REFRESH = "/refresh";

    /**
     * @return 방 전체 prefix 경로 (/topic/rooms)
     */
    public static String prefix() {
        return BASE;
    }

    /**
     * @param roomId 대상 방 UUID
     * @return 해당 방의 전용 topic 경로 (/topic/rooms/{roomId})
     */
    public static String of(UUID roomId) {
        return BASE + "/" + roomId;
    }

    public static String ofUser(UUID roomId) {
        return USER + "/" + roomId;
    }

    public static String refresh() {
        return BASE + REFRESH;
    }
}