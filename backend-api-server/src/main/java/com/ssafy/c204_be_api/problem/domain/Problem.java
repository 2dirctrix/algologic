package com.ssafy.c204_be_api.problem.domain;

import com.ssafy.c204_be_api.common.domain.BaseTimeEntity;
import com.ssafy.c204_be_api.problem.converter.ProblemExampleConverter;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@ToString
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@DynamicInsert
@Entity
public class Problem extends BaseTimeEntity {

    @Id
    @Column(name = "problem_id")
    private Long id;

    @Column(name = "problem_name", nullable = false)
    private String name;

    @Column(name = "problem_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProblemLevel level;

    @Column(name = "time_limit_sec", nullable = false)
    private Double timeLimitSec;

    @Column(name = "memory_limit_kb", nullable = false)
    private Integer memoryLimitKb;

    @Column(name = "problem_description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "problem_example", columnDefinition = "json", nullable = false)
    @Convert(converter = ProblemExampleConverter.class)
    private List<ProblemExample> example;

    @Column(name = "problem_constraint", columnDefinition = "TEXT", nullable = false)
    private String constraint;

}
