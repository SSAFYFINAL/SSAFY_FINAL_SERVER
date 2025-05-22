package com.ssafy.pjtaserver.repository.guestbook;

import com.ssafy.pjtaserver.domain.guestBook.GuestBook;
import com.ssafy.pjtaserver.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestBookRepository extends JpaRepository<GuestBook, Long>, GuestBookQueryRepository {
    List<GuestBook> findByOwner(User owner);
}
