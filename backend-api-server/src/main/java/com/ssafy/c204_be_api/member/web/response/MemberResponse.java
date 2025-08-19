package com.ssafy.c204_be_api.member.web.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {
    private Long id;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private String programmingLanguage;
    private Integer coin;
    private Integer score;
}
