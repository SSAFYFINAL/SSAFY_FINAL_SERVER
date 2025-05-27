package com.ssafy.pjtaserver.repository.book.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.pjtaserver.domain.book.*;
import com.ssafy.pjtaserver.dto.request.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.request.book.BookInfoSearchDto;
import com.ssafy.pjtaserver.dto.request.book.QBookInfoSearchDto;
import com.ssafy.pjtaserver.dto.response.book.BookInfoDto;
import com.ssafy.pjtaserver.dto.response.book.QBookInfoDto;
import com.ssafy.pjtaserver.repository.book.query.BookInfoQueryRepository;
import com.ssafy.pjtaserver.utils.SortUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.ssafy.pjtaserver.domain.book.QBookCheckout.bookCheckout;
import static com.ssafy.pjtaserver.domain.book.QBookInfo.bookInfo;
import static com.ssafy.pjtaserver.domain.book.QBookInstance.bookInstance;
import static com.ssafy.pjtaserver.domain.user.QFavoriteBookList.favoriteBookList;
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

    @Override
    public List<BookInfo> searchRecentBooks() {

        return jpaQueryFactory
                .select(bookInfo)
                .from(bookInfo)
                .orderBy(bookInfo.registryDate.desc())
                .limit(5)
                .fetch();
    }

    @Override
    public List<BookInfoDto> searchBookList() {
        return jpaQueryFactory
                .select(new QBookInfoDto(
                        bookInfo.id,
                        bookInfo.title,
                        bookInfo.authorName,
                        bookInfo.bookImgPath
                ))
                .from(bookInfo)
                .fetch();
    }

    @Override
    public List<BookInfoDto> searchBookListWithCategory(String userLoginId) {
        Category category = jpaQueryFactory
                .select(QCategory.category)
                .from(QCategory.category)
                .leftJoin(bookInfo).on(bookInfo.categoryId.id.eq(QCategory.category.id))
                .leftJoin(bookInstance).on(bookInstance.bookInfo.id.eq(bookInfo.id))
                .leftJoin(bookCheckout).on(bookCheckout.bookInstance.id.eq(bookInstance.id).and(bookCheckout.user.userLoginId.eq(userLoginId)))
                .leftJoin(favoriteBookList).on(favoriteBookList.bookInfo.id.eq(bookInfo.id).and(favoriteBookList.user.userLoginId.eq(userLoginId)))
                .groupBy(QCategory.category.id)
                .orderBy(bookCheckout.id.count().add(favoriteBookList.favoriteBookListId.count()).desc())
                .limit(1)
                .fetchOne();

        return jpaQueryFactory
                .select(new QBookInfoDto(
                        bookInfo.id,
                        bookInfo.title,
                        bookInfo.authorName,
                        bookInfo.bookImgPath
                ))
                .from(bookInfo)
                .where(bookInfo.categoryId.eq(category))
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
        return SortUtil.getOrderSpecifiers(pageable, this::mapSortProperty);
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