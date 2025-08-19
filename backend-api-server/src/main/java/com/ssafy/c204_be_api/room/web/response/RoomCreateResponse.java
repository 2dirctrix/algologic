package com.ssafy.c204_be_api.room.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomCreateResponse {

    @Schema(description = "방의 고유 ID")
    private UUID roomId;

}