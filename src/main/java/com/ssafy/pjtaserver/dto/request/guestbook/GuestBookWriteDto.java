package com.ssafy.pjtaserver.dto.request.guestbook;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GuestBookWriteDto {
    private String ownerId;

    @NotBlank(message = "wirterId는 필수 입력값 입니다.")
    private String writerId;

    @NotBlank(message = "content는 필수 입력값 입니다.")
    private String content;
}
