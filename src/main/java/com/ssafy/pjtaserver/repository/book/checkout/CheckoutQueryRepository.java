package com.ssafy.pjtaserver.repository.book.checkout;

import com.ssafy.pjtaserver.dto.request.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.response.book.CheckOutRankingDto;
import com.ssafy.pjtaserver.dto.response.book.CheckoutHistoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CheckoutQueryRepository {
    Page<CheckoutHistoryDto> searchCheckoutHistory(BookInfoSearchCondition condition, Pageable pageable, Long userId);
    List<CheckOutRankingDto> getCheckoutRanking();
}
