package com.ssafy.pjtaserver.domain.book;

import com.ssafy.pjtaserver.domain.user.FavoriteBookList;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "book_info")
public class BookInfo {

    @Id @GeneratedValue
    @Column(name = "book_info_id")
    private Long id;

    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "author_name", nullable = false)
    private String authorName;

    @Column(name = "publisher_name", nullable = false)
    private String publisherName;

    @Column(name = "series_name")
    private String seriesName;

    @Column(name = "classification_name")
    private String classificationName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category categoryId;

    @Column(name = "registry_date", nullable = false)
    private LocalDateTime registryDate;

    @Column(name = "book_img_path")
    private String bookImgPath;

    @OneToMany(mappedBy = "bookInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteBookList> favoriteBookList = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        registryDate = LocalDateTime.now();
    }

    public static BookInfo createBook(
            String isbn,
            String title,
            String description,
            String authorName,
            String publisherName,
            String classificationName,
            Category categoryId,
            String bookImgPath
    ) {
        BookInfo bookInfo = new BookInfo();
        bookInfo.isbn = isbn;
        bookInfo.title = title;
        bookInfo.description = description;
        bookInfo.authorName = authorName;
        bookInfo.publisherName = publisherName;
        bookInfo.seriesName = classificationName;
        bookInfo.categoryId = categoryId;
        bookInfo.bookImgPath = bookImgPath;
        return bookInfo;
    }

    public void addFavoriteBook(FavoriteBookList favoriteBookList) {
        this.favoriteBookList.add(favoriteBookList);

        if (favoriteBookList.getBookInfo() != this) {
            favoriteBookList.setBookInfo(this);
        }
    }
}
