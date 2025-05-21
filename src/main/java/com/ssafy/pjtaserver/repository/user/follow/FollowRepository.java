package com.ssafy.pjtaserver.repository.user.follow;

import com.ssafy.pjtaserver.domain.user.Follow;
import com.ssafy.pjtaserver.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
}
