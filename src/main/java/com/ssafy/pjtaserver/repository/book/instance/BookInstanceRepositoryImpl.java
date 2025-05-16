package com.ssafy.pjtaserver.repository.book.instance;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.pjtaserver.enums.BookCheckoutStatus;
import lombok.RequiredArgsConstructor;

import static com.ssafy.pjtaserver.domain.book.QBookInfo.bookInfo;
import static com.ssafy.pjtaserver.domain.book.QBookInstance.bookInstance;

@RequiredArgsConstructor
public class BookInstanceRepositoryImpl implements BookInstanceQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean isBookAvailableForCheckout(Long bookInfoId) {
        Long availableCnt = queryFactory
                .select(bookInstance.id.count())
                .from(bookInstance)
                .join(bookInstance.bookInfo, bookInfo)
                .where(bookInfo.id.eq(bookInfoId).and(bookInstance.status.eq(BookCheckoutStatus.AVAILABLE)))
                .fetchOne();
        return availableCnt != null && availableCnt > 0L;
    }
}
