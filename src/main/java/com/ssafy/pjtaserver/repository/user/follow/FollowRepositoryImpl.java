package com.ssafy.pjtaserver.repository.user.follow;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.pjtaserver.domain.user.User;
import com.ssafy.pjtaserver.dto.request.user.FollowListDto;
import com.ssafy.pjtaserver.dto.request.user.FollowUserSearchCondition;
import com.ssafy.pjtaserver.dto.request.user.QFollowListDto;
import com.ssafy.pjtaserver.util.SortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.ssafy.pjtaserver.domain.user.QFollow.follow;
import static com.ssafy.pjtaserver.domain.user.QUser.user;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowQueryRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<FollowListDto> selectFollowing(User ownerId, String type, FollowUserSearchCondition condition, Pageable pageable) {
        String FOLLOWING = "following";
        List<FollowListDto> results = jpaQueryFactory
                .select(new QFollowListDto(
                        follow.following.userLoginId,
                        follow.following.usernameMain,
                        follow.following.profileImgPath
                ))
                .from(follow)
                .join(follow.following, user)
                .where(
                        follow.followOwner.eq(ownerId),
                        follow.followOwner.isNotNull(),
                        usernameMainEq(ownerId.getUsernameMain(), FOLLOWING),
                        userLoginIdEq(ownerId.getUserLoginId(), FOLLOWING)
                )
                .orderBy(getOrderFollowingSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(follow.id.count())
                .from(follow)
                .join(follow.following, user)
                .where(
                        follow.followOwner.eq(ownerId),
                        follow.followOwner.isNotNull(),
                        usernameMainEq(ownerId.getUsernameMain(), FOLLOWING),
                        userLoginIdEq(ownerId.getUserLoginId(), FOLLOWING)
                );
        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<FollowListDto> selectFollowers(User follower, String type, FollowUserSearchCondition condition, Pageable pageable) {
        String FOLLOWER = "follower";
        List<FollowListDto> results = jpaQueryFactory
                .select(new QFollowListDto(
                        follow.followOwner.userLoginId,
                        follow.followOwner.usernameMain,
                        follow.followOwner.profileImgPath
                ))
                .from(follow)
                .join(follow.followOwner, user)
                .where(
                        follow.follower.eq(follower),
                        follow.follower.isNotNull(),
                        usernameMainEq(follower.getUsernameMain(), FOLLOWER),
                        userLoginIdEq(follower.getUserLoginId(), FOLLOWER)
                )
                .orderBy(getOrderFollowerSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(follow.id.count())
                .from(follow)
                .join(follow.followOwner, user)
                .where(
                        follow.follower.eq(follower),
                        follow.follower.isNotNull(),
                        usernameMainEq(follower.getUsernameMain(), FOLLOWER),
                        userLoginIdEq(follower.getUserLoginId(), FOLLOWER)
                );
        return PageableExecutionUtils.getPage(results, pageable, countQuery::fetchOne);
    }


    private BooleanExpression usernameMainEq(String usernameMain, String type) {
        String FOLLOWER = "follower";

        if(type.equals(FOLLOWER)) {
            return hasText(usernameMain) ? follow.follower.usernameMain.contains(usernameMain) : null;
        } else {
            return hasText(usernameMain) ? follow.following.usernameMain.contains(usernameMain) : null;
        }
    }

    private BooleanExpression userLoginIdEq(String userLoginId, String type) {
        String FOLLOWER = "follower";

        if(type.equals(FOLLOWER)) {
            return hasText(userLoginId) ? follow.follower.userLoginId.contains(userLoginId) : null;
        } else {
            return hasText(userLoginId) ? follow.following.userLoginId.contains(userLoginId) : null;
        }
    }

    private OrderSpecifier<?>[] getOrderFollowerSpecifiers(Pageable pageable) {
        return SortUtils.getOrderSpecifiers(pageable, this::mapSortFollowerProperty);
    }

    // 정렬 조건정의
    private OrderSpecifier<?> mapSortFollowerProperty(String property, boolean isAscending) {
        return switch (property) {
            case "userLoginId" -> isAscending ? follow.followOwner.userLoginId.asc() : follow.followOwner.userLoginId.desc();
            case "usernameMain" -> isAscending ? follow.followOwner.usernameMain.asc() : follow.followOwner.usernameMain.desc();
            default -> follow.follower.userLoginId.asc();
        };
    }

    private OrderSpecifier<?>[] getOrderFollowingSpecifiers(Pageable pageable) {
        return SortUtils.getOrderSpecifiers(pageable, this::mapSortFollowingProperty);
    }

    // 정렬 조건정의
    private OrderSpecifier<?> mapSortFollowingProperty(String property, boolean isAscending) {
        return switch (property) {
            case "userLoginId" -> isAscending ? follow.following.userLoginId.asc() : follow.following.userLoginId.desc();
            case "usernameMain" -> isAscending ? follow.following.usernameMain.asc() : follow.following.usernameMain.desc();
            default -> follow.following.userLoginId.asc();

        };
    }


}
