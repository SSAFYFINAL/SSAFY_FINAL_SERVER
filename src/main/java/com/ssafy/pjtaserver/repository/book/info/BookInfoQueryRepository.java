package com.ssafy.pjtaserver.repository.book.info;

import com.ssafy.pjtaserver.domain.book.BookInfo;
import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchDto;
import com.ssafy.pjtaserver.dto.response.book.RecentBooksDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookInfoQueryRepository {
    Page<BookInfoSearchDto> searchPageComplex(BookInfoSearchCondition condition, Pageable pageable);
    List<BookInfo> searchRecentBooks();
}
