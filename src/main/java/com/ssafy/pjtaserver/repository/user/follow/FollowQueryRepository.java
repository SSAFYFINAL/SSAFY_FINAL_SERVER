package com.ssafy.pjtaserver.repository.user.follow;

import com.ssafy.pjtaserver.domain.user.Follow;
import com.ssafy.pjtaserver.domain.user.User;

import java.util.List;

public interface FollowQueryRepository {
    List<Follow> selectFollowing(User follower);
    List<Follow> selectFollowers(User following);
}
