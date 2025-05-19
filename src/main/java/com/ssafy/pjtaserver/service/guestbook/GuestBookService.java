package com.ssafy.pjtaserver.service.guestbook;

import com.ssafy.pjtaserver.dto.request.guestbook.GuestBookWriteDto;
import com.ssafy.pjtaserver.dto.response.book.PageResponseDto;
import com.ssafy.pjtaserver.dto.response.guestbook.GuestbookCondition;
import com.ssafy.pjtaserver.dto.response.guestbook.GuestbookListDto;
import org.springframework.data.domain.Pageable;

public interface GuestBookService {
    boolean writeGuestBook(GuestBookWriteDto guestBookWriteDto);
    PageResponseDto<GuestbookListDto> searchGuestbookPageComplex(GuestbookCondition condition, Pageable pageable, Long ownerId);
    boolean deleteGuestBook(String userLoginId, Long guestBookId);
    boolean updateGuestBook(String userLoginId, Long guestBookId, String content);
}
