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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> children = new ArrayList<>();


    // 필수 정보만 포함한 생성자
    public Category(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    // 부모를 설정하면서 트리 관계를 연결하는 메서드
    public void connectToParent(Category parentCategory) {
        if (this.parent != null) {
            this.parent.children.remove(this); // 기존 부모와의 관계 해제
        }
        this.parent = parentCategory;

        if (parentCategory != null && !parentCategory.children.contains(this)) {
            parentCategory.children.add(this); // 부모의 자식 목록에 추가
        }
    }

    // 자식 카테고리를 추가하는 편의 메서드
    public void addChild(Category childCategory) {
        if (!this.children.contains(childCategory)) {
            this.children.add(childCategory);
            childCategory.connectToParent(this); // 자식 측에서도 연결
        }
    }

    // 자식 카테고리를 삭제하는 메서드
    public void removeChild(Category childCategory) {
        if (this.children.contains(childCategory)) {
            this.children.remove(childCategory);
            childCategory.connectToParent(null); // 자식 측에서도 부모 관계 해제
        }
    }

    public void printCategoryTree(String prefix) {
        System.out.println(prefix + this.categoryTitle);
        for (Category child : children) {
            child.printCategoryTree(prefix + "  "); // 계층 구분을 위해 들여쓰기 추가
        }
    }


}
