package com.ssafy.pjtaserver.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserFindIdDto {

    @NotBlank(message = "email은 필수 입력값 입니다.")
    @Email
    private String email;

    @NotBlank(message = "usernameMain은 필수 입력값 입니다.")
    private String usernameMain;

}
