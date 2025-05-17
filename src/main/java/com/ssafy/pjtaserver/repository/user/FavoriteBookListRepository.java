package com.ssafy.pjtaserver.repository.user;

import com.ssafy.pjtaserver.domain.book.BookInfo;
import com.ssafy.pjtaserver.domain.user.FavoriteBookList;
import com.ssafy.pjtaserver.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteBookListRepository extends JpaRepository<FavoriteBookList, Long> {
    Optional<FavoriteBookList> findFavoriteBookListByUserAndBookInfo(User user, BookInfo bookInfo);
}
