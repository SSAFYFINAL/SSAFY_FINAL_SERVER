package com.ssafy.pjtaserver.repository.book;

import com.ssafy.pjtaserver.domain.book.BookInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<BookInfo, Long>, BookQueryRepository{

}
