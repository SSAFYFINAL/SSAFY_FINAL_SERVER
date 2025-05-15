package com.ssafy.pjtaserver;

import com.ssafy.pjtaserver.domain.book.BookInfo;
import com.ssafy.pjtaserver.repository.book.BookRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookDummyData {

    private final BookRepository bookRepository;

    @PostConstruct
    public void init() {
        List<String> titles = List.of(
                "1984", "To Kill a Mockingbird", "The Great Gatsby", "The Catcher in the Rye",
                "Pride and Prejudice", "Brave New World", "The Hobbit", "Fahrenheit 451",
                "Jane Eyre", "Animal Farm", "The Lord of the Rings", "Harry Potter and the Sorcerer's Stone",
                "The Chronicles of Narnia", "The Book Thief", "Wuthering Heights", "The Giver",
                "The Alchemist", "The Little Prince", "Crime and Punishment", "War and Peace"
        );

        List<String> authors = List.of(
                "George Orwell", "Harper Lee", "F. Scott Fitzgerald", "J.D. Salinger",
                "Jane Austen", "Aldous Huxley", "J.R.R. Tolkien", "Ray Bradbury",
                "Charlotte Brontë", "Markus Zusak", "Fyodor Dostoevsky", "Leo Tolstoy",
                "Paulo Coelho", "Antoine de Saint-Exupéry", "C.S. Lewis", "Lois Lowry"
        );

        List<String> publishers = List.of(
                "Penguin", "HarperCollins", "Random House", "Simon & Schuster", "Bloomsbury"
        );

        List<String> classifications = List.of(
                "Fiction", "Classic", "Sci-Fi", "Fantasy", "Literature", "Philosophy"
        );

        for (int i = 1; i <= 100; i++) {
            String title = titles.get(i % titles.size());
            String author = authors.get(i % authors.size());
            String publisher = publishers.get(i % publishers.size());
            String classification = classifications.get(i % classifications.size());
            String isbn = "978-" + UUID.randomUUID().toString().substring(0, 10);

            BookInfo book = BookInfo.createDummyBook(
                    isbn,
                    title,
                    title + " is a fascinating book by " + author + ".",
                    author,
                    publisher,
                    classification,
                    null, // 카테고리 더미 넣을 거면 여기 넣기
                    "https://placehold.co/300x400?text=" + title.replace(" ", "+")
            );

            bookRepository.save(book);
        }
    }
}
