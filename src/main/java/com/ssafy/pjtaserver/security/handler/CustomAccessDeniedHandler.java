package com.ssafy.pjtaserver.security.handler;

import com.google.gson.Gson;
import com.ssafy.pjtaserver.util.ApiResponse;
import com.ssafy.pjtaserver.enums.ApiResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Gson gson = new Gson();

        log.info("------------------------------access denied------------------------------");
        String jsonStr = gson.toJson(ApiResponse.of(ApiResponseCode.DENIED_ACCESS_TOKEN));
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonStr);
        printWriter.close();
    }
}
