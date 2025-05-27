package com.ssafy.pjtaserver.security.handler;

import com.google.gson.Gson;
import com.ssafy.pjtaserver.utils.ApiResponseUtil;
import com.ssafy.pjtaserver.enums.ApiResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class ApiLoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("------------------------------api 로그인 실패------------------------------");
        log.info("로그인 실패 : " + exception);

        Gson gson = new Gson();
        String jsonStr = gson.toJson(ApiResponseUtil.of(ApiResponseCode.AUTHENTICATION_FAILED));
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonStr);
        printWriter.close();
    }
}
