package com.ssafy.c204_be_api.scheduler;

import com.ssafy.c204_be_api.common.web.event.Event;
import com.ssafy.c204_be_api.common.web.message.ApiMessage;
import com.ssafy.c204_be_api.ingame.domain.InGamePhase;
import com.ssafy.c204_be_api.ingame.event.InGameEvent;
import com.ssafy.c204_be_api.ingame.message.constant.InGameTopic;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.SyncResponse;
import com.ssafy.c204_be_api.ingame.service.InGameStore;
import com.ssafy.c204_be_api.ingame.service.PhaseTransitionResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PhaseTimeoutScheduler {

    private final InGameStore store;
    private final SimpMessagingTemplate messaging;

    /**
     * - 스케줄러는 "전환이 필요한지"만 묻고, 실제 전환/룰렛 계산은 store 가 처리
     * - 전환이 있었다면(=Optional present) finished -> started 순서로 이벤트 발행
     */
    @Scheduled(fixedRateString = "${ingame.timeout-check-interval-ms:1000}")
    public void checkAndAdvancePhases() {
        Set<UUID> roomIds = store.getActiveRoomIds(); // 인터페이스에 노출 권장
        for (UUID roomId : roomIds) {
            try {
                store.advancePhaseIfTimeout(roomId).ifPresent(tr -> {
                    publishPhaseFinished(roomId, tr);
                    if (tr.getTo() != InGamePhase.FINISHED) {
                        publishPhaseStarted(roomId, tr.getTo());
                    }
                });
            } catch (Throwable t) {
                log.error("[PhaseTimeout] roomId={} 처리 중 오류", roomId, t);
            }
        }
    }

    /** 이전 Phase 에 대한 FINISHED 이벤트 발행 */
    private void publishPhaseFinished(UUID roomId, PhaseTransitionResult tr) {
        InGamePhase from = tr.getFrom();
        switch (from) {
            case BAN_CHOICE -> {
                messaging.convertAndSend(
                        InGameTopic.choice(roomId),
                        ApiMessage.event(
                                HttpStatus.OK.value(), "벤 단계가 종료되었습니다.",
                                Event.of(InGameEvent.BAN_FINISHED, tr.getPayload())
                        )
                );
            }
            case PICK_CHOICE -> {
                messaging.convertAndSend(
                        InGameTopic.choice(roomId),
                        ApiMessage.event(
                                HttpStatus.OK.value(), "픽 단계가 종료되었습니다.",
                                Event.of(InGameEvent.PICK_FINISHED, tr.getPayload())
                        )
                );
            }
            case PURCHASE -> {
                messaging.convertAndSend(
                        InGameTopic.choice(roomId),
                        ApiMessage.event(
                                HttpStatus.OK.value(), "구매 단계가 종료되었습니다.",
                                Event.of(InGameEvent.PURCHASE_FINISHED, tr.getPayload())
                        )
                );
            }
            case BATTLE -> {
                messaging.convertAndSend(
                        InGameTopic.battle(roomId),
                        ApiMessage.event(
                                HttpStatus.OK.value(), "배틀 단계가 종료되었습니다.",
                                Event.of(InGameEvent.BATTLE_FINISHED, tr.getPayload())
                        )
                );
            }
        }
    }

    /** 다음 Phase 시작(STARTED) 이벤트 **/
    private void publishPhaseStarted(UUID roomId, InGamePhase next) {
        // 각 플레이어에게 personalized된 SyncResponse 전송
        for (Long memberId : store.getMemberIds(roomId)) {
            try {
                SyncResponse sync = store.sync(roomId, memberId);

                String topic = InGameTopic.userChoice(roomId);
                if (next == InGamePhase.BATTLE) {
                    topic = InGameTopic.userBattle(roomId);
                }

                messaging.convertAndSendToUser(
                        String.valueOf(memberId),
                        topic,
                        ApiMessage.event(
                                HttpStatus.OK.value(),
                                next.name() + " 단계가 시작되었습니다.",
                                Event.of(InGameEvent.PHASE_STARTED, sync)
                        )
                );
            } catch (Throwable ex) {
                log.warn("[publishPhaseStarted] memberId={} 에게 전송 실패", memberId, ex);
            }
        }
    }

}
