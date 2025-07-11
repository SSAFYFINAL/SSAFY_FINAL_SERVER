package com.ssafy.pjtaserver.service.book;

import com.querydsl.core.Tuple;
import com.ssafy.pjtaserver.domain.book.BookCheckout;
import com.ssafy.pjtaserver.domain.book.BookInfo;
import com.ssafy.pjtaserver.domain.book.BookInstance;
import com.ssafy.pjtaserver.domain.book.BookReservation;
import com.ssafy.pjtaserver.domain.user.FavoriteBookList;
import com.ssafy.pjtaserver.domain.user.User;
import com.ssafy.pjtaserver.dto.request.book.BookInfoSearchCondition;
import com.ssafy.pjtaserver.dto.request.book.BookInfoSearchDto;
import com.ssafy.pjtaserver.dto.response.book.*;
import com.ssafy.pjtaserver.dto.response.user.WeeklyPopularBookDto;
import com.ssafy.pjtaserver.enums.BookCheckoutStatus;
import com.ssafy.pjtaserver.enums.BookResponseType;
import com.ssafy.pjtaserver.enums.ReservationStatus;
import com.ssafy.pjtaserver.repository.book.CheckoutRepository;
import com.ssafy.pjtaserver.repository.book.BookInfoRepository;
import com.ssafy.pjtaserver.repository.book.BookInstanceRepository;
import com.ssafy.pjtaserver.repository.book.BookReservationRepository;
import com.ssafy.pjtaserver.repository.user.FavoriteRepository;
import com.ssafy.pjtaserver.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.ssafy.pjtaserver.enums.BookResponseType.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookInfoRepository bookInfoRepository;
    private final BookInstanceRepository bookInstanceRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final BookReservationRepository bookReservationRepository;
    private final CheckoutRepository checkoutRepository;

    @Override
    public PageResponseDto<BookInfoSearchDto> searchPageComplex(BookInfoSearchCondition condition, Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.fromString(condition.getOrderDirection()), condition.getOrderBy());
        Pageable updatedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        // page 객체를 pageResponseDto 로 변환해서 반환해준다 안그러면 warn 로그 찍힘
        Page<BookInfoSearchDto> bookInfoSearchDto = bookInfoRepository.searchPageComplex(condition, updatedPageable);

        return new PageResponseDto<>(
                bookInfoSearchDto.getContent(),
                bookInfoSearchDto.getTotalElements(),
                bookInfoSearchDto.getTotalPages(),
                bookInfoSearchDto.getNumber(),
                bookInfoSearchDto.getSize()
        );
    }

    @Override
    public BookDetailDto getDetail(Long bookInfoId, Optional<String> userLoginId) {
        BookInfo bookInfo = getBookInfo(bookInfoId);

        boolean isFavorite = userLoginId
                .map(this::getUser)
                .map(user -> isBookFavorite(bookInfo, user))
                .orElse(false);

        boolean isAvailableForCheckout = isBookAvailableForCheckout(bookInfoId);

        return bookInfoRepository.findBookInfoById(bookInfoId)
                .map(info -> BookDetailDto.builder()
                        .bookInfoId(info.getId())
                        .title(info.getTitle())
                        .authorName(info.getAuthorName())
                        .isbn(info.getIsbn())
                        .registryDate(info.getRegistryDate())
                        .publisherName(info.getPublisherName())
                        .isAvailableCheckedOut(isAvailableForCheckout)
                        .isBookFavorite(isFavorite)
                        .bookImgPath(info.getBookImgPath())
                        .description(info.getDescription())
                        .categoryName(info.getCategoryId().getCategoryTitle())
                        .build()
                )
                .orElseThrow(() -> new EntityNotFoundException("해당 책이 존재하지 않습니다.: " + bookInfoId));
    }

    /**
     * 해당 유저가 해당 책을 즐겨찾기 한적이 없다면 즐겨찾기 추가
     * 만약 이미 즐겨찾기 해놓은 책이라면 즐겨찾기 취소.
     * @param userLoginId : 로그인한 해당유저의 아이디
     * @param bookInfoId : 즐겨찾기 추가할 책의 infoId
     * @return : 즐겨찾기 성공, 실패 여부
     */
    @Transactional
    @Override
    public BookResponseType favoriteBookManager(String userLoginId, Long bookInfoId) {
        User user = getUser(userLoginId); // 유저 조회
        BookInfo bookInfo = getBookInfo(bookInfoId); // 책 정보 조회

        // 즐겨찾기 존재 확인
        Optional<FavoriteBookList> favoriteBookHistory =
                favoriteRepository.findFavoriteBookListByUserAndBookInfo(user, bookInfo);

        // 만약 이미 즐겨찾기에 해당 책이 존재한다면 취소처리
        if (favoriteBookHistory.isPresent()) {
            favoriteBookHistory.get().delete();
            favoriteRepository.delete(favoriteBookHistory.get());

            log.info("favorite book list removed for user: {}, book: {}", user.getId(), bookInfo.getId());
            return BookResponseType.FAVORITE_CALCLE;
        }

        // 즐겨찾기 추가
        FavoriteBookList favoriteBookList = FavoriteBookList.createFavoriteBookList(user, bookInfo);
        favoriteRepository.save(favoriteBookList);

        log.info("favorite book list created: {}", favoriteBookList);
        return BookResponseType.FAVORITE_ADD;
    }


    /**
     * 하나의 info 에 여러개의 instance 가 존재할 수 있는 구조이므로
     * - 유저가 대출을 하고 싶으나 해당 instance 가 모두 대출중일때 자동으로 해당 유저가 이미 해당 책을 예약걸어놨는지 확인한뒤
     *   예약 기록이 존재하지 않은면 자동으로 예약을 걸어준다.
     * - 만약 대출할 수 있는 instance 가 남아있다면 바로 대출을 진행해준다.
     *
     * @param userLoginId : 로그인 되어있는 유저의 id
     * @param bookInfoId : 대출, 예약할 bookInfoId
     * @return : 어떤 작업을 성공했는지에 대한 여부
     */
    @Transactional
    @Override
    public BookResponseType checkoutAndReservationManager(String userLoginId, Long bookInfoId) {
        BookInfo bookInfo = getBookInfo(bookInfoId);
        User user = getUser(userLoginId);

        boolean existedReservation = bookReservationRepository.existsBookReservationsByBookInfoAndUserId(bookInfo, user);
        if (existedReservation) {
            throw new IllegalStateException("해당 유저는 이미 같은책을 예약중에 있습니다.");
        }

        boolean existsDuplicatedCheckoutBook = bookInstanceRepository.existsBookInstanceByBookInfoAndCurrentUserId(bookInfo, user);
        if (existsDuplicatedCheckoutBook) {
            throw new IllegalStateException("해당 유저는 이미 같은책을 대출중에 있습니다.");
        }

        boolean isBookAvailableForCheckout = isBookAvailableForCheckout(bookInfoId);
        if (!isBookAvailableForCheckout) {
            // 대출 불가 = 예약 수행
            BookReservation bookReservation = BookReservation.createBookReservation(
                    user, bookInfo, null, ReservationStatus.ACTIVE);
            bookReservationRepository.save(bookReservation);

            log.info("예약 성공: {}", bookReservation);
            return RESERVATION_SUCCESS;
        }

        // 이 시점에만 실제로 대출 가능한 인스턴스를 가져옴
        BookInstance bookInstance = searchCheckoutAvailable(bookInfoId);
        bookInstance.checkout(user);
        checkoutRepository.save(BookCheckout.createCheckout(user, bookInstance));
        log.info("대출 성공 - BookInstanceId: {}, UserId: {}", bookInstance.getId(), user.getId());
        return CHECKOUT_SUCCESS;
    }

    @Override
    public PageResponseDto<BookInfoSearchDto> searchFavoritePageComplex(BookInfoSearchCondition condition, Pageable pageable, Long userId) {
        Sort sort = Sort.by(Sort.Direction.fromString(condition.getOrderDirection()), condition.getOrderBy());
        Pageable updatedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<BookInfoSearchDto> bookInfoSearchDto = favoriteRepository.searchFavoriteBook(condition, updatedPageable, userId);

        return new PageResponseDto<>(
                bookInfoSearchDto.getContent(),
                bookInfoSearchDto.getTotalElements(),
                bookInfoSearchDto.getTotalPages(),
                bookInfoSearchDto.getNumber(),
                bookInfoSearchDto.getSize()
        );
    }

    @Override
    public List<WeeklyPopularBookDto> getWeeklyPopular() {
        List<Tuple> tuples = favoriteRepository.weeklyPopular();
        return convertTuplesToDtos(tuples);
    }

    @Override
    public List<RecentBooksDto> getRecentBooks() {
        List<BookInfo> bookInfos = bookInfoRepository.searchRecentBooks();
        List<RecentBooksDto> results = new ArrayList<>();
        for (BookInfo bookInfo : bookInfos) {
            results.add(RecentBooksDto.builder()
                    .bookInfoId(bookInfo.getId())
                    .title(bookInfo.getTitle())
                    .authorName(bookInfo.getAuthorName())
                    .bookImgPath(bookInfo.getBookImgPath())
                    .build());
        }
        return results;
    }

    @Override
    public PageResponseDto<CheckoutHistoryDto> getCheckoutHistory(BookInfoSearchCondition condition, Long userId, Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.fromString(condition.getOrderDirection()), condition.getOrderBy());
        Pageable updatedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<CheckoutHistoryDto> checkoutHistory = checkoutRepository.searchCheckoutHistory(condition, updatedPageable, userId);

        return new PageResponseDto<>(
                checkoutHistory.getContent(),
                checkoutHistory.getTotalElements(),
                checkoutHistory.getTotalPages(),
                checkoutHistory.getNumber(),
                checkoutHistory.getSize()
        );
    }

    @Override
    public List<CheckOutRankingDto> getCheckOutRanking() {
        return checkoutRepository.getCheckoutRanking();
    }

    @Override
    public List<BookInfoDto> randomBookSearch(int n) {
        List<BookInfoDto> bookInfoList = bookInfoRepository.searchBookList();

        if (bookInfoList.size() <= n) {
            return bookInfoList;
        }

        Collections.shuffle(bookInfoList);

        return bookInfoList.subList(0, n);
    }

    @Override
    public List<BookInfoDto> userLikeBookList(String userLoginId,int n) {
        List<BookInfoDto> likeBookList = bookInfoRepository.searchBookListWithCategory(userLoginId);

        if (likeBookList.size() != n) {
            List<BookInfoDto> bookList = bookInfoRepository.searchBookList();
            Collections.shuffle(bookList);
            return bookList.subList(0, n);
        }

        Collections.shuffle(likeBookList);

        return likeBookList.subList(0, n);
    }

    private List<WeeklyPopularBookDto> convertTuplesToDtos(List<Tuple> tuples) {
        return IntStream.range(0, tuples.size())
                .mapToObj(i -> {
                    Tuple tuple = tuples.get(i);
                    Long bookInfoId = tuple.get(1, Long.class);

                    BookInfo bookInfo = bookInfoRepository.findBookInfoById(bookInfoId)
                            .orElseThrow(() -> new EntityNotFoundException("해당 책이 존재하지 않습니다. ID: " + bookInfoId));

                    return WeeklyPopularBookDto.builder()
                            .bookInfoId(bookInfoId)
                            .title(bookInfo.getTitle())
                            .authorName(bookInfo.getAuthorName())
                            .imgPath(bookInfo.getBookImgPath())
                            .ranking((long) i + 1)
                            .description(bookInfo.getDescription())
                            .build();
                })
                .toList();
    }

    // 책의 대출여부를 확인해주는 메서드
    private boolean isBookAvailableForCheckout(Long bookInfoId) {
        return bookInstanceRepository.isBookAvailableForCheckout(bookInfoId);
    }

    private boolean isBookFavorite(BookInfo bookInfo, User user) {
        return favoriteRepository.existsFavoriteBookListByUserAndBookInfo(user, bookInfo);
    }

    // bookInfoId 와 대출상태를 이용해 해당 InfoId를 가진 인스턴스들중 대출 가능한 책이 있다면 그책중 가장 빠른 bookInfoId를 가져온다.
    private BookInstance searchCheckoutAvailable (Long bookInfoId) {

        return bookInstanceRepository.findByStatusAndBookInfo_IdOrderByIdAsc(BookCheckoutStatus.AVAILABLE, bookInfoId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("대출 가능한 책이 없습니다. bookInfoId: " + bookInfoId));
    }

    // id로 유저정보 가져오는 메서드
    private User getUser(String userLoginId) {
        return userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디의 유저를 찾을 수 없습니다.: " + userLoginId));
    }

    // id로 유저정보 bookInfo 가져오는 메서드
    private BookInfo getBookInfo(Long bookInfoId) {
        return bookInfoRepository.findById(bookInfoId)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디의 책을 찾을 수 없습니다.: " + bookInfoId));
    }
}
