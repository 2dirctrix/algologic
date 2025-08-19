package com.ssafy.c204_be_api.member.domain;

import com.ssafy.c204_be_api.common.domain.BaseTimeEntity;
import com.ssafy.c204_be_api.common.domain.Platform;
import com.ssafy.c204_be_api.common.domain.ProgrammingLanguage;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

@ToString
@Getter
@SuperBuilder
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@DynamicInsert
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    private Long id;

    @Column(updatable = false, nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Platform provider;

    @Column(nullable = false)
    @ColumnDefault("'https://picsum.photos/200'")
    private String profileImageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProgrammingLanguage programmingLanguage;

    @Column(nullable = false)
    @ColumnDefault("3000")
    private Integer coin;

    @Column(nullable = false)
    @ColumnDefault("800")
    private Integer score;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean isDeleted;

    public String getRole() {
        return "NO_ROLE";
    }

    public void addScore(int earnedScore) {
        this.score += earnedScore;
        this.score = Math.max(300, this.score);  // score 최솟값 300
    }

    public void addCoin(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("획득 코인은 음수일 수 없습니다.");
        }
        this.coin += amount;
    }

    public void useCoin(int amount) {
        if (amount < 0) {
            throw new IllegalStateException("사용 코인은 음수일 수 없습니다.");
        }
        this.coin -= amount;
    }

    public void updateProfile(String nickname, String profileImageUrl, ProgrammingLanguage programmingLanguage) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (profileImageUrl != null) {
            this.profileImageUrl = profileImageUrl;
        }
        if (programmingLanguage != null) {
            this.programmingLanguage = programmingLanguage;
        }
    }
}
