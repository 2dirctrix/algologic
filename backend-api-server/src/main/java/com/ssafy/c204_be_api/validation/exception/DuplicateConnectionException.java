package com.ssafy.c204_be_api.validation.exception;

public class DuplicateConnectionException extends RuntimeException {
    public DuplicateConnectionException(String message) {
        super(message);
    }
}