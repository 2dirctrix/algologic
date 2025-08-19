package com.ssafy.c204_be_api.chat.service;

import com.ssafy.c204_be_api.chat.web.controller.request.ChatRequest;
import com.ssafy.c204_be_api.chat.web.controller.response.ChatResponse;
import com.ssafy.c204_be_api.member.domain.Member;
import com.ssafy.c204_be_api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MemberRepository memberRepository;

    public ChatResponse createChatResponse(ChatRequest chatRequest) {
        Instant now = Instant.now();
        String isoTime = now.toString();

        return new ChatResponse(getNicknameByMemberId(chatRequest.getMemberId()), chatRequest.getContent(), isoTime);
    }

    private String getNicknameByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .map(Member::getNickname)
                .orElse("Unknown User");
    }
}
