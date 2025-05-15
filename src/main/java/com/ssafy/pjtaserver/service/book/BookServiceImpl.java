package com.ssafy.pjtaserver.service.book;

import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchDto;
import com.ssafy.pjtaserver.dto.response.book.PageResponseDto;
import com.ssafy.pjtaserver.repository.book.BookRepository;
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

    private final BookRepository bookRepository;

    @Override
    public PageResponseDto<BookInfoSearchDto> searchPageComplex(BookInfoSearchCondition condition, Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.fromString(condition.getOrderDirection()), condition.getOrderBy());
        Pageable updatedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        // page객체를 pageResponseDto로 변환해서 반환해준다 은그러면 warn로그 찍힘
        Page<BookInfoSearchDto> bookInfoSearchDto = bookRepository.searchPageComplex(condition, updatedPageable);
        return new PageResponseDto<>(
                bookInfoSearchDto.getContent(),
                bookInfoSearchDto.getTotalElements(),
                bookInfoSearchDto.getTotalPages(),
                bookInfoSearchDto.getNumber(),
                bookInfoSearchDto.getSize()
        );
    }
}
