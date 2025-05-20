package com.ssafy.pjtaserver.dto.response.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeeklyPopularBookDto {
    private Long bookInfoId;
    private String title;
    private String authorName;
    private String imgPath;

}
