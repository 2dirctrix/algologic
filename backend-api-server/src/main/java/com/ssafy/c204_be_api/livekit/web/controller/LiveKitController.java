package com.ssafy.c204_be_api.livekit.web.controller;

import com.ssafy.c204_be_api.common.web.response.ApiResponse;
import com.ssafy.c204_be_api.livekit.service.LiveKitService;
import com.ssafy.c204_be_api.livekit.web.request.TokenRequest;
import com.ssafy.c204_be_api.livekit.web.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/webrtc")
@RequiredArgsConstructor
public class LiveKitController {

    private final LiveKitService liveKitService;

    @PostMapping("/token")
    public ResponseEntity<ApiResponse<TokenResponse>> createToken(@RequestBody TokenRequest tokenResponse) {
        if (tokenResponse.getRoomName() == null || tokenResponse.getParticipantName() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.with(HttpStatus.BAD_REQUEST, "방 이름과 참가자 이름은 필수입니다."));
        }

        return ResponseEntity.ok(ApiResponse.with(HttpStatus.OK, "토큰 발급이 완료되었습니다.",
                liveKitService.generateToken(tokenResponse)));
    }

    @PostMapping(value = "/livekit/webhook", consumes = "application/webhook+json")
    public ResponseEntity<String> receiveWebhook(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody String body
    ) {
        liveKitService.handleWebhook(authHeader, body);
        return ResponseEntity.ok("ok");
    }
}
