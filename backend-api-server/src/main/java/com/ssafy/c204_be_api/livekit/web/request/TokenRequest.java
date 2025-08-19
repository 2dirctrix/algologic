package com.ssafy.c204_be_api.livekit.web.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TokenRequest {
    @NotBlank
    private String roomName;
    @NotBlank
    private String participantName;
}
