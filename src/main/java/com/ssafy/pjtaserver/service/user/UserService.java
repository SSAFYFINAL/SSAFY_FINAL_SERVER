package com.ssafy.pjtaserver.service.user;

import com.ssafy.pjtaserver.domain.user.User;
import com.ssafy.pjtaserver.dto.UserDto;
import com.ssafy.pjtaserver.enums.SocialLogin;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.stream.Collectors;

public interface UserService {
    // 로그인 회원 인증 서비스
    void authenticateAndRespond(UserDto userDto, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    // accessToken 을 전달받아 유저의 dto을 반환
    UserDto getSocialUser(String accessToken, SocialLogin socialLogin);

    // user 엔티티를 userDto로 변환시켜주는 로직
    default UserDto entityToDto(User user) {
        return new UserDto(
                user.getUserLoginId(),
                user.getUserPwd(),
                user.getUsernameMain(),
                user.getNickName(),
                user.getUserEmail(),
                user.getUserPhone(),
                user.isSocial(),
                user.getUserRoleList().stream()
                        .map(Enum::name).collect(Collectors.toList()));
    }
}
