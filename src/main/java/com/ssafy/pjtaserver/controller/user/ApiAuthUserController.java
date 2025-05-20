package com.ssafy.pjtaserver.controller.user;

import com.ssafy.pjtaserver.dto.request.user.UserResetPwDto;
import com.ssafy.pjtaserver.enums.ApiResponseCode;
import com.ssafy.pjtaserver.service.user.UserService;
import com.ssafy.pjtaserver.util.ApiResponse;
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
}
