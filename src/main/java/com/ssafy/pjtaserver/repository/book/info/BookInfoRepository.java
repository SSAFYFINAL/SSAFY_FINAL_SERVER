package com.ssafy.pjtaserver.repository.book.info;

import com.ssafy.pjtaserver.domain.book.BookInfo;
import com.ssafy.pjtaserver.dto.response.book.BookInfoDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookInfoRepository extends JpaRepository<BookInfo, Long>, BookInfoQueryRepository {
    Optional<BookInfo> findBookInfoById(Long bookInfoId);
}
