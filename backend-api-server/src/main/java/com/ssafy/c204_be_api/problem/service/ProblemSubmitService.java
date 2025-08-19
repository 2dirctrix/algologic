package com.ssafy.c204_be_api.problem.service;

import com.ssafy.c204_be_api.aws.service.SQSService;
import com.ssafy.c204_be_api.aws.service.message.JudgeRequestMessage;
import com.ssafy.c204_be_api.common.domain.ProgrammingLanguage;
import com.ssafy.c204_be_api.player.domain.Player;
import com.ssafy.c204_be_api.player.repository.PlayerRepository;
import com.ssafy.c204_be_api.problem.domain.Problem;
import com.ssafy.c204_be_api.problem.domain.ProblemSubmit;
import com.ssafy.c204_be_api.problem.repository.ProblemRepository;
import com.ssafy.c204_be_api.problem.repository.ProblemSubmitRepository;
import com.ssafy.c204_be_api.problem.web.message.JudgeResultMessage;
import com.ssafy.c204_be_api.problem.web.request.ProblemSubmitRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemSubmitService {

    private final SQSService sqsService;
    private final ProblemRepository problemRepository;
    private final ProblemSubmitRepository problemSubmitRepository;
    private final PlayerRepository playerRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void submit(Long problemId, Long memberId, ProblemSubmitRequest request) {
        Problem problem = problemRepository.findById(problemId).orElseThrow(() ->
                new EntityNotFoundException("문제 ID = %s 에 해당하는 문제를 찾을 수 없습니다.".formatted(problemId)));
        System.out.println(LocalDateTime.now(ZoneOffset.UTC));
        sqsService.sendToJudgeQueue(JudgeRequestMessage.builder()
                .playerId(request.playerId())
                .memberId(memberId)
                .problemId(problemId)
                .programmingLanguage(request.programmingLanguage())
                .sourceCode(request.sourceCode())
                .timeLimit(problem.getTimeLimitSec())
                .memoryLimit(problem.getMemoryLimitKb())
                .submittedAt(LocalDateTime.now(ZoneOffset.UTC))
                .build());
    }

    public void saveAndNotifyJudgeResult(Long memberId, JudgeResultMessage message) {
        Problem problem = problemRepository.findById(message.problemId()).orElseThrow(() -> new EntityNotFoundException(
                "문제 ID = %s 에 해당하는 문제를 찾을 수 없습니다.".formatted(message.problemId())));
        Player player = playerRepository.findById(message.playerId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "플레이어 ID = %s 에 해당하는 플레이어를 찾을 수 없습니다.".formatted(message.playerId())));

        notifySubmit(memberId, message);

        saveResult(ProblemSubmit.builder()
                .problem(problem)
                .player(player)
                .memory(message.maxMemoryUsage())
                .runningTime(message.maxRunningTime())
                .programmingLanguage(ProgrammingLanguage.fromDisplayName(message.programmingLanguage()))
                .isCorrect(message.isSolved())
                .submittedAt(message.submittedAt())
                .build());
    }

    @Transactional
    public void saveResult(ProblemSubmit problemSubmit) {
        problemSubmitRepository.save(problemSubmit);
    }

    public void notifySubmit(Long memberId, JudgeResultMessage message) {
        messagingTemplate.convertAndSendToUser(memberId.toString(), "/queue/judge-result", message);
    }

    public List<ProblemSubmit> findAllByPlayerIds(List<Long> playerIds) {
        return problemSubmitRepository.findAllByPlayer_IdInAndIsCorrectTrue(playerIds);
    }
}
