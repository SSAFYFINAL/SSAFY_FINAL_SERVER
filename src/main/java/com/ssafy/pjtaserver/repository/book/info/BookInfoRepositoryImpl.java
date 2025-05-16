package com.ssafy.pjtaserver.repository.book.info;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchDto;
import com.ssafy.pjtaserver.dto.response.book.QBookInfoSearchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Objects;

import static com.ssafy.pjtaserver.domain.book.QBookInfo.bookInfo;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Slf4j
public class BookInfoRepositoryImpl implements BookInfoQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<BookInfoSearchDto> searchPageComplex(BookInfoSearchCondition condition, Pageable pageable) {
        List<BookInfoSearchDto> results = jpaQueryFactory
                .select(new QBookInfoSearchDto(
                        bookInfo.id.as("bookInfoId"),
                        bookInfo.authorName.as("authorName"),
                        bookInfo.bookImgPath.as("bookImgPath"),
                        bookInfo.isbn,
                        bookInfo.publisherName.as("publisherName"),
                        bookInfo.seriesName.as("seriesName"),
                        bookInfo.title))
                .from(bookInfo)
                .where(
                        titleEq(condition.getTitle()),
                        authorNameEq(condition.getAuthorName()),
                        publisherNameEq(condition.getPublisherName())
                )
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(bookInfo.id.count())
                .from(bookInfo)
                .where(
                        titleEq(condition.getTitle()),
                        authorNameEq(condition.getAuthorName()),
                        publisherNameEq(condition.getPublisherName())
                );
        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    private BooleanExpression titleEq(String title) {
        return hasText(title) ? bookInfo.title.contains(title) : null;
    }

    private BooleanExpression authorNameEq(String authorName) {
        return hasText(authorName) ? bookInfo.authorName.contains(authorName) : null;
    }

    private BooleanExpression publisherNameEq(String publisherName) {
        return hasText(publisherName) ? bookInfo.publisherName.contains(publisherName) : null;
    }

    /**
     * Pageable 에 정의되어있는 정렬 정보를 queryDsl의 OrderSpecifier 배열로 변환해주는 역할을 한다.
     * @param pageable : 페이징과 정렬 조건을 갖고있는 Pageable 객체이다. getSort()를 이용해 정렬속성, 방향에 대한 정보를
     *                 가지고 올 수 있음
     * @return : queryDsl 쿼리에서 정렬 조건으로 사용할 수 있는 orderSpecifier 배열을 반환
     *            이게 위의 쿼리문의 orderBy에 들어가 정렬을 진행해준다.
     */
    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        pageable.getSort().forEach(order -> {
            log.info("Order Property: {}, Direction: {}", order.getProperty(), order.getDirection());
        });

        return pageable.getSort().stream().map(order -> switch (order.getProperty()) {
                    case "title" -> order.isAscending() ? bookInfo.title.asc() : bookInfo.title.desc();
                    case "authorName" -> order.isAscending() ? bookInfo.authorName.asc() : bookInfo.authorName.desc();
                    case "publisherName" -> order.isAscending() ? bookInfo.publisherName.asc() : bookInfo.publisherName.desc();
                    default -> null;
                }).filter(Objects::nonNull)
                .toArray(OrderSpecifier<?>[]::new);
    }

}