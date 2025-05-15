package com.ssafy.pjtaserver.controller.book;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/public")
public class ApiPublicBookController {

    private final BookService bookService;

    @GetMapping("/search/book-list")
    public ResponseEntity<ApiResponse> searchBookList(
            @Validated @RequestBody BookInfoSearchCondition condition,
            Pageable pageable) {

        log.info("------------------------------api search book list------------------------------");
        PageResponseDto<BookInfoSearchDto> results = bookService.searchPageComplex(condition, pageable);
        return ApiResponse.of(ApiResponseCode.SUCCESS, results);
    }
}
