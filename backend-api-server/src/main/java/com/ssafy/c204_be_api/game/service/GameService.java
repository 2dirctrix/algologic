package com.ssafy.c204_be_api.game.service;

import com.ssafy.c204_be_api.game.domain.Game;
import com.ssafy.c204_be_api.game.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    public Game createGame(Game game) {
        Game savedGame = gameRepository.save(game);
        log.info("Game created: {}", savedGame);
        return savedGame;
    }
}
