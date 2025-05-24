package com.ssafy.pjtaserver.utils;

import com.querydsl.core.types.OrderSpecifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.Objects;
import java.util.function.BiFunction;

@Slf4j
public class SortUtil {


    private SortUtil() {
    }

    /**
     * Pageable 에 정의되어있는 정렬 정보를 queryDsl의 OrderSpecifier 배열로 변환해주는 역할을 한다.
     * @param pageable : 페이징과 정렬 조건을 갖고있는 Pageable 객체이다. getSort()를 이용해 정렬속성, 방향에 대한 정보를
     *                 가지고 올 수 있음
     * @return : queryDsl 쿼리에서 정렬 조건으로 사용할 수 있는 orderSpecifier 배열을 반환
     *            이게 위의 쿼리문의 orderBy에 들어가 정렬을 진행해준다.
     */
    public static OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, BiFunction<String, Boolean, OrderSpecifier<?>> orderSpecifierMapper) {
        pageable.getSort().forEach(order ->
                log.info("Order Property: {}, Direction: {}", order.getProperty(), order.getDirection())
        );

        return pageable.getSort()
                .stream()
                .map(order -> {
                    // 정렬 속성과 방향 매핑 (true -> ASC, false -> DESC)
                    return orderSpecifierMapper.apply(order.getProperty(), order.isAscending());
                })
                .filter(Objects::nonNull) // null 제거
                .toArray(OrderSpecifier<?>[]::new);
    }
}