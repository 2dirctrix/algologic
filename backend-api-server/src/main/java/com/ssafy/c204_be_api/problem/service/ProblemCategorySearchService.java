package com.ssafy.c204_be_api.problem.service;

import com.ssafy.c204_be_api.problem.domain.ProblemCategory;
import com.ssafy.c204_be_api.problem.repository.ProblemCategoryRepository;
import com.ssafy.c204_be_api.problem.web.response.ProblemCategoryRankResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProblemCategorySearchService {

    private final ProblemCategoryRepository problemCategoryRepository;

    public List<ProblemCategory> searchAll() {
        return problemCategoryRepository.findAll();
    }

    public ProblemCategory searchRandomCategory() {
        return problemCategoryRepository.findRandomCategory()
                .orElseThrow(() -> new EntityNotFoundException("No problem categories available in the system"));
    }

    public void validatePickedCategoryId(Long categoryId) {
        if (categoryId == null || !problemCategoryRepository.existsById(categoryId)) {
            throw new IllegalArgumentException("유효하지 않은 문제 카테고리 id: " + categoryId);
        }
    }

    public ProblemCategoryRankResponse getTopPickedAndBanned() {
        List<ProblemCategory> topPicked = problemCategoryRepository.findTop10ByOrderByPickedCountDescUpdatedAtDesc();
        List<ProblemCategory> topBanned = problemCategoryRepository.findTop10ByOrderByBannedCountDescUpdatedAtDesc();

        long totalPicked = problemCategoryRepository.sumPickedCount();
        long totalBanned = problemCategoryRepository.sumBannedCount();

        List<ProblemCategoryRankResponse.PickedCategoryDto> pickedRank = topPicked.stream()
                .map(pc -> new ProblemCategoryRankResponse.PickedCategoryDto(
                        pc.getId(),
                        pc.getName(),
                        rate(pc.getPickedCount(), totalPicked)
                ))
                .toList();

        List<ProblemCategoryRankResponse.BannedCategoryDto> bannedRank = topBanned.stream()
                .map(bc -> new ProblemCategoryRankResponse.BannedCategoryDto(
                        bc.getId(),
                        bc.getName(),
                        rate(bc.getBannedCount(), totalBanned)
                ))
                .toList();

        return new ProblemCategoryRankResponse(pickedRank, bannedRank);
    }

    private long rate(Integer count, long total) {
        if (total <= 0 || count == null) return 0;
        return Math.round(count * 100.0 / total);
    }
}
