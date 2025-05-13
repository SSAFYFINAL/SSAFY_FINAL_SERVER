package com.ssafy.pjtaserver.domain.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static com.ssafy.pjtaserver.domain.user.QUser.user;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class UserTest {

    @Autowired
    EntityManager em;

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    @Test
    public void test1() {
        User normalUser = User.createNormalUser("dojin", "1234", "dojin", "dojin", "<EMAIL>", "010-1234-5678");
        em.persist(normalUser);

        User adminUser = User.createAdminUser("admin", "1234", "dojin", "admin", "<EMAIL>1", "010-1234-5678");
        em.persist(adminUser);

        System.out.println("normalUser = " + normalUser);
        System.out.println("adminUser = " + adminUser);

        assertThat(normalUser.getUsernameMain()).isEqualTo(adminUser.getUsernameMain());

    }

    @Test
    public void queryFactoryTest() {

        User normalUser = User.createNormalUser("dojin", "1234", "dojin", "dojin", "<EMAIL>", "010-1234-5678");
        em.persist(normalUser);

        User findUser = jpaQueryFactory
                .selectFrom(user)
                .where(user.usernameMain.eq("dojin"))
                .fetchOne();
        assertThat(findUser.getUsernameMain()).isEqualTo("dojin");
    }

}