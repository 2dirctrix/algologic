package com.ssafy.c204_be_api.ingame.repository;

import com.ssafy.c204_be_api.ingame.domain.PurchaseHistory;
import com.ssafy.c204_be_api.ingame.domain.PurchaseType;
import com.ssafy.c204_be_api.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory,Long> {
    boolean existsByMemberAndPurchaseTypeAndPurchaseTargetIdAndIdempotencyKey(
            Member member,
            PurchaseType purchaseType,
            Long purchaseTargetId,
            String idempotencyKey
    );

    @Query("SELECT COALESCE(SUM(p.quantity), 0) FROM PurchaseHistory p " +
            "WHERE p.playerId = :playerId " +
            "AND p.purchaseType = :purchaseType " +
            "AND p.purchaseTargetId = :purchaseTargetId")
    int sumPurchasedQuantity(
            @Param("playerId") Long playerId,
            @Param("purchaseType") PurchaseType purchaseType,
            @Param("purchaseTargetId") Long purchaseTargetId
    );
}