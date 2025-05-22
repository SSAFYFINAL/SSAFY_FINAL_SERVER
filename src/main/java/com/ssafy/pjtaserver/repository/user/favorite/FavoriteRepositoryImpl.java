package com.ssafy.pjtaserver.repository.user.favorite;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.pjtaserver.dto.request.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.request.book.BookInfoSearchDto;
import com.ssafy.pjtaserver.dto.request.book.QBookInfoSearchDto;
import com.ssafy.pjtaserver.util.SortUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.ssafy.pjtaserver.domain.book.QBookInfo.bookInfo;
import static com.ssafy.pjtaserver.domain.user.QFavoriteBookList.favoriteBookList;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteQueryRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<BookInfoSearchDto> searchFavoriteBook(BookInfoSearchCondition condition, Pageable pageable, String userLoginId) {

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
                .join(bookInfo.favoriteBookList, favoriteBookList)
                .on(favoriteBookList.user.userLoginId.eq(userLoginId)) // 조인 조건
                .where(
                        titleEq(condition.getTitle()),
                        authorNameEq(condition.getAuthorName()),
                        publisherNameEq(condition.getPublisherName()),
                        favoriteBookList.user.userLoginId.eq(userLoginId)
                )
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(bookInfo.id.count())
                .from(bookInfo)
                .join(bookInfo.favoriteBookList, favoriteBookList)
                .on(favoriteBookList.user.userLoginId.eq(userLoginId))
                .where(
                        titleEq(condition.getTitle()),
                        authorNameEq(condition.getAuthorName()),
                        publisherNameEq(condition.getPublisherName())
                );

        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    @Override
    public List<Tuple> weeklyPopular() {

        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(7);

        return jpaQueryFactory
                .select(bookInfo.count().as("count"), bookInfo.id.as("bookInfoId"))
                .from(favoriteBookList)
                .join(favoriteBookList.bookInfo, bookInfo)
                .where(
                        favoriteBookList.likeAt.between(startDate, endDate)
                                .or(
                                        JPAExpressions
                                                .selectFrom(favoriteBookList)
                                                .where(favoriteBookList.likeAt.between(startDate, endDate))
                                                .notExists()
                                )
                )
                .groupBy(bookInfo.id)
                .orderBy(bookInfo.count().desc())
                .limit(5)
                .fetch();
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
            case "id" -> isAscending ? bookInfo.id.asc() : bookInfo.isbn.desc();
            case "title" -> isAscending ? bookInfo.title.asc() : bookInfo.title.desc();
            case "authorName" -> isAscending ? bookInfo.authorName.asc() : bookInfo.authorName.desc();
            case "publisherName" -> isAscending ? bookInfo.publisherName.asc() : bookInfo.publisherName.desc();
            default -> null;
        };
    }
}
