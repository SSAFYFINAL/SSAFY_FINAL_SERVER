package com.ssafy.pjtaserver.controller.book;

import com.ssafy.pjtaserver.dto.response.book.BookDetailDto;
import com.ssafy.pjtaserver.dto.request.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.request.book.BookInfoSearchDto;
import com.ssafy.pjtaserver.dto.response.book.BookInfoDto;
import com.ssafy.pjtaserver.dto.response.book.CheckOutRankingDto;
import com.ssafy.pjtaserver.dto.response.book.PageResponseDto;
import com.ssafy.pjtaserver.enums.ApiResponseCode;
import com.ssafy.pjtaserver.service.book.BookService;
import com.ssafy.pjtaserver.utils.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/public/book")
public class ApiPublicBookController {

    private final BookService bookService;

    @PostMapping("/search/list")
    public ResponseEntity<ApiResponseUtil> searchBookList(@RequestBody BookInfoSearchCondition condition,
                                                          Pageable pageable) {
        log.info("------------------------------api search book list------------------------------");

        PageResponseDto<BookInfoSearchDto> results = bookService.searchPageComplex(condition, pageable);

        return ApiResponseUtil.of(ApiResponseCode.SUCCESS, results);
    }

    @GetMapping("/details")
    public ResponseEntity<ApiResponseUtil> getBookInfoDetails(@RequestParam Long bookInfoId) {
        log.info("------------------------------api is book available for checkout------------------------------");
        BookDetailDto results = bookService.getDetail(bookInfoId, java.util.Optional.empty());
        log.info("results : {}", results);

        return ApiResponseUtil.of(ApiResponseCode.SUCCESS, results);
    }

    @GetMapping("/top-borrower")
    public ResponseEntity<ApiResponseUtil> getTopBorrower() {
        log.info("------------------------------api get top borrower------------------------------");

        List<CheckOutRankingDto> checkOutRanking = bookService.getCheckOutRanking();

        return ApiResponseUtil.of(ApiResponseCode.SUCCESS, checkOutRanking);
    }

    @GetMapping("/recommend-books")
    public ResponseEntity<ApiResponseUtil> getRecommendBooks() {
        log.info("------------------------------api get recommend books------------------------------");
        List<BookInfoDto> result = bookService.randomBookSearch(5);
        return ApiResponseUtil.of(ApiResponseCode.SUCCESS, result);
    }
}