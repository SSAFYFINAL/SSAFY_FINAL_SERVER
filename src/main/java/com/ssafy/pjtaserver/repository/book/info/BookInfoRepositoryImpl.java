package com.ssafy.pjtaserver.repository.book.info;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchDto;
import com.ssafy.pjtaserver.dto.response.book.QBookInfoSearchDto;
import com.ssafy.pjtaserver.util.SortUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

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

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        return SortUtils.getOrderSpecifiers(pageable, this::mapSortProperty);
    }

    // 정렬 조건정의
    private OrderSpecifier<?> mapSortProperty(String property, boolean isAscending) {
        return switch (property) {
            case "" -> isAscending ? bookInfo.id.asc() : bookInfo.isbn.desc();
            case "title" -> isAscending ? bookInfo.title.asc() : bookInfo.title.desc();
            case "authorName" -> isAscending ? bookInfo.authorName.asc() : bookInfo.authorName.desc();
            case "publisherName" -> isAscending ? bookInfo.publisherName.asc() : bookInfo.publisherName.desc();
            default -> null;
        };
    }

}