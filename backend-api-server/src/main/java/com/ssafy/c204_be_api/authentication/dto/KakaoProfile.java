package com.ssafy.c204_be_api.authentication.dto;

import com.ssafy.c204_be_api.common.domain.Platform;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoProfile {
    private String email;
    private Platform provider;
}
