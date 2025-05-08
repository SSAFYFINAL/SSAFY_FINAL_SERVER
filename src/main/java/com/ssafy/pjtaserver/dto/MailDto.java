package com.ssafy.pjtaserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailDto {
    private String emailAddr;                   // 수신자 이메일
    private String subject;                     // 이메일 제목
    private String content;                     // 이메일 내용
    private String certifyCode;                 // 인증 코드
    private String requestType;                 // 요청 타입
}
