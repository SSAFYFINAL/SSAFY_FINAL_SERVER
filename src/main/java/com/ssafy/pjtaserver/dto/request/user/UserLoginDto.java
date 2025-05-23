package com.ssafy.pjtaserver.dto.request.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;

import static java.util.stream.Collectors.*;

// 시큐리티에서 제공해주는 User를 상속받아 dto정의
@Getter
public class UserLoginDto extends User {

    private Long userId;
    private final String userLoginId;
    private String userPwd;
    private String username;
    private String nickName;
    private String userEmail;
    private String userPhone;

    private boolean social;

    private List<String> roleNames = new ArrayList<>();

    public UserLoginDto(String username, String password, Collection<? extends GrantedAuthority> authorities, String userLoginId) {
        super(username, password, authorities);
        this.userLoginId = userLoginId;
    }

    public UserLoginDto(Long userId, String userLoginId, String userPwd, String username, String nickName, String userEmail, String userPhone, boolean social, List<String> roleNames) {
        super(userLoginId, userPwd, getCollect(roleNames));
        this.userId = userId;
        this.userLoginId = userLoginId;
        this.userPwd = userPwd;
        this.username = username;
        this.nickName = nickName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.social = social;
        this.roleNames = roleNames;
    }

    private static List<SimpleGrantedAuthority> getCollect(List<String> roleNames) {
        return roleNames.stream()
                .map(str -> new SimpleGrantedAuthority("ROLE_" + str))
                .collect(toList());
    }

    @JsonCreator
    public UserLoginDto(
            @JsonProperty(value = "userLoginId", required = true) String userLoginId,
            @JsonProperty(value = "password", required = true) String password) {
        super(userLoginId, password, List.of());
        this.userLoginId = userLoginId;
        this.userPwd = password;
    }

    // JWT
    public Map<String, Object> getClaims() {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("userId", userId);
        dataMap.put("email", userEmail);
        dataMap.put("userLoginId", userLoginId);
        dataMap.put("userPwd", userPwd);
        dataMap.put("username", username);
        dataMap.put("nickName", nickName);
        dataMap.put("userPhone", userPhone);
        dataMap.put("social", social);
        dataMap.put("roleNames", roleNames);

        return dataMap;
    }

    public String getUsername() {
        return userLoginId;
    }

}
