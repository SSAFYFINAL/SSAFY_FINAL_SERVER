package com.ssafy.pjtaserver.service.book;

import com.ssafy.pjtaserver.domain.book.BookInfo;
import com.ssafy.pjtaserver.domain.user.FavoriteBookList;
import com.ssafy.pjtaserver.domain.user.User;
import com.ssafy.pjtaserver.dto.response.book.BookDetailDto;
import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchDto;
import com.ssafy.pjtaserver.dto.response.book.PageResponseDto;
import com.ssafy.pjtaserver.exception.DuplicateFavoriteBookException;
import com.ssafy.pjtaserver.repository.book.info.BookInfoRepository;
import com.ssafy.pjtaserver.repository.book.instance.BookInstanceRepository;
import com.ssafy.pjtaserver.repository.user.FavoriteBookListRepository;
import com.ssafy.pjtaserver.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookInfoRepository bookInfoRepository;
    private final BookInstanceRepository bookInstanceRepository;
    private final UserRepository userRepository;
    private final FavoriteBookListRepository favoriteBookListRepository;

    @Override
    public PageResponseDto<BookInfoSearchDto> searchPageComplex(BookInfoSearchCondition condition, Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.fromString(condition.getOrderDirection()), condition.getOrderBy());
        Pageable updatedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        // page객체를 pageResponseDto로 변환해서 반환해준다 은그러면 warn로그 찍힘
        Page<BookInfoSearchDto> bookInfoSearchDto = bookInfoRepository.searchPageComplex(condition, updatedPageable);
        return new PageResponseDto<>(
                bookInfoSearchDto.getContent(),
                bookInfoSearchDto.getTotalElements(),
                bookInfoSearchDto.getTotalPages(),
                bookInfoSearchDto.getNumber(),
                bookInfoSearchDto.getSize()
        );
    }

    @Override
    public BookDetailDto getDetail(Long bookInfoId) {
        boolean isAvailableForCheckout = isBookAvailableForCheckout(bookInfoId);

        return bookInfoRepository.findBookInfoById(bookInfoId)
                .map(bookInfo -> BookDetailDto.builder()
                        .bookInfoId(bookInfo.getId())
                        .title(bookInfo.getTitle())
                        .authorName(bookInfo.getAuthorName())
                        .isbn(bookInfo.getIsbn())
                        .registryDate(bookInfo.getRegistryDate())
                        .publisherName(bookInfo.getPublisherName())
                        .isAvailableCheckedOut(isAvailableForCheckout)
                        .bookImgPath(bookInfo.getBookImgPath())
                        .seriesName(bookInfo.getSeriesName())
                        .description(bookInfo.getDescription())
                        .categoryName(bookInfo.getCategoryId())
                        .build()
                )
                .orElseThrow(() -> new EntityNotFoundException("해당 책이 존재하지 않습니다.: " + bookInfoId));
    }

    @Transactional
    @Override
    public boolean addFavoriteBook(String userLoginId, Long bookInfoId) {
        User user = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디의 유저를 찾을 수 없습니다.: " + userLoginId));
        BookInfo bookInfo = bookInfoRepository.findById(bookInfoId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디의 책을 찾을 수 없습니다.: " + bookInfoId));

        boolean exists = favoriteBookListRepository.existsFavoriteBookListByUserAndBookInfo(user, bookInfo);

        if(exists) {
            throw new DuplicateFavoriteBookException("이미 유저가 해당 책을 즐겨찾기에 등록했습니다.");
        }

        FavoriteBookList favoriteBookList = FavoriteBookList.createFavoriteBookList(user, bookInfo);
        favoriteBookListRepository.save(favoriteBookList);
        favoriteBookListRepository.flush();
        log.info("favorite book list saved: {}", favoriteBookList);
        log.info("user favorite book list saved: {}", user.getFavoriteBookList());
        return true;
    }

    // 책의 대출여부를 확인해주는 메서드
    private boolean isBookAvailableForCheckout(Long bookInfoId) {
        return bookInstanceRepository.isBookAvailableForCheckout(bookInfoId);
    }
}
