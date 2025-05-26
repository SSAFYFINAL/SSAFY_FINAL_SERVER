package com.ssafy.pjtaserver.repository.user.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.pjtaserver.domain.user.User;
import com.ssafy.pjtaserver.repository.user.query.UserQueryRepository;
import lombok.RequiredArgsConstructor;

import static com.ssafy.pjtaserver.domain.user.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserQueryRepository {

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
