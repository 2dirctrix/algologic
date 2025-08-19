package com.ssafy.c204_be_api.scheduler;

import com.ssafy.c204_be_api.common.web.event.Event;
import com.ssafy.c204_be_api.common.web.message.ApiMessage;
import com.ssafy.c204_be_api.ingame.event.InGameEvent;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.SyncResponse;
import com.ssafy.c204_be_api.ingame.service.InGameStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

/**
 * 10초마다(기본값) 각 방의 모든 플레이어에게 "개인화된" SYNC 스냅샷을 푸시
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InGameSyncScheduler {

    private final InGameStore store;
    private final SimpMessagingTemplate messaging;

    @Scheduled(fixedRateString = "${ingame.sync-interval-ms:10000}")
    public void pushPersonalizedSync() {
        Set<UUID> roomIds = store.getActiveRoomIds();
        for (UUID roomId : roomIds) {
            try {
                // 각 멤버별 개인화된 스냅샷 전송
                for (Long memberId : store.getMemberIds(roomId)) {
                    try {
                        SyncResponse personalized = store.sync(roomId, memberId);
                        messaging.convertAndSendToUser(
                                String.valueOf(memberId),
                                "/queue/ingame/" + roomId + "/sync",
                                ApiMessage.sync(
                                        HttpStatus.OK.value(),
                                        "게임의 현재 상태를 전달합니다.",
                                        Event.of(InGameEvent.SYNC, personalized)
                                )
                        );
                    } catch (Throwable perUserEx) {
                        log.warn("[InGameSyncScheduler] roomId={} memberId={} 전송 실패", roomId, memberId, perUserEx);
                    }
                }
            } catch (Throwable roomEx) {
                log.warn("[InGameSyncScheduler] roomId={} 처리 실패", roomId, roomEx);
            }
        }
    }
}
