package com.ssafy.pjtaserver.security;

import com.ssafy.pjtaserver.domain.user.User;
import com.ssafy.pjtaserver.dto.UserDto;
import com.ssafy.pjtaserver.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
// 유저 검증 해주는곳 아이디 비밀번호 실질적으로 로그인 이루어지는곳
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 주어진 사용자 이름(username)을 기준으로 데이터베이스에서 사용자 정보를 가져온다.
     * 비밀번호(password)을 기준으로 가져온 비밀번호와 데이터베이스 저장된 비밀번호가 일치하는지 확인해준다.
     *
     * @param username 프론트엔드에서 전달받은 사용자 ID (로그인 ID)
     * @return UserDetails 사용자 인증 및 권한 정보를 포함한 객체
     * @throws UsernameNotFoundException 사용자 정보가 없을 경우 발생
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("------------------------------user detail service------------------------------");
        log.info("username : {}", username);

        User user = userRepository.getWithRoles(username);


        if(user == null) {
            throw new UsernameNotFoundException("유저 찾을수 없음");
        }
        log.info("database encoded password: {}", user.getUserPwd()); // 암호화된 비밀번호 출력

        UserDto userDto = new UserDto(
                user.getUserLoginId(),
                user.getUserPwd(),
                user.getUsername(),
                user.getNickName(),
                user.getUserPhone(),
                user.getProfileImgPath(),
                user.isSocial(),
                user.getUserRoleList()
                        .stream()
                        .map(Enum::name).collect(Collectors.toList()));

        log.info("userDto : {}", userDto);

        return userDto;
    }
}
