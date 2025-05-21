package com.ssafy.pjtaserver.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseCode {
    // 성공
    SUCCESS(HttpStatus.OK, 100, "요청이 정상적으로 처리되었습니다."),
    USER_CREATED(HttpStatus.CREATED, 101, "회원가입이 완료되었습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, 102, "로그인에 성공했습니다."),
    EMAIL_SEND_SUCCESS(HttpStatus.OK, 103, "이메일이 정상적으로 발송되었습니다."),
    EMAIL_VERIFY_SUCCESS(HttpStatus.OK, 104, "이메일 인증이 정상적으로 처리되었습니다."),
    CHECKOUT_SUCCESS(HttpStatus.OK, 105, "대출이 완료되었습니다."),
    FAVORITE_ADD(HttpStatus.OK, 106, "즐겨찾기 추가 완료되었습니다."),
    RESERVATION_SUCCESS(HttpStatus.OK, 107, "예약이 완료되었습니다."),
    FAVORITE_CALCLE(HttpStatus.OK, 108, "즐겨찾기 취소 완료되었습니다."),
    CAN_USE_LOGIN_ID(HttpStatus.OK, 109, "사용가능한 로그인 아이디입니다."),
    FOLLOW_COMPLETED(HttpStatus.OK, 110, "팔로우 추가 완료."),
    UNFOLLOW_SUCCESS(HttpStatus.OK, 111, "팔로우 취소 완료."),
    // 실패
    REQUEST_FAILED(HttpStatus.OK, 400, "요청이 실패하였습니다."),
    INVALID_REQUEST(HttpStatus.OK, 400, "잘못된 요청입니다."),
    DUPLICATE_LOGIN_ID(HttpStatus.OK, 409, "이미 존재하는 로그인 아이디입니다."),
    VALIDATION_ERROR(HttpStatus.OK, 401, "필수 요청값 누락"),
    AUTHENTICATION_FAILED(HttpStatus.OK, 402, "인증 실패"),
    FORBIDDEN(HttpStatus.OK, 403, "권한이 없습니다."),
    NOT_FOUND(HttpStatus.OK, 404, "요청한 리소스를 찾을 수 없습니다."),
    USER_CREATED_ERROR(HttpStatus.OK,401,"회원가입 실패"),


    EMAIL_SEND_ERROR(HttpStatus.OK,403,"이메일 인증 휫수 초과."),
    EMAIL_VERIFY_ERROR(HttpStatus.OK, 403, "이메일 인증에 실패하였습니다."),

    // 토큰 에러
    ERROR_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 401, "만료되거나 잘못된 토큰입니다."),
    DENIED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 401, "권한이 미달되는 토큰입니다."),
    ERROR_TOKEN_ISEMPTY(HttpStatus.UNAUTHORIZED, 401, "토큰의 값이 비어있습니다."),
    ERROR_TOKEN_FIELD(HttpStatus.UNAUTHORIZED, 401, "토큰의 형식이 올바르지 않습니다."),

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
