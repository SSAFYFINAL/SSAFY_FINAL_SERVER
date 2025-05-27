package com.ssafy.pjtaserver.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckUserPwDto {
    @NotBlank(message = "userPw는 필수 입력값 입니다.")
    private String userPw;
}
