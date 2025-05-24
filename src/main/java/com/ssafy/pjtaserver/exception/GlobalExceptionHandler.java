package com.ssafy.pjtaserver.exception;

import com.ssafy.pjtaserver.enums.ApiResponseCode;
import com.ssafy.pjtaserver.utils.ApiResponseUtil;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 공통 예외 응답 생성 (경고 수준 로그)
     */
    private ResponseEntity<ApiResponseUtil> buildErrorResponse(ApiResponseCode responseCode, String message, Exception e) {
        log.warn("[WARN] {}: {}", e.getClass().getSimpleName(), message);
        log.debug("[DETAIL] Exception: ", e);
        return ApiResponseUtil.of(responseCode, message);
    }

    /**
     * 공통 예외 응답 생성 (에러 수준 로그 - 스택 트레이스 포함)
     */
    private ResponseEntity<ApiResponseUtil> buildErrorResponseWithLog(String message, Exception e) {
        log.error("[ERROR] {}: {}", e.getClass().getSimpleName(), message, e);
        return ApiResponseUtil.of(ApiResponseCode.SERVER_ERROR, message);
    }

    /**
     * 파라미터 값 누락 시 예외 처리
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponseUtil> handleMissingParams(MissingServletRequestParameterException ex) {
        String message = ex.getParameterName() + " 파라미터가 누락되었습니다.";
        log.warn("[PARAMETER MISSING] ParameterName: {}", ex.getParameterName());
        return buildErrorResponse(ApiResponseCode.VALIDATION_ERROR, message, ex);
    }

    /**
     * 상태 이상 시 예외 처리
     */
    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<ApiResponseUtil> handleIllegalStateException(IllegalStateException e) {
        String message = "요청 처리 중 잘못된 상태로 인해 오류가 발생했습니다: " + e.getMessage();
        log.warn("[ILLEGAL STATE] {}", message);
        return buildErrorResponse(ApiResponseCode.INVALID_REQUEST, message, e);
    }

    /**
     * 조회하고자 하는 엔티티가 존재하지 않는 경우 처리
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ApiResponseUtil> handleEntityNotFoundException(EntityNotFoundException e) {
        String message = "해당 엔티티를 찾을 수 없습니다: " + e.getMessage();
        log.warn("[ENTITY NOT FOUND] {}", message);
        return buildErrorResponse(ApiResponseCode.NOT_FOUND, message, e);
    }

    /**
     * 이메일 발송 관련 커스텀 예외 처리
     */
    @ExceptionHandler(CustomEmailException.class)
    protected ResponseEntity<ApiResponseUtil> handleCustomEmailException(CustomEmailException e) {
        String message = "이메일 전송 중 오류가 발생했습니다: " + e.getMessage();
        log.error("[EMAIL ERROR] {}", message, e);
        return buildErrorResponse(ApiResponseCode.EMAIL_SEND_FAILED, message, e);
    }

    /**
     * Validation 관련 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponseUtil> handleValidationException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        String message = "Validation 오류 발생: " + String.join(", ", errors);
        log.warn("[VALIDATION ERROR] {}", message);
        return buildErrorResponse(ApiResponseCode.VALIDATION_ERROR, message, e);
    }

    /**
     * JWT 관련 커스텀 예외 처리
     */
    @ExceptionHandler(CustomJWTException.class)
    protected ResponseEntity<ApiResponseUtil> handleJWTException(CustomJWTException e) {
        String message = "JWT 처리 중 오류가 발생했습니다: " + e.getMessage();
        log.warn("[JWT ERROR] {}", message);
        return buildErrorResponse(ApiResponseCode.ERROR_ACCESS_TOKEN, message, e);
    }

    /**
     * 메시징 관련 예외 처리
     */
    @ExceptionHandler(MessagingException.class)
    protected ResponseEntity<ApiResponseUtil> handleMessagingException(MessagingException e) {
        String message = "메시지 전송 중 문제가 발생했습니다.";
        log.error("[MESSAGING ERROR] {}", message, e);
        return buildErrorResponseWithLog(message, e);
    }

    /**
     * 잘못된 입력 처리 (IllegalArgumentException 등)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ApiResponseUtil> handleIllegalArgumentException(IllegalArgumentException e) {
        String message = "잘못된 요청입니다: " + e.getMessage();
        log.warn("[INVALID ARGUMENT] {}", message);
        return buildErrorResponse(ApiResponseCode.INVALID_REQUEST, message, e);
    }

    /**
     * HTTP 메서드가 잘못된 경우 예외 처리
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponseUtil> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String message = "지원하지 않는 HTTP 메서드 요청입니다.";
        log.warn("[METHOD NOT SUPPORTED] {}", message);
        return buildErrorResponse(ApiResponseCode.INVALID_REQUEST, message, e);
    }

    /**
     * HTTP Content-Type이 잘못된 요청 처리
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponseUtil> handleMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        String message = "지원하지 않는 Content-Type 요청입니다.";
        log.warn("[CONTENT-TYPE NOT SUPPORTED] {}", message);
        return buildErrorResponse(ApiResponseCode.INVALID_REQUEST, message, e);
    }

    /**
     * 예상하지 못한(Runtime) 예외 처리
     */
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<ApiResponseUtil> handleRuntimeException(RuntimeException e) {
        String message = "시스템 내부 에러가 발생했습니다. 관리자에게 문의하세요.";
        log.error("[RUNTIME ERROR] {}", message, e);
        return buildErrorResponseWithLog(message, e);
    }

    /**
     * 내부 컨트롤러에서 발생하지 않은 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponseUtil> handleGeneralException(Exception e) {
        String message = "예상하지 못한 오류가 발생했습니다. 관리자에게 문의하세요.";
        log.error("[UNEXPECTED ERROR] {}", message, e);
        return buildErrorResponseWithLog(message, e);
    }
}