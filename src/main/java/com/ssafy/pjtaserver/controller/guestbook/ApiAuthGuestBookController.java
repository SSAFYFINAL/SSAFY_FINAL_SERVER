package com.ssafy.pjtaserver.controller.guestbook;

import com.ssafy.pjtaserver.dto.request.guestbook.GuestBookUpdateDto;
import com.ssafy.pjtaserver.dto.request.guestbook.GuestBookWriteDto;
import com.ssafy.pjtaserver.dto.response.book.PageResponseDto;
import com.ssafy.pjtaserver.dto.response.guestbook.GuestbookCondition;
import com.ssafy.pjtaserver.dto.response.guestbook.GuestbookListDto;
import com.ssafy.pjtaserver.enums.ApiResponseCode;
import com.ssafy.pjtaserver.service.guestbook.GuestBookService;
import com.ssafy.pjtaserver.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/guestbook")
@Slf4j
@RequiredArgsConstructor
public class ApiAuthGuestBookController {

    private final GuestBookService guestBookService;

    /**
     *  ownerId 방명록의 주인은 writer가 방문한 마이페이지의 주인이 된다.
     */
    @PostMapping("/write")
    public ResponseEntity<ApiResponse> writeGuestBook(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody GuestBookWriteDto guestBookWriteDto) {
        log.info("------------------------------api guestbook write------------------------------");
        String userLoginId = userDetails.getUsername();
        guestBookWriteDto.setWriterId(userLoginId);

        boolean result = guestBookService.writeGuestBook(guestBookWriteDto);

        if(!result) {
            return ApiResponse.of(ApiResponseCode.GUESTBOOK_WRITE_FAILED,false);
        }
        return ApiResponse.of(ApiResponseCode.GUESTBOOK_WRITE_SUCCESS, true);
    }

    @PostMapping("/list/{ownerId}")
    public ResponseEntity<ApiResponse> getGuestBookList(Pageable pageable, @RequestBody GuestbookCondition condition, @PathVariable("ownerId") Long ownerId) {
        log.info("------------------------------api guestbook list------------------------------");
        PageResponseDto<GuestbookListDto> results = guestBookService.searchGuestbookPageComplex(condition, pageable, ownerId);
        return ApiResponse.of(ApiResponseCode.GUESTBOOK_LIST_SUCCESS, results);
    }

    @DeleteMapping("/delete/{guestBookId}")
    public ResponseEntity<ApiResponse> deleteGuestBook(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String guestBookId) {
        log.info("------------------------------api guestbook delete------------------------------");
        String userLoginId = userDetails.getUsername();

        boolean result = guestBookService.deleteGuestBook(userLoginId, Long.parseLong(guestBookId));

        if (!result) {
            return ApiResponse.of(ApiResponseCode.GUESTBOOK_DELETE_FAILED, false);
        }
        return ApiResponse.of(ApiResponseCode.GUESTBOOK_DELETE_SUCCESS, true);
    }

    @PutMapping("/update/{guestBookId}")
    public ResponseEntity<ApiResponse> updateGuestBook(@AuthenticationPrincipal UserDetails userDetails,
                                                       @PathVariable String guestBookId,
                                                       @Valid @RequestBody GuestBookUpdateDto guestBookUpdateDto) {
        log.info("------------------------------api guestbook update------------------------------");
        String userLoginId = userDetails.getUsername();
        boolean result = guestBookService.updateGuestBook(userLoginId, Long.parseLong(guestBookId), guestBookUpdateDto.getContent());

        if (!result) {
            return ApiResponse.of(ApiResponseCode.GUESTBOOK_UPDATE_FAILED, false);
        }
        return ApiResponse.of(ApiResponseCode.GUESTBOOK_UPDATE_SUCCESS, true);
    }

}
