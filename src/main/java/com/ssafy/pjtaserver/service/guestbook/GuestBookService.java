package com.ssafy.pjtaserver.service.guestbook;

import com.ssafy.pjtaserver.dto.request.guestbook.GuestBookWriteDto;

public interface GuestBookService {
    boolean writeGuestBook(GuestBookWriteDto guestBookWriteDto);
}
