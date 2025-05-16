package com.ssafy.pjtaserver.exception;

import com.ssafy.pjtaserver.enums.ApiResponseCode;
import com.ssafy.pjtaserver.util.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ApiResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("해당 책을 찾을 수 없습니다: {}", e.getMessage());
        return ApiResponse.of(ApiResponseCode.INVALID_REQUEST, e.getMessage());
    }

    @ExceptionHandler(CustomEmailException.class)
    protected ResponseEntity<ApiResponse> handleCustomEmailException(CustomEmailException e) {
        log.warn("이메일 발송이 제한됨: {}", e.getMessage());
        return ApiResponse.of(ApiResponseCode.EMAIL_SEND_ERROR, e.getMessage());
    }

    /**
     * 
     * @param e MethodArgumentNotValidException
     * @return 400 , 필수값 응답 반환
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected  ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException e) {
        List<String> errMsg = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        return ApiResponse.of(ApiResponseCode.INVALID_REQUEST, errMsg);
    }

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
        log.error("MessagingException 발생: {}", e.getMessage(), e);
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

    @ExceptionHandler(JoinValidationException.class)
    protected ResponseEntity<ApiResponse> handleJoinValidationException(JoinValidationException e) {
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
        log.error("예상치 못한 에러 발생: {}", e.getMessage(), e);
        return ApiResponse.of(ApiResponseCode.SERVER_ERROR, "예상하지 못한 오류가 발생했습니다.");
    }

}
