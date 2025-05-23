package com.ssafy.pjtaserver.dto.request.guestbook;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GuestBookWriteDto {
    @NotBlank(message = "ownerId는 필수 입력값 입니다.")
    private Long ownerId;

    private String writerId;

    @NotBlank(message = "content는 필수 입력값 입니다.")
    private String content;
}
