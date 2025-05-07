package com.ssafy.pjtaserver.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserListener {

    // 추후 유저 삭제시 guestBook 의 isDelete컬럼을 true로 돌려줄 수 있도록 만들 예정

    // 추후 유저 삭제시 대출중인 책에서 userId를 null로 세팅가능하도록 만들 예정

}
