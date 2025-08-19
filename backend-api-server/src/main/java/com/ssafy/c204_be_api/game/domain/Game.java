package com.ssafy.c204_be_api.game.domain;

import com.ssafy.c204_be_api.common.domain.BaseTimeEntity;
import com.ssafy.c204_be_api.common.domain.ProgrammingLanguage;
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
public class Game extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long id;

    @Column(name = "game_name", nullable = false)
    private String name;

    @Column(nullable = false)
    @ColumnDefault("2")
    private Integer maxPlayers;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private GameType gameType;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ProgrammingLanguage programmingLanguage;

    @Column(nullable = false)
    @ColumnDefault("3600")
    private Integer timeLimit;
}
