package com.ssafy.pjtaserver.dto.response.book;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class BookInfoDto {
    private Long bookInfoId;
    private String title;
    private String authorName;
    private String bookImgPath;

    @QueryProjection
    public BookInfoDto(Long bookInfoId, String title, String authorName, String bookImgPath) {
        this.bookInfoId = bookInfoId;
        this.title = title;
        this.authorName = authorName;
        this.bookImgPath = bookImgPath;
    }
}
