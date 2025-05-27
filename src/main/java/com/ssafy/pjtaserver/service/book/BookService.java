package com.ssafy.pjtaserver.service.book;

import com.ssafy.pjtaserver.dto.request.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.request.book.BookInfoSearchDto;
import com.ssafy.pjtaserver.dto.response.book.*;
import com.ssafy.pjtaserver.dto.response.user.WeeklyPopularBookDto;
import com.ssafy.pjtaserver.enums.BookResponseType;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService {
    PageResponseDto<BookInfoSearchDto> searchPageComplex(BookInfoSearchCondition condition, Pageable updatedPageable);
    BookDetailDto getDetail(Long bookInfoId, Optional<String> userLoginId);
    BookResponseType favoriteBookManager(String userLoginId, Long bookInfoId);
    BookResponseType checkoutAndReservationManager(String userLoginId, Long bookInfoId);
    PageResponseDto<BookInfoSearchDto> searchFavoritePageComplex(BookInfoSearchCondition condition, Pageable updatedPageable, Long userId);
    List<WeeklyPopularBookDto> getWeeklyPopular();
    List<RecentBooksDto> getRecentBooks();
    PageResponseDto<CheckoutHistoryDto> getCheckoutHistory(BookInfoSearchCondition condition ,Long userId, Pageable updatedPageable);
    List<CheckOutRankingDto> getCheckOutRanking();
    List<BookInfoDto> randomBookSearch(int n);
    List<BookInfoDto> userLikeBookList(String userLoginId, int n);
}
