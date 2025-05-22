package com.ssafy.pjtaserver.repository.user.favorite;

import com.ssafy.pjtaserver.domain.book.BookInfo;
import com.ssafy.pjtaserver.domain.user.FavoriteBookList;
import com.ssafy.pjtaserver.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteBookList, Long>, FavoriteQueryRepository {
    Optional<FavoriteBookList> findFavoriteBookListByUserAndBookInfo(User user, BookInfo bookInfo);
    boolean existsFavoriteBookListByUserAndBookInfo(User user, BookInfo bookInfo);

    List<FavoriteBookList> findByUser(User user);
}
