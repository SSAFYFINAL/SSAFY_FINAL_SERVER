package com.ssafy.pjtaserver.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserJoinDto {
    @NotBlank(message = "userLoginId는 필수값 입니다.")
    private String userLoginId;

    @NotBlank(message = "userPwd는 필수값 입니다.")
    private String userPwd;

    @NotBlank(message = "checkUserPwd는 필수값 입니다.")
    private String checkUserPwd;

    @NotBlank(message = "usernameMain는 필수값 입니다.")
    private String usernameMain;

    @NotBlank(message = "userNickName는 필수값 입니다.")
    private String userNickName;

    @NotBlank(message = "userEmail는 필수값 입니다.")
    @Email
    private String userEmail;

    @NotBlank(message = "userPhone는 필수값 입니다.")
    @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$")
    private String userPhone;

    @NotNull(message = "isCheckedPw는 필수값 입니다.")
    private Boolean isCheckedPw;

    @NotNull(message = "isDuplicatedUserLoginId는 필수값 입니다.")
    private Boolean isDuplicatedUserLoginId;

    @NotNull(message = "isEmailChecked는 필수값 입니다.")
    private Boolean isEmailChecked;
}
