package com.ssafy.c204_be_api.ingame.domain;

import com.ssafy.c204_be_api.ingame.message.outbound.InGamePlayerSnapshot;
import com.ssafy.c204_be_api.member.domain.Member;
import com.ssafy.c204_be_api.player.domain.Player;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InGamePlayer {

    Long memberId;

    Long playerId;

    String nickname;

    String profileImageUrl;

    @Builder.Default
    boolean connected = true;

    public static InGamePlayer fromEntity(Player player) {
        Member member = player.getMember();
        return InGamePlayer.builder()
                .playerId(player.getId())
                .memberId(member.getId())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImageUrl())
                .build();
    }

    public InGamePlayerSnapshot toSnapshot() {
        return InGamePlayerSnapshot.builder()
                .memberId(memberId)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .connected(connected)
                .build();
    }
}
