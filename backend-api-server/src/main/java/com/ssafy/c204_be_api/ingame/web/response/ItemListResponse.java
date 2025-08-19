package com.ssafy.c204_be_api.ingame.web.response;

import com.ssafy.c204_be_api.ingame.domain.Item;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "아이템 목록 응답")
public class ItemListResponse {

    @Schema(description = "아이템 목록")
    private List<ItemInfo> itemList;

    public static ItemListResponse fromEntity(List<Item> items) {
        List<ItemInfo> itemInfoList = items.stream()
                .map(ItemInfo::fromEntity)
                .toList();

        return ItemListResponse.builder()
                .itemList(itemInfoList)
                .build();
    }

    @Getter
    @SuperBuilder
    @Schema(description = "아이템 정보")
    public static class ItemInfo {

        @Schema(description = "아이템 ID")
        private Long itemId;

        @Schema(description = "아이템 이름")
        private String itemName;

        @Schema(description = "아이템 이미지 URL")
        private String itemImageUrl;

        @Schema(description = "아이템 설명")
        private String description;

        @Schema(description = "아이템 효과 지속 시간(초)")
        private int duration;

        @Schema(description = "아이템 가격(코인)")
        private int cost;

        public static ItemInfo fromEntity(Item item) {
            return ItemInfo.builder()
                    .itemId(item.getId())
                    .itemName(item.getName())
                    .itemImageUrl(item.getImageUrl())
                    .description(item.getDescription())
                    .duration(item.getDuration())
                    .cost(item.getCost())
                    .build();
        }
    }
}
