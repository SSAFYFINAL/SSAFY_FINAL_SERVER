package com.ssafy.pjtaserver.service.user;

import com.ssafy.pjtaserver.domain.user.User;
import com.ssafy.pjtaserver.dto.request.mail.MailSendDto;
import com.ssafy.pjtaserver.dto.request.user.*;
import com.ssafy.pjtaserver.enums.EmailType;
import com.ssafy.pjtaserver.enums.UserRole;
import com.ssafy.pjtaserver.exception.JoinValidationException;
import com.ssafy.pjtaserver.repository.user.user.UserRepository;
import com.ssafy.pjtaserver.security.handler.ApiLoginFailHandler;
import com.ssafy.pjtaserver.security.handler.ApiLoginSuccessHandler;
import com.ssafy.pjtaserver.enums.SocialLogin;
import com.ssafy.pjtaserver.service.mail.MailService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final String DEFAULT_PHONE_NUMBER = "010-xxxx-xxxx"; // 소셜 로그인 사용자를 위한 기본 휴대폰 번호
    private static final String DEFAULT_SOCIAL_NICKNAME = "소셜회원"; // 소셜 로그인 사용자를 위한 기본 닉네임
    private static final int PASSWORD_LENGTH = 10; // 임의 비밀번호 길이

    private final MailService mailService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    /**
     * 로그인 성공/실패 핸들러를 통해 결과를 응답
     *
     * @param userLoginDto 유저 인증 정보 (username, password)
     * @param request HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @throws IOException  핸들러 처리 중 발생 가능한 예외
     * @throws ServletException 핸들러 처리 중 발생 가능한 예외
     */
    @Override
    public void authenticateAndRespond(UserLoginDto userLoginDto, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            // 사용자 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDto.getUserLoginId(), userLoginDto.getPassword())
            );
            // 인증 성공 시 성공 핸들러 처리
            getSuccessHandler().onAuthenticationSuccess(request, response, authentication);
        } catch (AuthenticationException e) {
            // 인증 실패 시 실패 핸들러 처리
            getFailHandler().onAuthenticationFailure(request, response, e);
        }
    }

    /**
     * 소셜 통해 사용자 정보를 가져온다
     * 이미 가입된 이메일의 경우 사용자의 정보를 반환하며, 가입된 정보가 없으면 새로 등록 후 반환
     *
     * @param accessToken 소셜 로그인 플랫폼에서 발급받은 액세스 토큰
     * @param socialLogin 소셜 로그인 플랫폼 정보 (KAKAO, GOOGLE)
     * @return UserDto 사용자 정보 DTO
     */
    @Transactional
    @Override
    public UserLoginDto getSocialUser(String accessToken, SocialLogin socialLogin) throws MessagingException {
        // 소셜 플랫폼에서 사용자 정보 가져오기
        Map<String, String> socialUserInfo = getUserInfoFromSocial(accessToken, socialLogin);
        String email = socialUserInfo.get("email");

        // DB에서 해당 이메일로 이미 등록된 사용자 검색
        Optional<User> user = userRepository.findByUserEmail(email);
        if (user.isPresent()) {
            // 기존 사용자 DTO 반환
            return entityToDto(user.get());
        }
        String rawPassword = makePassword();

        // 사용자 정보가 없으면 새 사용자 생성 및 저장
        User socialUser = makeSocialUser(socialUserInfo, socialLogin.getName(), rawPassword);
        userRepository.save(socialUser);

        mailService.sendEmail(new MailSendDto(
                email,
                rawPassword,
                EmailType.TEMP_PASSWORD
        ));
        return entityToDto(socialUser);
    }

    @Transactional
    @Override
    public boolean joinUser(UserJoinDto userJoinDto) {
        if(!userJoinDto.getIsEmailChecked()) {
            throw new JoinValidationException("이메일 인증 누락");
        }
        if(!userJoinDto.getIsCheckedPw()) {
            throw new JoinValidationException("비밀번호, 비밀번호 확인 불일치");
        }
        if(!userJoinDto.getIsDuplicatedUserLoginId()) {
            throw new JoinValidationException("아이디 중복 확인 실패, 누락");
        }

        User joinUser = User.createNormalUser(
                userJoinDto.getUserLoginId(),
                passwordEncoder.encode(userJoinDto.getUserPwd()),
                userJoinDto.getUsernameMain(),
                userJoinDto.getUserNickName(),
                userJoinDto.getUserEmail(),
                userJoinDto.getUserPhone()
        );
        userRepository.save(joinUser);

        return true;
    }

    @Override
    public boolean findByUserId(String userLoginId) {
        Optional<User> user = userRepository.findByUserLoginId(userLoginId);
        return user.isPresent();
    }

    @Transactional
    @Override
    public boolean resetUserPwd(String userLoginId, UserResetPwDto userResetPwDto) {
        return userRepository.findByUserLoginId(userLoginId)
                .map(user -> {
                    String encodedPwd = passwordEncoder.encode(userResetPwDto.getResetPwd());
                    user.changePwd(encodedPwd);
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public String findUserIdByUserEmailAndName(UserFindIdDto userFindIdDto) {

        return userRepository.findByUserEmailAndUsernameMain(userFindIdDto.getEmail(), userFindIdDto.getUsernameMain())
                .map(User::getUserLoginId)
                .orElseThrow(() -> new IllegalStateException("해당 " + userFindIdDto.getEmail() + "과 " + userFindIdDto.getUsernameMain() + "을 가진 유저가 존재하지 않습니다."));
    }

    @Transactional
    @Override
    public boolean updateUser(String userLoginId, UserUpdateDto userUpdateDto) {

        User user = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디의 유저를 찾을 수 없습니다.: " + userLoginId));

        String storedFilePath = null;
        MultipartFile profileImg = userUpdateDto.getProfileImg();
        if (profileImg != null && !profileImg.isEmpty()) {
            try {
                storedFilePath = saveProfileImage(profileImg); // 파일 저장
            } catch (Exception e) {
                throw new RuntimeException("파일 저장 중 오류 발생: " + e.getMessage());
            }
        }

        user.updateUserInfo(
                storedFilePath,
                userUpdateDto.getUsernameMain(),
                userUpdateDto.getUserNickname(),
                userUpdateDto.getUserPhone(),
                userUpdateDto.getUserPwd()
        );

        userRepository.save(user);

        return true;
    }

    // 파일 이미지 저장
    private String saveProfileImage(MultipartFile profileImg) {
        try {
            // 파일 이름 생성
            String originalFilename = profileImg.getOriginalFilename();
            String storedFilename = UUID.randomUUID() + "_" + originalFilename;

            // 저장 경로 생성
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일 저장
            Path filePath = uploadPath.resolve(storedFilename);
            Files.copy(profileImg.getInputStream(), filePath);
            return filePath.toString();
        } catch (Exception e) {
            throw new RuntimeException("파일 저장 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 소셜 로그인 사용자 정보를 요청 및 파싱
     *
     * @param accessToken 소셜 로그인 플랫폼에서 발급받은 액세스 토큰
     * @param socialLogin 소셜 로그인 플랫폼 정보
     * @return 사용자 정보 (이메일, 닉네임)
     */
    private Map<String, String> getUserInfoFromSocial(String accessToken, SocialLogin socialLogin) {
        validateAccessToken(accessToken); // 액세스 토큰 유효성 검사

        // 요청 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        // 소셜 로그인 플랫폼 API 호출
        ResponseEntity<?> response = restTemplate.exchange(
                socialLogin.getUrl(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                socialLogin.getResponseType()
        );
        log.info("{} API 응답 정보: {}", socialLogin.getName(), response);

        Object responseBody = response.getBody();
        if (responseBody == null) {
            throw new IllegalStateException(socialLogin.getName() + " API로부터 유효한 응답을 받지 못했습니다.");
        }

        // 응답 본문을 파싱하여 사용자 정보 추출
        return socialLogin.parseResponse(responseBody);
    }

    /**
     * 소셜 사용자 정보를 기반으로 새로운 사용자 엔티티를 생성
     *
     * @param userInfo 소셜 로그인 플랫폼으로부터 받은 사용자 정보
     * @param socialType 소셜 로그인 플랫폼 타입 (KAKAO, GOOGLE)
     * @return 새로 생성된 사용자 엔티티
     */
    private User makeSocialUser(Map<String, String> userInfo, String socialType, String rawPassword) {
        // 사용자 엔티티 생성
        User user = User.builder()
                .userLoginId(userInfo.get("email"))
                .userEmail(userInfo.get("email"))
                .userPwd(passwordEncoder.encode(rawPassword))
                .usernameMain(userInfo.get("username"))
                .nickName(socialType + DEFAULT_SOCIAL_NICKNAME)
                .userPhone(DEFAULT_PHONE_NUMBER)
                .social(true)
                .build();

        user.addRole(UserRole.USER);

        return user;
    }

    /**
     * 랜덤 대문자로 이루어진 임의의 비밀번호를 생성
     *
     * @return 생성된 임의 비밀번호
     */
    private String makePassword() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            buffer.append((char) ((int) (Math.random() * 26) + 65));
        }
        return buffer.toString();
    }

    /**
     * 액세스 토큰의 유효성을 검사
     *
     * @param accessToken 유효성 검사 대상 액세스 토큰
     */
    private void validateAccessToken(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("액세스 토큰이 비어있습니다.");
        }
    }

    /**
     * 로그인 성공 핸들러 생성
     *
     * @return ApiLoginSuccessHandler 인스턴스
     */
    private ApiLoginSuccessHandler getSuccessHandler() {
        return new ApiLoginSuccessHandler();
    }

    /**
     * 로그인 실패 핸들러 생성
     *
     * @return ApiLoginFailHandler 인스턴스
     */
    private ApiLoginFailHandler getFailHandler() {
        return new ApiLoginFailHandler();
    }
}