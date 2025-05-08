package com.ssafy.pjtaserver.controller;

import com.ssafy.pjtaserver.dto.UserDto;
import com.ssafy.pjtaserver.security.handler.ApiLoginFailHandler;
import com.ssafy.pjtaserver.security.handler.ApiLoginSuccessHandler;
import com.ssafy.pjtaserver.util.apiResponseUtil.ApiResponse;
import com.ssafy.pjtaserver.util.apiResponseUtil.ApiResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiTestController {

    @GetMapping("/api/public/test")
    public ResponseEntity<ApiResponse> test1() {
        log.info("------------------------------api test------------------------------");
        log.info("들어왔니");
        return ApiResponse.of(ApiResponseCode.USER_CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/api/auth/test1")
    public ResponseEntity<ApiResponse> test2() {
        return ApiResponse.of(ApiResponseCode.USER_CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/api/auth/test2")
    public ResponseEntity<ApiResponse> test3() {
        return ApiResponse.of(ApiResponseCode.USER_CREATED);
    }

    @GetMapping("/api/auth/test3")
    public ResponseEntity<ApiResponse> test4() {
        return ApiResponse.of(ApiResponseCode.USER_CREATED);
    }

}
