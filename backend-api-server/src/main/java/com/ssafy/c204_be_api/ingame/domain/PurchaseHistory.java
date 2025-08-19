package com.ssafy.c204_be_api.ingame.domain;

import com.ssafy.c204_be_api.common.domain.BaseTimeEntity;
import com.ssafy.c204_be_api.game.domain.Game;
import com.ssafy.c204_be_api.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {
                "member_id", "purchase_type", "purchaseTargetId", "idempotencyKey"
        })
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class PurchaseHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PurchaseType purchaseType;

    @Column(nullable = false)
    private Long purchaseTargetId;

    @Column(nullable = false, length = 128)
    private String idempotencyKey;

    @Column(nullable = false)
    private int quantity;

}