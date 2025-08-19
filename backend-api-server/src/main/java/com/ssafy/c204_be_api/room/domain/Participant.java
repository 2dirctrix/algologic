package com.ssafy.c204_be_api.room.domain;

import com.ssafy.c204_be_api.member.domain.Member;
import com.ssafy.c204_be_api.room.web.message.outbound.ParticipantSnapshot;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@ToString
@Builder
public class Participant {

    private Long memberId;

    private String nickname;

    private Integer score;

    private String profileImageUrl;

    @Builder.Default
    private boolean ready = false;  // primitive는 Lombok이 자동으로 isReady() 생성

    @Builder.Default
    private boolean connected = false;    // primitive는 Lombok이 자동으로 isConnected() 생성

    @Builder.Default
    private Instant disconnectedAt = Instant.now();

    public void connect() {
        this.connected = true;
        this.disconnectedAt = null;
    }

    public void disconnect() {
        this.connected = false;
        this.disconnectedAt = Instant.now();
        this.ready = false;
    }

    public void ready(boolean ready) {
        this.ready = ready;
    }

    public static Participant fromEntity(Member member) {
        return Participant.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .score(member.getScore())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }

    public ParticipantSnapshot toSnapshot() {
        return ParticipantSnapshot.builder()
                .memberId(memberId)
                .nickname(nickname)
                .score(score)
                .profileImageUrl(profileImageUrl)
                .ready(ready)
                .connected(connected)
                .build();
    }
}