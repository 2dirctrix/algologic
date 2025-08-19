package com.ssafy.c204_be_api.ingame.message.constant;

import java.util.UUID;

public class InGameTopic {

    private static final String BASE = "/topic/ingame";
    private static final String USER = "/queue/ingame";
    private static final String CHOICE = "/choice";
    private static final String BATTLE = "/battle";

    /**
     * @return 방 전체 prefix 경로 (/topic/ingame)
     */
    public static String prefix() {
        return BASE;
    }

    /**
     * @param roomId 대상 방 UUID
     * @return 해당 방의 전용 topic 경로 (/topic/ingame/{roomId})
     */
    public static String of(UUID roomId) {
        return BASE + "/" + roomId;
    }

    public static String choice(UUID roomId) {
        return BASE + "/" + roomId + CHOICE;
    }

    public static String userChoice(UUID roomId) {
        return USER + "/" + roomId + CHOICE;
    }

    public static String battle(UUID roomId) {
        return BASE + "/" + roomId + BATTLE;
    }

    public static String userBattle(UUID roomId) {
        return USER + "/" + roomId + BATTLE;
    }

}
