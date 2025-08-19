package com.ssafy.c204_be_api.ingame.service;

import com.ssafy.c204_be_api.game.domain.GameType;
import com.ssafy.c204_be_api.ingame.command.Purchase;
import com.ssafy.c204_be_api.ingame.domain.InGamePlayer;
import com.ssafy.c204_be_api.ingame.domain.Item;
import com.ssafy.c204_be_api.ingame.domain.Spell;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.SyncResponse;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface InGameStore {

    void createSession(UUID roomId, long gameId, GameType gameType, long timelimit, List<InGamePlayer> players);

    void deleteSession(UUID roomId);

    void banChoice(UUID roomId, long memberId, long problemCategoryId);

    void pickChoice(UUID roomId, long memberId, long problemCategoryId);

    long purchaseItem(UUID roomId, long memberId, long itemId);

    long purchaseSpell(UUID roomId, long memberId, long spellId);

    void bulkPurchase(UUID roomId, long memberId, List<Purchase> purchases);

    void surrender(UUID roomId, long memberId);

    void useItem(UUID roomId, long memberId, Item item, long targetMemberId);

    void useSpell(UUID roomId, long memberId, Spell spell);

    SyncResponse sync(UUID roomId, long memberId);

    Optional<PhaseTransitionResult> advancePhaseIfTimeout(UUID roomId);

    Set<UUID> getActiveRoomIds();

    Set<Long> getMemberIds(UUID roomId);
}
