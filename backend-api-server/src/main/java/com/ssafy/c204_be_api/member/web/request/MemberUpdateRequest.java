package com.ssafy.c204_be_api.member.web.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberUpdateRequest {

    @Size(min = 2, max = 10, message = "닉네임은 2~10자 이내여야 합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "닉네임은 한글, 영어, 숫자만 사용할 수 있습니다.")
    private String nickname;
    private String programmingLanguage;
}
