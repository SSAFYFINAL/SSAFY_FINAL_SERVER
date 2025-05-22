package com.ssafy.pjtaserver.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseCode {
    // 공용 사용 응답
    SUCCESS(HttpStatus.OK, 1000, "요청이 정상적으로 처리되었습니다."),
    REQUEST_FAILED(HttpStatus.OK, 2000, "요청이 실패하였습니다."),
    INVALID_REQUEST(HttpStatus.OK, 2001, "잘못된 요청입니다."),
    VALIDATION_ERROR(HttpStatus.OK, 2002, "필수 요청값 누락."),
    AUTHENTICATION_FAILED(HttpStatus.OK, 2003, "인증 실패."),
    FORBIDDEN(HttpStatus.OK, 2004, "권한이 없습니다."),
    NOT_FOUND(HttpStatus.OK, 2005, "요청한 리소스를 찾을 수 없습니다."),

    // 사용자 관련 응답
    USER_CREATED(HttpStatus.OK, 1100, "회원가입이 완료되었습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, 1101, "로그인에 성공했습니다."),
    CAN_USE_LOGIN_ID(HttpStatus.OK, 1102, "사용 가능한 로그인 아이디입니다."),
    USER_DELETE_SUCCESS(HttpStatus.OK, 1103, "해당 유저 회원탈퇴가 완료되었습니다."),
    USER_CREATED_ERROR(HttpStatus.OK, 2100, "회원가입 실패."),
    LOGIN_FAILED(HttpStatus.OK, 2101, "로그인에 실패했습니다."),
    DUPLICATE_LOGIN_ID(HttpStatus.OK, 2102, "이미 존재하는 로그인 아이디입니다."),
    USER_DELETE_FAIL(HttpStatus.OK, 2103, "해당 유저 회원탈퇴가 실패하였습니다."),
    FIND_USER_ID_SUCCESS(HttpStatus.OK, 1104, "아이디 찾기가 완료 되었습니다."),
    FIND_USER_ID_FAIL(HttpStatus.OK, 2104, "아이디 찾기가 실패 되었습니다."),

    // 대출 예약 관련 응답
    CHECKOUT_SUCCESS(HttpStatus.OK, 1200, "대출이 완료되었습니다."),
    RESERVATION_SUCCESS(HttpStatus.OK, 1201, "예약이 완료되었습니다."),
    CHECKOUT_FAILED(HttpStatus.OK, 2200, "대출에 실패하였습니다."),
    RESERVATION_FAILED(HttpStatus.OK, 2201, "예약에 실패하였습니다."),

    // 찜 관련 응답
    FAVORITE_ADD(HttpStatus.OK, 1300, "즐겨찾기 추가가 완료되었습니다."),
    FAVORITE_CANCEL(HttpStatus.OK, 1301, "즐겨찾기 취소가 완료되었습니다."),
    FAVORITE_ADD_FAILED(HttpStatus.OK, 2300, "즐겨찾기 추가에 실패하였습니다."),
    FAVORITE_CANCEL_FAILED(HttpStatus.OK, 2301, "즐겨찾기 취소에 실패하였습니다."),

    // FOLLOW 관련 응답
    FOLLOW_COMPLETED(HttpStatus.OK, 1400, "팔로우가 추가되었습니다."),
    UNFOLLOW_SUCCESS(HttpStatus.OK, 1401, "팔로우가 취소되었습니다."),
    FOLLOW_FAILED(HttpStatus.OK, 2400, "팔로우에 실패하였습니다."),
    UNFOLLOW_FAILED(HttpStatus.OK, 2401, "팔로우 취소에 실패하였습니다."),

    // 게스트북 관련 응답
    GUESTBOOK_WRITE_SUCCESS(HttpStatus.OK, 1500, "방명록이 정상적으로 작성되었습니다."),
    GUESTBOOK_LIST_SUCCESS(HttpStatus.OK, 1501, "방명록 목록 조회가 완료되었습니다."),
    GUESTBOOK_DELETE_SUCCESS(HttpStatus.OK, 1502, "방명록이 정상적으로 삭제되었습니다."),
    GUESTBOOK_UPDATE_SUCCESS(HttpStatus.OK, 1503, "방명록이 정상적으로 수정되었습니다."),
    GUESTBOOK_WRITE_FAILED(HttpStatus.OK, 2500, "방명록 작성에 실패했습니다."),
    GUESTBOOK_LIST_FAILED(HttpStatus.OK, 2501, "방명록 목록 조회에 실패했습니다."),
    GUESTBOOK_DELETE_FAILED(HttpStatus.OK, 2502, "방명록 삭제에 실패했습니다."),
    GUESTBOOK_UPDATE_FAILED(HttpStatus.OK, 2503, "방명록 수정에 실패했습니다."),

    // EMAIL 관련 응답
    EMAIL_SEND_SUCCESS(HttpStatus.OK, 1600, "이메일이 정상적으로 발송되었습니다."),
    EMAIL_VERIFY_SUCCESS(HttpStatus.OK, 1601, "이메일 인증이 정상적으로 처리되었습니다."),
    EMAIL_SEND_FAILED(HttpStatus.OK, 2600, "이메일 발송에 실패했습니다."),
    EMAIL_VERIFY_FAILED(HttpStatus.OK, 2601, "이메일 인증에 실패했습니다."),

    // 토큰 관련 응답
    ERROR_ACCESS_TOKEN(HttpStatus.OK, 2700, "만료되었거나 잘못된 토큰입니다."),
    DENIED_ACCESS_TOKEN(HttpStatus.OK, 2701, "권한이 부족한 토큰입니다."),
    ERROR_TOKEN_ISEMPTY(HttpStatus.OK, 2702, "토큰 값이 비어있습니다."),
    ERROR_TOKEN_FIELD(HttpStatus.OK, 2703, "토큰 형식이 올바르지 않습니다."),

    WEEKLY_POPULAR_LIST_SUCCESS(HttpStatus.OK, 1800, "주간 인기 도서 목록 조회가 완료되었습니다."),
    RECENT_BOOK_LIST_SUCCESS(HttpStatus.OK, 1801, "신간 도서 목록 조회가 완료되었습니다."),

    // 서버 내부 에러
    SERVER_ERROR(HttpStatus.OK, 2800, "서버 내부 오류입니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseCode(HttpStatus httpStatus, int code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}