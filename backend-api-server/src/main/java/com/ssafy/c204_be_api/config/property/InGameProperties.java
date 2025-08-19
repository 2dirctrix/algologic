package com.ssafy.c204_be_api.config.property;

import com.ssafy.c204_be_api.game.domain.GameType;
import com.ssafy.c204_be_api.ingame.domain.InGamePhase;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.EnumMap;
import java.util.Map;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "ingame")
public class InGameProperties {

    /** 각 단계별 제한 시간(ms) */
    private Map<InGamePhase, Duration> phaseDurations;

    /** phase 전환 후 padding 시간(ms) */
    private Duration paddingDuration;

    private Map<GameType, Scoring> scoring = new EnumMap<>(GameType.class);


    /** GameType별 정책 조회 (없으면 NORMAL) */
    public Scoring scoringFor(GameType type) {
        if (scoring == null || scoring.isEmpty()) {
            return new Scoring();  // 기본값으로 설정
        }
        Scoring direct = scoring.get(type);
        if (direct != null) return direct;

        Scoring normal = scoring.get(GameType.NORMAL);
        return normal != null ? normal : new Scoring();
    }

    @Getter
    @Setter
    public static class Scoring {
        /** 문제를 풀지 못한 경우 보상 */
        private Unsolved unsolved = new Unsolved();
        /** 문제를 푼 경우 보상 */
        private Solved solved = new Solved();
    }

    @Getter
    @Setter
    public static class Unsolved {
        /** 기본: -100 */
        private Integer score = -100;
        /** 기본: 0 */
        private Integer coin  = 0;
    }

    @Getter
    @Setter
    public static class Solved {
        /** 기본: 100 */
        private Integer baseScore = 100;
        /** 기본: 1000 */
        private Integer baseCoin  = 1000;

    }
}