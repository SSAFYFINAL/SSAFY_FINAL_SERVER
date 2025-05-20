package com.ssafy.pjtaserver.service.guestbook;

import com.ssafy.pjtaserver.domain.guestBook.GuestBook;
import com.ssafy.pjtaserver.domain.user.User;
import com.ssafy.pjtaserver.dto.request.guestbook.GuestBookWriteDto;
import com.ssafy.pjtaserver.dto.response.book.PageResponseDto;
import com.ssafy.pjtaserver.dto.response.guestbook.GuestbookCondition;
import com.ssafy.pjtaserver.dto.response.guestbook.GuestbookListDto;
import com.ssafy.pjtaserver.repository.guestbook.GuestBookRepository;
import com.ssafy.pjtaserver.repository.user.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuestBookServiceImpl implements GuestBookService{

    private final GuestBookRepository guestBookRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public boolean writeGuestBook(GuestBookWriteDto guestBookWriteDto) {

        Optional<User> writer = userRepository.findByUserLoginId(guestBookWriteDto.getWriterId());
        Optional<User> owner = userRepository.findByUserLoginId(guestBookWriteDto.getOwnerId());

        if(writer.isEmpty()) {
            throw new EntityNotFoundException("해당 아이디의 유저를 찾을 수 없습니다." + guestBookWriteDto.getWriterId());
        }

        if(owner.isEmpty()) {
            throw new EntityNotFoundException("해당 아이디의 유저를 찾을 수 없습니다." + guestBookWriteDto.getOwnerId());
        }

        guestBookRepository.save(GuestBook.createGuestBook(owner.get(), writer.get(), guestBookWriteDto.getContent()));

        return true;
    }

    @Override
    public PageResponseDto<GuestbookListDto> searchGuestbookPageComplex(GuestbookCondition condition, Pageable pageable, Long ownerId) {

        Sort sort = Sort.by(Sort.Direction.fromString(condition.getOrderDirection()), condition.getOrderBy());
        PageRequest updatedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<GuestbookListDto> guestBookList = guestBookRepository.getGuestBookList(condition, updatedPageable, ownerId);

        return new PageResponseDto<>(
                guestBookList.getContent(),
                guestBookList.getTotalElements(),
                guestBookList.getTotalPages(),
                guestBookList.getNumber(),
                guestBookList.getSize()
        );
    }

    /**
        본인의 마이페이지에 존재하는 게스트 북이라면 삭제가 가능해야함
        본인의 마이페이지가 아닌경우에는 작성자가 본이이여야만 삭제가 가능해야한다.
     */
    @Transactional
    @Override
    public boolean deleteGuestBook(String userLoginId, Long guestBookId) {
        GuestBook guestBook = guestBookRepository.findById(guestBookId)
                .orElseThrow(() -> new IllegalStateException("해당 ID의 게스트북이 존재하지 않습니다."));

        User owner = guestBook.getOwner();
        User writer = guestBook.getWriter();

        if (!owner.getUserLoginId().equals(userLoginId) && !writer.getUserLoginId().equals(userLoginId)) {
            throw new IllegalStateException("해당 게스트북(" + guestBookId + ")에 대한 삭제 권한이 없습니다.");
        } else if (guestBook.isDeleted()) {
            throw new IllegalStateException("해당 게스트북(" + guestBookId + ")은 이미 삭제 되었습니다.");
        }

        guestBook.setIsDeleted(true);
        guestBookRepository.save(guestBook);

        return true;
    }

    @Transactional
    @Override
    public boolean updateGuestBook(String userLoginId, Long guestBookId, String content) {
        GuestBook guestBook = guestBookRepository.findById(guestBookId)
                .orElseThrow(() -> new IllegalStateException("해당 ID의 게스트북이 존재하지 않습니다."));

        User owner = guestBook.getOwner();
        User writer = guestBook.getWriter();

        if (!owner.getUserLoginId().equals(userLoginId) && !writer.getUserLoginId().equals(userLoginId)) {
            throw new IllegalStateException("해당 게스트북(" + guestBookId + ")에 대한 수정 권한이 없습니다.");
        } else if (guestBook.isDeleted()) {
            throw new IllegalStateException("해당 게스트북(" + guestBookId + ")은 이미 삭제 되었습니다.");
        }

        guestBook.changeContent(content);
        guestBookRepository.save(guestBook);

        return true;
    }
}

