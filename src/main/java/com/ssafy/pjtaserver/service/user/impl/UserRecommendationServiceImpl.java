package com.ssafy.pjtaserver.service.user.impl;

import com.ssafy.pjtaserver.domain.user.User;
import com.ssafy.pjtaserver.dto.response.user.RecommendUserDto;
import com.ssafy.pjtaserver.repository.user.user.UserRepository;
import com.ssafy.pjtaserver.service.user.UserRecommendationService;
import com.ssafy.pjtaserver.utils.SimilarityCalculatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j

public class UserRecommendationServiceImpl implements UserRecommendationService {
    private final UserRepository userRepository;

    @Override
    public List<RecommendUserDto> recommendSimilarUser(String userLoginId, int topN) {
        Map<String, Double> similarityScores = getSimilarityScores(userLoginId);

        // 유사도 순으로 정렬하고 상위 N명의 UserLoginId 목록만 추출
        List<String> recommendedUserLoginIds = similarityScores.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue())) // 유사도 내림차순 정렬
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 한 번의 쿼리로 추천 대상 유저 데이터 가져오기
        List<User> users = userRepository.findAllByUserLoginIdIn(recommendedUserLoginIds);

        // DTO로 변환
        return users.stream().map(user -> {
            RecommendUserDto dto = new RecommendUserDto();
            dto.setUserId(user.getId());
            dto.setUserLoginId(user.getUserLoginId());
            dto.setUsernameMain(user.getUsernameMain());
            dto.setUserNickname(user.getNickName());
            dto.setUserImgPath(user.getProfileImgPath());
            return dto;
        }).collect(Collectors.toList());
    }

    // 유사도 점수 반환해주는 메서드
    private Map<String, Double> getSimilarityScores(String userLoginId) {
        Map<String, List<String>> userPreferences = getUserPreferences();

        // 기준 유저의 선호 정보 추출
        List<String> targetUserPreferences = userPreferences.get(userLoginId);
        if (targetUserPreferences == null) {
            throw new IllegalArgumentException("해당 ID를 가진 유저를 찾을 수 없습니다.");
        }

        // 나머지 유저와의 유사도 비교
        Map<String, Double> similarityScores = new HashMap<>();
        userPreferences.forEach((otherUserId, preferences) -> {
            if (!otherUserId.equals(userLoginId)) { // 자신 제외
                double similarity = SimilarityCalculatorUtil.calculateCosineSimilarity(targetUserPreferences, preferences);
                similarityScores.put(otherUserId, similarity);
            }
        });
        return similarityScores;
    }

    private Map<String, List<String>> getUserPreferences() {
        List<User> users = userRepository.findAllUsersWithFavorites();

        return users.stream().collect(Collectors.toMap(
                User::getUserLoginId,
                user -> {
                    if (user.getFavoriteBookList() == null) {
                        return Collections.emptyList(); // 관심사 없음
                    }
                    return user.getFavoriteBookList().stream()
                            .filter(fb -> fb.getBookInfo() != null && fb.getBookInfo().getCategoryId() != null)
                            .map(fb -> fb.getBookInfo().getCategoryId().getCategoryTitle())
                            .collect(Collectors.toList());
                }
        ));
    }
}
