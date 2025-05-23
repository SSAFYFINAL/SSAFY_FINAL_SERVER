package com.ssafy.pjtaserver.repository.book.checkout;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.pjtaserver.dto.request.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.response.book.CheckoutHistoryDto;
import com.ssafy.pjtaserver.dto.response.book.QCheckoutHistoryDto;
import com.ssafy.pjtaserver.util.SortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.ssafy.pjtaserver.domain.book.QBookCheckout.bookCheckout;
import static com.ssafy.pjtaserver.domain.book.QBookInfo.bookInfo;
import static com.ssafy.pjtaserver.domain.book.QBookInstance.bookInstance;

@RequiredArgsConstructor
public class CheckoutRepositoryImpl implements CheckoutQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CheckoutHistoryDto> searchCheckoutHistory(BookInfoSearchCondition condition, Pageable pageable, Long userId) {
        List<CheckoutHistoryDto> results = queryFactory
                .select(new QCheckoutHistoryDto(
                        bookInstance.bookInfo.id,
                        bookCheckout.id.as("checkoutId"),
                        bookInfo.title,
                        bookInfo.authorName,
                        bookInfo.publisherName,
                        bookCheckout.checkoutDate,
                        bookCheckout.dueDate))
                .from(bookCheckout)
                .where(bookCheckout.user.id.eq(userId))
                .join(bookCheckout.bookInstance, bookInstance)
                .join(bookInstance.bookInfo, bookInfo)
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery =
                queryFactory
                        .select(bookCheckout.id.count())
                        .from(bookCheckout)
                        .join(bookCheckout.bookInstance, bookInstance)
                        .join(bookInstance.bookInfo, bookInfo)
                        .where(bookCheckout.user.id.eq(userId));

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        return SortUtils.getOrderSpecifiers(pageable, this::mapSortProperty);
    }

    // 정렬 조건정의
    private OrderSpecifier<?> mapSortProperty(String property, boolean isAscending) {
        return switch (property) {
            case "id" -> isAscending ? bookCheckout.id.asc() : bookCheckout.id.desc();
            case "checkoutDate" -> isAscending ? bookCheckout.checkoutDate.asc() : bookCheckout.checkoutDate.desc();
            default -> null;
        };
    }
}
