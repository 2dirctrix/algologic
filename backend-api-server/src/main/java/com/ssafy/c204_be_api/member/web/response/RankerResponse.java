package com.ssafy.c204_be_api.member.web.response;

import com.ssafy.c204_be_api.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RankerResponse {
    private String nickname;
    private String programmingLanguage;
    private Integer score;

    public static RankerResponse fromEntity(String nickname, String programmingLanguage, Integer score) {
        return RankerResponse.builder()
                .nickname(nickname)
                .programmingLanguage(programmingLanguage)
                .score(score)
                .build();
    }

    public static List<RankerResponse> fromEntities(List<Member> members) {
        return members.stream()
                .map(member -> RankerResponse.fromEntity(
                        member.getNickname(),
                        member.getProgrammingLanguage().toString(),
                        member.getScore()))
                .toList();
    }}
