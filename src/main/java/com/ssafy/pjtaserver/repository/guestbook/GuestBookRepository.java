package com.ssafy.pjtaserver.repository.guestbook;

import com.ssafy.pjtaserver.domain.guestBook.GuestBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestBookRepository extends JpaRepository<GuestBook, Long> {

}
