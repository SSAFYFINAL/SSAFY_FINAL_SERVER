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
    public void sendEmail(MailDto mailDto) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Context 객체를 통한 템플릿 데이터
            Context context = new Context();
            context.setVariable("subject", mailDto.getSubject());
            context.setVariable("message", mailDto.getContent());
            context.setVariable("certifyCode", 123123);

            // 사용자 타입 설정
            switch (mailDto.getRequestType()) {
                case "join" -> context.setVariable("requestType", "회원가입");
                case "findPwd" -> context.setVariable("requestType", "비밀번호 찾기");
                default -> context.setVariable("userType", "알 수 없음");
            }

            // Thymeleaf를 사용한 템플릿 처리
            String htmlContent = templateEngine.process("email-template", context);

            helper.setTo(mailDto.getEmailAddr());    // 이메일을 받는사람 email
            helper.setSubject(mailDto.getSubject()); // 이메일의 제목
            helper.setText(htmlContent, true);  // helper에 등록해줄 text를 html 코드로 정의해 준당
            helper.setFrom(EMAIL_SENDER);            // 보내는 사람의 주소

            mailSender.send(message);

            log.info("이메일 전송 성공 대상 이메일: {}", mailDto.getEmailAddr());

        } catch (MessagingException e) {
            log.error("이메일 전송 실패 - MessagingException 발생: {}", e.getMessage(), e);
            throw new MessagingException("이메일 전송 실패: " + e.getMessage());
        } catch (MailException e) {
            log.error("이메일 전송 실패 - MailException 발생: {}", e.getMessage(), e);
            throw new RuntimeException("이메일 전송 중 오류가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            log.error("알 수 없는 오류로 이메일 전송 실패: {}", e.getMessage(), e);
            throw new RuntimeException("알 수 없는 오류로 인해 이메일 전송에 실패했습니다: " + e.getMessage());
        }
    }

    // 추후 이미지도 메일에 포함될경우 사용
    private String getBase64EncodedImage(String imagePath) throws IOException {
        Resource resource = new ClassPathResource(imagePath);
        byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
        return Base64.getEncoder().encodeToString(bytes);
    }
}
