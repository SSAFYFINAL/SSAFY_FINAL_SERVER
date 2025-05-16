package com.ssafy.pjtaserver.repository.user;

import com.ssafy.pjtaserver.domain.user.FavoriteBookList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteBookListRepository extends JpaRepository<FavoriteBookList, Long> {

}
