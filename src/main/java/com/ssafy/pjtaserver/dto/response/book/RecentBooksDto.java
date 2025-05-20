package com.ssafy.pjtaserver.dto.response.book;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecentBooksDto {
    private Long bookInfoId;
    private String title;
    private String authorName;
    private String bookImgPath;
}
