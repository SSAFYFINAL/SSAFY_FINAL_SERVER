package com.ssafy.pjtaserver.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserUpdateDto {

    private MultipartFile profileImg;

    @NotBlank(message = "usernameMain은 필수 입력값 입니다.")
    private String usernameMain;

    @NotBlank(message = "usernameMain은 필수 입력값 입니다.")
    private String userNickname;

    @NotBlank(message = "userPhone는 필수값 입니다.")
    @Pattern(
            regexp = "^010\\d{4}\\d{4}$",
            message = "전화번호는 010으로 시작하고 8자리 숫자로만 구성되어야 합니다."
    )
    private String userPhone;

    @NotBlank(message = "userPwd는 필수값 입니다.")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*()])(?=.*[a-zA-Z]).{8,}$",
            message = "비밀번호는 최소 8자 이상이며, 숫자와 특수 문자, 영문자를 포함해야 합니다."
    )
    private String userPwd;
}
