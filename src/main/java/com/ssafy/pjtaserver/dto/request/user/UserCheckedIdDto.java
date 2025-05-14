package com.ssafy.pjtaserver.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCheckedIdDto {
    @NotBlank(message = "userLoginId는 필수값 입니다.")
    private String userLoginId;
}
