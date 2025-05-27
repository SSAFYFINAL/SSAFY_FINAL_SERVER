package com.ssafy.pjtaserver.domain.book;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_title", nullable = false, length = 50)
    private String categoryTitle;

    // 필수 정보만 포함한 생성자
    public Category(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public static Category createCategory(String categoryTitle) {
        return new Category(categoryTitle);
    }
}
