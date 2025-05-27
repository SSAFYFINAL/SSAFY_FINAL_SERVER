package com.ssafy.pjtaserver.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ssafy.pjtaserver.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(UserListener.class)
@Table(name = "user")
@ToString(exclude = "userRoleList")
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_login_id", nullable = false, unique = true)
    private String userLoginId;

    @Column(name = "user_pwd", nullable = false)
    private String userPwd;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "user_role", nullable = false)
    @JsonIgnore
    private List<UserRole> userRoleList = new ArrayList<>();

    @Column(name = "username-main", nullable = false)
    private String usernameMain;

    @Column(name = "user_nick_name", nullable = false)
    private String nickName;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "user_phone", nullable = false)
    private String userPhone;

    @Column(name = "social")
    private boolean social;

    @Column(name = "profile_img_path")
    private String profileImgPath;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteBookList> favoriteBookList = new ArrayList<>();

    /**
     * 일반 유저 권한의 새로운 User 인스턴스를 생성합니다.
     *
     * @param userLoginId 일반 유저 로그인 ID.
     * @param userPwd 일반 유저 비밀번호(암호화 완료된).
     * @param username 일반 유저 이름 (풀 네임).
     * @param nickName 일반 유저 닉네임.
     * @param userEmail 일반 유저 이메일 주소.
     * @param userPhone 일반 유저 전화번호.
     * @return 일반 유저 역할이 설정된 User 객체.
     */
    public static User createNormalUser(String userLoginId, String userPwd, String username, String nickName, String userEmail, String userPhone) {
        User user = new User();
        user.userLoginId = userLoginId;
        user.userPwd = userPwd;
        user.usernameMain = username;
        user.nickName = nickName;
        user.userEmail = userEmail;
        user.userPhone = userPhone;
        user.userRoleList.add(UserRole.USER);
        user.isDeleted = false;
        return user;
    }

    /**
     * 관리자 권한이 있는 새로운 User 인스턴스를 생성합니다.
     *
     * @param userLoginId 관리자 로그인 ID.
     * @param userPwd 관리자 비밀번호(암호화 완료된).
     * @param username 관리자의 이름 (풀 네임).
     * @param nickName 관리자의 닉네임.
     * @param userEmail 관리자의 이메일 주소.
     * @param userPhone 관리자의 전화번호.
     * @return 관리자 역할이 설정된 User 객체.
     */
    public static User createAdminUser(String userLoginId, String userPwd, String username, String nickName, String userEmail, String userPhone) {
        User admin = new User();
        admin.userLoginId = userLoginId;
        admin.userPwd = userPwd;
        admin.usernameMain = username;
        admin.nickName = nickName;
        admin.userEmail = userEmail;
        admin.userPhone = userPhone;
        admin.userRoleList.add(UserRole.ADMIN);
        admin.isDeleted = false;
        return admin;
    }

    @Builder
    public User(String userLoginId, String userPwd, String usernameMain, String nickName, String userEmail, String userPhone, boolean social) {
        this.userLoginId = userLoginId;
        this.userPwd = userPwd;
        this.usernameMain = usernameMain;
        this.nickName = nickName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.social = social;
    }

    public void updateUserInfo(String profileImgPath, String usernameMain, String nickName, String userPhone, String userPwd) {
        this.profileImgPath = profileImgPath;
        this.usernameMain = usernameMain;
        this.nickName = nickName;
        this.userPhone = userPhone;
        this.userPwd = userPwd;
    }

    public void updateUserInfo(String profileImgPath, String usernameMain, String nickName, String userPhone) {
        this.profileImgPath = profileImgPath;
        this.usernameMain = usernameMain;
        this.nickName = nickName;
        this.userPhone = userPhone;
    }

    public User deleteUser() {
        this.isDeleted = true; // `this`를 직접 사용하여 내부 상태 변경
        return this;
    }


    public void addRole(UserRole role) {
        this.userRoleList.add(role);
    }

    public void clearRole() {
        this.userRoleList.clear();
    }

    public void changeNickname(String nickname) {
        this.nickName = nickname;
    }

    public void changePwd(String pwd) {
        this.userPwd = pwd;
    }

    public void changeSocial(boolean social) {
        this.social = social;
    }

    public void addFavoriteBook(FavoriteBookList favoriteBookList) {
        this.favoriteBookList.add(favoriteBookList);

        if (favoriteBookList.getUser() != this) {
            favoriteBookList.setUser(this);
        }
    }
}
