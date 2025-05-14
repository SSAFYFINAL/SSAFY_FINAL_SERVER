package com.ssafy.pjtaserver.controller;

import com.ssafy.pjtaserver.dto.MailDto;
import com.ssafy.pjtaserver.enums.ApiResponseCode;
import com.ssafy.pjtaserver.service.mail.MailService;
import com.ssafy.pjtaserver.util.ApiResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Slf4j
public class MailController {

    private final MailService mailService;

    /**
     * HTML 구성 기반 이메일을 전송합니다.
     * @param mailDto
     * @return
     */
    @PostMapping("/html-email")
    public ResponseEntity<ApiResponse> sendHtmlEmail(@RequestBody MailDto mailDto){
        boolean isSend = mailService.sendEmail(mailDto);

        if (isSend) {
            return ApiResponse.of(ApiResponseCode.SUCCESS);
        } else {
            log.error("이메일 전송 실패 대상 이메일: {}", mailDto.getEmailAddr());
            return ApiResponse.of(ApiResponseCode.INVALID_REQUEST);
        }
    }
}