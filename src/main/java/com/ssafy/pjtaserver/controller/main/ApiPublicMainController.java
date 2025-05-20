package com.ssafy.pjtaserver.controller.main;

import com.ssafy.pjtaserver.dto.response.user.WeeklyPopularBookDto;
import com.ssafy.pjtaserver.enums.ApiResponseCode;
import com.ssafy.pjtaserver.service.book.BookService;
import com.ssafy.pjtaserver.util.ApiResponse;
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
    public ResponseEntity<ApiResponse> getWeeklyPopular() {
        log.info("------------------------------api weekly popular------------------------------");
        List<WeeklyPopularBookDto> weeklyPopular = bookService.getWeeklyPopular();
        log.info("weeklyPopular : {}", weeklyPopular);

        return ApiResponse.of(ApiResponseCode.SUCCESS, weeklyPopular);
    }
}
