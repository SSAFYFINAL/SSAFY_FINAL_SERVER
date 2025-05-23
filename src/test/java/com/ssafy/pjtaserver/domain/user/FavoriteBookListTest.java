package com.ssafy.pjtaserver.domain.user;

import com.ssafy.pjtaserver.domain.book.BookInfo;
import com.ssafy.pjtaserver.repository.book.info.BookInfoRepository;
import com.ssafy.pjtaserver.repository.user.favorite.FavoriteRepository;
import com.ssafy.pjtaserver.repository.user.user.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class FavoriteBookListTest {

    @Autowired
    private BookInfoRepository bookInfoRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;


    @Test
    public void testFavorite() {
        // 1. 사용자 생성 및 저장
        User user = User.createNormalUser("dojin", "1234", "dojin", "dojin", "dojin1111@gmail.com", "010-1234-5678");
        userRepository.save(user);
        System.out.println("[User 저장 완료]: " + user);

        // 2. 책 생성 및 FavoriteBookList 저장
        for (int i = 1; i <= 10; i++) {
            BookInfo book = BookInfo.createBook(
                    "isbn" + i,
                    "책제목" + i,
                    "설명" + i,
                    "작가" + i,
                    "출판사" + i,
                    null,
                    "이미지" + i
            );
            bookInfoRepository.save(book);
            System.out.println("[Book 저장 완료]: " + book);

            FavoriteBookList favoriteBookList = FavoriteBookList.createFavoriteBookList(user, book);
            favoriteRepository.save(favoriteBookList);
            System.out.println("[FavoriteBookList 저장 완료]: " + favoriteBookList);
        }

        // 3. 저장된 FavoriteBookList 출력
        System.out.println("\n[저장된 FavoriteBookList 찾기]");
        favoriteRepository.findAll().forEach(System.out::println);

        // 4. User와 연결된 FavoriteBookList 출력
        System.out.println("\n[User와 연관된 FavoriteBookList]");
        user.getFavoriteBookList().forEach(System.out::println);

        userRepository.delete(user);
        assertThat(userRepository.findById(user.getId())).isEmpty();
        assertThat(favoriteRepository.count()).isEqualTo(0);
    }
}