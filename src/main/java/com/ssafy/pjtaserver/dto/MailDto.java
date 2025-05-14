package com.ssafy.pjtaserver.dto;

import com.ssafy.pjtaserver.enums.EmailType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailDto {

    @NotBlank(message = "emailAddr는 필수값 입니다.")
    private String emailAddr;                   // 수신자 이메일

    @NotBlank(message = "subject는 필수값 입니다.")
    private String subject;                     // 이메일 제목

    @NotBlank(message = "content는 필수값 입니다.")
    private String content;                     // 이메일 내용

    private String certifyCode;                 // 인증 코드

    @NotBlank(message = "requestType는 필수값 입니다.")
    private EmailType requestType;                 // 요청 타입
}
