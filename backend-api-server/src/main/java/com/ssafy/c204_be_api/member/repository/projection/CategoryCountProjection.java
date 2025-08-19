package com.ssafy.c204_be_api.member.repository.projection;

public interface CategoryCountProjection {
    Long getProblemCategoryId();
    String getProblemCategoryName();
    Integer getSolvedCount();
}
