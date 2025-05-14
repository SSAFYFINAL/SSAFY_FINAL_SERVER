package com.ssafy.pjtaserver.enums;

import lombok.Getter;

@Getter
public enum EmailType {
    REGISTER("register","회원가입 인증", "회원가입을 위한 인증 코드입니다."),
    RESET_PASSWORD("resetPassword","비밀번호 재설정", "비밀번호 재설정을 위한 인증 코드입니다."),
    TEMP_PASSWORD("tempPassword","임시 비밀번호 발송", "임시 비밀번호는 다음과 같습니다.");

    private final String emailType;
    private final String subject;
    private final String content;

    EmailType(String emailType, String subject, String content) {
        this.emailType = emailType;
        this.subject = subject;
        this.content = content;
    }

}
