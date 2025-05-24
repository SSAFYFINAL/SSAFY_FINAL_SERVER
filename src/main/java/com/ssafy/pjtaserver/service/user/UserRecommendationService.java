package com.ssafy.pjtaserver.service.user;

import com.ssafy.pjtaserver.dto.response.user.RecommendUserDto;

import java.util.List;

public interface UserRecommendationService {

    List<RecommendUserDto> recommendSimilarUser(String userLoginId, int topN);

}
