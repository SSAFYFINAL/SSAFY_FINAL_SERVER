package com.ssafy.pjtaserver.controller.user;

import com.ssafy.pjtaserver.dto.request.user.*;
import com.ssafy.pjtaserver.dto.response.book.PageResponseDto;
import com.ssafy.pjtaserver.dto.response.user.RecommendUserDto;
import com.ssafy.pjtaserver.enums.ApiResponseCode;
import com.ssafy.pjtaserver.enums.SearchFollowType;
import com.ssafy.pjtaserver.service.user.FollowService;
import com.ssafy.pjtaserver.service.user.UserManagementService;
import com.ssafy.pjtaserver.service.user.UserRecommendationService;
import com.ssafy.pjtaserver.utils.ApiResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth/user")
public class ApiAuthUserController {

    private final UserManagementService userManagementService;
    private final UserRecommendationService userRecommendationService;
    private final FollowService followService;
    @PutMapping("/password-reset")
    public ResponseEntity<ApiResponseUtil> updatePassword(@AuthenticationPrincipal UserDetails userDetails, @Validated @RequestBody UserResetPwDto userResetPwDto) {
        log.info("------------------------------api user password reset------------------------------");
        log.info("userResetPwDto : {}", userResetPwDto);
        log.info("userDetails : {}", userDetails.getUsername());
        String userLoginId = userDetails.getUsername();

        boolean isReset = userManagementService.resetUserPwd(userLoginId, userResetPwDto);

        if(!isReset) {
            return ApiResponseUtil.of(ApiResponseCode.REQUEST_FAILED, false);
        }
        return ApiResponseUtil.of(ApiResponseCode.SUCCESS, true);
    }

    @PutMapping(value = "/update", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponseUtil> updateUserInfo(@AuthenticationPrincipal UserDetails userDetails,
                                                          @Valid @ModelAttribute UserUpdateDto userUpdateDto) {
        log.info("------------------------------api user update------------------------------");
        log.info("userUpdateDto : {}", userUpdateDto);

        if(!userManagementService.updateUser(userDetails.getUsername(), userUpdateDto)) {
            return ApiResponseUtil.of(ApiResponseCode.REQUEST_FAILED, false);
        }
        return ApiResponseUtil.of(ApiResponseCode.SUCCESS, true);
    }

    @PostMapping("/follow/{targetUserId}")
    public ResponseEntity<ApiResponseUtil> followAdd(@AuthenticationPrincipal UserDetails userDetails,
                                                     @PathVariable Long targetUserId) {
        log.info("------------------------------api follow add------------------------------");
        log.info("targetUserID : {}", targetUserId);
        boolean result = followService.addFollowRelation(userDetails.getUsername(), targetUserId);

        if(!result) {
            return ApiResponseUtil.of(ApiResponseCode.UNFOLLOW_SUCCESS, true);
        }

        return ApiResponseUtil.of(ApiResponseCode.FOLLOW_COMPLETED, true);
    }

    // 본인의 팔로잉, 팔로워 다른사람의 팔로잉 팔로워 조회하고 싶을때 조회하고 싶은사람의 String id를 넘겨주면된다
    @PostMapping("/follow-list/{targetUserId}")
    public ResponseEntity<ApiResponseUtil> getFollowRelation(@PathVariable Long targetUserId,
                                                             @RequestBody FollowUserSearchCondition condition,
                                                             Pageable pageable,
                                                             @RequestParam String type) {
        log.info("------------------------------api follow-list ------------------------------");

        PageResponseDto<FollowListDto> followList = followService.getFollowList(targetUserId, condition, pageable, type);
        log.info("type = {}", type);
        return ApiResponseUtil.of(ApiResponseCode.SUCCESS, followList);
    }

    @PostMapping("/check-pw")
    public ResponseEntity<ApiResponseUtil> checkPw(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody CheckUserPwDto checkUserPwDto) {
        log.info("------------------------------api check pw------------------------------");

        if (!userManagementService.checkPw(userDetails.getUsername(), checkUserPwDto.getUserPw())) {
            return ApiResponseUtil.of(ApiResponseCode.CHECK_USER_PW_FAIL, false);
        }
        return ApiResponseUtil.of(ApiResponseCode.CHECK_USER_PW_SUCCESS, true);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponseUtil> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("------------------------------api user delete------------------------------");
        if (!userManagementService.deleteUser(userDetails.getUsername())) {
            return ApiResponseUtil.of(ApiResponseCode.REQUEST_FAILED, false);
        }
        return ApiResponseUtil.of(ApiResponseCode.SUCCESS, true);
    }

    @GetMapping("/recommend/similar-user")
    public ResponseEntity<ApiResponseUtil> recommendSimilarUser(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("------------------------------api recommend similar user------------------------------");
        List<RecommendUserDto> results = userRecommendationService.recommendSimilarUser(userDetails.getUsername(), 3);
        log.info("results : {}", results);
        return ApiResponseUtil.of(ApiResponseCode.SUCCESS, results);
    }
}
