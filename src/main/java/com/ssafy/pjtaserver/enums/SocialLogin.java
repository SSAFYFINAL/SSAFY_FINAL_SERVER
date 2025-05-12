package com.ssafy.pjtaserver.enums;

import com.ssafy.pjtaserver.dto.GoogleResponseDto;
import com.ssafy.pjtaserver.dto.KakaoResponseDto;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Getter
public enum SocialLogin {
    KAKAO(
            "카카오",
            "https://kapi.kakao.com/v2/user/me",
            KakaoResponseDto.class,
            response -> {
                KakaoResponseDto kakaoResponse = (KakaoResponseDto) response;
                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("email", kakaoResponse.getKakaoAccount().getEmail());
                userInfo.put("username", kakaoResponse.getProperties().getNickname());
                return userInfo;
            }
    ),

    GOOGLE(
            "구글",
            "https://www.googleapis.com/oauth2/v2/userinfo",
            GoogleResponseDto.class,
            response -> {
                GoogleResponseDto googleResponse = (GoogleResponseDto) response;
                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("email", googleResponse.getEmail());
                userInfo.put("username", googleResponse.getName());
                return userInfo;
            }
    );

    private final String name;
    private final String url;
    private final Class<?> responseType;
    private final Function<Object, Map<String, String>> responseParser;

    SocialLogin(String name, String url, Class<?> responseType, Function<Object, Map<String, String>> responseParser) {
        this.name = name;
        this.url = url;
        this.responseType = responseType;
        this.responseParser = responseParser;
    }

    public Map<String, String> parseResponse(Object response) {
        return responseParser.apply(response);
    }
}