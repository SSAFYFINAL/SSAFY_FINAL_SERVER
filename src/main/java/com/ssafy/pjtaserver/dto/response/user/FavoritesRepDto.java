package com.ssafy.pjtaserver.dto.response.user;

import com.ssafy.pjtaserver.domain.book.Category;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FavoritesRepDto {
    private Category category;
    private String title;
    private String authorName;
    private String publisher;
    private LocalDateTime registryDate;
    private Long bookInfoId;
}
