package com.ssafy.pjtaserver.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "follow",
        uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"}))
public class Follow {
    @Id @GeneratedValue
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private User following;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follow_owner", nullable = false)
    private User followOwner;

    @Column(name = "follow_date", nullable = false)
    private LocalDateTime followDate;

    @PrePersist
    public void prePersist() {
        if (followDate == null) {
            followDate = LocalDateTime.now();
        }
    }

    // ** 생성자 정의 **
    private Follow(User followOwner, User follower, User following) {
        this.followOwner = followOwner;
        this.follower = follower;
        this.following = following;
    }

    // ** 정적 팩토리 메서드 **
    // Follower 관계 생성: 팔로우 관계의 소유자와 팔로워 사용자
    public static Follow createFollower(User followOwner, User follower) {
        return new Follow(followOwner, follower, null);
    }

    // Following 관계 생성: 팔로우 관계의 소유자와 팔로잉 사용자
    public static Follow createFollowing(User followOwner, User following) {
        return new Follow(followOwner, null, following);
    }


}
