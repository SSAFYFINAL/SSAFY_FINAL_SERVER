package com.ssafy.pjtaserver.controller.user;

import com.ssafy.pjtaserver.dto.request.user.*;
import com.ssafy.pjtaserver.dto.response.book.PageResponseDto;
import com.ssafy.pjtaserver.enums.ApiResponseCode;
import com.ssafy.pjtaserver.service.user.FollowService;
import com.ssafy.pjtaserver.service.user.UserService;
import com.ssafy.pjtaserver.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth/user")
public class ApiAuthUserController {

    private final UserService userService;
    private final FollowService followService;
    @PutMapping("/password-reset")
    public ResponseEntity<ApiResponse> updatePassword(@AuthenticationPrincipal UserDetails userDetails, @Validated @RequestBody UserResetPwDto userResetPwDto) {
        log.info("------------------------------api user password reset------------------------------");
        log.info("userResetPwDto : {}", userResetPwDto);
        log.info("userDetails : {}", userDetails.getUsername());
        String userLoginId = userDetails.getUsername();

        boolean isReset = userService.resetUserPwd(userLoginId, userResetPwDto);

        if(!isReset) {
            return ApiResponse.of(ApiResponseCode.REQUEST_FAILED, false);
        }
        return ApiResponse.of(ApiResponseCode.SUCCESS, true);
    }

    @PutMapping(value = "/update", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> updateUserInfo(@AuthenticationPrincipal UserDetails userDetails,
                                                      @Valid @ModelAttribute UserUpdateDto userUpdateDto) {
        log.info("------------------------------api user update------------------------------");
        log.info("userUpdateDto : {}", userUpdateDto);

        if(!userService.updateUser(userDetails.getUsername(), userUpdateDto)) {
            return ApiResponse.of(ApiResponseCode.REQUEST_FAILED, false);
        }
        return ApiResponse.of(ApiResponseCode.SUCCESS, true);
    }

    @PostMapping("/follow/{targetUserId}")
    public ResponseEntity<ApiResponse> followAdd(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String targetUserId) {
        log.info("------------------------------api follow add------------------------------");
        log.info("targetUserID : {}", targetUserId);
        boolean result = followService.addFollowRelation(userDetails.getUsername(), targetUserId);

        if(!result) {
            return ApiResponse.of(ApiResponseCode.UNFOLLOW_SUCCESS, true);
        }

        return ApiResponse.of(ApiResponseCode.FOLLOW_COMPLETED, true);
    }

    // 본인의 팔로잉, 팔로워 다른사람의 팔로잉 팔로워 조회하고 싶을때 조회하고 싶은사람의 String id를 넘겨주면된다
    @PostMapping("/follow-list/{targetUserId}")
    public ResponseEntity<ApiResponse> getFollowRelation(@AuthenticationPrincipal UserDetails userDetails,
                                                         @PathVariable String targetUserId,
                                                         @RequestBody FollowUserSearchCondition condition,
                                                         Pageable pageable,
                                                         @RequestParam String type) {
        log.info("------------------------------api follow-list ------------------------------");
        PageResponseDto<FollowListDto> followList = followService.getFollowList(targetUserId, condition, pageable, type);

        return ApiResponse.of(ApiResponseCode.SUCCESS, followList);
    }

    @PostMapping("/check-pw")
    public ResponseEntity<ApiResponse> checkPw(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody CheckUserPwDto checkUserPwDto) {
        log.info("------------------------------api check pw------------------------------");

        if (!userService.checkPw(userDetails.getUsername(), checkUserPwDto.getUserPw())) {
            return ApiResponse.of(ApiResponseCode.CHECK_USER_PW_FAIL, false);
        }
        return ApiResponse.of(ApiResponseCode.CHECK_USER_PW_SUCCESS, true);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("------------------------------api user delete------------------------------");
        if (!userService.deleteUser(userDetails.getUsername())) {
            return ApiResponse.of(ApiResponseCode.REQUEST_FAILED, false);
        }
        return ApiResponse.of(ApiResponseCode.SUCCESS, true);
    }
}
