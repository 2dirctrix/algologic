package com.ssafy.c204_be_api.room.service;

import com.ssafy.c204_be_api.common.web.event.Event;
import com.ssafy.c204_be_api.common.web.message.ApiMessage;
import com.ssafy.c204_be_api.game.domain.Game;
import com.ssafy.c204_be_api.game.service.GameService;
import com.ssafy.c204_be_api.ingame.domain.InGamePlayer;
import com.ssafy.c204_be_api.ingame.message.outbound.InGameSnapshot;
import com.ssafy.c204_be_api.ingame.service.InGameService;
import com.ssafy.c204_be_api.player.domain.Player;
import com.ssafy.c204_be_api.player.service.PlayerService;
import com.ssafy.c204_be_api.room.domain.Participant;
import com.ssafy.c204_be_api.room.domain.Room;
import com.ssafy.c204_be_api.room.repository.RoomRegistry;
import com.ssafy.c204_be_api.room.web.event.RoomEvent;
import com.ssafy.c204_be_api.room.web.message.constant.RoomTopic;
import com.ssafy.c204_be_api.room.web.message.outbound.ParticipantSnapshot;
import com.ssafy.c204_be_api.room.web.message.outbound.RoomSnapshot;
import com.ssafy.c204_be_api.room.web.message.outbound.RoomSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomStore store;
    private final SimpMessagingTemplate messaging;
    private final GameService gameService;
    private final InGameService inGameService;
    private final PlayerService playerService;
    private final RoomRegistry roomRegistry;

    public RoomSnapshot create(Room room) {
        RoomSnapshot snap = store.create(room);
        return snap;
    }

    public List<RoomSummary> list() {
        return store.list();
    }

    public void bookJoin(UUID roomId, Participant participant) {
        Long memberId = participant.getMemberId();

        roomRegistry.validDuplicate(memberId, roomId);  // 중복 입장 검증
        RoomSnapshot snap = sendRoomListRefresh(store.bookJoin(roomId, participant));
        roomRegistry.register(roomId, memberId);  // 입장 정보 저장
    }

    public RoomSnapshot join(UUID roomId, Participant participant) {
        RoomSnapshot snap = sendRoomListRefresh(store.join(roomId, participant));

        messaging.convertAndSend(RoomTopic.of(roomId),
                ApiMessage.event(HttpStatus.OK.value(),
                        "방에 새로운 참가자가 있습니다.",
                        Event.of(RoomEvent.JOINED, snap)));
        return snap;
    }

    public RoomSnapshot ready(UUID roomId, Long memberId, boolean ready) {
        RoomSnapshot snap = store.setReady(roomId, memberId, ready);
        messaging.convertAndSend(RoomTopic.of(roomId),
                ApiMessage.event(HttpStatus.OK.value(),
                        "방 참가자의 준비상태가 변경되었습니다.",
                        Event.of(RoomEvent.READY_CHANGED,snap)));
        return snap;
    }

    public RoomSnapshot leave(UUID roomId, Long memberId) {
        RoomSnapshot snap = sendRoomListRefresh(store.leave(roomId, memberId));
        roomRegistry.unregister(memberId);  // 입장 정보 제거

        messaging.convertAndSend(RoomTopic.of(roomId),
                ApiMessage.event(HttpStatus.OK.value(),
                        "방을 떠난 참가자가 있습니다.",
                        Event.of(RoomEvent.LEFT, snap)));
        return snap;
    }

    public RoomSnapshot kick(UUID roomId, Long memberId, Long targetId) {
        RoomSnapshot snap = sendRoomListRefresh(store.kick(roomId, memberId, targetId));
        roomRegistry.unregister(targetId);  // 입장 정보 제거

        sendToUser(targetId, RoomTopic.ofUser(roomId), "방에서 강제 퇴장 되었습니다.",
                RoomEvent.KICKED, null);

        messaging.convertAndSend(RoomTopic.of(roomId),
                ApiMessage.event(HttpStatus.OK.value(),
                        "방을 떠난 참가자가 있습니다.",
                        Event.of(RoomEvent.LEFT, snap)));
        return snap;
    }


    public InGameSnapshot enterGame(UUID roomId, Long memberId) {
        RoomSnapshot snap = sendRoomListRefresh(store.enterGame(roomId, memberId));

        List<Long> playerIds = snap.getParticipants().stream()
                .map(ParticipantSnapshot::getMemberId).toList();

        Game game = gameService.createGame(snap.toEntity());
        List<Player> players = playerService.createPlayers(game.getId(), playerIds);
        List<InGamePlayer> inGamePlayers = players.stream().map(InGamePlayer::fromEntity).toList();

        InGameSnapshot inGameSnap = inGameService.enterGame(roomId, game.getId(), game.getGameType(), game.getTimeLimit(), inGamePlayers);

        // TODO. 메시지 페이로드 변경 보류
        messaging.convertAndSend(RoomTopic.of(roomId),
                ApiMessage.event(HttpStatus.OK.value(),
                        "게임에 입장했습니다.",
                        Event.of(RoomEvent.ENTERED, inGameSnap)));

        return inGameSnap;
    }

    public RoomSnapshot disconnect(UUID roomId, Long memberId) {
        RoomSnapshot snap = store.disconnect(roomId, memberId);

        messaging.convertAndSend(RoomTopic.of(roomId),
                ApiMessage.event(HttpStatus.OK.value(),
                        "방 참가자의 연결상태가 변경되었습니다.",
                        Event.of(RoomEvent.DISCONNECTED, snap)));
        return snap;
    }

    public RoomSnapshot cleanupDisconnected(Duration timeout) {
        if (store instanceof InMemoryRoomStore InMemoryStore) {
            for (UUID roomId : InMemoryStore.getAllRoomIds()) {
                for (Long memberId : InMemoryStore.getParticipantIds(roomId)) {
                    boolean removed = InMemoryStore.removeIfDisconnectedAndTimedOut(roomId, memberId, timeout);
                    if (removed) {
                        leave(roomId, memberId); // 이벤트 발행 등 후처리
                    }
                }
            }
        }
        return null;
    }

    /***** HELPER *****/

    /**
     * 해당 플레이어에게 personalized된 SyncResponse 전송
     */
    private void sendToUser(Long memberId, String topic, String message, RoomEvent event, Object payload) {
        messaging.convertAndSendToUser(
                String.valueOf(memberId),
                topic,
                ApiMessage.event(
                        HttpStatus.OK.value(),
                        message,
                        Event.of(event, payload)
                )
        );
    }

    private RoomSnapshot sendRoomListRefresh(RoomStatusChange change) {
        if (change.changed) {
            messaging.convertAndSend(RoomTopic.refresh(),
                    ApiMessage.event(HttpStatus.OK.value(),
                            "방 목록이 변경되었습니다.",
                            Event.of(RoomEvent.REFRESHED, null)));
        }
        return change.snapshot;
    }

}