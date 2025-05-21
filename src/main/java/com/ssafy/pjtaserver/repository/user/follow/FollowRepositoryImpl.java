package com.ssafy.pjtaserver.repository.user.follow;

import com.ssafy.pjtaserver.domain.user.Follow;
import com.ssafy.pjtaserver.domain.user.User;

import java.util.List;

public class FollowRepositoryImpl implements FollowQueryRepository{

    @Override
    public List<Follow> selectFollowing(User follower) {
        return List.of();
    }

    @Override
    public List<Follow> selectFollowers(User following) {
        return List.of();
    }
}
