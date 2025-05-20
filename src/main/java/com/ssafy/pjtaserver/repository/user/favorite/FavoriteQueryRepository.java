package com.ssafy.pjtaserver.repository.user.favorite;

import com.querydsl.core.Tuple;
import com.ssafy.pjtaserver.domain.book.BookInfo;
import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteQueryRepository {
    Page<BookInfoSearchDto> searchFavoriteBook(BookInfoSearchCondition condition, Pageable pageable, String userLoginId);
    List<Tuple> weeklyPopular();
}
