package com.ssafy.c204_be_api.ingame.message.inbound;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BanChoiceRequest {

    @NotNull
    private Long problemCategoryId;

}
