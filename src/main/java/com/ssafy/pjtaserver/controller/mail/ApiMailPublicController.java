package com.ssafy.pjtaserver.controller.mail;

import com.ssafy.pjtaserver.dto.request.mail.MailSendDto;
import com.ssafy.pjtaserver.dto.request.mail.MailVerifyDto;
import com.ssafy.pjtaserver.enums.ApiResponseCode;
import com.ssafy.pjtaserver.service.mail.MailService;
import com.ssafy.pjtaserver.util.ApiResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Slf4j
public class ApiMailPublicController {

    private final MailService mailService;

    /**
     * HTML 구성 기반 이메일을 전송합니다.
     */
    @PostMapping("/send-email")
    public ResponseEntity<ApiResponse> sendHtmlEmail(@RequestBody MailSendDto mailSendDto) throws MessagingException {
        boolean isSend = mailService.sendEmail(mailSendDto);

        if (isSend) {
            return ApiResponse.of(ApiResponseCode.EMAIL_SEND_SUCCESS);
        } else {
            log.error("이메일 전송 실패 대상 이메일: {}", mailSendDto.getEmailAddr());
            return ApiResponse.of(ApiResponseCode.EMAIL_SEND_ERROR);
        }
    }

    @PostMapping("/verify-auth-code")
    public ResponseEntity<ApiResponse> verifyEmail(@Validated @RequestBody MailVerifyDto mailVerifyDto) {
        boolean isVerifyEmail = mailService.verifyEmail(mailVerifyDto);

        if(isVerifyEmail) {
            return ApiResponse.of(ApiResponseCode.EMAIL_VERIFY_SUCCESS,true);
        } else {
            log.error("이메일 인증 실패 대상 이메일: {}", mailVerifyDto.getEmail());
            return ApiResponse.of(ApiResponseCode.EMAIL_VERIFY_ERROR,false);
        }
    }
}