package com.ssafy.pjtaserver.repository.book.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.pjtaserver.dto.request.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.response.book.CheckOutRankingDto;
import com.ssafy.pjtaserver.dto.response.book.CheckoutHistoryDto;
import com.ssafy.pjtaserver.dto.response.book.QCheckOutRankingDto;
import com.ssafy.pjtaserver.dto.response.book.QCheckoutHistoryDto;
import com.ssafy.pjtaserver.repository.book.query.CheckoutQueryRepository;
import com.ssafy.pjtaserver.utils.SortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.ssafy.pjtaserver.domain.book.QBookCheckout.bookCheckout;
import static com.ssafy.pjtaserver.domain.book.QBookInfo.bookInfo;
import static com.ssafy.pjtaserver.domain.book.QBookInstance.bookInstance;
import static com.ssafy.pjtaserver.domain.user.QUser.user;

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
                        bookCheckout.dueDate,
                        bookInfo.bookImgPath))
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

    @Override
    public List<CheckOutRankingDto> getCheckoutRanking() {
        List<CheckOutRankingDto> result = queryFactory
                .select(new QCheckOutRankingDto(
                        user.id.count().as("checkoutCnt"),
                        user.id.as("userId"),
                        user.nickName.as("userNickname"),
                        user.profileImgPath.as("profileImgPath")
                ))
                .from(bookCheckout)
                .join(bookCheckout.user, user)
                .groupBy(user.id)
                .orderBy(bookCheckout.count().desc())
                .limit(3)
                .fetch();

        result.sort((dto1, dto2) -> dto2.getCheckoutCnt().compareTo(dto1.getCheckoutCnt()));

        for(int i = 0; i < result.size(); i++) {
            result.get(i).setRanking(i+1L);
        }

        return result;
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        return SortUtil.getOrderSpecifiers(pageable, this::mapSortProperty);
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
