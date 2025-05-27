package com.ssafy.pjtaserver.dto.response.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailDto {
    private Long userId;
    private String userLoginId;
    private String usernameMain;
    private String userNickname;
    private String userImgPath;
}
