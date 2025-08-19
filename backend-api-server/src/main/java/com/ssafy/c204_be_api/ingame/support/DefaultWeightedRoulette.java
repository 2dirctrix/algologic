package com.ssafy.c204_be_api.ingame.support;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.random.RandomGenerator;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class DefaultWeightedRoulette implements WeightedRoulette {

    @Override
    public <K> Optional<K> pick(Map<K, ? extends Number> weights) {
        return pick(weights, ThreadLocalRandom.current());
    }

    @Override
    public <K> Optional<K> pick(Map<K, ? extends Number> weights, RandomGenerator rng) {
        if (weights == null || weights.isEmpty()) return Optional.empty();

        long total = 0L;
        List<Map.Entry<K, ? extends Number>> entries = new ArrayList<>(weights.size());

        for (var e : weights.entrySet()) {
            long w = e.getValue() == null ? 0L : e.getValue().longValue();
            if (w > 0L) {
                entries.add(e);
                total += w;
            }
        }
        if (total <= 0L || entries.isEmpty()) return Optional.empty();

        long r = 1L + rng.nextLong(total); // [1..total]
        long acc = 0L;
        for (var e : entries) {
            acc += e.getValue().longValue();
            if (r <= acc) {
                return Optional.of(e.getKey());
            }
        }
        // 논리상 도달 X, 방어용
        return Optional.of(entries.get(entries.size() - 1).getKey());
    }
}
