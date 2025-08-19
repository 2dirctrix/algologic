package com.ssafy.c204_be_api.player.repository;

import com.ssafy.c204_be_api.player.domain.PlayerResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerResultRepository extends JpaRepository<PlayerResult, Long> {

    List<PlayerResult> findByPlayerIdIn(List<Long> playerIds);

    List<PlayerResult> findByGameResultId(Long gameResultId);

    List<PlayerResult> findByGameResultIdIn(List<Long> gameResultIds);
}

