package com.ssafy.pjtaserver.controller.user;

import com.ssafy.pjtaserver.dto.request.user.UserUpdateDto;
import com.ssafy.pjtaserver.service.user.UserService;
import com.ssafy.pjtaserver.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class ApiUserAuthController {

    private final UserService userService;

    @PutMapping("/user/update/user-info")
    public ResponseEntity<ApiResponse> updateUserInfo   (@RequestBody UserUpdateDto userUpdateDto, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("------------------------------api update user info------------------------------");
        log.info("updateUserInfo : {}", userUpdateDto);
        log.info("Username in userDetails: {}", userDetails.getUsername());
        return null;
    }

}
// eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzM4NCJ9.eyJ1c2VyTG9naW5JZCI6ImRvOTY5OEBuYXZlci5jb20iLCJzb2NpYWwiOnRydWUsIm5pY2tOYW1lIjoi7Lm07Lm07Jik7IaM7IWc7ZqM7JuQIiwidXNlclBob25lIjoiMDEwLXh4eHgteHh4eCIsInVzZXJQd2QiOiIkMmEkMTAkQnloMVJkRzkwMnU4V2ZxeUJGT2lHdUtrYlRkZGhOY1NUNEpod3hhaDQ2cTNnZlhZUWt3R20iLCJyb2xlTmFtZXMiOlsiVVNFUiJdLCJlbWFpbCI6ImRvOTY5OEBuYXZlci5jb20iLCJ1c2VybmFtZSI6Iuq5gOuPhO2YhCIsImlhdCI6MTc0NzIzNTc1MiwiZXhwIjoxNzQ3MjM3NTUyfQ.jme0ge7EUpVAQz9TyNiEme91KZcAJCtfH8stbER3YOQVJt7b0YYxtOKs4bjw2BBe
