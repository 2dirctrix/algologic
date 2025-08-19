package com.ssafy.c204_be_api.player.domain;

import com.ssafy.c204_be_api.common.domain.BaseTimeEntity;
import com.ssafy.c204_be_api.problem.domain.ProblemCategory;
import com.ssafy.c204_be_api.game.domain.GameResult;
import com.ssafy.c204_be_api.problem.domain.ProblemSubmit;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@ToString
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@DynamicInsert
@Entity
public class PlayerResult extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "player_result_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_result_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private GameResult gameResult;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banned_problem_category_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProblemCategory bannedProblemCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "picked_problem_category_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProblemCategory pickedProblemCategory;

    @Column(nullable = false, updatable = false)
    private Integer ranks;

    @Column(nullable = false, updatable = false)
    @ColumnDefault("0")
    private Integer earnedScore;

    @Column(nullable = false, updatable = false)
    @ColumnDefault("0")
    private Integer earnedCoin;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submit_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProblemSubmit submit;

    @Column(updatable = false)
    private Integer solveDuration;
}
