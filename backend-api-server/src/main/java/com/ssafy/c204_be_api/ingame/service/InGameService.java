package com.ssafy.c204_be_api.ingame.service;

import com.ssafy.c204_be_api.common.web.event.Event;
import com.ssafy.c204_be_api.common.web.message.ApiMessage;
import com.ssafy.c204_be_api.game.domain.GameType;
import com.ssafy.c204_be_api.ingame.domain.InGamePlayer;
import com.ssafy.c204_be_api.ingame.domain.Item;
import com.ssafy.c204_be_api.ingame.domain.Spell;
import com.ssafy.c204_be_api.ingame.event.InGameEvent;
import com.ssafy.c204_be_api.ingame.message.constant.InGameTopic;
import com.ssafy.c204_be_api.ingame.message.outbound.InGameSnapshot;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.SyncResponse;
import com.ssafy.c204_be_api.problem.service.ProblemCategorySearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InGameService {

    private final InGameStore store;
    private final SimpMessagingTemplate messaging;
    private final ProblemCategorySearchService problemCategorySearchService;
    private final InGameShopService inGameShopService;

    /**
     * 게임 생성(입장) 시 호출.
     */
    public InGameSnapshot enterGame(UUID roomId, long gameId, GameType gameType, long timelimit, List<InGamePlayer> players) {
        store.createSession(roomId, gameId, gameType, timelimit, players);

        InGameSnapshot inGameSnap = InGameSnapshot.builder()
                .gameId(gameId)
                .players(players.stream().map(InGamePlayer::toSnapshot).toList())
                .build();

        return inGameSnap;
    }

    /**
     * 알고리즘 벤 선택  // TODO. 미사용 수거 보류
     */
    public void banChoice(UUID roomId, long memberId, long problemCategoryId) {
        // 입력값 검증
        problemCategorySearchService.validatePickedCategoryId(problemCategoryId);

        store.banChoice(roomId, memberId, problemCategoryId);

        sendToUsers(roomId, InGameTopic.userChoice(roomId),
                InGameEvent.BAN_CHOSEN, "벤 선택을 완료한 플레이어가 있습니다.");
    }

    /**
     * 알고리즘 픽 선택  // TODO. 미사용 수거 보류
     */
    public void pickChoice(UUID roomId, long memberId, long problemCategoryId) {
        // 입력값 검증
        problemCategorySearchService.validatePickedCategoryId(problemCategoryId);

        store.pickChoice(roomId, memberId, problemCategoryId);

        sendToUsers(roomId, InGameTopic.userChoice(roomId),
                InGameEvent.PICK_CHOSEN, "픽 선택을 완료한 플레이어가 있습니다.");

    }

    /**
     * 아이템 사용
     */
    public void useItem(UUID roomId, long memberId, long itemId, long targetMemberId) {

        Item item = inGameShopService.findItemById(itemId);
        store.useItem(roomId, memberId, item, targetMemberId);
        SyncResponse sync = store.sync(roomId, memberId);

        // ITEM_SENT 이벤트
        sendToUser(memberId,
                InGameTopic.userBattle(roomId), "아이템을 사용했습니다.",
                InGameEvent.ITEM_SENT, sync
        );

        SyncResponse targetSync = store.sync(roomId, targetMemberId);

        // ITEM_RECEIVED
        sendToUser(targetMemberId,
                InGameTopic.userBattle(roomId), "아이템이 적용되었습니다.",
                InGameEvent.ITEM_RECEIVED, targetSync
        );
    }

    /**
     * 스펠 사용
     */
    public void useSpell(UUID roomId, long memberId, long spellId) {
        Spell spell = inGameShopService.findSpellById(spellId);
        store.useSpell(roomId, memberId, spell);
        SyncResponse sync = store.sync(roomId, memberId);

        // SPELL_ACTIVATED 이벤트
        sendToUser(memberId,
                InGameTopic.userBattle(roomId), "스펠을 활성화 했습니다.",
                InGameEvent.SPELL_ACTIVATED, sync
        );
    }

    /**
     * 항복 요청
     */
    public void surrender(UUID roomId, long memberId) {
        store.surrender(roomId, memberId);
        SyncResponse sync = store.sync(roomId, memberId);

        sendToUser(memberId,
                InGameTopic.userBattle(roomId), "서렌 의사가 확인되었습니다.",
                InGameEvent.SURRENDERED, sync
        );
    }

    /**
     * 상태 동기화 요청
     */
    public SyncResponse sync(UUID roomId, long memberId) {
        return store.sync(roomId, memberId);
    }


    /***** HELPER *****/

    /**
     * 해당 플레이어에게 personalized된 SyncResponse 전송
     */
    private void sendToUser(Long memberId, String topic, String message, InGameEvent event, Object payload) {
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

    /**
     * 각 플레이어에게 personalized된 SyncResponse 전송
     */
    private void sendToUsers(UUID roomId, String topic, InGameEvent event, String message) {
        for (Long memberId : store.getMemberIds(roomId)) {
            try {
                SyncResponse sync = store.sync(roomId, memberId);
                sendToUser(memberId, topic, message, event, sync);
            } catch (Throwable ex) {
                log.warn("memberId={} 에게 전송 실패", memberId, ex);
            }
        }
    }

}
