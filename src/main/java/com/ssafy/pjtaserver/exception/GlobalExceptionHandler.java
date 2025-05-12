package com.ssafy.pjtaserver.exception;

import com.ssafy.pjtaserver.util.ApiResponse;
import com.ssafy.pjtaserver.enums.ApiResponseCode;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * JWT 관련 커스텀 예외 처리
     *
     * @param e CustomJWTException
     * @return 401 Unauthorized 응답 반환
     */
    @ExceptionHandler(CustomJWTException.class)
    protected ResponseEntity<ApiResponse> handleJWTException(CustomJWTException e) {
        return ApiResponse.of(ApiResponseCode.ERROR_ACCESS_TOKEN, e.getMessage());
    }

    /**
     * 메시징 예외 처리
     *
     * @param e MessagingException
     * @return 500 Internal Server Error 응답 반환
     */
    @ExceptionHandler(MessagingException.class)
    protected ResponseEntity<ApiResponse> handleMessagingException(MessagingException e) {
        return ApiResponse.of(ApiResponseCode.SERVER_ERROR, e.getMessage());
    }

    /**
     * 잘못된 입력 처리 (예: IllegalArgumentException 등)
     *
     * @param e IllegalArgumentException
     * @return 400 Bad Request 응답 반환
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return ApiResponse.of(ApiResponseCode.INVALID_REQUEST, e.getMessage());
    }

    /**
     * 예상하지 못한(Runtime) 예외 처리
     *
     * @param e RuntimeException
     * @return 500 Internal Server Error 응답 반환
     */
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e) {
        return ApiResponse.of(ApiResponseCode.SERVER_ERROR, "예상하지 못한 에러가 발생했습니다.");
    }
}
