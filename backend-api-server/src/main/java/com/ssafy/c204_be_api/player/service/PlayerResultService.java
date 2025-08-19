package com.ssafy.c204_be_api.player.service;

import com.ssafy.c204_be_api.common.web.event.Event;
import com.ssafy.c204_be_api.common.web.message.ApiMessage;
import com.ssafy.c204_be_api.game.domain.GameResult;
import com.ssafy.c204_be_api.game.repository.GameResultRepository;
import com.ssafy.c204_be_api.member.domain.Member;
import com.ssafy.c204_be_api.member.service.MemberService;
import com.ssafy.c204_be_api.member.web.event.MemberEvent;
import com.ssafy.c204_be_api.member.web.response.RankerResponse;
import com.ssafy.c204_be_api.player.domain.Player;
import com.ssafy.c204_be_api.player.domain.PlayerResult;
import com.ssafy.c204_be_api.player.repository.PlayerRepository;
import com.ssafy.c204_be_api.player.repository.PlayerResultRepository;
import com.ssafy.c204_be_api.player.web.request.PlayerResultRequest;
import com.ssafy.c204_be_api.player.web.response.PlayerResultResponse;
import com.ssafy.c204_be_api.problem.domain.ProblemCategory;
import com.ssafy.c204_be_api.problem.domain.ProblemSubmit;
import com.ssafy.c204_be_api.problem.repository.ProblemCategoryRepository;
import com.ssafy.c204_be_api.problem.repository.ProblemSubmitRepository;
import com.ssafy.c204_be_api.problem.service.ProblemCategorySearchService;
import com.ssafy.c204_be_api.problem.web.event.ProblemCategoryEvent;
import com.ssafy.c204_be_api.problem.web.response.ProblemCategoryRankResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerResultService {
    private final PlayerResultRepository playerResultRepository;
    private final GameResultRepository gameResultRepository;
    private final PlayerRepository playerRepository;
    private final ProblemCategoryRepository problemCategoryRepository;
    private final ProblemCategorySearchService problemCategorySearchService;
    private final ProblemSubmitRepository problemSubmitRepository;
    private final MemberService memberService;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void savePlayerResults(List<PlayerResultRequest> playerResults, Long gameResultId) {
        List<RankerResponse> lastRankers = memberService.findTopMembers();
        ProblemCategoryRankResponse lastProblemCategoryRank = problemCategorySearchService.getTopPickedAndBanned();

        for (PlayerResultRequest req : playerResults) {
            PlayerResult playerResult = buildPlayerResult(req, gameResultId);
            playerResultRepository.save(playerResult);

            updateMemberScoreAndCoin(playerResult.getPlayer(), req.getEarnedScore(), req.getEarnedCoin());
        }

        notifyIfRankerChanged(lastRankers);
        notifyIfProblemCategoryRankChanged(lastProblemCategoryRank);
    }

    public PlayerResultResponse getPlayerResultById(Long playerResultId) {
        PlayerResult playerResult = playerResultRepository.findById(playerResultId)
                .orElseThrow(() -> new EntityNotFoundException("플레이어 결과가 존재하지 않습니다."));

        Member member = playerResult.getPlayer().getMember();
        ProblemSubmit submit = playerResult.getSubmit();
        Integer runningTime = submit == null ? null : (int) (submit.getRunningTime() * 1000);
        Integer memory = submit == null ? null : submit.getMemory();

        return PlayerResultResponse.builder()
                .playerResultId(playerResult.getId())
                .memberId(member.getId())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .score(member.getScore())
                .rank(playerResult.getRanks())
                .earnedScore(playerResult.getEarnedScore())
                .earnedCoin(playerResult.getEarnedCoin())
                .bannedProblemCategoryName(
                        playerResult.getBannedProblemCategory() != null
                                ? playerResult.getBannedProblemCategory().getName()
                                : null
                )
                .pickedProblemCategoryName(
                        playerResult.getPickedProblemCategory() != null
                                ? playerResult.getPickedProblemCategory().getName()
                                : null
                )
                .solveDuration(playerResult.getSolveDuration())
                .runningTime(runningTime)
                .memory(memory)
                .build();
    }


    private PlayerResult buildPlayerResult(PlayerResultRequest req, Long gameResultId) {
        GameResult gameResult = gameResultRepository.findById(gameResultId)
                .orElseThrow(() -> new EntityNotFoundException("게임 결과가 존재하지 않습니다."));
        Player player = playerRepository.findById(req.getPlayerId())
                .orElseThrow(() -> new EntityNotFoundException("플레이어가 존재하지 않습니다."));

        ProblemCategory bannedProblemCategory = null;
        if (req.getBannedProblemCategoryId() != null) {
            bannedProblemCategory = problemCategoryRepository.findById(req.getBannedProblemCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Banned ProblemCategory가 존재하지 않습니다."));
        }
        ProblemCategory pickedProblemCategory = null;
        if (req.getPickedProblemCategoryId() != null) {
            pickedProblemCategory = problemCategoryRepository.findById(req.getPickedProblemCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Picked ProblemCategory가 존재하지 않습니다."));
        }

        if (bannedProblemCategory != null) {
            bannedProblemCategory.incrementBannedCount();
        }
        if (pickedProblemCategory != null) {
            pickedProblemCategory.incrementPickedCount();
        }

        ProblemSubmit submit = null;
        if ( req.getSubmitId() != null) {
            submit = problemSubmitRepository.findById(req.getSubmitId())
                    .orElseThrow(() -> new EntityNotFoundException("정답 제출 기록이 존재하지 않습니다."));
        }

        return PlayerResult.builder()
                .gameResult(gameResult)
                .player(player)
                .bannedProblemCategory(bannedProblemCategory)
                .pickedProblemCategory(pickedProblemCategory)
                .ranks(req.getRanks())
                .earnedScore(req.getEarnedScore())
                .earnedCoin(req.getEarnedCoin())
                .submit(submit)
                .solveDuration(req.getSolveDuration())
                .build();
    }

    private void updateMemberScoreAndCoin(Player player, int earnedScore, int earnedCoin) {
        Long memberId = player.getMember().getId();
        memberService.addScoreAndCoin(memberId, earnedScore, earnedCoin);
    }

    private void notifyIfRankerChanged(List<RankerResponse> lastRankers) {
        List<RankerResponse> curRankers = memberService.findTopMembers();
        if (!lastRankers.equals(curRankers)) {
            messagingTemplate.convertAndSend("/topic/rankers",
                    ApiMessage.event(HttpStatus.OK.value(), "실시간 랭커 정보가 갱신되었습니다.",
                            Event.of(MemberEvent.RANKER_UPDATED, curRankers)));
        }
    }

    private void notifyIfProblemCategoryRankChanged(ProblemCategoryRankResponse lastProblemCategoryRank) {
        ProblemCategoryRankResponse curProblemCategoryRank = problemCategorySearchService.getTopPickedAndBanned();
        if (!lastProblemCategoryRank.equals(curProblemCategoryRank)) {
            messagingTemplate.convertAndSend("/topic/problems/category/rank",
                    ApiMessage.event(HttpStatus.OK.value(), "알고리즘 유형 랭크가 갱신되었습니다.",
                            Event.of(ProblemCategoryEvent.PROBLEM_CATEGORY_RANK_UPDATED, curProblemCategoryRank)));
        }
    }
}
