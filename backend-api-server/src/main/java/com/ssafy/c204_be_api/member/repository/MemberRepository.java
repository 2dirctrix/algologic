package com.ssafy.c204_be_api.member.repository;

import com.ssafy.c204_be_api.member.domain.Member;
import com.ssafy.c204_be_api.member.repository.projection.CategoryCountProjection;
import com.ssafy.c204_be_api.member.repository.projection.DailyCountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    List<Member> findTop10ByOrderByScoreDescUpdatedAtAsc();

    boolean existsByNickname(String nickname);

    @Query(value = """
        SELECT DATE(gr.started_at) AS date,
               COUNT(DISTINCT gr.game_result_id) AS cnt
        FROM player_result pr
        JOIN player p
          ON p.player_id = pr.player_id
        JOIN game_result gr
          ON gr.game_result_id = pr.game_result_id
        WHERE p.member_id = :memberId
          AND gr.started_at >= :startInclusive
          AND gr.started_at < :endExclusive
        GROUP BY DATE(gr.started_at)
        ORDER BY DATE(gr.started_at)
    """, nativeQuery = true)
    List<DailyCountProjection> countDailyGameResultsByMember(
            @Param("memberId") Long memberId,
            @Param("startInclusive") LocalDateTime startInclusive,
            @Param("endExclusive") LocalDateTime endExclusive
    );

    @Query(value = """
    SELECT
      pc.problem_category_id   AS problemCategoryId,
      pc.problem_category_name AS problemCategoryName,
      IFNULL(cnt.solvedCount, 0) AS solvedCount
    FROM problem_category pc
    LEFT JOIN (
        SELECT
          pcm.problem_category_id,
          COUNT(*) AS solvedCount
        FROM player_result pr
        JOIN player               p   ON p.player_id = pr.player_id
        JOIN game_result          gr  ON gr.game_result_id = pr.game_result_id
        JOIN problem_category_map pcm ON pcm.problem_id = gr.problem_id
        WHERE p.member_id = :memberId
          AND pr.submit_id IS NOT NULL
        GROUP BY pcm.problem_category_id
    ) cnt ON cnt.problem_category_id = pc.problem_category_id
    ORDER BY solvedCount DESC, pc.problem_category_id ASC
    LIMIT 6
""", nativeQuery = true)
    List<CategoryCountProjection> findTopSolvedCategoriesByMember(
            @Param("memberId") Long memberId
    );
}
