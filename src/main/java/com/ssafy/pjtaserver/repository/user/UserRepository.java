package com.ssafy.pjtaserver.repository.user;

import com.ssafy.pjtaserver.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserQueryRepository{
    User findByUserEmail(String userEmail);
    void deleteByUserEmail(String userEmail);

}
