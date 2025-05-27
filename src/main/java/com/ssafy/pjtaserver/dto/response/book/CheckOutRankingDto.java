package com.ssafy.pjtaserver.dto.response.book;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class CheckOutRankingDto {
    private Long ranking;
    private Long checkoutCnt;
    private Long userId;
    private String userNickname;
    private String profileImgPath;

    @QueryProjection
    public CheckOutRankingDto(Long checkoutCnt,Long userId, String userNickname, String profileImgPath) {
        this.checkoutCnt = checkoutCnt;
        this.userId = userId;
        this.userNickname = userNickname;
        this.profileImgPath = profileImgPath;
    }
}
