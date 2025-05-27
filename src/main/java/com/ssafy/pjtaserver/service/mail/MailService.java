package com.ssafy.pjtaserver.service.mail;

import com.ssafy.pjtaserver.dto.request.mail.MailSendDto;
import com.ssafy.pjtaserver.dto.request.mail.MailVerifyDto;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface MailService {
    boolean sendEmail(MailSendDto mailSendDto) throws MessagingException;
    boolean verifyEmail(MailVerifyDto mailVerifyDto);
}
