package com.ssafy.pjtaserver;

import com.ssafy.pjtaserver.domain.book.Category;
import com.ssafy.pjtaserver.repository.category.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DummyData2 {

    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void init() {
        List<Category> categories = Arrays.asList(
                Category.createCategory("축구"),
                Category.createCategory("야구"),
                Category.createCategory("농구"),
                Category.createCategory("테니스"),
                Category.createCategory("수상레저스포츠"),
                Category.createCategory("승마"),
                Category.createCategory("카레이싱, 자전거"),
                Category.createCategory("일반 스포츠"),
                Category.createCategory("에어로빅, 댄스"),
                Category.createCategory("마라톤")
        );

        categoryRepository.saveAll(categories);
    }

}
