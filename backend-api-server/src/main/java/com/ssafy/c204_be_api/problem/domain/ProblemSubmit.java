package com.ssafy.c204_be_api.problem.domain;

import com.ssafy.c204_be_api.common.domain.BaseTimeEntity;
import com.ssafy.c204_be_api.common.domain.ProgrammingLanguage;
import com.ssafy.c204_be_api.player.domain.Player;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@ToString
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@DynamicInsert
@Entity
public class ProblemSubmit extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "problem_submit_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Player player;

    @Column(updatable = false)
    private Double runningTime;

    @Column(updatable = false)
    private Integer memory;

    @Column(nullable = false, updatable = false)
    @ColumnDefault("''")
    private String sourceCode;

    @Column(nullable = false, updatable = false)
    @Enumerated(value = EnumType.STRING)
    private ProgrammingLanguage programmingLanguage;

    @Column(nullable = false, updatable = false)
    @ColumnDefault("false")
    private Boolean isCorrect;

    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt;
}
