package com.ssafy.pjtaserver.controller.book;

import com.ssafy.pjtaserver.dto.request.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.request.book.BookInfoSearchDto;
import com.ssafy.pjtaserver.dto.response.book.*;
import com.ssafy.pjtaserver.enums.ApiResponseCode;
import com.ssafy.pjtaserver.enums.BookResponseType;
import com.ssafy.pjtaserver.service.book.BookService;
import com.ssafy.pjtaserver.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static java.util.Optional.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth/book")
public class ApiAuthBookController {

    private final BookService bookService;

    // 대출 요청하는 로직
    @PostMapping("/checkout-reservation/{bookInfoId}")
    public ResponseEntity<ApiResponse> checkoutReservation(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("bookInfoId") Long bookInfoId){
        String userLoginId = userDetails.getUsername();

        BookResponseType result = bookService.checkoutAndReservationManager(userLoginId, bookInfoId);
        log.info("------------------------------api checkout-reservation------------------------------");
        if(result == BookResponseType.RESERVATION_SUCCESS){
            return ApiResponse.of(ApiResponseCode.RESERVATION_SUCCESS,true);
        }
        return ApiResponse.of(ApiResponseCode.CHECKOUT_SUCCESS, true);
    }

    // 찜하기
    @PostMapping("/favorite/{bookInfoId}")
    public ResponseEntity<ApiResponse> likeBook(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("bookInfoId") Long bookInfoId) {
        String userLoginId = userDetails.getUsername();
        log.info("------------------------------api like book------------------------------");

        BookResponseType result = bookService.favoriteBookManager(userLoginId, bookInfoId);

        if(result == BookResponseType.FAVORITE_ADD) {
            return ApiResponse.of(ApiResponseCode.FAVORITE_ADD, true);
        }
        return ApiResponse.of(ApiResponseCode.FAVORITE_CANCEL, true);
    }

    // 상세
    @GetMapping("/details")
    public ResponseEntity<ApiResponse> getBookInfoDetails(@AuthenticationPrincipal UserDetails userDetails,@RequestParam Long bookInfoId) {
        BookDetailDto results = bookService.getDetail(bookInfoId, userDetails.getUsername() == null ? empty() : of(userDetails.getUsername()));

        log.info("------------------------------api is book available for checkout------------------------------");
        log.info("results : {}", results);

        return ApiResponse.of(ApiResponseCode.SUCCESS, results);
    }

    // 찜목록
    @PostMapping("/favorites")
    public ResponseEntity<ApiResponse> getFavoriteList(@AuthenticationPrincipal UserDetails userDetails, @RequestBody BookInfoSearchCondition condition, Pageable pageable) {
        PageResponseDto<BookInfoSearchDto> results = bookService.searchFavoritePageComplex(condition, pageable, userDetails.getUsername());

        log.info("------------------------------api get favorite list------------------------------");
        log.info("results : {}", results);

        return ApiResponse.of(ApiResponseCode.SUCCESS,results);
    }

    @PostMapping("/checkout-history")
    public ResponseEntity<ApiResponse> getCheckoutHistory(@AuthenticationPrincipal UserDetails userDetails, @RequestBody BookInfoSearchCondition condition, Pageable pageable) {
        log.info("------------------------------api get checkout history------------------------------");

        PageResponseDto<CheckoutHistoryDto> results = bookService.getCheckoutHistory(condition, userDetails.getUsername(), pageable);

        return ApiResponse.of(ApiResponseCode.SUCCESS, results);
    }
}
