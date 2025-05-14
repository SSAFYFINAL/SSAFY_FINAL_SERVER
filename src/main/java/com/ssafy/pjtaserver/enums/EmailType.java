package com.ssafy.pjtaserver.enums;

import lombok.Getter;

@Getter
public enum EmailType {
    REGISTER("register"), RESET_PASSWORD("resetPassword"), TEMP_PASSWORD("tempPassword");

    private final String emailType;

    EmailType(String emailType) {
        this.emailType = emailType;
    }

}
