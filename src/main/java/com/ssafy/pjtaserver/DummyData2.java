package com.ssafy.pjtaserver;

import com.ssafy.pjtaserver.domain.book.BookInfo;
import com.ssafy.pjtaserver.domain.book.BookInstance;
import com.ssafy.pjtaserver.domain.book.Category;
import com.ssafy.pjtaserver.domain.user.User;
import com.ssafy.pjtaserver.repository.book.BookInfoRepository;
import com.ssafy.pjtaserver.repository.book.BookInstanceRepository;
import com.ssafy.pjtaserver.repository.category.CategoryRepository;
import com.ssafy.pjtaserver.repository.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DummyData2 {

    private final CategoryRepository categoryRepository;
    private final BookInstanceRepository bookInstanceRepository;
    private final BookInfoRepository bookInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {

        // 유저 등록
//        List<User> userList = new ArrayList<>();
//        User user1 = User.createNormalUser("dojin9654", passwordEncoder.encode("!ehgus12"), "김도현", "dojin8351", "dojin8351@gmail.com", "01085392392");
//        User user2 = User.createNormalUser("joohyun0913", passwordEncoder.encode("!ehgus12"), "권주현", "소개팅만3번째", "joohyun0913@naver.com", "01065111647");
//        User user3 = User.createNormalUser("yoonjeongKm", passwordEncoder.encode("!ehgus12"), "김윤정", "잠이보약!", "yoommnj@gmail.com", "01032571477");
//        User user4 = User.createNormalUser("bkw0820", passwordEncoder.encode("!ehgus12"), "변가원", "응가", "bkw0820@gmail.com", "01025047113");
//        User user5 = User.createNormalUser("woqls1009", passwordEncoder.encode("!ehgus12"), "이재빈", "재빈퐁", "woqls1009@daum.net", "01055474833");
//        User user6 = User.createNormalUser("hyunji1132", passwordEncoder.encode("!ehgus12"), "이현지", "올해31", "hyunji1132@gmail.com", "01048168496");
//        User user7 = User.createNormalUser("juywoo", passwordEncoder.encode("!ehgus12"), "주연우", "슈웃~", "juywoo@naver.com", "01028202675");
//        User user8 = User.createNormalUser("istory1997", passwordEncoder.encode("!ehgus12"), "이언지", "짝짝이소녀", "istory1997@naver.com", "01094660152");
//        User user9 = User.createNormalUser("junho9661", passwordEncoder.encode("!ehgus12"), "문준호", "당구는어려워..", "junho9661@gmail.com", "01059266632");
//        User user10 = User.createNormalUser("dsazxc035", passwordEncoder.encode("!ehgus12"), "김현재", "언지야 나 그만좋아해", "dsazxc035@naver.com", "01062563914");
//
//        userList.add(user1);
//        userList.add(user2);
//        userList.add(user3);
//        userList.add(user4);
//        userList.add(user5);
//        userList.add(user6);
//        userList.add(user7);
//        userList.add(user8);
//        userList.add(user9);
//        userList.add(user10);
//
//        userRepository.saveAll(userList);
//
//        List<Category> categories = Arrays.asList(
//                Category.createCategory("축구"),
//                Category.createCategory("야구"),
//                Category.createCategory("농구"),
//                Category.createCategory("테니스"),
//                Category.createCategory("수상레저스포츠"),
//                Category.createCategory("승마"),
//                Category.createCategory("카레이싱, 자전거"),
//                Category.createCategory("일반 스포츠"),
//                Category.createCategory("에어로빅, 댄스"),
//                Category.createCategory("마라톤")
//        );
//        categoryRepository.saveAll(categories);

        // 책 한권당 3개의 청구번호를 가진 책 삽입
//        List<BookInfo> bookInfoAll = bookInfoRepository.findAll();
//        for (BookInfo bookInfo : bookInfoAll) {
//            for (int j = 1; j <= 3; j++) {
//                String callNumber = bookInfo.getIsbn() + "-" + j;
//                BookInstance bookInstance = BookInstance.create(bookInfo, callNumber);
//                bookInstanceRepository.save(bookInstance);
//            }
//        }
    }
}
