package com.ssafy.c204_be_api.problem.web.response;

import java.util.List;

public record ProblemCategoryRankResponse(
        List<PickedCategoryDto> topPicked,
        List<BannedCategoryDto> topBanned
) {
    public record PickedCategoryDto(Long problemCategoryId, String name, long pickedRate) {}
    public record BannedCategoryDto(Long problemCategoryId, String name, long bannedRate) {}
}
