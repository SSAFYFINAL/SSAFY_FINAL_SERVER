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
public class ApiUserAuthController {

    private final UserService userService;

    @PutMapping("/password-reset")
    public ResponseEntity<ApiResponse> updatePassword(@AuthenticationPrincipal UserDetails userDetails, @Validated @RequestBody UserResetPwDto userResetPwDto) {
        log.info("------------------------------api user password reset------------------------------");
        log.info("userResetPwDto : {}", userResetPwDto);
        log.info("userDetails : {}", userDetails.getUsername());
        String userLoginId = userDetails.getUsername();

        boolean isReset = userService.resetUserPwd(userLoginId, userResetPwDto);

        if(!isReset) {
            return ApiResponse.of(ApiResponseCode.INVALID_REQUEST);
        }
        return ApiResponse.of(ApiResponseCode.SUCCESS);
    }
}
// eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzM4NCJ9.eyJ1c2VyTG9naW5JZCI6ImRvOTY5OEBuYXZlci5jb20iLCJzb2NpYWwiOnRydWUsIm5pY2tOYW1lIjoi7Lm07Lm07Jik7IaM7IWc7ZqM7JuQIiwidXNlclBob25lIjoiMDEwLXh4eHgteHh4eCIsInVzZXJQd2QiOiIkMmEkMTAkQnloMVJkRzkwMnU4V2ZxeUJGT2lHdUtrYlRkZGhOY1NUNEpod3hhaDQ2cTNnZlhZUWt3R20iLCJyb2xlTmFtZXMiOlsiVVNFUiJdLCJlbWFpbCI6ImRvOTY5OEBuYXZlci5jb20iLCJ1c2VybmFtZSI6Iuq5gOuPhO2YhCIsImlhdCI6MTc0NzIzNTc1MiwiZXhwIjoxNzQ3MjM3NTUyfQ.jme0ge7EUpVAQz9TyNiEme91KZcAJCtfH8stbER3YOQVJt7b0YYxtOKs4bjw2BBe
