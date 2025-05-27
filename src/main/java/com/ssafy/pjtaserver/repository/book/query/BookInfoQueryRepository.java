package com.ssafy.pjtaserver.repository.book.query;

import com.ssafy.pjtaserver.domain.book.BookInfo;
import com.ssafy.pjtaserver.dto.request.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.request.book.BookInfoSearchDto;
import com.ssafy.pjtaserver.dto.response.book.BookInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookInfoQueryRepository {
    Page<BookInfoSearchDto> searchPageComplex(BookInfoSearchCondition condition, Pageable pageable);
    List<BookInfo> searchRecentBooks();
    List<BookInfoDto> searchBookList();
    List<BookInfoDto> searchBookListWithCategory(String userLoginId);
}
