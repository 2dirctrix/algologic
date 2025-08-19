package com.ssafy.c204_be_api.chat.web.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatResponse {
    private String nickname;
    private String content;
    private String isoTime;
}
