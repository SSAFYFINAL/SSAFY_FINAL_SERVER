package com.ssafy.pjtaserver.controller.user;

import com.ssafy.pjtaserver.dto.request.user.UserFindIdDto;
import com.ssafy.pjtaserver.dto.request.user.UserLoginDto;
import com.ssafy.pjtaserver.dto.request.user.UserCheckedIdDto;
import com.ssafy.pjtaserver.dto.request.user.UserJoinDto;
import com.ssafy.pjtaserver.service.user.UserService;
import com.ssafy.pjtaserver.util.JWTUtil;
import com.ssafy.pjtaserver.enums.SocialLogin;
import com.ssafy.pjtaserver.util.ApiResponse;
import com.ssafy.pjtaserver.enums.ApiResponseCode;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/public/user")
public class ApiUserPublicController {

    private final UserService userService;

    @PostMapping("/login")
    public void login(@RequestBody UserLoginDto userLoginDto,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws IOException, ServletException {
        log.info("------------------------------api login------------------------------");
        log.info("login : {}", userLoginDto);
        userService.authenticateAndRespond(userLoginDto, request, response);
    }

    /**
     * 카카오 소셜 로그인
     * @param accessToken : 카카오 서버에서 넘겨주는 api 정보
     * @return : 응답코드와, 유저의 정보를 토큰에 담아서 반환해준다.
     */
    // 카카오 소셔
    @GetMapping("/login/kakao")
    public ResponseEntity<ApiResponse> getMemberFromKakao(String accessToken) throws MessagingException {
        log.info("------------------------------api kakao data------------------------------");
        log.info("getMemberFromKakao : {}", accessToken);
        log.info("------------------------------api kakao data------------------------------");

        // 서비스에서 받은 유저의 정보를 담아준다.
        UserLoginDto kakaoUser = userService.getSocialUser(accessToken, SocialLogin.KAKAO);

        // 토큰의 payload 에 정보를 담는다.
        Map<String, Object> claims = kakaoUser.getClaims();

        // 토근 생성
        String jwtAccessToken = JWTUtil.generateToken(claims, 30);
        String jetRefreshToken = JWTUtil.generateToken(claims, 60 * 24);
        claims.put("accessToken", jwtAccessToken);
        claims.put("refreshToken", jetRefreshToken);

        return ApiResponse.of(ApiResponseCode.USER_CREATED, claims);
    }

    @GetMapping("/login/google")
    public ResponseEntity<ApiResponse> getMemberFromGoogle(String accessToken) throws MessagingException {
        log.info("------------------------------api google data------------------------------");
        log.info("getMemberFromGoogle : {}", accessToken);
        log.info("------------------------------api google data------------------------------");
        UserLoginDto googleUser = userService.getSocialUser(accessToken, SocialLogin.GOOGLE);

        Map<String, Object> claims = googleUser.getClaims();
        // 토근 생성
        String jwtAccessToken = JWTUtil.generateToken(claims, 30);
        String jetRefreshToken = JWTUtil.generateToken(claims, 60 * 24);
        claims.put("accessToken", jwtAccessToken);
        claims.put("refreshToken", jetRefreshToken);
        return ApiResponse.of(ApiResponseCode.USER_CREATED, claims);
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse> joinUser(@Validated @RequestBody UserJoinDto userJoinDto) {
        log.info("------------------------------api user join------------------------------");
        log.info("joinUser : {}", userJoinDto);

        if(!userService.joinUser(userJoinDto)) {
            return ApiResponse.of(ApiResponseCode.USER_CREATED_ERROR);
        }

        return ApiResponse.of(ApiResponseCode.USER_CREATED, userJoinDto.getUserLoginId());
    }

    @GetMapping("/check-id")
    public ResponseEntity<ApiResponse> checkId(@Validated @RequestBody UserCheckedIdDto userCheckedIdDto) {
        if(userService.findByUserId(userCheckedIdDto.getUserLoginId())){
            return ApiResponse.of(ApiResponseCode.VALIDATION_ERROR);
        }
        return ApiResponse.of(ApiResponseCode.SUCCESS);
    }

    @GetMapping("/find-id")
    public ResponseEntity<ApiResponse> findUserId(@Validated @RequestBody UserFindIdDto userFindIdDto){
        String userLoginId = userService.findUserIdByUserEmailAndName(userFindIdDto);
        if(userLoginId == null) {
            return ApiResponse.of(ApiResponseCode.AUTHENTICATION_FAILED);
        }
        return ApiResponse.of(ApiResponseCode.SUCCESS, userLoginId);
    }
}
