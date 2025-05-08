package com.ssafy.pjtaserver.service.user;

import com.ssafy.pjtaserver.dto.UserDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface UserService {
    // 로그인 회원 인증 서비스
    void authenticateAndRespond(UserDto userDto, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}
