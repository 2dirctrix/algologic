package com.ssafy.c204_be_api.player.service;

import com.ssafy.c204_be_api.game.domain.Game;
import com.ssafy.c204_be_api.game.repository.GameRepository;
import com.ssafy.c204_be_api.member.repository.MemberRepository;
import com.ssafy.c204_be_api.player.domain.Player;
import com.ssafy.c204_be_api.player.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public List<Player> createPlayers(Long gameId, List<Long> memberIds) {
        Game game = gameRepository.getReferenceById(gameId);
        List<Player> players = memberIds.stream()
                .map(memberId -> Player.builder()
                        .game(game)
                        .member(memberRepository.getReferenceById(memberId))
                        .build())
                .collect(Collectors.toList());

        log.info("Created players: {}", players);
        return playerRepository.saveAll(players);
    }
}
