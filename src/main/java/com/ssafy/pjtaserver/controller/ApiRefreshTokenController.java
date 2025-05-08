package com.ssafy.pjtaserver.controller;

import com.ssafy.pjtaserver.exception.CustomJWTException;
import com.ssafy.pjtaserver.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ApiRefreshTokenController {

    @PostMapping("/api/auth/refresh-token")
    public Map<String, Object> refresh(
            @RequestHeader("Authorization") String authHeader,
            String refreshToken) {

        if(refreshToken == null) {
            throw new CustomJWTException("refreshToken 이 없습니다.");
        }

        if(authHeader == null || authHeader.length() < 7) {
            throw new CustomJWTException("INVALID STRING");
        }

        String accessToken = authHeader.substring(7);

        // AccessToken의 만료 여부 확인해주기
        if(!checkExpiredToken(accessToken)) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        // Refresh 토큰 검증하는 로지ㄱ
        Map<String, Object> claims = JWTUtil.validateToken(refreshToken);
        log.info("refresh claims: {}", claims);

        String newAccessToken = JWTUtil.generateToken(claims, 30);

        String newRefreshToken = checkTime((Integer) claims.get("exp")) ? JWTUtil.generateToken(claims, 60 * 24) : refreshToken;

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }

    private boolean checkTime(Integer exp) {
        Date expDate = new Date((long)exp * 1000);

        long gap = expDate.getTime() - System.currentTimeMillis();

        long leftMin = gap / (1000 * 60);

        return leftMin < 60;
    }

    private boolean checkExpiredToken(String token) {
        try {
            JWTUtil.validateToken(token);
        } catch (CustomJWTException ex) {
            if(ex.getMessage().equals("Expired")) {
                return true;
            }
        }
        return false;
    }
}
