package com.ssafy.pjtaserver.service.book;

import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchDto;
import com.ssafy.pjtaserver.dto.response.book.PageResponseDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    PageResponseDto<BookInfoSearchDto> searchPageComplex(BookInfoSearchCondition condition, Pageable updatedPageable);
}
