package com.ssafy.c204_be_api.ingame.support;

import java.util.Map;
import java.util.Optional;
import java.util.random.RandomGenerator;

public interface WeightedRoulette {

    /** ThreadLocalRandom 기반 기본 선택 */
    <K> Optional<K> pick(Map<K, ? extends Number> weights);

    /** 커스텀 난수원(고정 시드 등) 사용 선택 */
    <K> Optional<K> pick(Map<K, ? extends Number> weights, RandomGenerator rng);
}
