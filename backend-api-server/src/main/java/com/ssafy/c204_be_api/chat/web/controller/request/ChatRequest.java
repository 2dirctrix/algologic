package com.ssafy.c204_be_api.chat.web.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRequest {
    private Long memberId;
    private String content;
}
