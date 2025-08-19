package com.ssafy.c204_be_api.authentication.util;

import com.ssafy.c204_be_api.authentication.dto.CustomUserDetails;
import com.ssafy.c204_be_api.member.domain.Member;
import org.springframework.security.core.Authentication;

import java.security.Principal;

public class PrincipalUtils {

    public static Member extractMember(Principal principal) {
        if (principal instanceof Authentication auth) {
            Object principalObj = auth.getPrincipal();
            if (principalObj instanceof CustomUserDetails userDetails) {
                return userDetails.getMember();
            }
        }
        throw new IllegalStateException("인증 정보가 유효하지 않습니다.");
    }
}
