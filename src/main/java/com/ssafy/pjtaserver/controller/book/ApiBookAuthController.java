package com.ssafy.pjtaserver.controller.book;

import com.ssafy.pjtaserver.enums.ApiResponseCode;
import com.ssafy.pjtaserver.enums.BookResponseType;
import com.ssafy.pjtaserver.service.book.BookService;
import com.ssafy.pjtaserver.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth/book")
public class ApiBookAuthController {

    private final BookService bookService;

    // 대출 요청하는 로직
    @PostMapping("/checkout-reservation/{bookInfoId}")
    public ResponseEntity<ApiResponse> checkoutReservation(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("bookInfoId") Long bookInfoId){
        String userLoginId = userDetails.getUsername();

        BookResponseType result = bookService.checkoutAndReservationManager(userLoginId, bookInfoId);
        log.info("------------------------------api checkout-reservation------------------------------");
        if(result == BookResponseType.RESERVATION_SUCCESS){
            return ApiResponse.of(ApiResponseCode.RESERVATION_SUCCESS);
        }
        return ApiResponse.of(ApiResponseCode.CHECKOUT_SUCCESS);
    }

    // 찜하기 로직 db 수정 필요해보인다.
    @PostMapping("/like/{bookInfoId}")
    public ResponseEntity<ApiResponse> likeBook(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("bookInfoId") Long bookInfoId) {
        String userLoginId = userDetails.getUsername();
        log.info("------------------------------api like book------------------------------");

        BookResponseType result = bookService.favoriteBookManager(userLoginId, bookInfoId);

        if(result == BookResponseType.FAVORITE_ADD) {
            return ApiResponse.of(ApiResponseCode.FAVORITE_ADD);
        }

        return ApiResponse.of(ApiResponseCode.FAVORITE_CALCLE);
    }
}
