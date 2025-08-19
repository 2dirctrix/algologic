package com.ssafy.c204_be_api.problem.repository;

import com.ssafy.c204_be_api.problem.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    @Query(
        """
            SELECT pcm.problem
            FROM ProblemCategoryMap pcm
            WHERE pcm.problemCategory.id = :categoryId
            ORDER BY RAND()
            LIMIT 1
        """
    )
    Optional<Problem> findRandomByCategoryId(Long categoryId);

}
