package com.ssafy.c204_be_api.authentication.web.response;

import com.ssafy.c204_be_api.authentication.dto.KakaoProfile;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class KakaoLoginResponse {
    private String email;
    private boolean needSignup;

    public static KakaoLoginResponse ofExistingMember(String email) {
        return KakaoLoginResponse.builder()
                .email(email)
                .needSignup(false)
                .build();
    }

    public static KakaoLoginResponse ofNewMember(KakaoProfile profile) {
        return KakaoLoginResponse.builder()
                .email(profile.getEmail())
                .needSignup(true)
                .build();
    }
}