package com.ssafy.pjtaserver.dto.response.book;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CheckoutHistoryDto {

    private Long bookInfoId;
    private Long checkoutId;
    private String title;
    private String authorName;
    private String publisherName;
    private LocalDateTime checkoutDate;
    private LocalDateTime dueDate;
    private String profileImgPath;

    @QueryProjection
    public CheckoutHistoryDto(Long bookInfoId, Long checkoutId, String title, String authorName, String publisherName, LocalDateTime checkoutDate, LocalDateTime dueDate, String profileImgPath) {
        this.bookInfoId = bookInfoId;
        this.checkoutId = checkoutId;
        this.title = title;
        this.authorName = authorName;
        this.publisherName = publisherName;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.profileImgPath = profileImgPath;
    }
    
}
