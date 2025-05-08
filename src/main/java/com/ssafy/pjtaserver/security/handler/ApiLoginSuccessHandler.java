package com.ssafy.pjtaserver.security.handler;

import com.google.gson.Gson;
import com.ssafy.pjtaserver.dto.UserDto;
import com.ssafy.pjtaserver.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class ApiLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("------------------------------api 로그인 성공------------------------------");
        log.info(authentication.toString());
        log.info("------------------------------api 로그인 성공------------------------------");

        UserDto userDto = (UserDto) authentication.getPrincipal();

        // 유저의 정보를 담고있다.
        Map<String, Object> claims = userDto.getClaims();

        String accessToken = JWTUtil.generateToken(claims, 30);
        String refreshToken = JWTUtil.generateToken(claims, 60 * 24);

        // 엑세스 토큰
        claims.put("accessToken",accessToken);

        // 리프레쉬 토큰
        claims.put("refreshToken",refreshToken);

        Gson gson = new Gson();

        // json 으로 파싱
        String jsonStr = gson.toJson(claims);

        // 반환타입 json으로 설정
        response.setContentType("application/json; charset=UTF-8");

        // 출력
        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonStr);
        printWriter.close();

    }
}
