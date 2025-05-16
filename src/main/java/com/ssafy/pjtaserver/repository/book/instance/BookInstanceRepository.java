package com.ssafy.pjtaserver.repository.book.instance;

import com.ssafy.pjtaserver.domain.book.BookInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookInstanceRepository extends JpaRepository<BookInstance, Long>, BookInstanceQueryRepository {

}
