package com.ssafy.c204_be_api.problem.domain;

import com.ssafy.c204_be_api.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

@ToString
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@DynamicInsert
@Entity
public class ProblemCategoryMap extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "problem_category_map_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_category_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProblemCategory problemCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Problem problem;

}
