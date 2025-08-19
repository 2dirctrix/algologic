package com.ssafy.c204_be_api.member.service;

import com.ssafy.c204_be_api.aws.service.S3Service;
import com.ssafy.c204_be_api.common.domain.ProgrammingLanguage;
import com.ssafy.c204_be_api.member.domain.Member;
import com.ssafy.c204_be_api.member.repository.MemberRepository;
import com.ssafy.c204_be_api.member.repository.projection.CategoryCountProjection;
import com.ssafy.c204_be_api.member.repository.projection.DailyCountProjection;
import com.ssafy.c204_be_api.member.web.request.MemberUpdateRequest;
import com.ssafy.c204_be_api.member.web.response.*;
import com.ssafy.c204_be_api.player.domain.Player;
import com.ssafy.c204_be_api.player.domain.PlayerResult;
import com.ssafy.c204_be_api.player.repository.PlayerRepository;
import com.ssafy.c204_be_api.player.repository.PlayerResultRepository;
import com.ssafy.c204_be_api.problem.repository.ProblemSubmitRepository;
import com.ssafy.c204_be_api.validation.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final S3Service s3Service;
    private final MemberRepository memberRepository;
    private final PlayerRepository playerRepository;
    private final PlayerResultRepository playerResultRepository;
    private final ProblemSubmitRepository problemSubmitRepository;

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public MemberResponse findById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        // 해당 내용 더미 데이터 수정 후에 지울 예정
        String profileImageUrl;
        // 현재 더미데이터
        if (member.getProfileImageUrl().startsWith("https:")) {
            profileImageUrl = member.getProfileImageUrl();
        } else {
            // S3 Presigned URL 생성
            profileImageUrl = s3Service.getPresignedUrl(member.getProfileImageUrl());
        }

        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImageUrl(profileImageUrl)
                .programmingLanguage(member.getProgrammingLanguage().toString())
                .coin(member.getCoin())
                .score(member.getScore())
                .build();
    }

    public MemberDetailResponse getMyDetail(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        List<Player> players = playerRepository.findByMemberId(memberId);
        List<Long> playerIds = players.stream()
                .map(Player::getId)
                .toList();

        List<PlayerResult> results = playerResultRepository.findByPlayerIdIn(playerIds);

        int totalGameCount = results.size();
        Long totalWinCount = results.stream()
                .filter(r -> r.getRanks() == 1)
                .count();

        double winRate = totalGameCount == 0 ? 0.0 : round((double) totalWinCount / totalGameCount * 100, 2);

        double avgRanking = results.stream()
                .mapToInt(PlayerResult::getRanks)
                .average()
                .orElse(0.0);
        avgRanking = round(avgRanking, 2);

        Long problemSolvedCount = problemSubmitRepository.countSolvedProblemsByMember(playerIds);
        double problemSolvedRate = players.isEmpty() ? 0.0 : round((double) problemSolvedCount / players.size() * 100, 2);

        return MemberDetailResponse.builder()
                .provider(member.getProvider().getDescription())
                .createdAt(member.getCreatedAt())
                .totalGameCount(totalGameCount)
                .totalWinCount(totalWinCount)
                .winRate(winRate)
                .avgRanking(avgRanking)
                .problemSolvedRate(problemSolvedRate)
                .build();
    }

    public List<MemberResponse> findAll() {
        List<Member> members = memberRepository.findAll();
        List<MemberResponse> result = new ArrayList<>();

        for (Member member : members) {
            String profileImageUrl = member.getProfileImageUrl();
            String resolvedProfileImageUrl;

            // 해당 내용 더미 데이터 수정 후에 지울 예정
            if (profileImageUrl != null && (profileImageUrl.startsWith("https://"))) {
                resolvedProfileImageUrl = profileImageUrl;
            } else if (profileImageUrl != null && !profileImageUrl.isBlank()) {
                resolvedProfileImageUrl = s3Service.getPresignedUrl(profileImageUrl);
            } else {
                resolvedProfileImageUrl = null;
            }

            result.add(MemberResponse.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .nickname(member.getNickname())
                    .profileImageUrl(resolvedProfileImageUrl)
                    .programmingLanguage(member.getProgrammingLanguage().toString())
                    .coin(member.getCoin())
                    .score(member.getScore())
                    .build());
        }

        return result;
    }

    public List<RankerResponse> findTopMembers() {
        List<Member> topMembers = memberRepository.findTop10ByOrderByScoreDescUpdatedAtAsc();
        return RankerResponse.fromEntities(topMembers);
    }

    @Transactional
    public void addScoreAndCoin(Long memberId, Integer earnedScore, Integer earnedCoin) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        member.addCoin(earnedCoin);
        member.addScore(earnedScore);
    }

    @Transactional
    public void updateProfile(MemberUpdateRequest request, String uploadUrl, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        if (uploadUrl != null) {
            s3Service.deleteImage(member.getProfileImageUrl());
        }

        member.updateProfile(
                request.getNickname() != null ? request.getNickname() : member.getNickname(),
                uploadUrl != null ? uploadUrl : member.getProfileImageUrl(),
                request.getProgrammingLanguage() != null
                        ? ProgrammingLanguage.valueOf(request.getProgrammingLanguage())
                        : member.getProgrammingLanguage()
        );
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public List<DailyGameCountResponse> getMyDailyGameCount(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        LocalDate fromDate = member.getCreatedAt().toLocalDate();
        LocalDate toDate = LocalDate.now();

        LocalDateTime startInclusive = fromDate.atStartOfDay();
        LocalDateTime endExclusive = toDate.atStartOfDay().plusDays(1);

        List<DailyCountProjection> rows =
                memberRepository.countDailyGameResultsByMember(memberId, startInclusive, endExclusive);

        Map<LocalDate, Integer> counted = rows.stream().collect(Collectors.toMap(
                r -> r.getDate().toLocalDate(),
                r -> r.getCnt().intValue()
        ));

        List<DailyGameCountResponse> daily = new ArrayList<>();
        for (LocalDate d = fromDate; !d.isAfter(toDate); d = d.plusDays(1)) {
            daily.add(new DailyGameCountResponse(d.toString(), counted.getOrDefault(d, 0)));
        }

        return daily;
    }

    @Transactional(readOnly = true)
    public List<TopCategoryResponse> getMyTopSolvedCategories(Long memberId) {
        List<CategoryCountProjection> rows =
                memberRepository.findTopSolvedCategoriesByMember(memberId);

        return rows.stream()
                .map(r -> new TopCategoryResponse(
                        r.getProblemCategoryId(),
                        r.getProblemCategoryName(),
                        r.getSolvedCount()
                ))
                .toList();
    }


    public boolean isNicknameDuplicate(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
