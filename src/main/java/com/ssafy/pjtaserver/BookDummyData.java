//package com.ssafy.pjtaserver;
//
//import com.ssafy.pjtaserver.domain.book.BookInfo;
//import com.ssafy.pjtaserver.domain.book.BookInstance;
//import com.ssafy.pjtaserver.repository.book.info.BookInfoRepository;
//import com.ssafy.pjtaserver.repository.book.instance.BookInstanceRepository;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//public class BookDummyData {
//
//    private final BookInfoRepository bookInfoRepository;
//    private final BookInstanceRepository bookInstanceRepository;
//
//    private static final String[] AUTHOR_FIRST_NAMES = {"Alice", "Bob", "Cathy", "David", "Eva", "Frank", "Grace", "Henry", "Ivy", "Jack"};
//    private static final String[] AUTHOR_LAST_NAMES = {"Anderson", "Brown", "Clark", "Davis", "Evans", "Foster", "Green", "Hill", "Irving", "Johnson"};
//    private static final String[] PUBLISHERS = {"AlphaBooks", "BetaPress", "CodeHouse", "DevPublishers", "EngineInk"};
//
//    @PostConstruct
//    public void init() {
//        List<BookInfo> allBookInfos = new ArrayList<>();
//
//        for (int i = 1; i <= 50; i++) {
//            String isbn = String.format("%013d", i);
//            String title = "Book Title " + (char)('A' + (i % 26)) + i;
//            String description = "Description for " + title;
//            String authorName = AUTHOR_FIRST_NAMES[i % AUTHOR_FIRST_NAMES.length] + " " +
//                    AUTHOR_LAST_NAMES[i % AUTHOR_LAST_NAMES.length];
//            String publisherName = PUBLISHERS[i % PUBLISHERS.length];
//            String classificationName = String.format("00%d.%d", (i % 10), i % 5);
//            String imgPath = "/images/book_" + i + ".jpg";
//
//            BookInfo bookInfo = BookInfo.createBook(
//                    isbn, title, description, authorName, publisherName, classificationName,
//                    null, imgPath
//            );
//
//            allBookInfos.add(bookInfo);
//        }
//
//        bookInfoRepository.saveAll(allBookInfos);
//
//        List<BookInstance> allInstances = new ArrayList<>();
//        for (BookInfo info : allBookInfos) {
//            for (int j = 0; j < 3; j++) {
//                String callNumber = "CALL-" + UUID.randomUUID().toString().substring(0, 8);
//                BookInstance instance = BookInstance.create(info, callNumber);
//                allInstances.add(instance);
//            }
//        }
//
//        bookInstanceRepository.saveAll(allInstances);
//    }
//}