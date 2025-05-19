package com.ssafy.pjtaserver.dto.response.guestbook;

import lombok.Data;

@Data
public class GuestbookCondition {
    private String orderBy;
    private String orderDirection;
}
