package com.ssafy.pjtaserver.controller;

import com.ssafy.pjtaserver.util.ApiResponse;
import com.ssafy.pjtaserver.enums.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
