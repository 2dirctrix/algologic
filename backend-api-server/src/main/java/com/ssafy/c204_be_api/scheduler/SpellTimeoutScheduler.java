package com.ssafy.c204_be_api.scheduler;

import com.ssafy.c204_be_api.ingame.service.InGameStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SpellTimeoutScheduler {
    private final InGameStore store;

    @Scheduled(fixedRateString = "${ingame.timeout-check-interval-ms:1000}")
    public void checkSpellTimeout() {
        Set<UUID> roomIds = store.getActiveRoomIds();
        for (UUID roomId : roomIds) {
            try {

            } catch (Throwable t) {
                log.error("[SpellTimeout] roomId={} 처리 중 오류", roomId, t);
            }
        }
    }
}
