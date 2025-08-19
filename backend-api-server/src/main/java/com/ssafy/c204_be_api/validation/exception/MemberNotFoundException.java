package com.ssafy.c204_be_api.validation.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException() {
        super("회원 조회 실패");
    }
}
