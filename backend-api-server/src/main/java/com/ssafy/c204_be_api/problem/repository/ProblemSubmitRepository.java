package com.ssafy.c204_be_api.problem.repository;

import com.ssafy.c204_be_api.problem.domain.ProblemSubmit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProblemSubmitRepository extends JpaRepository<ProblemSubmit, Long> {

    @Query("SELECT COUNT(DISTINCT ps.player.id) FROM ProblemSubmit ps WHERE ps.player.id IN :playerIds AND ps.isCorrect = true")
    Long countSolvedProblemsByMember(@Param("playerIds") List<Long> playerIds);

    List<ProblemSubmit> findAllByPlayer_IdInAndIsCorrectTrue(Collection<Long> playerIds);
}
