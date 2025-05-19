package com.ssafy.pjtaserver.dto.response.guestbook;

import com.querydsl.core.annotations.QueryProjection;
import com.ssafy.pjtaserver.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GuestbookListDto {
    private Long guestbookId;
    private String ownerId;
    private String writerId;
    private String content;
    private LocalDateTime writeDate;
    private boolean isDeleted;

    @QueryProjection
    public GuestbookListDto(Long guestbookId, String ownerId, String writerId, String content, LocalDateTime writeDate, boolean isDeleted) {
        this.guestbookId = guestbookId;
        this.ownerId = ownerId;
        this.writerId = writerId;
        this.content = content;
        this.writeDate = writeDate;
        this.isDeleted = isDeleted;
    }
}
