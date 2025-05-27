package com.ssafy.pjtaserver.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserJoinDto {

    @NotBlank(message = "userLoginId는 필수값 입니다.")
    @Pattern(
            regexp = "^[a-zA-Z][a-zA-Z0-9]{3,15}$",
            message = "아이디는 영문자로 시작하며, 영문자와 숫자를 포함한 4~16자여야 합니다."
    )
    private String userLoginId;


    @NotBlank(message = "userPwd는 필수값 입니다.")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*()])(?=.*[a-zA-Z]).{8,}$",
            message = "비밀번호는 최소 8자 이상이며, 숫자와 특수 문자, 영문자를 포함해야 합니다."
    )
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
    @Pattern(
            regexp = "^010\\d{4}\\d{4}$",
            message = "전화번호는 010으로 시작하고 8자리 숫자로만 구성되어야 합니다."
    )
    private String userPhone;

    @NotNull(message = "isCheckedPw는 필수값 입니다.")
    private Boolean isCheckedPw;

    @NotNull(message = "isDuplicatedUserLoginId는 필수값 입니다.")
    private Boolean isDuplicatedUserLoginId;

    @NotNull(message = "isEmailChecked는 필수값 입니다.")
    private Boolean isEmailChecked;
}
