package com.ssafy.pjtaserver.repository.user.query;

import com.ssafy.pjtaserver.domain.user.User;
import com.ssafy.pjtaserver.dto.request.user.FollowListDto;
import com.ssafy.pjtaserver.dto.request.user.FollowUserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FollowQueryRepository {
    Page<FollowListDto> selectFollowing(User follower, String type, FollowUserSearchCondition condition, Pageable pageable);
    Page<FollowListDto> selectFollowers(User following, String type, FollowUserSearchCondition condition, Pageable pageable);
}
