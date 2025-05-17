package com.ssafy.pjtaserver.repository.user.favorite;

import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteQueryRepository {
    Page<BookInfoSearchDto> searchFavoriteBook(BookInfoSearchCondition condition, Pageable pageable, String userLoginId);

}
