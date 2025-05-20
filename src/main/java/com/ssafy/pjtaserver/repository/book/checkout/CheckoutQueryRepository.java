package com.ssafy.pjtaserver.repository.book.checkout;

import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.response.book.CheckoutHistoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CheckoutQueryRepository {
    Page<CheckoutHistoryDto> searchCheckoutHistory(BookInfoSearchCondition condition, Pageable pageable, String userLoginId);
}
