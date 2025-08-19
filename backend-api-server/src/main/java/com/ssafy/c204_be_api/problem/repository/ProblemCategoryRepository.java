package com.ssafy.c204_be_api.problem.repository;

import com.ssafy.c204_be_api.problem.domain.ProblemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemCategoryRepository extends JpaRepository<ProblemCategory, Long> {

    @Query("""
        SELECT pc
        FROM ProblemCategory pc
        JOIN ProblemCategoryMap pcm
          ON pc.id = pcm.problemCategory.id
        WHERE pcm.problem.id = :problemId
    """)
    List<ProblemCategory> findAllCategories(Long problemId);

    @Query("""
        SELECT pc
        FROM ProblemCategory pc
        ORDER BY function('RAND')
        LIMIT 1
    """)
    Optional<ProblemCategory> findRandomCategory();

    boolean existsById(Long problemId);

    List<ProblemCategory> findTop10ByOrderByPickedCountDescUpdatedAtDesc();

    List<ProblemCategory> findTop10ByOrderByBannedCountDescUpdatedAtDesc();

    @Query("select coalesce(sum(pc.pickedCount), 0) from ProblemCategory pc")
    long sumPickedCount();

    @Query("select coalesce(sum(pc.bannedCount), 0) from ProblemCategory pc")
    long sumBannedCount();
}
