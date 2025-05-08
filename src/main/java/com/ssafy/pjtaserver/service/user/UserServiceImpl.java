package com.ssafy.pjtaserver.service.user;

import com.ssafy.pjtaserver.dto.UserDto;
import com.ssafy.pjtaserver.security.handler.ApiLoginFailHandler;
import com.ssafy.pjtaserver.security.handler.ApiLoginSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;

    @Override
    public void authenticateAndRespond(UserDto userDto, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
            );
            // 인증 성공 시 핸들러 호출
            ApiLoginSuccessHandler successHandler = new ApiLoginSuccessHandler();
             successHandler.onAuthenticationSuccess(request, response, authentication);

        } catch (AuthenticationException e) {
            // 인증 실패 시 핸들러 호출
            ApiLoginFailHandler failHandler = new ApiLoginFailHandler();
            failHandler.onAuthenticationFailure(request, response, e);

        }
    }
}
