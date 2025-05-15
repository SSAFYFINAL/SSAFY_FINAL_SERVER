package com.ssafy.pjtaserver.service.mail;

import com.ssafy.pjtaserver.domain.mail.Email;
import com.ssafy.pjtaserver.dto.request.mail.MailSendDto;
import com.ssafy.pjtaserver.dto.request.mail.MailVerifyDto;
import com.ssafy.pjtaserver.enums.EmailType;
import com.ssafy.pjtaserver.exception.CustomEmailException;
import com.ssafy.pjtaserver.repository.mail.MailRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final MailRepository mailRepository;

    @Value("${spring.mail.username}")
    private String EMAIL_SENDER;

    /**
     * html 기반 메일 전송
     * @param mailSendDto
     */
    @Transactional
    @Override
    public boolean sendEmail(MailSendDto mailSendDto) throws MessagingException {
        try {
            Optional<Email> emailHistory = mailRepository.findByEmail(mailSendDto.getEmailAddr());

            // 잠김 상태 여부 확인
            emailHistory.ifPresent(email -> {
                if (email.isLocked()) {
                    throw new CustomEmailException("이메일 인증 횟수가 초과되었습니다. " +
                            "잠금 해제까지 남은 시간: " + email.getLockedUntil());
                }
            });

            // 메일 생성 및 발송 준비
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            String certifyCode;

            // 인증 코드 생성 또는 가져오기
            if (mailSendDto.getRequestType() == EmailType.REGISTER || mailSendDto.getRequestType() == EmailType.RESET_PASSWORD) {
                certifyCode = getCertifyCode();
            } else {
                certifyCode = mailSendDto.getCertifyCode();
            }

            setEmailContextVariables(context, mailSendDto.getRequestType(), certifyCode);

            String htmlContent = templateEngine.process("email-template", context);
            helper.setTo(mailSendDto.getEmailAddr());
            helper.setSubject(context.getVariable("subject").toString());
            helper.setText(htmlContent, true);
            helper.setFrom(EMAIL_SENDER);

            // 이메일 발송
            mailSender.send(message);

            // 이메일 히스토리 업데이트 또는 생성
            emailHistory.ifPresentOrElse(
                    email -> handleExistingEmail(email, certifyCode,"send"),
                    () -> {
                        Email newEmail = Email.createEmailHistory(mailSendDto.getEmailAddr(), certifyCode, mailSendDto.getRequestType());
                        mailRepository.save(newEmail);
                    });

            log.info("이메일 전송 성공 대상 이메일: {}", mailSendDto.getEmailAddr());
            return true;

        } catch (MessagingException | MailException e) {
            log.error("이메일 관련 예외 발생: {}", e.getMessage(), e);
            throw e;
        } catch (CustomEmailException cee) {
            log.warn("이메일 발송 제한: {}", cee.getMessage());
            throw cee;
        }
    }

    @Transactional
    @Override
    public boolean verifyEmail(MailVerifyDto mailVerifyDto) {
        return mailRepository.findByEmail(mailVerifyDto.getEmail())
                .map(email -> {
                    if (email.getVerificationCode().equals(mailVerifyDto.getCertifyCode())) {
                        handleExistingEmail(email, mailVerifyDto.getCertifyCode(), "verify");
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }



    // 인증번호 생성기
    private String getCertifyCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder certifyCode = new StringBuilder();
        int length = 8; // 인증코드 길이

        for (int i = 0; i < length; i++) {
            certifyCode.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }

        return certifyCode.toString();
    }

    // 타입에 맞게 제목과, 내용을 세팅해주는 메서드
    private void setEmailContextVariables(Context context, EmailType emailType, String certifyCode) {
        context.setVariable("subject", emailType.getSubject());
        context.setVariable("message", emailType.getContent());
        context.setVariable("certifyCode", certifyCode);
    }

    // 기존 이메일의 상태를 처리
    private void handleExistingEmail(Email email, String certifyCode, String type) {
        // 잠김 상태 확인
        if (email.isLocked()) {
            throw new CustomEmailException("이메일 인증휫수 초과 입니다. 잠시후 다시 시도해주세요 : " + email.getLockedUntil());
        }

        // 시도 횟수 증가
        email.addAttemptCount();
        email.settingVerificationCode(certifyCode);

        // 인증 성공 여부 확인
        if (email.getVerificationCode().equals(certifyCode) && type.equals("verify")) {
            email.unlockAccount();
            email.settingVerified();
        }

        mailRepository.save(email);
    }

    // 추후 이미지도 메일에 포함될경우 사용
    private String getBase64EncodedImage(String imagePath) throws IOException {
        Resource resource = new ClassPathResource(imagePath);
        byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
        return Base64.getEncoder().encodeToString(bytes);
    }
}
