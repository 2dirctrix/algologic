package com.ssafy.c204_be_api.ingame.service;

import com.ssafy.c204_be_api.common.web.message.Meta;
import com.ssafy.c204_be_api.config.property.InGameProperties;
import com.ssafy.c204_be_api.game.domain.GameType;
import com.ssafy.c204_be_api.game.service.GameResultService;
import com.ssafy.c204_be_api.game.web.request.GameResultCreateRequest;
import com.ssafy.c204_be_api.ingame.command.Purchase;
import com.ssafy.c204_be_api.ingame.domain.InGamePhase;
import com.ssafy.c204_be_api.ingame.domain.InGamePlayer;
import com.ssafy.c204_be_api.ingame.domain.Item;
import com.ssafy.c204_be_api.ingame.domain.Spell;
import com.ssafy.c204_be_api.ingame.message.outbound.finished.BanChoice;
import com.ssafy.c204_be_api.ingame.message.outbound.finished.BanFinishedPayload;
import com.ssafy.c204_be_api.ingame.message.outbound.finished.BattleFinishedPayload;
import com.ssafy.c204_be_api.ingame.message.outbound.finished.PickFinishedPayload;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.SyncResponse;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.inner.ActivatedItem;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.inner.ActivatedSpell;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.inner.PurchasedItem;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.inner.PurchasedSpell;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.player.*;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.response.*;
import com.ssafy.c204_be_api.ingame.support.WeightedRoulette;
import com.ssafy.c204_be_api.player.web.request.PlayerResultRequest;
import com.ssafy.c204_be_api.problem.domain.ProblemSubmit;
import com.ssafy.c204_be_api.problem.service.ProblemCategorySearchService;
import com.ssafy.c204_be_api.problem.service.ProblemSearchService;
import com.ssafy.c204_be_api.problem.service.ProblemSubmitService;
import com.ssafy.c204_be_api.problem.web.response.ProblemResponse;
import com.ssafy.c204_be_api.room.service.RoomStore;
import com.ssafy.c204_be_api.validation.code.ErrorCode;
import com.ssafy.c204_be_api.validation.exception.InGameException;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryInGameStore implements InGameStore {

    private final InGameProperties props;
    private final RoomStore roomStore;
    private final WeightedRoulette roulette;
    private final ProblemSearchService problemSearchService;
    private final GameResultService gameResultService;
    private final ProblemSubmitService problemSubmitService;
    private final ProblemCategorySearchService problemCategorySearchService;

    private final ConcurrentHashMap<UUID, SessionContext> sessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, ReentrantLock> locks = new ConcurrentHashMap<>();

    private final ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(2);
    {
        scheduler.setRemoveOnCancelPolicy(true);
    }

    ReentrantLock lockFor(UUID roomId) {
        return locks.computeIfAbsent(roomId, id -> new ReentrantLock());
    }

    SessionContext sessionOf(UUID roomId) {
        SessionContext ctx = sessions.get(roomId);
        if (ctx == null) {
            throw new InGameException(
                    ErrorCode.INGAME_SESSION_NOT_FOUND,
                    "게임 세션이 존재하지 않습니다.",
                    null,
                    Meta.of("roomId", roomId == null ? null : roomId.toString())
            );
        }
        return ctx;
    }

    @Override
    public void createSession(UUID roomId, long gameId, GameType gameType, long timelimit, List<InGamePlayer> players) {
        ReentrantLock lock = locks.computeIfAbsent(roomId, id -> new ReentrantLock());
        lock.lock();
        try {
            if (sessions.containsKey(roomId)) {
                throw new InGameException(
                        ErrorCode.INGAME_SESSION_ALREADY_EXISTS,
                        "이미 세션이 존재합니다.",
                        null,
                        Meta.of("roomId", roomId.toString())
                );
            }
            SessionContext ctx = new SessionContext(
                    roomId,
                    gameId,
                    gameType,
                    props.getPhaseDurations(),
                    props.getPaddingDuration(),
                    Duration.ofSeconds(timelimit),
                    roulette,
                    problemSearchService,
                    problemCategorySearchService,
                    scheduler
            );

            for (InGamePlayer p : players) {
                ctx.addPlayer(p);
            }

            sessions.put(roomId, ctx);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void deleteSession(UUID roomId) {
        ReentrantLock lock = locks.get(roomId);
        lock.lock();
        try {
            SessionContext ctx = sessions.remove(roomId);
            locks.remove(roomId);
            if (ctx != null) {
                ctx.cancelAllExpirations(); // 예약 만료 작업 정리
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void banChoice(UUID roomId, long memberId, long problemCategoryId) {
        ReentrantLock lock = lockFor(roomId);
        lock.lock();
        try {
            sessionOf(roomId).banChoice(memberId, problemCategoryId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void pickChoice(UUID roomId, long memberId, long problemCategoryId) {
        ReentrantLock lock = lockFor(roomId);
        lock.lock();
        try {
            sessionOf(roomId).pickChoice(memberId, problemCategoryId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long purchaseItem(UUID roomId, long memberId, long itemId) {
        ReentrantLock lock = lockFor(roomId);
        lock.lock();
        try {
            return sessionOf(roomId).purchaseItem(memberId, itemId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public long purchaseSpell(UUID roomId, long memberId, long spellId) {
        ReentrantLock lock = lockFor(roomId);
        lock.lock();
        try {
            return sessionOf(roomId).purchaseSpell(memberId, spellId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void bulkPurchase(UUID roomId, long memberId, List<Purchase> purchases) {
        ReentrantLock lock = lockFor(roomId);
        lock.lock();
        try {
            sessionOf(roomId).bulkPurchase(memberId, purchases);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void useItem(UUID roomId, long memberId, Item item, long targetMemberId) {
        ReentrantLock lock = lockFor(roomId);
        lock.lock();
        try {
            sessionOf(roomId).useItem(memberId, item, targetMemberId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void useSpell(UUID roomId, long memberId, Spell spell) {
        ReentrantLock lock = lockFor(roomId);
        lock.lock();
        try {
            sessionOf(roomId).useSpell(memberId, spell);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void surrender(UUID roomId, long memberId) {
        ReentrantLock lock = lockFor(roomId);
        lock.lock();
        try {
            SessionContext ctx = sessionOf(roomId);
            ctx.surrender(memberId);
            if (ctx.canSurrender()) {
                ctx.deadline = Instant.now();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public SyncResponse sync(UUID roomId, long memberId) {
        ReentrantLock lock = lockFor(roomId);
        lock.lock();
        try {
            return sessionOf(roomId).buildSyncResponse(memberId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Optional<PhaseTransitionResult> advancePhaseIfTimeout(UUID roomId) {
        ReentrantLock lock = lockFor(roomId);
        lock.lock();
        try {
            SessionContext ctx = sessionOf(roomId);

            // 아직 deadline 전이면 아무 일도 하지 않음
            if (Instant.now().isBefore(ctx.getDeadline())) {
                return Optional.empty();
            }

            InGamePhase prev = ctx.getPhase();
            PhaseTransitionResult result;

            switch (prev) {
                case LOADING -> result = new PhaseTransitionResult(prev, InGamePhase.BAN_CHOICE, null);

                case BAN_CHOICE -> result = new PhaseTransitionResult(
                        prev, InGamePhase.PICK_CHOICE, ctx.computeBanResult()
                );

                case PICK_CHOICE -> result = new PhaseTransitionResult(
                        prev, InGamePhase.PURCHASE, ctx.computePickResult()
                );

                case PURCHASE -> result = new PhaseTransitionResult(prev, InGamePhase.BATTLE, null);

                case BATTLE -> {
                    roomStore.markClosed(roomId);
                    log.info("roomId={} is closed",  roomId);
                    result = new PhaseTransitionResult(
                            prev, InGamePhase.FINISHED,
                            BattleFinishedPayload.builder()
                                    .gameResultId(computeGameResult(roomId))
                                    .build()
                    );
                }

                case FINISHED -> {
                    deleteSession(roomId);
                    log.info("roomId={} game session is deleted",  roomId);
                    result = new PhaseTransitionResult(prev, InGamePhase.FINISHED, null);
                }

                default -> { return Optional.empty(); }
            }

            // 실제로 phase 전환
            ctx.startPhase(result.getTo());
            return Optional.of(result);

        } finally {
            lock.unlock();
        }
    }

    @Override
    public Set<UUID> getActiveRoomIds() {
        return Collections.unmodifiableSet(sessions.keySet());
    }

    @Override
    public Set<Long> getMemberIds(UUID roomId) {
        ReentrantLock lock = lockFor(roomId);
        lock.lock();
        try {
            return sessionOf(roomId).getPlayers().keySet();
        } finally {
            lock.unlock();
        }
    }

    @Transactional
    Long computeGameResult(UUID roomId) {
        SessionContext ctx = sessionOf(roomId);

        // 1) 현재 방 참가자 ID 수집
        List<Long> playerIds = ctx.getPlayers().values().stream()
                .map(PlayerInternal::getPlayerId)
                .toList();

        Map<Long, Long> playerToMember = ctx.getPlayers().values().stream()
                .collect(Collectors.toMap(PlayerInternal::getPlayerId, PlayerInternal::getMemberId));

        // 2) 문제/플레이어별 맞춘 제출 이력 조회
        List<ProblemSubmit> correctSubmits = problemSubmitService.findAllByPlayerIds(playerIds);

        // 3) 모든 정답 제출에 대해 메트릭 산출
        //   - submittedAt, runningTime, memory가 null일 수 있으므로 Double로 보관
        //   - delaySec = 게임 시작(Instant)부터 제출시각(UTC)까지의 경과 ms
        record M(ProblemSubmit ps, long pid, Double delaySec, Double rtSec, Double memKb) {}

        ZoneId APP_ZONE = ZoneOffset.UTC;
        Instant startTs = ctx.getStartTime();  // 게임 시작 Instant

        Map<Long, M> metrics = new HashMap<>();  // <submitId, M>

        for (ProblemSubmit ps : correctSubmits) {
            long pid = ps.getPlayer().getId();

            Double delaySec = null;
            if (ps.getSubmittedAt() != null) {
                Instant submittedAt = ps.getSubmittedAt().atZone(APP_ZONE).toInstant();
                delaySec = (double) Duration.between(startTs, submittedAt).toSeconds();
            }

            Double rtSec  = (ps.getRunningTime() == null) ? null : ps.getRunningTime();
            Double memKb = (ps.getMemory() == null) ? null : ps.getMemory().doubleValue();

            metrics.put(ps.getId(), new M(ps, pid, delaySec, rtSec, memKb));
        }

        // 4) 정규화용 min/max 계산 (null 제외)
        //   - max == min 방어: 단일값이면 정규화 결과는 0.0(모두 동일)
        ProblemResponse problem = problemSearchService.search(ctx.getProblemId());

        double minDelay = 0.0; double maxDelay = ctx.getBattleDuration().toSeconds();
        double minRt    = 0.0; double maxRt    = problem.timeLimit();
        double minMem   = 0.0; double maxMem   = problem.memoryLimit();


        // 5) 플레이어별 "최소 점수 제출" 선택
        final double W_DELAY = 0.5;
        final double W_RT    = 0.4;
        final double W_MEM   = 0.1;

        Map<Long, ProblemSubmit> repByPlayer = new HashMap<>();  // 대표 제출
        Map<Long, Double> scoreByPlayer = new HashMap<>();  // 대표 점수

        for (Long pid : playerIds) {
            double bestScore = Double.POSITIVE_INFINITY;
            ProblemSubmit best = null;

            for (M m : metrics.values()) {
                if (m.pid != pid) continue;

                // null = 최악(1.0)으로 정규화
                double nd = normMinMaxWorstIsNull(m.delaySec, minDelay, maxDelay);
                double nr = normMinMaxWorstIsNull(m.rtSec, minRt, maxRt);
                double nm = normMinMaxWorstIsNull(m.memKb, minMem, maxMem);

                double score = W_DELAY * nd + W_RT * nr + W_MEM * nm;
                if (score < bestScore) {
                    bestScore = score;
                    best = m.ps;
                }
            }

            if (best != null) {
                repByPlayer.put(pid, best);
                scoreByPlayer.put(pid, bestScore);
            }
        }

        // 6) 랭킹 산정
        //   - 점수 오름차순
        //   - 동점(+-EPS) 보조정렬: submittedAt -> runningTime -> memory (모두 null은 최악)
        final double EPS = 1e-9;  // 앱실론(부동 소수점 비교 오차 허용값)

        List<Map.Entry<Long, ProblemSubmit>> sortedSolved = repByPlayer.entrySet().stream()
                .sorted((a, b) -> {
                    double sa = scoreByPlayer.get(a.getKey());
                    double sb = scoreByPlayer.get(b.getKey());
                    if (Math.abs(sa - sb) > EPS) return Double.compare(sa, sb);

                    ProblemSubmit pa = a.getValue();
                    ProblemSubmit pb = b.getValue();

                    // submittedAt: null이 더 나쁨(늦은 것으로 취급) -> nullsLast
                    int c1 = Comparator.comparing(
                                    ProblemSubmit::getSubmittedAt,
                                    Comparator.nullsLast(Comparator.naturalOrder()))
                            .compare(pa, pb);
                    if (c1 != 0) return c1;

                    // runtime: null이 더 나쁨(큰 것으로 취급) -> nullsLast, 오름차순
                    int c2 = Comparator.comparing(
                                    ProblemSubmit::getRunningTime,
                                    Comparator.nullsLast(Comparator.naturalOrder()))
                            .compare(pa, pb);
                    if (c2 != 0) return c2;

                    // memory: null이 더 나쁨(큰 것으로 취급) -> nullsLast, 오름차순
                    return Comparator.comparing(
                                    ProblemSubmit::getMemory,
                                    Comparator.nullsLast(Comparator.naturalOrder()))
                            .compare(pa, pb);
                })
                .toList();

        Map<Long, Integer> rankByPlayer = new HashMap<>();
        List<Long> solvedPlayers = new ArrayList<>();
        List<Long> unsolvedPlayers = new ArrayList<>();

        int rank = 0;
        int index = 0;
        Double lastScore = null;

        for (Map.Entry<Long, ProblemSubmit> e : sortedSolved) {
            index++;
            double cur = scoreByPlayer.get(e.getKey());
            if (lastScore == null || Math.abs(cur - lastScore) > EPS) {
                rank = index;              // Competition ranking
                lastScore = cur;
            }
            rankByPlayer.put(e.getKey(), rank);
            solvedPlayers.add(e.getKey());
        }

        // 정답 없는 플레이어(DNF) 수집
        for (Long pid : playerIds) {
            if (!rankByPlayer.containsKey(pid)) unsolvedPlayers.add(pid);
        }
        int dnfRank = playerIds.size();
        for (Long pid : unsolvedPlayers) {
            // 필요 시 점수 보관: 최악값 1.0
            scoreByPlayer.put(pid, 1.0);
            rankByPlayer.put(pid, dnfRank);
        }

        // 7) 보상 계산
        InGameProperties.Scoring rules = props.scoringFor(ctx.getGameType());
        InGameProperties.Unsolved unsolvedCfg = rules.getUnsolved();
        InGameProperties.Solved solvedCfg = rules.getSolved();

        int maxRank = rankByPlayer.values().stream().mapToInt(Integer::intValue).max().orElse(1);

        List<PlayerResultRequest> playerResultRequests = playerIds.stream()
                .map(pid -> {
                    int rankValue = rankByPlayer.get(pid);
                    boolean solved = solvedPlayers.contains(pid);

                    int score;
                    int coin;

                    if (!solved) {
                        score = ctx.canSurrender() ? 0 : unsolvedCfg.getScore();
                        coin  = ctx.canSurrender() ? 0 : unsolvedCfg.getCoin();
                    } else {
                        int steps = (maxRank - rankValue) + 1;
                        score = solvedCfg.getBaseScore() * steps;
                        coin  = solvedCfg.getBaseCoin()  * steps;
                    }

                    PlayerInternal p = ctx.getPlayers().get(playerToMember.get(pid));

                    Long submitId = repByPlayer.containsKey(pid) ? repByPlayer.get(pid).getId() : null;
                    Integer solveDuration = null;
                    if (submitId != null) {
                        Double d = metrics.get(submitId).delaySec;
                        if (d != null) {
                            solveDuration = (int) Math.floor(d);
                        }
                    }

                    return PlayerResultRequest.builder()
                            .playerId(pid)
                            .ranks(rankValue)
                            .earnedScore(score)
                            .earnedCoin(coin)
                            .bannedProblemCategoryId(p.getBannedCategoryId())
                            .pickedProblemCategoryId(p.getPickedCategoryId())
                            .submitId(submitId)
                            .solveDuration(solveDuration)
                            .build();
                })
                .toList();

        // 8) 게임 결과 저장
        long gameResultId = saveGameResult(GameResultCreateRequest.builder()
                .gameId(ctx.getGameId())
                .problemId(ctx.getProblemId())
                .startedAt(LocalDateTime.ofInstant(ctx.getStartTime(), APP_ZONE))
                .finishedAt(LocalDateTime.ofInstant(ctx.getDeadline(), APP_ZONE))
                .playerResults(playerResultRequests)
                .build()
        );

        return gameResultId;
    }

    @Transactional
    Long saveGameResult(GameResultCreateRequest request) {
        return gameResultService.saveGameResult(request);
    }

    /**
     * min-max 정규화 함수
     * - x == null 이면 "최악"으로 간주하여 1.0 반환
     * - max == min (단일값) 이면 0.0 반환(모두 동일)
     * - 0.0일수록 좋은 값(빠름/작음), 1.0일수록 나쁜 값(느림/큼)
     */
    private static double normMinMaxWorstIsNull(@Nullable Double x, double min, double max) {
        if (x == null) return 1.0;            // null -> 최악
        double den = max - min;
        if (den <= 0.0) return 0.0;           // max==min(또는 잘못된 범위) -> 모두 동일 취급
        double v = (x - min) / den;           // 정규화
        return (v < 0.0) ? 0.0 : (Math.min(v, 1.0));  // [0,1] 클램프
    }
    /** null을 최대로 간주하여 비교 시 뒤로 밀리도록 함 */
    private static int nullSafeInt(Integer v) { return v == null ? Integer.MAX_VALUE : v; }
    /** null을 최대로 간주하여 비교 시 뒤로 밀리도록 함 */
    private static double nullSafeDouble(Double v) { return v == null ? Double.MAX_VALUE : v; }

    // ──────────────────────────────────────────────────────────────
    // 내부 세션 컨텍스트
    // ──────────────────────────────────────────────────────────────
    @Getter
    private static class SessionContext {
        private final UUID roomId;
        private final long gameId;
        private final GameType gameType;
        private InGamePhase phase;
        private Long bannedCategoryId;
        private Long pickedCategoryId;
        private Long problemId;
        private Instant startTime;
        private Instant deadline;
        private int version;

        private final Map<Long, PlayerInternal> players = new LinkedHashMap<>();
        private final Map<InGamePhase, Duration> phaseDurations;
        private final Duration padding;
        private final Duration battleDuration;

        private final WeightedRoulette roulette;
        private final ProblemSearchService problemSearchService;
        private final ProblemCategorySearchService problemCategorySearchService;

        private final ScheduledThreadPoolExecutor scheduler;

        SessionContext(UUID roomId, long gameId, GameType gameType,
                       Map<InGamePhase, Duration> phaseDurations,
                       Duration padding,
                       Duration battleDuration,
                       WeightedRoulette roulette,
                       ProblemSearchService problemSearchService,
                       ProblemCategorySearchService problemCategorySearchService,
                       ScheduledThreadPoolExecutor scheduler
        ) {
            this.roomId = roomId;
            this.gameId = gameId;
            this.gameType = gameType;
            this.phaseDurations = phaseDurations;
            this.padding = padding;
            this.battleDuration = battleDuration;
            this.roulette = roulette;
            this.problemSearchService = problemSearchService;
            this.problemCategorySearchService = problemCategorySearchService;
            this.scheduler = scheduler;
            startPhase(InGamePhase.LOADING);
        }

        void addPlayer(InGamePlayer p) {
            players.put(
                    p.getMemberId(),
                    PlayerInternal.builder()
                            .memberId(p.getMemberId())
                            .playerId(p.getPlayerId())
                            .build()
            );
        }

        private void startPhase(InGamePhase newPhase) {
            Instant now = Instant.now();
            this.phase = newPhase;

            this.startTime = switch (newPhase) {
                case LOADING -> now;
                default -> now.plus(padding);
            };

            Duration dur = switch (newPhase) {
//                case BATTLE -> battleDuration;  // TODO. 배틀 시간 정상화
                default -> phaseDurations.getOrDefault(newPhase, Duration.ZERO);
            };
            this.deadline = startTime.plus(dur);
            this.version++;
        }

        void banChoice(long memberId, long categoryId) {
            ensurePhase(InGamePhase.BAN_CHOICE);

            PlayerInternal player = players.get(memberId);
            if (player == null) {
                throw new InGameException(
                        ErrorCode.INGAME_PLAYER_NOT_FOUND, "플레이어가 존재하지 않습니다.",
                        null, Meta.of("memberId", memberId, "roomId", roomId.toString())
                );
            }
            if (player.getBannedCategoryId() != null) {
                throw new InGameException(
                        ErrorCode.BAN_ALREADY_DONE, "이미 ban 선택을 마쳤습니다.",
                        null, Meta.of("memberId", memberId, "categoryId", player.getBannedCategoryId())
                );
            }

            player.setBannedCategoryId(categoryId);

            // 모두 벤 선택 했을 시, 자동 종료
            if (players.values().stream().allMatch(PlayerInternal::hasBanned)) {
                deadline = Instant.now();
            }

            version++;
        }

        void pickChoice(long memberId, long categoryId) {
            ensurePhase(InGamePhase.PICK_CHOICE);

            PlayerInternal player = players.get(memberId);
            if (player == null) {
                throw new InGameException(
                        ErrorCode.INGAME_PLAYER_NOT_FOUND, "플레이어가 존재하지 않습니다.",
                        null, Meta.of("memberId", memberId, "roomId", roomId.toString())
                );
            }
            if (player.getPickedCategoryId() != null) {
                throw new InGameException(
                        ErrorCode.PICK_ALREADY_DONE, "이미 pick 선택을 마쳤습니다.",
                        null, Meta.of("memberId", memberId, "pickedCategoryId", player.getPickedCategoryId())
                );
            }
            if (bannedCategoryId != null && Objects.equals(bannedCategoryId, categoryId)) {
                throw new InGameException(
                        ErrorCode.CATEGORY_BANNED, "ban 된 알고리즘 유형입니다.",
                        null, Meta.of("bannedCategoryId", bannedCategoryId, "pickedCategoryId", categoryId)
                );
            }

            player.setPickedCategoryId(categoryId);

            // 모두 픽 선택했을 시, 자동 종료
            if (players.values().stream().allMatch(PlayerInternal::hasPicked)) {
                deadline = Instant.now();
            }

            version++;
        }

        long purchaseItem(long memberId, long itemId) {
            ensurePhase(InGamePhase.PURCHASE);

            PlayerInternal p = players.get(memberId);
            if (p == null) {
                throw new InGameException(
                        ErrorCode.INGAME_PLAYER_NOT_FOUND, "플레이어가 존재하지 않습니다.",
                        null, Meta.of("memberId", memberId, "roomId", roomId.toString())
                );
            }

            PurchasedItemInternal exist = p.getPurchasedItemInternals().stream()
                    .filter(pi -> pi.getItemId() == itemId)
                    .findFirst()
                    .orElse(null);

            if (exist != null) {
                exist.setQuantity(exist.getQuantity() + 1);
            } else {
                p.getPurchasedItemInternals().add(
                        PurchasedItemInternal.builder()
                                .itemId(itemId)
                                .quantity(1)
                                .build()
                );
            }

            version++;
            return p.getPlayerId();
        }

        long purchaseSpell(long memberId, long spellId) {
            ensurePhase(InGamePhase.PURCHASE);

            PlayerInternal p = players.get(memberId);
            if (p == null) {
                throw new InGameException(
                        ErrorCode.INGAME_PLAYER_NOT_FOUND, "플레이어가 존재하지 않습니다.",
                        null, Meta.of("memberId", memberId, "roomId", roomId.toString())
                );
            }

            PurchasedSpellInternal exist = p.getPurchasedSpells().stream()
                    .filter(sp -> sp.getSpellId() == spellId)
                    .findFirst()
                    .orElse(null);

            if (exist != null) {
                exist.setQuantity(exist.getQuantity() + 1);
            } else {
                p.getPurchasedSpells().add(
                        PurchasedSpellInternal.builder()
                                .spellId(spellId)
                                .quantity(1)
                                .build()
                );
            }

            version++;
            return p.getPlayerId();
        }

        void bulkPurchase(long memberId, List<Purchase> purchases) {
            ensurePhase(InGamePhase.PURCHASE);

            PlayerInternal p = players.get(memberId);
            if (p == null) {
                throw new InGameException(
                        ErrorCode.INGAME_PLAYER_NOT_FOUND, "플레이어가 존재하지 않습니다.",
                        null, Meta.of("memberId", memberId, "roomId", roomId.toString())
                );
            }

            if (p.isPurchased()) {
                throw new InGameException(
                        ErrorCode.PURCHASE_ALREADY_PROCESSED, "이미 구매를 완료했습니다.",
                        null, Meta.of()
                );
            }

            // 기존 증가 로직을 그대로 활용(중복 아이템이면 수량 누적)
            for (Purchase x : purchases) {
                switch (x.getPurchaseType()) {
                    case ITEM -> {
                        p.getPurchasedItemInternals().add(
                                PurchasedItemInternal.builder()
                                        .itemId(x.getPurchaseTargetId())
                                        .quantity(x.getQuantity())
                                        .build()
                        );
                    }
                    case SPELL -> {
                        p.getPurchasedSpells().add(
                                PurchasedSpellInternal.builder()
                                        .spellId(x.getPurchaseTargetId())
                                        .quantity(x.getQuantity())
                                        .build()
                        );
                    }
                    default -> throw new InGameException(
                            ErrorCode.PURCHASE_TYPE_UNSUPPORTED, "지원하지 않는 구매 유형입니다.",
                            null, Meta.of("purchaseType", x.getPurchaseType())
                    );
                }
            }

            p.purchased = true;

            // 모두 픽 선택했을 시, 자동 종료
            if (players.values().stream().allMatch(PlayerInternal::hasPurchased)) {
                deadline = Instant.now();
            }

            version++;
        }

        void useItem(long memberId, Item item, long targetId) {
            ensurePhase(InGamePhase.BATTLE);

            PlayerInternal user = players.get(memberId);
            if (user == null) {
                throw new InGameException(
                        ErrorCode.INGAME_PLAYER_NOT_FOUND, "시전자(플레이어)를 찾을 수 없습니다.",
                        null, Meta.of("memberId", memberId, "roomId", roomId.toString())
                );
            }
            PlayerInternal target = players.get(targetId);
            if (target == null) {
                throw new InGameException(
                        ErrorCode.INGAME_PLAYER_NOT_FOUND, "대상(플레이어)을 찾을 수 없습니다.",
                        null, Meta.of("targetMemberId", targetId, "roomId", roomId.toString())
                );
            }

            Long itemId = item.getId();

            PurchasedItemInternal pi = user.getPurchasedItemInternals().stream()
                    .filter(i -> i.getItemId() == itemId)
                    .findFirst()
                    .orElseThrow(() -> new InGameException(
                            ErrorCode.ITEM_NOT_PURCHASED, "아이템을 구매하지 않았습니다.",
                            null, Meta.of("itemId", itemId, "memberId", memberId)));

            if (pi.getRemainingCount() <= 0) {
                throw new InGameException(
                        ErrorCode.ITEM_OUT_OF_STOCK, "사용 가능한 아이템이 없습니다.",
                        null, Meta.of("itemId", itemId, "memberId", memberId)
                );
            }
            pi.incrementUsedCount();

            // 활성화 + 만료 예약
            Instant activatedAt = Instant.now();
            Duration ttl = Duration.ofSeconds(item.getDuration());
            Instant expiresAt = activatedAt.plus(ttl);

            UUID activationId = UUID.randomUUID();
            ActivatedItemInternal act = ActivatedItemInternal.builder()
                    .id(activationId)
                    .itemId(itemId)
                    .fromMemberId(memberId)
                    .duration(item.getDuration())
                    .activatedAt(activatedAt)
                    .expiresAt(expiresAt)
                    .build();

            target.getActivatedItems().put(activationId, act);

            ScheduledFuture<?> f = scheduler.schedule(() -> {
                ActivatedItemInternal removedItem = target.getActivatedItems().remove(activationId);
                ScheduledFuture<?> old = target.getItemExpiryTasks().remove(activationId);
                if (old != null) old.cancel(false);
                log.info("expired item activation: {}", removedItem);
            }, ttl.toNanos(), TimeUnit.NANOSECONDS);
            target.getItemExpiryTasks().put(activationId, f);

            version++;
        }

        void useSpell(long memberId, Spell spell) {
            ensurePhase(InGamePhase.BATTLE);

            PlayerInternal user = players.get(memberId);
            if (user == null) {
                throw new InGameException(
                        ErrorCode.INGAME_PLAYER_NOT_FOUND, "플레이어를 찾을 수 없습니다.",
                        null, Meta.of("memberId", memberId, "roomId", roomId.toString())
                );
            }

            Long spellId = spell.getId();

            PurchasedSpellInternal ps = user.getPurchasedSpells().stream()
                    .filter(s -> s.getSpellId() == spellId)
                    .findFirst()
                    .orElseThrow(() -> new InGameException(
                            ErrorCode.SPELL_NOT_PURCHASED, "스펠을 구매하지 않았습니다.",
                            null, Meta.of("spellId", spellId, "memberId", memberId)));

            if (ps.getRemainingCount() <= 0) {
                throw new InGameException(
                        ErrorCode.SPELL_OUT_OF_STOCK, "사용 가능한 스펠이 없습니다.",
                        null, Meta.of("spellId", spellId, "memberId", memberId)
                );
            }
            ps.incrementUsedCount();

            // 활성화 + 만료 예약 (자기 자신)
            Instant activatedAt = Instant.now();
            Duration ttl = Duration.ofSeconds(spell.getDuration());
            Instant expiresAt = activatedAt.plus(ttl);

            UUID activationId = UUID.randomUUID();
            ActivatedSpellInternal act = ActivatedSpellInternal.builder()
                    .id(activationId)
                    .spellId(spellId)
                    .duration(spell.getDuration())
                    .activatedAt(activatedAt)
                    .expiresAt(expiresAt)
                    .build();

            user.getActivatedSpells().put(activationId, act);

            ScheduledFuture<?> f = scheduler.schedule(() -> {
                ActivatedSpellInternal removedSpell = user.getActivatedSpells().remove(activationId);
                ScheduledFuture<?> old = user.getSpellExpiryTasks().remove(activationId);
                if (old != null) old.cancel(false);
                log.info("expired spell activation: {}", removedSpell);
            }, ttl.toNanos(), TimeUnit.NANOSECONDS);
            user.getSpellExpiryTasks().put(activationId, f);

            version++;
        }

        void surrender(long memberId) {
            ensurePhase(InGamePhase.BATTLE);
            PlayerInternal p = players.get(memberId);
            if (p == null) {
                throw new InGameException(
                        ErrorCode.INGAME_PLAYER_NOT_FOUND, "플레이어가 존재하지 않습니다.",
                        null, Meta.of("memberId", memberId, "roomId", roomId.toString())
                );
            }
            p.setSurrendered(true);
            version++;
        }

        void cancelAllExpirations() {
            players.values().forEach(p -> {
                p.getItemExpiryTasks().values().forEach(f -> f.cancel(false));
                p.getSpellExpiryTasks().values().forEach(f -> f.cancel(false));
                p.getItemExpiryTasks().clear();
                p.getSpellExpiryTasks().clear();
                p.getActivatedItems().clear();
                p.getActivatedSpells().clear();
            });
        }

        BanFinishedPayload computeBanResult() {
            Map<Long, Integer> counter = players.values().stream()
                    .map(PlayerInternal::getBannedCategoryId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.groupingBy(id -> id, Collectors.summingInt(x -> 1)));

            int total = counter.values().stream().mapToInt(Integer::intValue).sum();

            List<BanChoice> banList = counter.entrySet().stream()
                    .map(e -> new BanChoice(e.getKey(), e.getValue()))
                    .sorted(Comparator.<BanChoice,Integer>comparing(BanChoice::getChoiceCount).reversed()
                            .thenComparing(BanChoice::getProblemCategoryId))
                    .toList();

            Long selected = roulette.pick(counter).orElse(null);
            this.bannedCategoryId = selected;

            return BanFinishedPayload.builder()
                    .banList(banList)
                    .bannedProblemCategoryId(selected)
                    .totalChoiceCount(total)
                    .build();
        }

        PickFinishedPayload computePickResult() {
            Map<Long, Integer> counter = players.values().stream()
                    .map(PlayerInternal::getPickedCategoryId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.groupingBy(id -> id, Collectors.summingInt(x -> 1)));

            Long selectedCategoryId = roulette.pick(counter)
                    .orElse(problemCategorySearchService.searchRandomCategory().getId());

            Long selectedProblemId  = resolveProblemIdForCategory(selectedCategoryId);

            // TODO. 테스트 수거
            selectedProblemId = 1L;
            //

            this.pickedCategoryId = selectedCategoryId;
            this.problemId = selectedProblemId;

            return PickFinishedPayload.builder()
                    .problemId(selectedProblemId)
                    .build();
        }

        private Long resolveProblemIdForCategory(long categoryId) {
            return problemSearchService.searchRandom(categoryId).getId();
        }


        /***** HELPER *****/

        private boolean canSurrender() {
            int need = players.size() / 2 + 1; // 과반
            return players.values().stream()
                    .filter(PlayerInternal::hasSurrendered)
                    .limit(need)        // 과반 채우면 더 안 셈
                    .count() >= need;
        }

        private void ensurePhase(InGamePhase expected) {
            if (phase != expected) {
                throw new InGameException(
                        ErrorCode.INGAME_WRONG_PHASE,
                        "잘못된 phase 호출: 현재=%s, 기대=%s".formatted(phase, expected),
                        null,
                        Meta.of("current", phase == null ? null : phase.name(), "expected", expected.name(), "roomId", roomId.toString())
                );
            }
            if (startTime.isAfter(Instant.now())) {
                throw new InGameException(
                        ErrorCode.INGAME_PHASE_NOT_STARTED,
                        "phase가 아직 시작되지 않았습니다.",
                        null,
                        Meta.of("phase", phase.name(), "startAt", startTime.toString(), "roomId", roomId.toString())
                );
            }
        }

        SyncResponse buildSyncResponse(long meId) {
            return switch (phase) {
                case LOADING, FINISHED -> buildLoadingResponse(meId);
                case BAN_CHOICE -> buildBanChoiceResponse(meId);
                case PICK_CHOICE -> buildPickChoiceResponse(meId);
                case PURCHASE -> buildPurchaseResponse(meId);
                case BATTLE -> buildBattleResponse(meId);
                default -> throw new InGameException(
                        ErrorCode.INVALID_GAME_STATE, "알 수 없는 게임 단계입니다.",
                        null, Meta.of("phase", phase == null ? null : phase.name())
                );
            };
        }

        private LoadingSyncResponse buildLoadingResponse(long meId) {
            long nowMs = nowMs();
            String nowIso = nowIso();
            long startMs = startMs();
            String startIso = startIso();
            long dlMs = deadlineMs();
            String dlIso = deadlineIso();

            LoadingPlayerSync meDto = LoadingPlayerSync.builder()
                    .memberId(meId)
                    .build();

            List<LoadingPlayerSync> others = players.values().stream()
                    .map(ps -> LoadingPlayerSync.builder()
                            .memberId(ps.getMemberId())
                            .build())
                    .collect(Collectors.toList());

            return LoadingSyncResponse.builder()
                    .phase(phase.name())
                    .currentUnix(nowMs).currentIso(nowIso)
                    .startUnix(startMs).startIso(startIso)
                    .deadlineUnix(dlMs).deadlineIso(dlIso)
                    .me(meDto).players(others)
                    .version(version)
                    .build();
        }

        private BanChoiceSyncResponse buildBanChoiceResponse(long meId) {
            long nowMs = nowMs();
            String nowIso = nowIso();
            long startMs = startMs();
            String startIso = startIso();
            long dlMs = deadlineMs();
            String dlIso = deadlineIso();

            PlayerInternal meState = players.get(meId);
            BanPlayerSync meDto = BanPlayerSync.builder()
                    .memberId(meId)
                    .bannedCategoryId(meState.getBannedCategoryId())
                    .chosen(meState.hasBanned())
                    .build();

            List<BanPlayerSync> others = players.values().stream()
                    .map(ps -> BanPlayerSync.builder()
                            .memberId(ps.getMemberId())
                            .chosen(ps.hasBanned())
                            .build())
                    .collect(Collectors.toList());

            return BanChoiceSyncResponse.builder()
                    .phase(phase.name())
                    .currentUnix(nowMs).currentIso(nowIso)
                    .startUnix(startMs).startIso(startIso)
                    .deadlineUnix(dlMs).deadlineIso(dlIso)
                    .me(meDto).players(others)
                    .version(version)
                    .build();
        }

        private PickChoiceSyncResponse buildPickChoiceResponse(long meId) {
            long nowMs = nowMs();
            String nowIso = nowIso();
            long startMs = startMs();
            String startIso = startIso();
            long dlMs = deadlineMs();
            String dlIso = deadlineIso();

            PlayerInternal meState = players.get(meId);
            PickPlayerSync meDto = PickPlayerSync.builder()
                    .memberId(meId)
                    .pickedCategoryId(meState.getPickedCategoryId())
                    .chosen(meState.hasPicked())
                    .build();
            List<PickPlayerSync> others = players.values().stream()
                    .map(ps -> PickPlayerSync.builder()
                            .memberId(ps.getMemberId())
                            .chosen(ps.hasPicked())
                            .build())
                    .collect(Collectors.toList());

            return PickChoiceSyncResponse.builder()
                    .phase(phase.name())
                    .bannedCategoryId(bannedCategoryId)
                    .currentUnix(nowMs).currentIso(nowIso)
                    .startUnix(startMs).startIso(startIso)
                    .deadlineUnix(dlMs).deadlineIso(dlIso)
                    .me(meDto).players(others)
                    .version(version)
                    .build();
        }

        private PurchaseSyncResponse buildPurchaseResponse(long meId) {
            long nowMs = nowMs();
            String nowIso = nowIso();
            long startMs = startMs();
            String startIso = startIso();
            long dlMs = deadlineMs();
            String dlIso = deadlineIso();

            PlayerInternal meState = players.get(meId);
            PurchasePlayerSync meDto = PurchasePlayerSync.builder()
                    .memberId(meId)
                    .purchased(meState.hasPurchased())
                    .purchasedItems(meState.getPurchasedItemInternals().stream()
                            .map(pi -> PurchasedItem.builder()
                                    .itemId(pi.getItemId())
                                    .quantity(pi.getQuantity())
                                    .remainingCount(pi.getRemainingCount())
                                    .usedCount(pi.getUsedCount())
                                    .build())
                            .collect(Collectors.toList()))
                    .purchasedSpells(meState.getPurchasedSpells().stream()
                            .map(sp -> PurchasedSpell.builder()
                                    .spellId(sp.getSpellId())
                                    .quantity(sp.getQuantity())
                                    .remainingCount(sp.getRemainingCount())
                                    .usedCount(sp.getUsedCount())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();

            List<PurchasePlayerSync> others = players.values().stream()
                    .map(ps -> PurchasePlayerSync.builder()
                            .memberId(ps.getMemberId())
                            .purchased(ps.hasPurchased())
                            .build())
                    .collect(Collectors.toList());

            return PurchaseSyncResponse.builder()
                    .phase(phase.name())
                    .currentUnix(nowMs)
                    .currentIso(nowIso)
                    .startUnix(startMs)
                    .startIso(startIso)
                    .deadlineUnix(dlMs)
                    .deadlineIso(dlIso)
                    .me(meDto)
                    .players(others)
                    .version(version)
                    .build();
        }

        private BattleSyncResponse buildBattleResponse(long meId) {
            long nowMs = nowMs();
            String nowIso = nowIso();
            long startMs = startMs();
            String startIso = startIso();
            long dlMs = deadlineMs();
            String dlIso = deadlineIso();

            PlayerInternal meState = players.get(meId);
            BattlePlayerSync meDto = BattlePlayerSync.builder()
                    .memberId(meId)
                    .playerId(meState.getPlayerId())
                    .surrender(meState.hasSurrendered())
                    .purchasedItems(meState.getPurchasedItemInternals().stream()
                            .map(pi -> PurchasedItem.builder()
                                    .itemId(pi.getItemId())
                                    .quantity(pi.getQuantity())
                                    .remainingCount(pi.getRemainingCount())
                                    .usedCount(pi.getUsedCount())
                                    .build())
                            .collect(Collectors.toList()))
                    .purchasedSpells(meState.getPurchasedSpells().stream()
                            .map(sp -> PurchasedSpell.builder()
                                    .spellId(sp.getSpellId())
                                    .quantity(sp.getQuantity())
                                    .remainingCount(sp.getRemainingCount())
                                    .usedCount(sp.getUsedCount())
                                    .build())
                            .collect(Collectors.toList()))
                    .activatedItems(meState.getActivatedItems().values().stream()
                            .map(ai -> ActivatedItem.builder()
                                    .activationId(ai.getId())
                                    .itemId(ai.getItemId())
                                    .from(ai.getFromMemberId())
                                    .duration(ai.getDuration())
                                    .activatedUnix(ai.getActivatedAt().toEpochMilli())
                                    .activatedIso(ai.getActivatedAt().toString())
                                    .expiresUnix(ai.getExpiresAt().toEpochMilli())
                                    .expiresIso(ai.getExpiresAt().toString())
                                    .build())
                            .collect(Collectors.toList()))
                    .activatedSpells(meState.getActivatedSpells().values().stream()
                            .map(as -> ActivatedSpell.builder()
                                    .activationId(as.getId())
                                    .spellId(as.getSpellId())
                                    .duration(as.getDuration())
                                    .activatedUnix(as.getActivatedAt().toEpochMilli())
                                    .activatedIso(as.getActivatedAt().toString())
                                    .expiresUnix(as.getExpiresAt().toEpochMilli())
                                    .expiresIso(as.getExpiresAt().toString())
                                    .build())
                            .collect(Collectors.toList()))
                    .build();

            return BattleSyncResponse.builder()
                    .phase(phase.name())
                    .currentUnix(nowMs)
                    .currentIso(nowIso)
                    .startUnix(startMs)
                    .startIso(startIso)
                    .deadlineUnix(dlMs)
                    .deadlineIso(dlIso)
                    .problemId(problemId)
                    .me(meDto)
                    .players(null)  // 정책상 타인 보유내역은 숨김
                    .version(version)
                    .build();
        }

        private long nowMs() { return Instant.now().toEpochMilli(); }
        private String nowIso() { return Instant.now().toString(); }
        private long startMs() { return startTime.toEpochMilli(); }
        private String startIso() { return startTime.toString(); }
        private long deadlineMs() { return deadline.toEpochMilli(); }
        private String deadlineIso() { return deadline.toString(); }
    }

    // ──────────────────────────────────────────────────────────────
    // 내부 DTO: 한 플레이어 상태 및 구매/사용 내역
    // ──────────────────────────────────────────────────────────────
    @Getter
    @Setter
    @SuperBuilder
    @RequiredArgsConstructor
    private static class PlayerInternal {
        private final long memberId;
        private final long playerId;
        private Long bannedCategoryId;
        private Long pickedCategoryId;
        private boolean surrendered;
        private boolean purchased;

        private final List<PurchasedItemInternal> purchasedItemInternals = new ArrayList<>();
        private final List<PurchasedSpellInternal> purchasedSpells = new ArrayList<>();

        private final ConcurrentHashMap<UUID, ActivatedItemInternal> activatedItems = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<UUID, ActivatedSpellInternal> activatedSpells = new ConcurrentHashMap<>();

        private final ConcurrentHashMap<UUID, ScheduledFuture<?>> itemExpiryTasks = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<UUID, ScheduledFuture<?>> spellExpiryTasks = new ConcurrentHashMap<>();

        boolean hasBanned() { return bannedCategoryId != null; }
        boolean hasPicked() { return pickedCategoryId != null; }
        boolean hasSurrendered() { return surrendered; }
        boolean hasPurchased() {
            return purchased;
        }
    }

    @Getter
    @Setter
    @Builder
    @RequiredArgsConstructor
    @AllArgsConstructor
    private static class PurchasedItemInternal {
        private final long itemId;
        private int quantity;
        private int usedCount = 0;
        int getRemainingCount() { return quantity - usedCount; }
        void incrementUsedCount() { this.usedCount++; }
    }

    @Getter
    @Setter
    @Builder
    @RequiredArgsConstructor
    @AllArgsConstructor
    private static class PurchasedSpellInternal {
        private final long spellId;
        private int quantity;
        private int usedCount = 0;
        int getRemainingCount() { return quantity - usedCount; }
        void incrementUsedCount() { this.usedCount++; }
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    private static class ActivatedItemInternal {
        private final UUID id;
        private final long itemId;
        private final long fromMemberId;
        private final int duration;  // seconds
        private final Instant activatedAt;
        private final Instant expiresAt;
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    private static class ActivatedSpellInternal {
        private final UUID id;
        private final long spellId;
        private final int duration;  // seconds
        private final Instant activatedAt;
        private final Instant expiresAt;
    }
}
