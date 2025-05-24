package com.ssafy.pjtaserver.dto.request.user;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class FollowListDto {
    private Long followId;
    private String userLoginId;
    private String usernameMain;;
    private String userNickname;
    private String profileImgPath;

    @QueryProjection
    public FollowListDto(Long followId, String userLoginId, String usernameMain, String userNickname, String profileImgPath) {
        this.followId = followId;
        this.userLoginId = userLoginId;
        this.usernameMain = usernameMain;
        this.userNickname = userNickname;
        this.profileImgPath = profileImgPath;
    }
}
