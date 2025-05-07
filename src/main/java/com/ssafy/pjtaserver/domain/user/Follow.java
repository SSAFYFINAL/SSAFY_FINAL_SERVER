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
    @JoinColumn(name = "follower_id", nullable = false)
    private User followerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User followingId;

    @Column(name = "follow_date", nullable = false)
    private LocalDateTime followDate;

    @PrePersist
    public void prePersist() {
        if (followDate == null) {
            followDate = LocalDateTime.now();
        }
    }

    public Follow(User followerId, User followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }

}
