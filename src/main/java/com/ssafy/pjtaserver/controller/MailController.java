package com.ssafy.pjtaserver.controller;

import com.ssafy.pjtaserver.dto.MailDto;
import com.ssafy.pjtaserver.service.mail.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Please explain the class!!
 *
 * @author : jonghoon
 * @fileName : MailSendController
 * @since : 11/11/24
 */
@Controller
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Slf4j
public class MailController {

    private final MailService mailService;

    /**
     * HTML 구성 기반 이메일을 전송합니다.
     *
     * @param mailDto
     * @return
     */
    @PostMapping("/html-email")
    public ResponseEntity<Object> sendHtmlEmail(@RequestBody MailDto mailDto) throws MessagingException {
        mailService.sendEmail(mailDto);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}