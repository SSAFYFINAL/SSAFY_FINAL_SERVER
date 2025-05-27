package com.ssafy.pjtaserver.repository.user.query;

import com.querydsl.core.Tuple;
import com.ssafy.pjtaserver.dto.request.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.request.book.BookInfoSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FavoriteQueryRepository {
    Page<BookInfoSearchDto> searchFavoriteBook(BookInfoSearchCondition condition, Pageable pageable, Long userId);
    List<Tuple> weeklyPopular();
}
