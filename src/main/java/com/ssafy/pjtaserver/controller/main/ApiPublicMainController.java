package com.ssafy.pjtaserver.controller.main;

import com.ssafy.pjtaserver.dto.response.book.RecentBooksDto;
import com.ssafy.pjtaserver.dto.response.user.WeeklyPopularBookDto;
import com.ssafy.pjtaserver.enums.ApiResponseCode;
import com.ssafy.pjtaserver.service.book.BookService;
import com.ssafy.pjtaserver.utils.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/main")
@Slf4j
public class ApiPublicMainController {

    private final BookService bookService;

    @GetMapping("/weekly-popular")
    public ResponseEntity<ApiResponseUtil> getWeeklyPopular() {
        log.info("------------------------------api weekly popular------------------------------");
        List<WeeklyPopularBookDto> results = bookService.getWeeklyPopular();
        log.info("weeklyPopular : {}", results);

        return ApiResponseUtil.of(ApiResponseCode.WEEKLY_POPULAR_LIST_SUCCESS, results);
    }

    @GetMapping("/recent-book-list")
    public ResponseEntity<ApiResponseUtil> getRecentBooks() {
        log.info("------------------------------api recent book list------------------------------");
        List<RecentBooksDto> results = bookService.getRecentBooks();
        return ApiResponseUtil.of(ApiResponseCode.RECENT_BOOK_LIST_SUCCESS, results);
    }

}
