package com.ssafy.c204_be_api.authentication.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.c204_be_api.authentication.dto.KakaoProfile;
import com.ssafy.c204_be_api.common.domain.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class KakaoOAuthService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret:}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userInfoUri;

    public String getKakaoAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        StringBuilder requestBody = new StringBuilder();
        requestBody.append("grant_type=authorization_code")
                .append("&client_id=").append(clientId)
                .append("&redirect_uri=").append(redirectUri)
                .append("&code=").append(code)
                .append("&client_secret=").append(clientSecret);

        ResponseEntity<String> response = restClient.post()
                .uri(tokenUri)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(requestBody.toString())
                .retrieve()
                .toEntity(String.class);

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("카카오 액세스 토큰 조회 실패", e);
        }
    }

    public KakaoProfile getKakaoProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "property_keys=[\"kakao_account.email\"]";

        ResponseEntity<String> response = restClient.post()
                .uri(userInfoUri)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(body)
                .retrieve()
                .toEntity(String.class);

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            String email = root.path("kakao_account").path("email").asText("");

            return new KakaoProfile(email, Platform.KAKAO);
        } catch (Exception e) {
            throw new RuntimeException("카카오 프로필 조회 실패", e);
        }
    }
}
