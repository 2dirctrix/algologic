package com.ssafy.c204_be_api.problem.domain;

import com.ssafy.c204_be_api.common.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
public class ProblemCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "problem_category_id")
    private Long id;

    @Column(name = "problem_category_name", nullable = false)
    private String name;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer pickedCount;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer bannedCount;

    public void incrementPickedCount() {
        this.pickedCount++;
    }

    public void incrementBannedCount() {
        this.bannedCount++;
    }
}
