package com.ssafy.pjtaserver.dto.request.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FollowUserSearchCondition {
    private String usernameMain;
    private String userLoginId;

    private String orderBy;
    private String orderDirection;
}
