package com.ssafy.c204_be_api.livekit.service;

import com.ssafy.c204_be_api.livekit.web.request.TokenRequest;
import com.ssafy.c204_be_api.livekit.web.response.TokenResponse;
import io.livekit.server.AccessToken;
import io.livekit.server.RoomJoin;
import io.livekit.server.RoomName;
import io.livekit.server.WebhookReceiver;
import livekit.LivekitWebhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LiveKitService {

    @Value("${livekit.api.key}")
    private String apiKey;
    @Value("${livekit.api.secret}")
    private String apiSecret;

    public TokenResponse generateToken(TokenRequest tokenRequest) {
        AccessToken token = new AccessToken(apiKey, apiSecret);

        token.setName(tokenRequest.getParticipantName());
        token.setIdentity(tokenRequest.getParticipantName());
        token.addGrants(new RoomJoin(true), new RoomName(tokenRequest.getRoomName()));

        return TokenResponse.builder().token(token.toJwt()).build();
    }

    public void handleWebhook(String authHeader, String body) {
        try {
            WebhookReceiver webhookReceiver = new WebhookReceiver(apiKey, apiSecret);
            LivekitWebhook.WebhookEvent event = webhookReceiver.receive(body, authHeader);

            // 필요 시 이벤트 타입별 분기 처리
            System.out.println("LiveKit Webhook: " + event);
        } catch (Exception e) {
            System.err.println("Error validating webhook event: " + e.getMessage());
        }
    }
}
