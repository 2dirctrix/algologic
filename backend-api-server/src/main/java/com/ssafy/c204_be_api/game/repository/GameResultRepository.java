package com.ssafy.c204_be_api.game.repository;

import com.ssafy.c204_be_api.game.domain.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {

    @Query("""
                select gr
                from GameResult gr
                join fetch gr.game g
                join fetch gr.problem p
                where gr.id = :id
            """)
    Optional<GameResult> findByIdWithGameAndProblem(@Param("id") Long id);

    @Query("""
                select distinct gr
                from PlayerResult pr
                join pr.player pl
                join pr.gameResult gr
                join fetch gr.game g
                join fetch gr.problem p
                where pl.member.id = :memberId
                order by gr.finishedAt desc
            """)
    List<GameResult> findAllByMemberIdWithJoins(@Param("memberId") Long memberId);

}
