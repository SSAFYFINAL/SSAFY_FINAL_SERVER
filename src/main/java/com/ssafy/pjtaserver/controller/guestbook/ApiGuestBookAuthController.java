package com.ssafy.pjtaserver.controller.guestbook;

import com.ssafy.pjtaserver.dto.request.guestbook.GuestBookWriteDto;
import com.ssafy.pjtaserver.dto.response.guestbook.GuestbookCondition;
import com.ssafy.pjtaserver.dto.response.guestbook.GuestbookListDto;
import com.ssafy.pjtaserver.enums.ApiResponseCode;
import com.ssafy.pjtaserver.repository.guestbook.GuestBookRepository;
import com.ssafy.pjtaserver.service.guestbook.GuestBookService;
import com.ssafy.pjtaserver.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/guestbook")
@Slf4j
@RequiredArgsConstructor
public class ApiGuestBookAuthController {

    private final GuestBookService guestBookService;
    private final GuestBookRepository guestBookRepository;

    @PostMapping("/write")
    public ResponseEntity<ApiResponse> writeGuestBook(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody GuestBookWriteDto guestBookWriteDto) {
        String userLoginId = userDetails.getUsername();
        log.info("------------------------------api guestbook write------------------------------");
        log.info("userLoginId : {}", userLoginId);
        guestBookWriteDto.setWriterId(userLoginId);

        log.info("guestBookWriteDto : {}", guestBookWriteDto);

        boolean result = guestBookService.writeGuestBook(guestBookWriteDto);

        if(!result) {
            return ApiResponse.of(ApiResponseCode.INVALID_REQUEST,false);
        }

        return ApiResponse.of(ApiResponseCode.SUCCESS, true);
    }

    @PostMapping("/list")
    public ResponseEntity<ApiResponse> getGuestBookList(@AuthenticationPrincipal UserDetails userDetails,
                                                        Pageable pageable, @RequestBody GuestbookCondition condition) {
        log.info("------------------------------api guestbook list------------------------------");
        Page<GuestbookListDto> guestBookList = guestBookRepository.getGuestBookList(condition, pageable, userDetails.getUsername());
        log.info("guestBookList : {}", guestBookList);
        return ApiResponse.of(ApiResponseCode.SUCCESS, guestBookList);
    }
}
