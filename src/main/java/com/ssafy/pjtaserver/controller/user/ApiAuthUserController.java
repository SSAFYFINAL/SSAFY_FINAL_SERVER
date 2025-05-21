package com.ssafy.pjtaserver.controller.user;

import com.ssafy.pjtaserver.dto.request.user.UserResetPwDto;
import com.ssafy.pjtaserver.dto.request.user.UserUpdateDto;
import com.ssafy.pjtaserver.enums.ApiResponseCode;
import com.ssafy.pjtaserver.service.user.UserService;
import com.ssafy.pjtaserver.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/follow/{targetUserLoginId}")
    public ResponseEntity<ApiResponse> followAdd(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String targetUserLoginId) {
        log.info("------------------------------api follow add------------------------------");
        log.info("targetUserLoginId : {}", targetUserLoginId);

        boolean results = userService.followManager(userDetails.getUsername(), targetUserLoginId);
        if(!results) {
            return ApiResponse.of(ApiResponseCode.FAVORITE_CALCLE);
        }
        return ApiResponse.of(ApiResponseCode.UNFOLLOW_SUCCESS);
    }

}
