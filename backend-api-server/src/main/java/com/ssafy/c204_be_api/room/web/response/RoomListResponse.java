package com.ssafy.c204_be_api.room.web.response;

import com.ssafy.c204_be_api.room.web.message.outbound.RoomSummary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "방 목록 응답 DTO")
public class RoomListResponse {
    @Schema(description = "방 요약 리스트")
    private List<RoomSummary> roomList;

    @Schema(description = "현재 페이지 번호")
    private int page;

    @Schema(description = "페이지 크기")
    private int size;

    @Schema(description = "전체 방 수")
    private int totalCount;

}
