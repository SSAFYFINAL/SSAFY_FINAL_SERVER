package com.ssafy.pjtaserver.dto.request.user;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class FollowListDto {
    private String userLoginId;
    private String usernameMain;;
    private String profileImgPath;

    @QueryProjection
    public FollowListDto(String userLoginId, String usernameMain, String profileImgPath) {
        this.userLoginId = userLoginId;
        this.usernameMain = usernameMain;
        this.profileImgPath = profileImgPath;
    }
}
