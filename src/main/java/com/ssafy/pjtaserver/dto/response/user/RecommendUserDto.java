package com.ssafy.pjtaserver.dto.response.user;

import lombok.Data;

@Data
public class RecommendUserDto {
    private Long userId;
    private String userLoginId;
    private String usernameMain;
    private String userNickname;
    private String userImgPath;
}
