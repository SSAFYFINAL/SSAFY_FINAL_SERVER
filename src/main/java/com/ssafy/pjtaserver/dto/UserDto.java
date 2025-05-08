package com.ssafy.pjtaserver.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;

import static java.util.stream.Collectors.*;

// 시큐리티에서 제공해주는 User를 상속받아 dto정의
public class UserDto extends User {

    private final String userLoginId;
    private String userPwd;
    private String username;
    private String nickName;
    private String userEmail;
    private String userPhone;

    private boolean social;

    private List<String> roleNames = new ArrayList<>();

    public UserDto(String username, String password, Collection<? extends GrantedAuthority> authorities, String userLoginId) {
        super(username, password, authorities);
        this.userLoginId = userLoginId;
    }

    public UserDto(String userLoginId, String userPwd, String username, String nickName, String userEmail, String userPhone, boolean social, List<String> roleNames) {
        super(userLoginId, userPwd, getCollect(roleNames));

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
    public UserDto(@JsonProperty("userLoginId") String userLoginId, @JsonProperty("password") String password) {
        super(userLoginId, password, new ArrayList<>());
        this.userLoginId = userLoginId;
        this.userPwd = password;
    }

    // JWT
    public Map<String, Object> getClaims() {
        HashMap<String, Object> dataMap = new HashMap<>();
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

}
