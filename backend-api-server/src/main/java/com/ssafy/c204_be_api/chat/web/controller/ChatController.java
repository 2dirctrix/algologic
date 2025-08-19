package com.ssafy.c204_be_api.chat.web.controller;

import com.ssafy.c204_be_api.chat.service.ChatService;
import com.ssafy.c204_be_api.chat.web.controller.request.ChatRequest;
import com.ssafy.c204_be_api.chat.web.controller.response.ChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@MessageMapping("/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/main")
    @SendTo("/topic/chat/main")
    public ChatResponse handleMessage(ChatRequest chatRequest) {
        return chatService.createChatResponse(chatRequest);
    }

    @MessageMapping("/rooms/{roomId}")
    @SendTo("/topic/chat/rooms/{roomId}")
    public ChatResponse handleRoomMessage(ChatRequest chatRequest) {
        return chatService.createChatResponse(chatRequest);
    }
}