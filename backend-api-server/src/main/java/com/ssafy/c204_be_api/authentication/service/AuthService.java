package com.ssafy.c204_be_api.authentication.service;

import com.ssafy.c204_be_api.authentication.dto.KakaoProfile;
import com.ssafy.c204_be_api.authentication.web.request.SignupRequest;
import com.ssafy.c204_be_api.authentication.web.response.KakaoLoginResponse;
import com.ssafy.c204_be_api.common.domain.Platform;
import com.ssafy.c204_be_api.common.domain.ProgrammingLanguage;
import com.ssafy.c204_be_api.member.domain.Member;
import com.ssafy.c204_be_api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoOAuthService kakaoOAuthService;
    private final MemberRepository memberRepository;

    public KakaoLoginResponse kakaoLogin(String code) {
        String kakaoAccessToken = kakaoOAuthService.getKakaoAccessToken(code);
        KakaoProfile profile = kakaoOAuthService.getKakaoProfile(kakaoAccessToken);

        Optional<Member> optionalMember = memberRepository.findByEmail(profile.getEmail());

        if (optionalMember.isPresent()) {
            return KakaoLoginResponse.ofExistingMember(profile.getEmail());
        }

        return KakaoLoginResponse.ofNewMember(profile);
    }

    @Transactional
    public void signup(SignupRequest signupRequest) {
        Member member = Member.builder()
                .email(signupRequest.getEmail())
                .nickname(signupRequest.getNickname())
                .provider(Platform.KAKAO)
                .programmingLanguage(ProgrammingLanguage.valueOf(signupRequest.getProgrammingLanguage()))
                .build();

        memberRepository.save(member);
    }
}
