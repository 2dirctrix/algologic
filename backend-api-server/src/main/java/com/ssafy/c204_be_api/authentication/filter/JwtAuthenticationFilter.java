package com.ssafy.c204_be_api.authentication.filter;

import com.ssafy.c204_be_api.authentication.token.JwtManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
    public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer";

    private final JwtManager jwtManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("Request Info: method={}, url={}, host={}, remoteAddr={}, userAgent={}",
                    request.getMethod(),
                    request.getRequestURL(),            // 전체 URL
                    request.getServerName(),            // 호스트명
                    request.getRemoteAddr(),            // 클라이언트 IP
                    request.getHeader("User-Agent")     // UA
            );
            String token = extractToken(request);

            if (jwtManager.validateToken(token)) {
                //토큰과 일치하는 회원 정보를 security context 에 저장
                Authentication authentication = jwtManager.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            //발생한 예외 자체를 entry point로 전달하기 위함
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authorization) && authorization.startsWith(BEARER)) {
            return authorization.substring(BEARER.length() + 1);
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        throw new AuthenticationCredentialsNotFoundException("%s Token 이 존재하지 않습니다.".formatted(BEARER));
    }
}
