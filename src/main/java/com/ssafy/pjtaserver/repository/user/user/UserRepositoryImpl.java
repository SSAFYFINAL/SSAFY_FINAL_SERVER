package com.ssafy.pjtaserver.repository.user.user;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.pjtaserver.domain.user.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ssafy.pjtaserver.domain.user.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserQueryRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public User getWithRoles(String userLoginId) {
        return jpaQueryFactory
                .selectFrom(user)
                .join(user.userRoleList).fetchJoin()
                .where(user.userLoginId.eq(userLoginId))
                .fetchOne();
    }
}
