package com.ssafy.pjtaserver.dto.response.book;

import lombok.Data;

@Data
public class BookInfoSearchCondition {
    private String title;
    private String authorName;
    private String publisherName;

    private String orderBy;
    private String orderDirection;
}
