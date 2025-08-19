package com.ssafy.c204_be_api.validation.handler;

import com.ssafy.c204_be_api.common.web.message.ApiMessage;
import com.ssafy.c204_be_api.common.web.response.ApiResponse;
import com.ssafy.c204_be_api.validation.code.ErrorCode;
import com.ssafy.c204_be_api.validation.exception.ClientException;
import com.ssafy.c204_be_api.validation.exception.InGameException;
import com.ssafy.c204_be_api.validation.exception.RoomException;
import com.ssafy.c204_be_api.websocket.util.StompHeadersUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.Principal;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final SimpMessagingTemplate messaging;

    // ==== REST 핸들링 ====

    @ExceptionHandler(RoomException.class)
    public ResponseEntity<ApiResponse<Object>> handleRoomException(RoomException error) {
        log.error("RoomException", error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.with(error.getErrorCode().getCode(), error.getMessage(), error.getMeta()));
    }

    @ExceptionHandler(InGameException.class)
    public ResponseEntity<ApiResponse<Object>> handleInGameException(InGameException error) {
        log.error("InGameException", error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.with(error.getErrorCode().getCode(), error.getMessage(), error.getMeta()));
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ApiResponse<Void>> handleClientException(ClientException error) {
        log.error("ClientException", error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.with(HttpStatus.BAD_REQUEST, error.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(AuthenticationException error) {
        log.error("authentication exception", error);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.with(HttpStatus.UNAUTHORIZED, error.getMessage()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<Void>> handleExpiredJwtException(ExpiredJwtException error) {
        log.error("ExpiredJwtException ", error);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.with(HttpStatus.UNAUTHORIZED, error.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException error) {
        log.error("IllegalArgumentException", error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.with(HttpStatus.BAD_REQUEST, error.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalStateException(IllegalStateException error) {
        log.error("IllegalStateException", error);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.with(HttpStatus.CONFLICT, error.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception error) {
        log.error("exception", error);
        return ResponseEntity.internalServerError()
                .body(ApiResponse.with(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("입력값이 유효하지 않습니다.");


        log.error("MethodArgumentNotValidException", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.with(HttpStatus.BAD_REQUEST, message));
    }


    // ==== STOMP 핸들링 ====

    @MessageExceptionHandler(RoomException.class)
    public void handleRoomWsException(RoomException ex, Principal principal, SimpMessageHeaderAccessor accessor) {
        String user = resolveUser(principal);
        String reqId = StompHeadersUtils.getCorrelationId(accessor); // 선택: 헤더에서 x-req-id 추출
        log.warn("ROOM_WS_EX: user={}, code={}, msg={}, meta={}", user, ex.getErrorCode(), ex.getMessage(), ex.getMeta());
        messaging.convertAndSendToUser(user, "/queue/rooms/errors",
                ApiMessage.error(ex.getErrorCode().getCode(), ex.getMessage(),
                        coalesce(ex.getReqId(), reqId), ex.getMeta()));
    }

    @MessageExceptionHandler(InGameException.class)
    public void handleInGameWsException(InGameException ex, Principal principal, SimpMessageHeaderAccessor accessor) {
        String user = resolveUser(principal);
        String reqId = StompHeadersUtils.getCorrelationId(accessor);
        log.warn("INGAME_WS_EX: user={}, code={}, msg={}, meta={}", user, ex.getErrorCode(), ex.getMessage(), ex.getMeta());
        messaging.convertAndSendToUser(user, "/queue/ingame/errors",
                ApiMessage.error(ex.getErrorCode().getCode(), ex.getMessage(),
                        coalesce(ex.getReqId(), reqId), ex.getMeta()));
    }

    @MessageExceptionHandler(Exception.class)
    public void handleAnyWsException(Exception ex, Principal principal) {
        String user = resolveUser(principal);
        log.error("UNEXPECTED_WS_EX: user={}, msg={}", user, ex.getMessage(), ex);
        messaging.convertAndSendToUser(user, "/queue/errors",
                ApiMessage.error(ErrorCode.INTERNAL_ERROR.getCode(), ErrorCode.INTERNAL_ERROR.getDefaultMessage(),
                        null, Map.of()));
    }


    // ***** HELPER *****

    private String resolveUser(Principal principal) {
        return principal != null ? principal.getName() : "anonymous";
    }

    private static String coalesce(String a, String b) {
        return (a != null && !a.isBlank()) ? a : b;
    }

}
