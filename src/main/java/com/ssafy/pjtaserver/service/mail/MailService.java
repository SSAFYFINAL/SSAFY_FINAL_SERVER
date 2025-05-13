package com.ssafy.pjtaserver.service.mail;

import com.ssafy.pjtaserver.dto.MailDto;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface MailService {
    boolean sendEmail(MailDto mailDto);
}
