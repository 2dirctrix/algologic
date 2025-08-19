package com.ssafy.c204_be_api.ingame.service;

import com.ssafy.c204_be_api.common.web.event.Event;
import com.ssafy.c204_be_api.common.web.message.ApiMessage;
import com.ssafy.c204_be_api.common.web.message.Meta;
import com.ssafy.c204_be_api.ingame.command.BulkPurchaseCommand;
import com.ssafy.c204_be_api.ingame.command.Purchase;
import com.ssafy.c204_be_api.ingame.command.PurchaseCommand;
import com.ssafy.c204_be_api.ingame.domain.Item;
import com.ssafy.c204_be_api.ingame.domain.PurchaseHistory;
import com.ssafy.c204_be_api.ingame.domain.Spell;
import com.ssafy.c204_be_api.ingame.event.InGameEvent;
import com.ssafy.c204_be_api.ingame.message.constant.InGameTopic;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.SyncResponse;
import com.ssafy.c204_be_api.ingame.message.outbound.sync.response.PurchaseSyncResponse;
import com.ssafy.c204_be_api.ingame.repository.ItemRepository;
import com.ssafy.c204_be_api.ingame.repository.PurchaseHistoryRepository;
import com.ssafy.c204_be_api.ingame.repository.SpellRepository;
import com.ssafy.c204_be_api.ingame.web.response.BulkPurchaseResponse;
import com.ssafy.c204_be_api.ingame.web.response.PurchaseResponse;
import com.ssafy.c204_be_api.member.domain.Member;
import com.ssafy.c204_be_api.member.repository.MemberRepository;
import com.ssafy.c204_be_api.validation.code.ErrorCode;
import com.ssafy.c204_be_api.validation.exception.InGameException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InGameShopService {

    private final InGameStore store;
    private final SimpMessagingTemplate messaging;
    private final ItemRepository itemRepository;
    private final SpellRepository spellRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final MemberRepository memberRepository;

    public Item findItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new InGameException(
                        ErrorCode.ITEM_NOT_FOUND, "존재하지 않는 아이템 번호 입니다.",
                        null, Meta.of("itemId", id)
                ));
    }

    public Spell findSpellById(Long id) {
        return spellRepository.findById(id)
                .orElseThrow(() -> new InGameException(
                        ErrorCode.SPELL_NOT_FOUND, "존재하지 않는 스펠 번호 입니다.",
                        null, Meta.of("spellId", id)
                ));
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public List<Spell> getAllSpells() {
        return spellRepository.findAll();
    }

    @Transactional
    public PurchaseResponse purchase(PurchaseCommand command) {
        // 회원 조회 (프록시 지연예외 방지 위해 findById 사용)
        Member member = memberRepository.findById(command.getMemberId())
                .orElseThrow(() -> new InGameException(
                        ErrorCode.INGAME_MEMBER_NOT_FOUND, "회원 정보를 찾을 수 없습니다.",
                        null, Meta.of("memberId", command.getMemberId())
                ));

        // 멱등성 체크
        boolean alreadyPurchased = purchaseHistoryRepository
                .existsByMemberAndPurchaseTypeAndPurchaseTargetIdAndIdempotencyKey(
                        member,
                        command.getPurchaseType(),
                        command.getPurchaseTargetId(),
                        command.getIdempotencyKey()
                );
        if (alreadyPurchased) {
            throw new InGameException(
                    ErrorCode.PURCHASE_ALREADY_PROCESSED, "이미 처리된 구매 요청입니다.",
                    null,
                    Meta.of(
                            "memberId", member.getId(),
                            "roomId", command.getRoomId(),
                            "purchaseType", command.getPurchaseType(),
                            "targetId", command.getPurchaseTargetId(),
                            "idempotencyKey", command.getIdempotencyKey()
                    )
            );
        }

        // 단가 계산
        int unitCost = switch (command.getPurchaseType()) {
            case ITEM -> {
                Item item = findItemById(command.getPurchaseTargetId());
                yield item.getCost();
            }
            case SPELL -> {
                Spell spell = findSpellById(command.getPurchaseTargetId());
                yield spell.getCost();
            }
            default -> throw new InGameException(
                    ErrorCode.PURCHASE_TYPE_UNSUPPORTED, "지원하지 않는 구매 유형입니다.",
                    null, Meta.of("purchaseType", command.getPurchaseType())
            );
        };

        // 코인 차감
        int totalCost = unitCost * command.getQuantity();
        if (member.getCoin() < totalCost) {
            throw new InGameException(
                    ErrorCode.INSUFFICIENT_COIN, "코인이 부족합니다.",
                    null, Meta.of("have", member.getCoin(), "need", totalCost, "unitCost", unitCost, "quantity", command.getQuantity())
            );
        }
        member.useCoin(totalCost);

        // 인게임 상태 반영 (Store 쪽에서 추가 검증/예외 발생 가능)
        long playerId = switch (command.getPurchaseType()) {
            case ITEM -> store.purchaseItem(command.getRoomId(), member.getId(), command.getPurchaseTargetId());
            case SPELL -> store.purchaseSpell(command.getRoomId(), member.getId(), command.getPurchaseTargetId());
            default -> throw new InGameException(
                    ErrorCode.PURCHASE_TYPE_UNSUPPORTED, "지원하지 않는 구매 유형입니다.",
                    null, Meta.of("purchaseType", command.getPurchaseType())
            );
        };

        // 구매 이력 저장
        PurchaseHistory history = command.toHistory(member, playerId);
        purchaseHistoryRepository.save(history);

        int totalPurchasedQuantity = purchaseHistoryRepository
                .sumPurchasedQuantity(playerId, command.getPurchaseType(), command.getPurchaseTargetId());

        return PurchaseResponse.builder()
                .remainingCoin(member.getCoin())
                .purchasedType(command.getPurchaseType())
                .purchasedTargetId(command.getPurchaseTargetId())
                .totalPurchasedQuantity(totalPurchasedQuantity)
                .build();
    }

    @Transactional
    public BulkPurchaseResponse bulkPurchase(BulkPurchaseCommand command) {
        // 1) 회원 조회
        Member member = memberRepository.findById(command.getMemberId())
                .orElseThrow(() -> new InGameException(
                        ErrorCode.INGAME_MEMBER_NOT_FOUND, "회원 정보를 찾을 수 없습니다.",
                        null, Meta.of("memberId", command.getMemberId())
                ));

        // 2) 단가/총액 계산
        int totalCost = 0;
        Map<Purchase, Integer> unitCostMap = new HashMap<>();
        for (Purchase p : command.getPurchases()) {
            int unitCost = switch (p.getPurchaseType()) {
                case ITEM -> findItemById(p.getPurchaseTargetId()).getCost();
                case SPELL -> findSpellById(p.getPurchaseTargetId()).getCost();
                default -> throw new InGameException(
                        ErrorCode.PURCHASE_TYPE_UNSUPPORTED, "지원하지 않는 구매 유형입니다.",
                        null, Meta.of("purchaseType", p.getPurchaseType())
                );
            };
            unitCostMap.put(p, unitCost);
            totalCost += unitCost * p.getQuantity();
        }

        // 3) 코인 차감
        if (member.getCoin() < totalCost) {
            throw new InGameException(
                    ErrorCode.INSUFFICIENT_COIN, "코인이 부족합니다.",
                    null, Meta.of("have", member.getCoin(), "need", totalCost)
            );
        }
        if (totalCost > 0) {
            member.useCoin(totalCost);
        }

        // 4) 인게임 store에 일괄 반영
        UUID roomId = command.getRoomId();

        store.bulkPurchase(roomId, member.getId(), command.getPurchases());
        PurchaseSyncResponse sync = (PurchaseSyncResponse) store.sync(roomId, command.getMemberId());

        // 5) 브로드 캐스트
        sendToUsers(
                roomId,
                InGameTopic.userChoice(roomId),
                InGameEvent.PURCHASED,
                "구매를 완료한 플레이어가 있습니다.");

        return BulkPurchaseResponse.builder()
                .remainingCoin(member.getCoin())
                .purchasedItem(sync.getMe().getPurchasedItems())
                .purchasedSpells(sync.getMe().getPurchasedSpells())
                .build();
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
