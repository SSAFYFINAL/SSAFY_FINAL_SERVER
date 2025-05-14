package com.ssafy.pjtaserver.dto.request.mail;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MailVerifyDto {
    @NotBlank(message = "email은 필수 요청값 입니다.")
    @Email
    private final String email;

    @NotBlank(message = "certifyCode 필수 요청값 입니다.")
    private final String certifyCode;
}
