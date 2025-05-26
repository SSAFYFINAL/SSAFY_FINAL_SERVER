package com.ssafy.pjtaserver.repository.book;

import com.ssafy.pjtaserver.domain.book.BookInfo;
import com.ssafy.pjtaserver.domain.book.BookReservation;
import com.ssafy.pjtaserver.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookReservationRepository extends JpaRepository<BookReservation, Long> {
    boolean existsBookReservationsByBookInfoAndUserId(BookInfo bookInfo, User userId);
    List<BookReservation> findByUserId(User userId);
}

