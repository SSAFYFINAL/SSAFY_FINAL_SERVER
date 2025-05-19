package com.ssafy.pjtaserver.service.guestbook;

import com.ssafy.pjtaserver.domain.guestBook.GuestBook;
import com.ssafy.pjtaserver.domain.user.User;
import com.ssafy.pjtaserver.dto.request.guestbook.GuestBookWriteDto;
import com.ssafy.pjtaserver.repository.guestbook.GuestBookRepository;
import com.ssafy.pjtaserver.repository.user.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GuestBookServiceImpl implements GuestBookService{

    private final GuestBookRepository guestBookRepository;
    private final UserRepository userRepository;

    @Override
    public boolean writeGuestBook(GuestBookWriteDto guestBookWriteDto) {

        Optional<User> writer = userRepository.findByUserLoginId(guestBookWriteDto.getWriterId());
        Optional<User> owner = userRepository.findByUserLoginId(guestBookWriteDto.getOwnerId());

        if(writer.isEmpty()) {
            throw new IllegalStateException("writer가 없음");
        }

        if(owner.isEmpty()) {
            throw new IllegalStateException("owner가 없음");
        }

        guestBookRepository.save(GuestBook.createGuestBook(owner.get(), writer.get(), guestBookWriteDto.getContent()));

        return true;
    }
}
