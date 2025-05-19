package com.ssafy.pjtaserver.repository.guestbook;

import com.ssafy.pjtaserver.domain.guestBook.GuestBook;
import com.ssafy.pjtaserver.dto.response.guestbook.GuestbookCondition;
import com.ssafy.pjtaserver.dto.response.guestbook.GuestbookListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface GuestBookQueryRepository {
    Page<GuestbookListDto> getGuestBookList(GuestbookCondition condition, Pageable pageable,Long ownerId);
}
