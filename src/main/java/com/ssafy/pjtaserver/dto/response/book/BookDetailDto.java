package com.ssafy.pjtaserver.dto.response.book;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookDetailDto {
    private Long bookInfoId;
    private String title;
    private String authorName;
    private String isbn;
    private LocalDateTime registryDate;
    private String publisherName;
    private boolean isAvailableCheckedOut;
    private boolean isBookFavorite;
    private String bookImgPath;
    private String seriesName;
    private String description;
    private String categoryName;
}
