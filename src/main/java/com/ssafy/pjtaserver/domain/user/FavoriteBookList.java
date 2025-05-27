package com.ssafy.pjtaserver.domain.user;

import com.ssafy.pjtaserver.domain.book.BookInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "favorite_book_list")
public class FavoriteBookList {
    @Id @GeneratedValue
    @Column(name = "favorite_book_list_id")
    private Long favoriteBookListId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_info_id", nullable = false)
    private BookInfo bookInfo;

    @Column(name = "like_at", nullable = false)
    private LocalDateTime likeAt;

    @PrePersist
    public void prePersist() {
        likeAt = LocalDateTime.now();
    }

    private FavoriteBookList(User user, BookInfo bookInfo) {
        this.user = user;
        this.bookInfo = bookInfo;
    }

    // 좋아요 만들어주는 create 메서드
    public static FavoriteBookList createFavoriteBookList(User user, BookInfo bookInfo) {
        FavoriteBookList favoriteBookList = new FavoriteBookList(user, bookInfo);
        favoriteBookList.setUser(user);
        favoriteBookList.setBookInfo(bookInfo);
        return  favoriteBookList;
    }

    public void setUser(User user) {
        this.user = user;

        // User 클래스로도 양방향 관계 세팅
        if (!user.getFavoriteBookList().contains(this)) {
            user.addFavoriteBook(this);
        }
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;

        // BookInfo 클래스로도 양방향 관계 세팅
        if (!bookInfo.getFavoriteBookList().contains(this)) {
            bookInfo.addFavoriteBook(this);
        }
    }

    public void delete() {
        // User와의 관계 제거
        if (this.user != null) {
            this.user.getFavoriteBookList().remove(this);
            this.user = null;
        }

        // BookInfo와의 관계 제거
        if (this.bookInfo != null) {
            this.bookInfo.getFavoriteBookList().remove(this);
            this.bookInfo = null;
        }
    }

}
