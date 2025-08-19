package com.ssafy.c204_be_api.room.service;

import com.ssafy.c204_be_api.common.domain.ProgrammingLanguage;
import com.ssafy.c204_be_api.common.web.message.Meta;
import com.ssafy.c204_be_api.game.domain.GameType;
import com.ssafy.c204_be_api.room.domain.Participant;
import com.ssafy.c204_be_api.room.domain.Room;
import com.ssafy.c204_be_api.room.domain.RoomState;
import com.ssafy.c204_be_api.room.web.message.outbound.ParticipantSnapshot;
import com.ssafy.c204_be_api.room.web.message.outbound.RoomSnapshot;
import com.ssafy.c204_be_api.room.web.message.outbound.RoomStatus;
import com.ssafy.c204_be_api.room.web.message.outbound.RoomSummary;
import com.ssafy.c204_be_api.validation.code.ErrorCode;
import com.ssafy.c204_be_api.validation.exception.RoomException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class InMemoryRoomStore implements RoomStore {

    @Builder
    @ToString
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class RoomInternal {
        final UUID roomId;
        String roomName;
        Host host;
        int maxSize;
        GameType gameType;
        ProgrammingLanguage programmingLanguage;
        Integer timelimit;

        @Builder.Default
        volatile RoomState state = RoomState.PENDED;
        @Builder.Default
        final Map<Long, Participant> participants = new LinkedHashMap<>(); // join 순서 유지
        @Builder.Default
        Set<Long> kickedParticipants = new LinkedHashSet<>();  // 강퇴 목록

        @Builder.Default
        final Instant createdAt = Instant.now();
        @Builder.Default
        final ReentrantLock lock = new ReentrantLock();
        @Builder.Default
        final AtomicLong version = new AtomicLong(1);

        boolean removeParticipant(Long memberId) {
            if (state == RoomState.STARTED) {
                throw new RoomException(
                        ErrorCode.ROOM_ALREADY_STARTED, "Game is started",
                        null, Meta.of("roomId", roomId.toString(), "state", state.name())
                );
            }

            participants.remove(memberId);

            if (state == RoomState.FULL) {
                state = RoomState.OPENED;
                return true;
            }
            return false;
        }

        static RoomInternal of(Room room, UUID roomId) {
            return RoomInternal.builder()
                    .roomId(roomId)
                    .roomName(room.getRoomName())
                    .host(Host.of(room.getHost()))
                    .gameType(room.getGameType())
                    .maxSize(room.getMaxSize())
                    .programmingLanguage(room.getProgrammingLanguage())
                    .timelimit(room.getTimelimit())
                    .build();
        }

        RoomSnapshot toSnapshot() {
            List<ParticipantSnapshot> participantSnapshots = participants.values().stream()
                    .map(Participant::toSnapshot)
                    .toList();
            return RoomSnapshot.builder()
                    .roomId(roomId)
                    .hostNickname(host.getNickname())
                    .roomName(roomName)
                    .size(participants.size())
                    .maxSize(maxSize)
                    .gameType(gameType)
                    .programmingLanguage(programmingLanguage)
                    .timelimit(timelimit)
                    .status(status(state))
                    .participants(participantSnapshots)
                    .version(version.get())
                    .build();
        }

        RoomSummary toSummary() {
            return RoomSummary.builder()
                    .roomId(roomId)
                    .hostNickname(host.getNickname())
                    .roomName(roomName)
                    .size(participants.size())
                    .maxSize(maxSize)
                    .gameType(gameType)
                    .programmingLanguage(programmingLanguage)
                    .timelimit(timelimit)
                    .status(status(state))
                    .build();
        }

        @Getter
        @ToString
        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        private static final class Host {
            Long memberId;
            String nickname;

            static Host of(Participant participant) {
                return new Host(participant.getMemberId(), participant.getNickname());
            }
        }
    }

    private final ConcurrentHashMap<UUID, RoomInternal> rooms = new ConcurrentHashMap<>();

    @Override
    public RoomSnapshot create(Room room) {
        UUID id = UUID.randomUUID();
        RoomInternal r = RoomInternal.of(room, id);
        Participant host = room.getHost();
        r.participants.put(host.getMemberId(), host);

        rooms.put(id, r);

        log.info("Room created: {}", r);
        return snap(r);
    }

    @Override public List<RoomSummary> list() {
        log.info("Room List: {}", rooms);
        return rooms.values().stream()
                .filter(r -> status(r.state) != RoomStatus.DISABLED)
                .sorted(Comparator.comparing((RoomInternal r) -> r.createdAt).reversed())
                .map(RoomInternal::toSummary)
                .toList();
    }

    @Override public RoomSnapshot get(UUID roomId) {
        return snap(req(roomId));
    }

    @Override
    public RoomStatusChange bookJoin(UUID roomId, Participant member) {
        RoomStatusChange roomStatusChange = join(roomId, member);

        RoomInternal r = req(roomId); r.lock.lock();
        try {
            Long memberId = member.getMemberId();
            member.disconnect();
            r.participants.put(memberId, member);
            r.version.incrementAndGet();

            log.info("memberId: {} join booked roomId: {}", memberId, roomId);
            return roomStatusChange;
        } finally { r.lock.unlock(); }
    }

    @Override
    public RoomStatusChange join(UUID roomId, Participant member) {
        RoomInternal r = req(roomId); r.lock.lock();
        try {
            boolean changed = false;
            Long memberId = member.getMemberId();

            if (r.kickedParticipants.contains(memberId)) {
                throw new RoomException(
                        ErrorCode.ROOM_KICKED, "You have been kicked from this room",
                        null, Meta.of("roomId", roomId.toString())
                );
            }
            if (!r.participants.containsKey(memberId)) {
                switch (r.state) {
                    case FULL -> throw new RoomException(
                            ErrorCode.ROOM_FULL, "Room is full",
                            null,
                            Meta.of("roomId", roomId.toString(), "size", r.participants.size(), "maxSize", r.maxSize, "state", r.state.name())
                    );
                    case PENDED -> throw new RoomException(
                            ErrorCode.ROOM_NOT_OPEN, "Room is pended",
                            null, Meta.of("roomId", roomId.toString(), "state", r.state.name())
                    );
                    case STARTED -> throw new RoomException(
                            ErrorCode.ROOM_ALREADY_STARTED, "Game is started",
                            null, Meta.of("roomId", roomId.toString(), "state", r.state.name())
                    );
                    case CLOSED -> throw new RoomException(
                            ErrorCode.ROOM_CLOSED, "Room is closed",
                            null, Meta.of("roomId", roomId.toString(), "state", r.state.name())
                    );
                }
            }

            if (r.state == RoomState.CLOSED) {  // TODO. 게임 끝난 방은 다시 못들어감?
                throw new RoomException(
                        ErrorCode.ROOM_CLOSED, "Room is closed",
                        null, Meta.of("roomId", roomId.toString(), "state", r.state.name())
                );
            }

            member.connect();
            r.participants.put(memberId, member);

            r.version.incrementAndGet();

            if (isFull(roomId)) {
                switch (r.state) {
                    case PENDED, OPENED, CLOSED -> {
                        r.state = RoomState.FULL;
                        changed = true;
                    }
                }
            } else {
                if (r.state == RoomState.PENDED || r.state == RoomState.CLOSED) {
                    changed = true;
                }
                r.state = RoomState.OPENED;
            }

            log.info("memberId: {} joined roomId: {}", memberId, roomId);
            return RoomStatusChange.of(changed, snap(r));
        } finally { r.lock.unlock(); }
    }

    @Override
    public RoomStatusChange leave(UUID roomId, Long memberId) {
        RoomInternal r = req(roomId); r.lock.lock();
        try {
            boolean changed = r.removeParticipant(memberId);

            if (r.participants.isEmpty()) {
                delete(roomId);
                changed = true;
            } else if (r.host.getMemberId().equals(memberId)) {
                Participant nextHost = r.participants.values().iterator().next();
                r.host = RoomInternal.Host.of(nextHost);
                changed = true;
            }

            r.version.incrementAndGet();

            log.info("memberId: {} leaved roomId: {}", memberId, roomId);
            return RoomStatusChange.of(changed, snap(r));
        } finally { r.lock.unlock(); }
    }

    @Override
    public RoomStatusChange kick(UUID roomId, Long memberId, Long targetId) {
        RoomInternal r = req(roomId); r.lock.lock();
        try {
            Long hostId = r.host.getMemberId();

            if (!hostId.equals(memberId)) {
                throw new RoomException(
                        ErrorCode.NOT_ROOM_HOST, "강퇴 권한이 없습니다.",
                        null, Meta.of("roomId", roomId.toString(), "hostId", hostId, "memberId", memberId)
                );
            }
            if (memberId.equals(targetId)) {
                throw new RoomException(
                        ErrorCode.INVALID_ACTION, "자신을 강퇴할 수 없습니다.",
                        null, Meta.of("memberId", memberId.toString(), "targetId", targetId)
                );
            }
            if (!r.participants.containsKey(targetId)) {
                throw new RoomException(
                        ErrorCode.ROOM_NOT_JOINED, "방에 찹가하지 않은 사용자입니다.",
                        null, Meta.of("roomId", roomId.toString(), "memberId", memberId.toString())
                );
            }

            // 실제로 방에서 제거 (여기서 예외 나면 kick 목록에도 넣지 않음)
            boolean changed = r.removeParticipant(targetId);

            // 강퇴 목록에 기록
            r.kickedParticipants.add(targetId);

            // 비어있는 방 삭제
            if (r.participants.isEmpty()) {
                delete(roomId);
                changed = true;
            }

            r.version.incrementAndGet();
            log.info("memberId: {} kicked targetId: {} from roomId: {}", memberId, targetId, roomId);
            return RoomStatusChange.of(changed, snap(r));
        } finally {
            r.lock.unlock();  // ← 반드시 unlock
        }
    }


    @Override
    public void delete(UUID roomId) {
        RoomInternal r = rooms.remove(roomId);
        log.info("Room deleted: {}", r);
    }

    @Override
    public RoomSnapshot setReady(UUID roomId, Long memberId, boolean ready) {
        RoomInternal r = req(roomId); r.lock.lock();
        try {
            if (r.state == RoomState.STARTED) {
                throw new RoomException(
                        ErrorCode.ROOM_ALREADY_STARTED, "Game is started",
                        null, Meta.of("roomId", roomId.toString(), "state", r.state.name())
                );
            }

            Participant m = r.participants.get(memberId);
            if (m == null) {
                throw new RoomException(
                        ErrorCode.ROOM_NOT_JOINED,
                        "Not Joined", null, Meta.of("roomId", roomId.toString(), "memberId", memberId)
                );
            }
            m.ready(ready);
            r.participants.put(memberId, m);
            r.version.incrementAndGet();
            return snap(r);
        } finally { r.lock.unlock(); }
    }

    @Override
    public RoomStatusChange enterGame(UUID roomId, Long memberId) {
        if (memberId == null) {
            throw new RoomException(
                    ErrorCode.BAD_REQUEST, "memberId is null",
                    null, Meta.of("roomId", roomId == null ? null : roomId.toString())
            );
        }
        RoomInternal r = req(roomId); r.lock.lock();
        try {
            boolean changed = false;
            if (!r.host.getMemberId().equals(memberId)) {
                throw new RoomException(
                        ErrorCode.NOT_ROOM_HOST, "not granted for enter the game.",
                        null, Meta.of("roomId", roomId.toString(), "hostId", r.host.getMemberId(), "memberId", memberId)
                );
            }
            if (r.state == RoomState.STARTED) {
                throw new RoomException(
                        ErrorCode.ROOM_ALREADY_STARTED, "already started",
                        null, Meta.of("roomId", roomId.toString(), "state", r.state.name())
                );
            }
//            if (r.state != RoomState.FULL) {  // TODO. 꽉 찬 방만 참가 주석 수거 보류
//                throw new RoomException(
//                        ErrorCode.ROOM_NOT_FULL, "not full",
//                        null, Meta.of("roomId", roomId.toString(), "state", r.state.name(), "size", r.participants.size(), "maxSize", r.maxSize)
//                );
//            }
            if (!allReady(roomId)) {
                throw new RoomException(
                        ErrorCode.ROOM_NOT_ALL_READY, "not all ready.",
                        null, Meta.of("roomId", roomId.toString())
                );
            }
            if (r.state == RoomState.CLOSED) {
                throw new RoomException(
                        ErrorCode.ROOM_CLOSED, "roomId: %s is closed".formatted(roomId),
                        null, Meta.of("roomId", roomId.toString())
                );
            }

            r.state = RoomState.STARTED;
            changed = true;

            log.info("Room entering: {}", r);
            return RoomStatusChange.of(changed, snap(r));
        } finally { r.lock.unlock(); }
    }

    @Override
    public RoomSnapshot disconnect(UUID roomId, Long memberId) {
        RoomInternal r = req(roomId); r.lock.lock();
        try {
            var p = r.participants.get(memberId);
            p.disconnect();
            return snap(r);
        } finally { r.lock.unlock(); }
    }

    public boolean removeIfDisconnectedAndTimedOut(UUID roomId, Long memberId, Duration timeout) {
        RoomInternal r = req(roomId); r.lock.lock();
        try {
            Participant p = r.participants.get(memberId);
            if (p == null || p.isConnected()) return false;
            if (p.getDisconnectedAt().isBefore(Instant.now().minus(timeout))) {
                r.participants.remove(memberId);
                log.info("Remove disconnected from roomId: {} of memberId: {}", roomId, memberId);
                return true;
            }
            return false;
        } finally { r.lock.unlock(); }
    }

    public List<UUID> getAllRoomIds() {
        return new ArrayList<>(rooms.keySet());
    }

    public List<Long> getParticipantIds(UUID roomId) {
        RoomInternal r = req(roomId); r.lock.lock();
        try {
            return new ArrayList<>(r.participants.keySet());
        } finally {
            r.lock.unlock();
        }
    }

    @Override
    public void markClosed(UUID roomId) {
        RoomInternal r = req(roomId); r.lock.lock();
        try {
            r.state = RoomState.CLOSED;

        } finally { r.lock.unlock(); }
    }

    // TODO. 게임 종료 관련 정책 변경 제안을 위한 함수
    @Override
    public void exitGame(UUID roomId) {
        RoomInternal r = req(roomId); r.lock.lock();
        try {
            for (Participant p : r.participants.values()) {
                p.ready(false);
            }

            if (isFull(roomId)) {
                r.state = RoomState.FULL;
            } else {
                r.state = RoomState.OPENED;
            }

        } finally { r.lock.unlock(); }
    }


    /***** HELPER *****/

    private boolean allReady(UUID roomId) {
        return req(roomId).participants.values().stream().allMatch(Participant::isReady);
    }

    private boolean isFull(UUID roomId) {
        RoomInternal r = req(roomId);
        return r.participants.size() >= r.maxSize;
    }


    /***** UTILS *****/

    private RoomInternal req(UUID id) {
        RoomInternal r = rooms.get(id);
        if (r == null) {
            throw new RoomException(
                    ErrorCode.ROOM_NOT_FOUND, "no such room",
                    null, Meta.of("roomId", id == null ? null : id.toString())
            );
        }
        return r;
    }

    private RoomSnapshot snap(RoomInternal r) {
        return r.toSnapshot();
    }

    private static RoomStatus status(RoomState state) {
        if (state == null) {
            throw new RoomException(
                    ErrorCode.ROOM_STATE_NULL, "state cannot be null",
                    null, Meta.of()
            );
        }
        return switch (state) {
            case OPENED -> RoomStatus.WAITING;
            case FULL -> RoomStatus.FULL;
            case STARTED -> RoomStatus.IN_GAME;
            case  PENDED, CLOSED -> RoomStatus.DISABLED;
        };
    }

}