package com.ssafy.pjtaserver.dto.request.book;

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
    private String title;

    @QueryProjection
    public BookInfoSearchDto(Long bookInfoId, String authorName, String bookImgPath, String isbn, String publisherName, String title) {
        this.bookInfoId = bookInfoId;
        this.authorName = authorName;
        this.bookImgPath = bookImgPath;
        this.isbn = isbn;
        this.publisherName = publisherName;
        this.title = title;
    }
}
