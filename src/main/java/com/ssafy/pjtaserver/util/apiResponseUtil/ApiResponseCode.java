package com.ssafy.pjtaserver.util.apiResponseUtil;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseCode {
    // 성공
    SUCCESS(HttpStatus.OK, 100, "요청이 정상적으로 처리되었습니다."),
    USER_CREATED(HttpStatus.CREATED, 101, "회원가입이 완료되었습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, 102, "로그인에 성공했습니다."),

    // 실패
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, 400, "잘못된 요청입니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, 401, "요청값 검증 실패"),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, 402, "인증 실패"),
    FORBIDDEN(HttpStatus.FORBIDDEN, 403, "권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, 404, "요청한 리소스를 찾을 수 없습니다."),
    USER_CREATED_ERROR(HttpStatus.BAD_REQUEST,401,"회원가입 실패"),

    // 토큰 에러
    ERROR_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 401, "ERROR_ACCESS_TOKEN"),
    DENIED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 401, "DENIED_ACCESS_TOKEN"),

    //서버에러
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 내부 오류입니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseCode(HttpStatus httpStatus, int code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
