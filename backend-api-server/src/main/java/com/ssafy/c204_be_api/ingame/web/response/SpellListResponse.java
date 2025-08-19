package com.ssafy.c204_be_api.ingame.web.response;

import com.ssafy.c204_be_api.ingame.domain.Spell;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "스펠 목록 응답")
public class SpellListResponse {

    @Schema(description = "스펠 목록")
    private List<SpellInfo> spellList;

    public static SpellListResponse fromEntity(List<Spell> spells) {
        List<SpellInfo> spellInfoList = spells.stream()
                .map(SpellInfo::fromEntity)
                .toList();

        return SpellListResponse.builder()
                .spellList(spellInfoList)
                .build();
    }

    @Getter
    @SuperBuilder
    @Schema(description = "스펠 정보")
    public static class SpellInfo {

        @Schema(description = "스펠 ID")
        private Long spellId;

        @Schema(description = "스펠 이름")
        private String spellName;

        @Schema(description = "스펠 이미지 URL")
        private String spellImageUrl;

        @Schema(description = "스펠 설명")
        private String description;

        @Schema(description = "스펠 효과 지속 시간(초)")
        private int duration;

        @Schema(description = "스펠 가격(코인)")
        private int cost;

        public static SpellInfo fromEntity(Spell spell) {
            return SpellInfo.builder()
                    .spellId(spell.getId())
                    .spellName(spell.getName())
                    .spellImageUrl(spell.getImageUrl())
                    .description(spell.getDescription())
                    .duration(spell.getDuration())
                    .cost(spell.getCost())
                    .build();
        }
    }
}
