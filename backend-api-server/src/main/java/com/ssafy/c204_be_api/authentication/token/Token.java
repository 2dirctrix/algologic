package com.ssafy.c204_be_api.authentication.token;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Token {

    private String grantType; //인증 타입
    private String accessToken;
    private String refreshToken;
}
