package com.ssafy.pjtaserver.repository.book.info;

import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookInfoQueryRepository {
    Page<BookInfoSearchDto> searchPageComplex(BookInfoSearchCondition condition, Pageable pageable);
}
