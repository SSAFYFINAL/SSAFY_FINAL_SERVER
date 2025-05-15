package com.ssafy.pjtaserver.dto.response.book;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookInfoSearchDto {
    private Long bookInfoId;
    private String authorName;
    private String bookImgPath;
    private String isbn;
    private String publisherName;
    private String seriesName;
    private String title;

    @QueryProjection
    public BookInfoSearchDto(Long bookInfoId, String authorName, String bookImgPath, String isbn, String publisherName, String seriesName, String title) {
        this.bookInfoId = bookInfoId;
        this.authorName = authorName;
        this.bookImgPath = bookImgPath;
        this.isbn = isbn;
        this.publisherName = publisherName;
        this.seriesName = seriesName;
        this.title = title;
    }
}
