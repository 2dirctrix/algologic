package com.ssafy.c204_be_api.scheduler;

import com.ssafy.c204_be_api.config.property.RoomCleanupProperties;
import com.ssafy.c204_be_api.room.service.InMemoryRoomStore;
import com.ssafy.c204_be_api.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RoomCleanupScheduler {

    private final InMemoryRoomStore roomStore;
    private final RoomCleanupProperties cleanupProperties;
    private final RoomService roomService;

    @Scheduled(fixedRateString = "${room.cleanup.interval-ms}")
    public void cleanupDisconnectedPlayers() {
        Duration timeout = Duration.ofSeconds(cleanupProperties.getDisconnectTimeoutSec());
        roomService.cleanupDisconnected(timeout);
    }
}