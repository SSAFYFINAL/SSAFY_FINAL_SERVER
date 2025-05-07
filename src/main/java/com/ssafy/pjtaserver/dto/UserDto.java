package com.ssafy.pjtaserver.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;
import java.util.stream.Collectors;

// 시큐리티에서 제공해주는 User를 상속받아 dto정의
public class UserDto extends User {

    private String userLoginId, userPwd, username, nickName, userEmail, userPhone;

    private boolean social;

    private List<String> roleNames = new ArrayList<>();

    public UserDto(String username, String password, Collection<? extends GrantedAuthority> authorities, String userLoginId) {
        super(username, password, authorities);
        this.userLoginId = userLoginId;
    }


    public UserDto(String userLoginId, String userPwd, String username, String nickName, String userEmail, String userPhone, boolean social, List<String> roleNames) {
        super(
                userLoginId,
                userPwd,
                roleNames.stream()
                        .map(str -> new SimpleGrantedAuthority("ROLE_" + str))
                        .collect(Collectors.toList()));

        this.userLoginId = userLoginId;
        this.userPwd = userPwd;
        this.username = username;
        this.nickName = nickName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.social = social;
        this.roleNames = roleNames;
    }

    // 로그인 요청시 넘어오는 파라미터 매핑해주는 생성자
    @JsonCreator // Jackson이 이 생성자를 사용하도록 지시
    public UserDto(@JsonProperty("username") String username, @JsonProperty("password") String password) {
        super(username, password, new ArrayList<>());
        this.username = username; // 필드에 매핑
        this.userPwd = password;
    }


    // JWT
    public Map<String, Object> getClaims() {
        HashMap<String, Object> dataMap = new HashMap<>();

        dataMap.put("email", userEmail);
        dataMap.put("userPwd", userPwd);
        dataMap.put("userLoginId", userLoginId);
        dataMap.put("username", username);
        dataMap.put("nickName", nickName);
        dataMap.put("userPhone", userPhone);
        dataMap.put("social", social);
        dataMap.put("roleNames", roleNames);

        return dataMap;
    }

}
