package com.ssafy.c204_be_api.player.repository;

import com.ssafy.c204_be_api.player.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByMemberId(Long memberId);

    Optional<Long> findPlayerIdByGameIdAndMemberId(Long gameId, Long memberId);

}
