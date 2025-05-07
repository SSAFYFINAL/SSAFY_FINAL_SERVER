package com.ssafy.pjtaserver.domain.book;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @PrePersist
    public void prePersist() {
        registryDate = LocalDateTime.now();
    }
}
