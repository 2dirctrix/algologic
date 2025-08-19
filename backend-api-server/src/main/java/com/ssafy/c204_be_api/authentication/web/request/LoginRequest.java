package com.ssafy.c204_be_api.authentication.web.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class LoginRequest {

    private String code;

}
