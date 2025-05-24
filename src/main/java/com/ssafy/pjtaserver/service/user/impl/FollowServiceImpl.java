package com.ssafy.pjtaserver.service.user.impl;

import com.ssafy.pjtaserver.domain.user.Follow;
import com.ssafy.pjtaserver.domain.user.User;
import com.ssafy.pjtaserver.dto.request.user.FollowListDto;
import com.ssafy.pjtaserver.dto.request.user.FollowUserSearchCondition;
import com.ssafy.pjtaserver.dto.response.book.PageResponseDto;
import com.ssafy.pjtaserver.repository.user.follow.FollowRepository;
import com.ssafy.pjtaserver.repository.user.user.UserRepository;
import com.ssafy.pjtaserver.service.user.FollowService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Override
    public PageResponseDto<FollowListDto> getFollowList(Long ownerId,
                                                        FollowUserSearchCondition condition,
                                                        Pageable pageable,
                                                        String type) {
        User user = getUser(ownerId);

        // 정렬 및 페이징 처리
        Sort sort = Sort.by(Sort.Direction.fromString(condition.getOrderDirection()), condition.getOrderBy());
        Pageable updatedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<FollowListDto> results;

        // 팔로워 또는 팔로잉 조회
        if ("follower".equals(type)) {
            results = followRepository.selectFollowers(user, type, condition, updatedPageable);
        } else {
            results = followRepository.selectFollowing(user, type, condition, updatedPageable);
        }

        return new PageResponseDto<>(
                results.getContent(),
                results.getTotalElements(),
                results.getTotalPages(),
                results.getNumber(),
                results.getSize()
        );
    }

    @Override
    @Transactional
    public boolean addFollowRelation(String ownerId, Long targetId) {
        // 사용자 조회
        User owner = getUser(ownerId);
        User target = getUser(targetId);

        validateRelation(owner, target); // 관계 유효성 확인

        // 이미 팔로우 상태인지 확인
        boolean isCurrentlyFollowed = isFollowed(owner, target);

        // 이미 팔로우 상태라면 삭제 (언팔로우 처리)
        if (isCurrentlyFollowed) {
            followRepository.deleteByFollowOwnerAndFollowing(owner, target);
            followRepository.deleteByFollowOwnerAndFollower(target, owner);
            return false;
        }

        // 팔로우 처리
        followRepository.save(Follow.createFollowing(owner, target));
        followRepository.save(Follow.createFollower(target, owner));
        return true;
    }

    // 관계 가능성 확인 (자기 자신을 팔로우하는 경우 금지)
    private void validateRelation(User owner, User target) {
        if (owner.equals(target)) {
            throw new IllegalStateException("자기 자신을 팔로우할 수 없습니다.");
        }
    }

    // 주어진 User 간의 팔로우 상태 확인
    private boolean isFollowed(User owner, User target) {
        return followRepository.existsByFollowOwnerAndFollowing(owner, target);
    }

    // 주어진 ID로 사용자 조회
    private User getUser(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디의 유저를 찾을 수 없습니다: " + userId));
    }

    private User getUser(String userLoginId) {
        return userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디의 유저를 찾을 수 없습니다: " + userLoginId));
    }
}