package com.ssafy.pjtaserver.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserResetPwDto {
    @NotBlank(message = "userLoginId는 필수 입력값 입니다.")
    private String userLoginId;

    @NotBlank(message = "resetPwd는 필수 입력값 입니다.")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*()])(?=.*[a-zA-Z]).{8,}$",
            message = "비밀번호는 최소 8자 이상이며, 숫자와 특수 문자, 영문자를 포함해야 합니다."
    )
    private String resetPwd;
}
