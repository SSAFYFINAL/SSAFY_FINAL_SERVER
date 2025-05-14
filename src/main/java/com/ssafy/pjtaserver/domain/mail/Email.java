package com.ssafy.pjtaserver.domain.mail;

import com.ssafy.pjtaserver.enums.EmailType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "email_verification")
public class Email {

    @Id @GeneratedValue
    @Column(name = "email_id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "verification_code", nullable = false)
    private String verificationCode;

    @Column(name = "email_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EmailType emailType;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name ="verified", nullable = false)
    private boolean verified;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "attempt_count", nullable = false)
    private Integer attemptCount;

    @Column(name = "locked", nullable = false)
    private boolean locked;

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    @PrePersist
    public void prePersist() {
        this.expiresAt = LocalDateTime.now().plusMinutes(5);
        this.verified = false;
        this.locked = false;
        this.attemptCount = 0;
    }

    public static Email createEmailHistory(String email, String verificationCode, EmailType emailType) {
        Email emailInfo = new Email();
        emailInfo.email = email;
        emailInfo.verificationCode = verificationCode;
        emailInfo.emailType = emailType;
        return emailInfo;
    }

    public static Email updateEmailHistory(String email, String verificationCode, EmailType emailType) {
        Email emailInfo = new Email();
        emailInfo.email = email;
        emailInfo.verificationCode = verificationCode;
        emailInfo.emailType = emailType;
        return emailInfo;
    }

    // 시도 휫수 5회이상이면 이메일 제한걸어준다.
    public void addAttemptCount() {
        this.attemptCount++;

        if (this.attemptCount >= 5) {
            lockAccount();
        }
    }

    public boolean isLocked() {
        // 현재 시간이 잠금 만료 시간을 지나지 않았으면 잠겨 있음
        if (this.locked && this.lockedUntil != null) {
            if (LocalDateTime.now().isBefore(this.lockedUntil)) {
                return true;
            }
            // 만료 시간이 지나면 잠금을 해제
            this.unlockAccount();
        }
        return false;
    }

    // 해당 이메일을 통한 인증 잠금처리 해준다 5분
    public void lockAccount() {
        this.locked = true;
        this.lockedUntil = LocalDateTime.now().plusMinutes(5);
    }

    public void unlockAccount() {
        this.locked = false;
        this.lockedUntil = null;
        this.attemptCount = 0;
    }

    public void settingVerified() {
        this.verified = true;                    // 인증 완료 상태 업데이트
        this.verifiedAt = LocalDateTime.now();   // 인증 완료 시간 저장
    }

    public void settingVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }



}
