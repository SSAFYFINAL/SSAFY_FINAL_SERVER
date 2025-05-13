package com.ssafy.pjtaserver.service.mail;

import com.ssafy.pjtaserver.dto.MailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    // 이메일이 발송될 나의 계정 정보
    @Value("${spring.mail.username}")
    private String EMAIL_SENDER;

    /**
     * html 기반 메일 전송
     *
     * @param mailDto
     */
    @Override
    public boolean sendEmail(MailDto mailDto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("subject", mailDto.getSubject());
            context.setVariable("message", mailDto.getContent());
            context.setVariable("certifyCode", mailDto.getCertifyCode());
            switch (mailDto.getRequestType()) {
                case "join" -> context.setVariable("requestType", "회원가입");
                case "findPwd" -> context.setVariable("requestType", "비밀번호 찾기");
                case "tempPwd" -> context.setVariable("requestType", "임시 비밀번호");
                default -> context.setVariable("userType", "알 수 없음");
            }

            String htmlContent = templateEngine.process("email-template", context);
            helper.setTo(mailDto.getEmailAddr());
            helper.setSubject(mailDto.getSubject());
            helper.setText(htmlContent, true);
            helper.setFrom(EMAIL_SENDER);

            mailSender.send(message);

            log.info("이메일 전송 성공 대상 이메일: {}", mailDto.getEmailAddr());
            return true;
        } catch (MessagingException | MailException e) {
            log.error("이메일 전송 실패: {}", e.getMessage(), e);
            return false;
        }
    }


    // 추후 이미지도 메일에 포함될경우 사용
    private String getBase64EncodedImage(String imagePath) throws IOException {
        Resource resource = new ClassPathResource(imagePath);
        byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
        return Base64.getEncoder().encodeToString(bytes);
    }
}
