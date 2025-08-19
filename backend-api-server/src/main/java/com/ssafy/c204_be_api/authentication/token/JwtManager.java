package com.ssafy.c204_be_api.authentication.token;

import com.ssafy.c204_be_api.authentication.dto.CustomUserDetails;
import com.ssafy.c204_be_api.authentication.service.CustomUserDetailsService;
import com.ssafy.c204_be_api.member.domain.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Slf4j
@Component
public class JwtManager {

    private final CustomUserDetailsService customUserDetailsService;
    private final SecretKey key;

    @Value("${jwt.accessToken.expiration.millis}")
    private long accessTokenExpirationMillis;
    @Value("${jwt.refreshToken.expiration.days}")
    private long refreshTokenExpirationDays;

    public static final String BEARER = "Bearer";
    private static final String MEMBER_ID_CLAIM_KEY = "id";
    private static final String MEMBER_ROLE_CLAIM_KEY = "role";
    private static final String ROLE_PREFIX = "ROLE_";

    private static final double REFRESH_ROTATE_RATIO = 0.5;

    public JwtManager(@Value("${jwt.secret}") String secret, CustomUserDetailsService customUserDetailsService) {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.customUserDetailsService = customUserDetailsService;
    }

    public Token generateToken(Member member) {
        return Token.builder()
                .grantType(BEARER)
                .accessToken(createAccessToken(member))
                .refreshToken(createRefreshToken(member))
                .build();
    }

    public String createAccessToken(Member member) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date accessTokenExpiration = new Date(nowMillis + accessTokenExpirationMillis);

        return Jwts.builder()
                .subject(member.getId().toString())
                .claim(MEMBER_ID_CLAIM_KEY, member.getId())
                .claim(MEMBER_ROLE_CLAIM_KEY, member.getRole())
                .issuedAt(now)
                .expiration(accessTokenExpiration)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Member member) {
        long nowMillis = System.currentTimeMillis();
        long refreshTokenExpirationMillis = Duration.ofDays(refreshTokenExpirationDays).toMillis();
        Date now = new Date(nowMillis);
        Date refreshTokenExpiration = new Date(nowMillis + refreshTokenExpirationMillis);

        return Jwts.builder()
                .claim(MEMBER_ID_CLAIM_KEY, member.getId())
                .issuedAt(now)
                .expiration(refreshTokenExpiration)
                .signWith(key)
                .compact();
    }

    public Token refreshAccessToken(String refreshToken) {
        validateToken(refreshToken);

        String username = resolveId(refreshToken);
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
        Member member = customUserDetails.getMember();

        String refreshTokenToReturn = shouldRotateRefreshToken(refreshToken)
                ? createRefreshToken(member)
                : refreshToken;

        return Token.builder()
                .grantType(BEARER)
                .accessToken(createAccessToken(member))
                .refreshToken(refreshTokenToReturn)
                .build();
    }

    private boolean shouldRotateRefreshToken(String refreshToken) {
        Claims claims = resolve(refreshToken);
        Date exp = claims.getExpiration();
        Date iat = claims.getIssuedAt();

        long remainingMillis = exp.getTime() - System.currentTimeMillis();

        if (remainingMillis <= 0) return false;

        long refreshTokenExpirationMillis = (iat != null)
                ? exp.getTime() - iat.getTime()
                : Duration.ofDays(refreshTokenExpirationDays).toMillis();

        long thresholdMillis = (long) (refreshTokenExpirationMillis * REFRESH_ROTATE_RATIO);

        return remainingMillis <= thresholdMillis;
    }

    /**
     * JWT 액세스 토큰을 기반으로 인증(Authentication) 객체를 생성합니다.
     *
     * <p>이 메서드는 Spring Security의 필터 체인에서 사용되며, JWT 토큰을 파싱하여
     * 사용자 식별자(subject)를 추출하고, 그에 해당하는 사용자 정보를 데이터베이스에서
     * 조회하여 UserDetails 객체를 생성합니다.</p>
     *
     * <p>추출된 사용자 권한 정보는 {@code SimpleGrantedAuthority}로 변환되며,
     * 최종적으로 {@code UsernamePasswordAuthenticationToken}을 반환합니다.</p>
     *
     * @param accessToken 클라이언트가 요청 헤더에 포함시킨 JWT Access Token
     * @return JWT로부터 생성된 인증(Authentication) 객체
     * @throws SecurityException JWT에 권한 정보가 포함되지 않은 경우
     */
    public Authentication getAuthentication(String accessToken) {
        Claims claims = resolve(accessToken);

        if (claims.get(MEMBER_ROLE_CLAIM_KEY) == null) {
            throw new SecurityException("accessToken = %s 는 권한 정보가 없는 토큰 입니다.".formatted(accessToken));
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(MEMBER_ROLE_CLAIM_KEY).toString().split(","))
                        .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                        .toList();

        UserDetails principal = customUserDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private Claims resolve(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String resolveId(String token) {
        return resolve(token).get(MEMBER_ID_CLAIM_KEY).toString();
    }

    /**
     * 파라미터로 전달받은 토큰이 유효한지 검사합니다
     *
     * @param token 검사 대상
     * @return 토큰이 유효하면 {@code true}, 그렇지 않으면 {@code false}
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.debug("Invalid JWT Token", e);
            throw e;
        } catch (ExpiredJwtException e) {
            log.debug("Expired JWT Token", e);
            throw e;
        } catch (UnsupportedJwtException e) {
            log.debug("Unsupported JWT Token", e);
            throw e;
        } catch (IllegalArgumentException e) {
            log.debug("JWT claims string is empty.", e);
            throw e;
        }
    }
}
