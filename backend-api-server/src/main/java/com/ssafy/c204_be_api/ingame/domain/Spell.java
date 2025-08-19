package com.ssafy.c204_be_api.ingame.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class Spell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spell_id")
    private Long id;

    @Column(name = "spell_name", nullable = false, length = 100)
    private String name;

    @Column(name = "spell_image_url", nullable = false, length = 255)
    @ColumnDefault("'https://picsum.photos/200'")
    private String imageUrl;

    @Column(name = "spell_description", nullable = false, length = 255)
    private String description;

    @Column(name = "spell_duration", nullable = false)
    private Integer duration;

    @Column(name = "spell_cost", nullable = false)
    private Integer cost;
}
