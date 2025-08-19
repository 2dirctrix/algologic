package com.ssafy.c204_be_api.game.repository;

import com.ssafy.c204_be_api.game.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
