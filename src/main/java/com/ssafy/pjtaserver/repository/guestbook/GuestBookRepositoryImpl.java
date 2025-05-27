package com.ssafy.pjtaserver.repository.guestbook;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.pjtaserver.dto.response.guestbook.GuestbookCondition;
import com.ssafy.pjtaserver.dto.response.guestbook.GuestbookListDto;
import com.ssafy.pjtaserver.dto.response.guestbook.QGuestbookListDto;
import com.ssafy.pjtaserver.utils.SortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.ssafy.pjtaserver.domain.guestBook.QGuestBook.guestBook;

@RequiredArgsConstructor
public class GuestBookRepositoryImpl implements GuestBookQueryRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<GuestbookListDto> getGuestBookList(GuestbookCondition condition, Pageable pageable, Long ownerId) {

        List<GuestbookListDto> result = queryFactory
                .select(new QGuestbookListDto(
                        guestBook.id.as("guestBookId"),
                        guestBook.owner.userLoginId,
                        guestBook.writer.userLoginId,
                        guestBook.content,
                        guestBook.writeDate,
                        guestBook.isDeleted
                ))
                .from(guestBook)
                .where(guestBook.owner.id.eq(ownerId), guestBook.isDeleted.isFalse())
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(guestBook.count())
                .from(guestBook)
                .where(guestBook.owner.id.eq(ownerId), guestBook.isDeleted.isFalse());


        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        return SortUtil.getOrderSpecifiers(pageable, this::mapSortProperty);
    }

    private OrderSpecifier<?> mapSortProperty(String property, boolean isAscending) {
        return isAscending ? guestBook.writeDate.asc() : guestBook.writeDate.desc();
    }
}