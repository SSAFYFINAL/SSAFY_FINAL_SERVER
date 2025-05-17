package com.ssafy.pjtaserver.service.book;

import com.ssafy.pjtaserver.dto.response.book.BookDetailDto;
import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchDto;
import com.ssafy.pjtaserver.dto.response.book.PageResponseDto;
import com.ssafy.pjtaserver.enums.BookResponseType;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookService {
    PageResponseDto<BookInfoSearchDto> searchPageComplex(BookInfoSearchCondition condition, Pageable updatedPageable);
    BookDetailDto getDetail(Long bookInfoId, Optional<String> userLoginId);
    BookResponseType favoriteBookManager(String userLoginId, Long bookInfoId);
    BookResponseType checkoutAndReservationManager(String userLoginId, Long bookInfoId);
}
