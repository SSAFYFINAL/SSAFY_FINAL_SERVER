package com.ssafy.pjtaserver.controller.book;

import com.ssafy.pjtaserver.dto.response.book.BookDetailDto;
import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.response.book.BookInfoSearchDto;
import com.ssafy.pjtaserver.dto.response.book.PageResponseDto;
import com.ssafy.pjtaserver.enums.ApiResponseCode;
import com.ssafy.pjtaserver.service.book.BookService;
import com.ssafy.pjtaserver.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/public/book")
public class ApiPublicBookController {

    private final BookService bookService;

    @GetMapping("/search/list")
    public ResponseEntity<ApiResponse> searchBookList(@RequestBody BookInfoSearchCondition condition,
            Pageable pageable) {

        log.info("------------------------------api search book list------------------------------");
        PageResponseDto<BookInfoSearchDto> results = bookService.searchPageComplex(condition, pageable);
        return ApiResponse.of(ApiResponseCode.SUCCESS, results);
    }

    @GetMapping("/details/{bookInfoId}")
    public ResponseEntity<ApiResponse> getBookInfoDetails(@PathVariable("bookInfoId") Long bookInfoId) {
        BookDetailDto results = bookService.getDetail(bookInfoId, java.util.Optional.empty());

        log.info("------------------------------api is book available for checkout------------------------------");
        log.info("results : {}", results);

        return ApiResponse.of(ApiResponseCode.SUCCESS, results);
    }
}