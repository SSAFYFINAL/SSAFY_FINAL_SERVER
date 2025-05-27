package com.ssafy.pjtaserver.service.user;

import com.ssafy.pjtaserver.dto.request.user.FollowListDto;
import com.ssafy.pjtaserver.dto.request.user.FollowUserSearchCondition;
import com.ssafy.pjtaserver.dto.response.book.PageResponseDto;
import org.springframework.data.domain.Pageable;

public interface FollowService {
    PageResponseDto<FollowListDto> getFollowList(Long userLoginId,
                                                 FollowUserSearchCondition condition,
                                                 Pageable pageable,
                                                 String type);

    boolean addFollowRelation(String ownerId, Long targetId);


}
