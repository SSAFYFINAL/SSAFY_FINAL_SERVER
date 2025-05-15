package com.ssafy.pjtaserver.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserResetPwDto {
    @NotBlank(message = "userLoginId는 필수 입력값 입니다.")
    private String userLoginId;

    @NotBlank(message = "resetPwd는 필수 입력값 입니다.")
    private String resetPwd;
}
