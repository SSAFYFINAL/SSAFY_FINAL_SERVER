package com.ssafy.pjtaserver.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.pjtaserver.domain.user.User;
import com.ssafy.pjtaserver.domain.user.UserRole;
import com.ssafy.pjtaserver.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
@Rollback(value = false)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testInsertMember() {
        for(int i = 0; i < 10; i++) {
            User normalUser = User.createNormalUser("test" + (i + 1), passwordEncoder.encode("1111"), "test" + (i + 1), "test" + (i + 1), "<EMAIL>" + (i + 1), "010-1234-5678");

            if(i >= 8) {
                normalUser.addRole(UserRole.ADMIN);
            }
            userRepository.save(normalUser);
        }

    }

    @Test
    public void testRead() {
        for(int i = 0; i < 10; i++) {
            User normalUser = User.createNormalUser("test" + (i + 1), passwordEncoder.encode("1111"), "test" + (i + 1), "test" + (i + 1), "<EMAIL>" + (i + 1), "010-1234-5678");

            if(i >= 8) {
                normalUser.addRole(UserRole.ADMIN);
            }
            userRepository.save(normalUser);
        }
        String userLoginId = "test3";
        User user = userRepository.getWithRoles(userLoginId);
        System.out.println("------------------------------");
        System.out.println("user = " + user);
        System.out.println(user.getUserRoleList());
    }

    @Test
    public void testInsertAndDelete() {
        User user = User.createNormalUser("test", passwordEncoder.encode("1111"), "test", "test", "dojin8351@gmail.com", "010-1234-5678");
        userRepository.save(user);

        String findEmail = "dojin8351@gmail.com";

        User findUser = userRepository.findByUserEmail(findEmail);
        System.out.println("findUser = " + findUser);

        userRepository.deleteByUserEmail(findEmail);
    }
}