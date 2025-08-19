package com.ssafy.c204_be_api.room.web.message.inbound;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReadyRequest {

    @NotNull
    private Boolean ready;
}