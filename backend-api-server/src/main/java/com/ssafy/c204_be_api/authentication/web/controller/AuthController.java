package com.ssafy.c204_be_api.authentication.web.controller;

import com.ssafy.c204_be_api.authentication.service.AuthService;
import com.ssafy.c204_be_api.authentication.token.JwtManager;
import com.ssafy.c204_be_api.authentication.token.Token;
import com.ssafy.c204_be_api.authentication.web.request.LoginRequest;
import com.ssafy.c204_be_api.authentication.web.request.SignupRequest;
import com.ssafy.c204_be_api.authentication.web.response.KakaoLoginResponse;
import com.ssafy.c204_be_api.common.web.response.ApiResponse;
import com.ssafy.c204_be_api.member.domain.Member;
import com.ssafy.c204_be_api.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "인증 API")
public class AuthController {

    private final JwtManager jwtManager;
    private final AuthService authService;
    private final MemberService memberService;

    @PostMapping("/kakao/access-token")
    @Operation(summary = "로그인", description = "카카오 인가 코드로 사용자 정보를 조회하고, 회원가입이 필요할 경우 회원가입 요청을 반환")
    public ResponseEntity<ApiResponse<KakaoLoginResponse>> kakaoLogin(@RequestBody LoginRequest request, HttpServletResponse response) {
        String code = request.getCode();
        KakaoLoginResponse loginResponse = authService.kakaoLogin(code);

        // 회원가입이 필요하지 않을 경우 JWT, 쿠키 발급
        if (!loginResponse.isNeedSignup()) {
            Optional<Member> optionalMember = memberService.findByEmail(loginResponse.getEmail());

            if (optionalMember.isPresent()) {
                Member member = optionalMember.get();
                Token jwt = jwtManager.generateToken(member);
                addJwtCookiesToResponse(jwt, response);
            }
            return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "액세스 토큰 발급 완료", loginResponse));
        }
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "회원가입 필요", loginResponse));
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "닉네임, 주언어 입력받고 회원가입 요청")
    public ResponseEntity<ApiResponse<String>> signup(@Valid @RequestBody SignupRequest signupRequest, HttpServletResponse response) {
        if (memberService.isNicknameDuplicate(signupRequest.getNickname())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.with(HttpStatus.BAD_REQUEST, "이미 사용 중인 닉네임입니다."));
        }

        authService.signup(signupRequest);

        Optional<Member> optionalMember = memberService.findByEmail(signupRequest.getEmail());

        if (optionalMember.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.with(HttpStatus.BAD_REQUEST, "회원가입 실패: 회원 정보를 찾을 수 없습니다."));
        }

        Member member = optionalMember.get();
        Token jwt = jwtManager.generateToken(member);
        addJwtCookiesToResponse(jwt, response);

        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "회원가입 완료"));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "JWT 쿠키를 삭제하여 로그아웃 처리")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletResponse response) {
        removeJwtCookiesFromResponse(response);
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "로그아웃 완료"));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "액세스 토큰 갱신", description = "REFRESH TOKEN을 이용하여 새로운 액세스 토큰 발급")
    public ResponseEntity<ApiResponse<String>> refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.with(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 없습니다."));

        Token newToken = jwtManager.refreshAccessToken(refreshToken);
        addJwtCookiesToResponse(newToken, response);
        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "액세스 토큰 갱신 완료"));
    }

    private void addJwtCookiesToResponse(Token jwt, HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("access_token", jwt.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setMaxAge(60 * 60 * 6); // 6시간

        Cookie refreshTokenCookie = new Cookie("refresh_token", jwt.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 30); // 30일

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }

    private void removeJwtCookiesFromResponse(HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("access_token", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);

        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }
}
