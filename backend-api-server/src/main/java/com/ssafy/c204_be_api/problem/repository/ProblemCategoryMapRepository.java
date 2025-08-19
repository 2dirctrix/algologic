package com.ssafy.c204_be_api.problem.repository;

import com.ssafy.c204_be_api.problem.domain.ProblemCategoryMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemCategoryMapRepository extends JpaRepository<ProblemCategoryMap, Long> {
}
