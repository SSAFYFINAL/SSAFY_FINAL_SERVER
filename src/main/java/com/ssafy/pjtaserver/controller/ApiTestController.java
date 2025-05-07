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

    private final AuthenticationManager authenticationManager;


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

    @PostMapping("/api/public/login")
    public ResponseEntity<?> login(
            @RequestBody UserDto userDto,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {
        try {
            // 사용자 인증 진행
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
            );

            // 인증 성공 시 AuthenticationSuccessHandler 호출
            ApiLoginSuccessHandler successHandler = new ApiLoginSuccessHandler();
            successHandler.onAuthenticationSuccess(request, response, authentication); // 강제 호출

            return ResponseEntity.ok().build(); // SuccessHandler에서 이미 응답을 처리하므로 빈 응답 반환
        } catch (AuthenticationException e) {
            // 인증 실패 시 AuthenticationFailureHandler 호출
            ApiLoginFailHandler failureHandler = new ApiLoginFailHandler();
            failureHandler.onAuthenticationFailure(request, response, e); // 강제 호출

            return ResponseEntity.ok().build(); // FailureHandler에서 응답 처리하므로 빈 응답 반환
        }
    }


}
