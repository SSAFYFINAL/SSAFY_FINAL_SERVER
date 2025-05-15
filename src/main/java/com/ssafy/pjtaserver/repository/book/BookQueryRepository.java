package com.ssafy.pjtaserver.repository.book;

import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookQueryRepository {
    Page<BookInfoSearchDto> searchPageComplex(BookInfoSearchCondition condition, Pageable pageable);
}
