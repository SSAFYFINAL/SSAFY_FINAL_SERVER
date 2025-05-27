package com.ssafy.pjtaserver.repository.book;

import com.ssafy.pjtaserver.domain.book.BookInfo;
import com.ssafy.pjtaserver.domain.book.BookInstance;
import com.ssafy.pjtaserver.domain.user.User;
import com.ssafy.pjtaserver.enums.BookCheckoutStatus;
import com.ssafy.pjtaserver.repository.book.query.BookInstanceQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookInstanceRepository extends JpaRepository<BookInstance, Long>, BookInstanceQueryRepository {
    List<BookInstance> findByStatusAndBookInfo_IdOrderByIdAsc(BookCheckoutStatus status, Long bookInfoId);
    boolean existsBookInstanceByBookInfoAndCurrentUserId(BookInfo bookInfo, User currentUserId);
    List<BookInstance> findBookInstanceByCurrentUserId(User user);
}
