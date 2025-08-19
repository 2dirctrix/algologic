package com.ssafy.c204_be_api.problem.service;

import com.ssafy.c204_be_api.problem.domain.Problem;
import com.ssafy.c204_be_api.problem.domain.ProblemCategory;
import com.ssafy.c204_be_api.problem.repository.ProblemCategoryMapRepository;
import com.ssafy.c204_be_api.problem.repository.ProblemCategoryRepository;
import com.ssafy.c204_be_api.problem.repository.ProblemRepository;
import com.ssafy.c204_be_api.problem.web.response.ProblemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProblemSearchService {

    private final ProblemRepository problemRepository;
    private final ProblemCategoryRepository problemCategoryRepository;
    private final ProblemCategoryMapRepository problemCategoryMapRepository;

    public ProblemResponse search(Long problemId) {
        Problem problem = problemRepository.findById(problemId).orElseThrow(() ->
                new IllegalArgumentException("문제 ID = %s 에 해당하는 문제를 찾을 수 없습니다.".formatted(problemId)));

        List<ProblemCategory> categories = problemCategoryRepository.findAllCategories(problemId);

        return ProblemResponse.of(problem, categories);
    }

    /**
     * 카테고리 ID에 해당하는 랜덤한 문제를 반환합니다.
     * @param categoryId 카테고리 ID
     * @return 랜덤한 문제
     */
    public Problem searchRandom(Long categoryId) {
        return problemRepository.findRandomByCategoryId(categoryId).orElseThrow(() ->
                new IllegalArgumentException("카테고리 ID = %s 에 해당하는 문제를 찾을 수 없습니다.".formatted(categoryId)));
    }
}
