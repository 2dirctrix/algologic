package com.ssafy.c204_be_api.game.service;

import com.ssafy.c204_be_api.game.domain.Game;
import com.ssafy.c204_be_api.game.domain.GameResult;
import com.ssafy.c204_be_api.game.repository.GameRepository;
import com.ssafy.c204_be_api.game.repository.GameResultRepository;
import com.ssafy.c204_be_api.game.web.request.GameResultCreateRequest;
import com.ssafy.c204_be_api.game.web.response.GameResultResponse;
import com.ssafy.c204_be_api.player.repository.PlayerResultRepository;
import com.ssafy.c204_be_api.player.service.PlayerResultService;
import com.ssafy.c204_be_api.player.web.response.PlayerResultResponse;
import com.ssafy.c204_be_api.problem.domain.Problem;
import com.ssafy.c204_be_api.problem.repository.ProblemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameResultService {
    private final GameRepository gameRepository;
    private final GameResultRepository gameResultRepository;
    private final ProblemRepository problemRepository;
    private final PlayerResultRepository playerResultRepository;
    private final PlayerResultService playerResultService;

    @Transactional
    public Long saveGameResult(GameResultCreateRequest request) {
        Game game = gameRepository.findById(request.getGameId())
                .orElseThrow(() -> new EntityNotFoundException("해당 게임이 존재하지 않습니다"));

        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new EntityNotFoundException("해당 문제가 존재하지 않습니다."));

        GameResult gameResult = GameResult.builder()
                .game(game)
                .problem(problem)
                .startedAt(request.getStartedAt())
                .finishedAt(request.getFinishedAt())
                .build();

        gameResultRepository.save(gameResult);

        playerResultService.savePlayerResults(request.getPlayerResults(), gameResult.getId());

        return gameResult.getId();
    }

    public GameResultResponse getGameResult(Long gameResultId) {
        GameResult gameResult = gameResultRepository.findByIdWithGameAndProblem(gameResultId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게임 결과가 존재하지 않습니다."));

        List<PlayerResultResponse> prs = playerResultRepository.findByGameResultId(gameResultId).stream()
                .map(pr -> playerResultService.getPlayerResultById(pr.getId()))
                .toList();

        return toResponse(gameResult, prs);
    }

    public List<GameResultResponse> getGameResultsByMemberId(Long memberId) {
        List<GameResult> results = gameResultRepository.findAllByMemberIdWithJoins(memberId);
        if (results.isEmpty()) return List.of();

        List<Long> grIds = results.stream()
                .map(GameResult::getId)
                .toList();

        Map<Long, List<PlayerResultResponse>> prsByGrId =
                playerResultRepository.findByGameResultIdIn(grIds).stream()
                        .collect(Collectors.groupingBy(
                                pr -> pr.getGameResult().getId(),
                                Collectors.mapping(
                                        pr -> playerResultService.getPlayerResultById(pr.getId()),
                                        Collectors.toList()
                                )
                        ));

        return results.stream()
                .map(gr -> toResponse(gr, prsByGrId.getOrDefault(gr.getId(), List.of())))
                .toList();
    }

    private GameResultResponse toResponse(GameResult gameResult, List<PlayerResultResponse> prs) {
        return GameResultResponse.builder()
                .gameResultId(gameResult.getId())
                .gameName(gameResult.getGame().getName())
                .maxPlayers(gameResult.getGame().getMaxPlayers())
                .problemName(gameResult.getProblem().getName())
                .problemLevel(gameResult.getProblem().getLevel().getDescription())
                .startedAt(gameResult.getStartedAt())
                .finishedAt(gameResult.getFinishedAt())
                .playerResults(prs)
                .build();
    }
}

