package com.ssafy.pjtaserver.repository.user;

import com.ssafy.pjtaserver.domain.user.Follow;
import com.ssafy.pjtaserver.domain.user.User;
import com.ssafy.pjtaserver.repository.user.query.FollowQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FollowRepository extends JpaRepository<Follow, Long>, FollowQueryRepository {
    boolean existsByFollowOwnerAndFollowing(User followOwner, User followings);
    void deleteByFollowOwnerAndFollowing(User followOwner, User followings);
    void deleteByFollowOwnerAndFollower(User followOwner, User follower);

    void deleteByFollowOwner(User followOwner);
    void deleteByFollower(User follower);
}
