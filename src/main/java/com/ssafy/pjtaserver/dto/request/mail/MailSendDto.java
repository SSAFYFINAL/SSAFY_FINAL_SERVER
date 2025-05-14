package com.ssafy.pjtaserver.dto.request.mail;

import com.ssafy.pjtaserver.enums.EmailType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailSendDto {

    @NotBlank(message = "emailAddr는 필수값 입니다.")
    @Email
    private String emailAddr;                   // 수신자 이메일

    private String certifyCode;                 // 인증 코드

    @NotBlank(message = "requestType는 필수값 입니다.")
    private EmailType requestType;                 // 요청 타입
}
