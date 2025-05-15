package com.ssafy.pjtaserver.service.user;

import com.ssafy.pjtaserver.domain.user.User;
import com.ssafy.pjtaserver.dto.request.user.UserLoginDto;
import com.ssafy.pjtaserver.dto.request.user.UserJoinDto;
import com.ssafy.pjtaserver.dto.request.user.UserResetPwDto;
import com.ssafy.pjtaserver.enums.SocialLogin;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.stream.Collectors;

public interface UserService {
    // 로그인 회원 인증 서비스
    void authenticateAndRespond(UserLoginDto userLoginDto, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

    // accessToken 을 전달받아 유저의 dto을 반환
    UserLoginDto getSocialUser(String accessToken, SocialLogin socialLogin) throws MessagingException;

    // 유저 회원가입
    boolean joinUser(UserJoinDto userJoinDto);

    boolean findByUserId(String userLoginId);

    boolean resetUserPwd(String userLoginId, UserResetPwDto userResetPwDto);

    // user 엔티티를 userDto로 변환시켜주는 로직
    default UserLoginDto entityToDto(User user) {
        return new UserLoginDto(
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
