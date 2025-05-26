package com.ssafy.pjtaserver.repository.user;

import com.ssafy.pjtaserver.domain.user.User;
import com.ssafy.pjtaserver.repository.user.query.UserQueryRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserQueryRepository {
    Optional<User> findByUserEmail(String userEmail);
    Optional<User> findByUserLoginId(String userLoginId);
    Optional<User> findByUserEmailAndUsernameMain(String userEmail, String usernameMain);
    Optional<User> findUserById(Long id);
    List<User> findAllByUserLoginIdIn(List<String> userLoginIds);

    @EntityGraph(attributePaths = {"favoriteBookList.bookInfo.categoryId"}) // 수정
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.favoriteBookList fb JOIN FETCH fb.bookInfo bi JOIN FETCH bi.categoryId")
    List<User> findAllUsersWithFavorites();
}
