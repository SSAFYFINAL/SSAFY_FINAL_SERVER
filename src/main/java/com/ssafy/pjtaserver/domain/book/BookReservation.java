package com.ssafy.pjtaserver.domain.book;

import com.ssafy.pjtaserver.enums.ReservationStatus;
import com.ssafy.pjtaserver.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "book_reservation")
public class BookReservation {

    @Id @GeneratedValue
    @Column(name = "book_reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_instance_id")
    private BookInstance bookInstanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_info_id")
    private BookInfo bookInfo;

    @Column(name = "reservation_date", nullable = false)
    private LocalDateTime reservationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;

    @PrePersist
    public void prePersist() {
        reservationDate = LocalDateTime.now();
        status = ReservationStatus.ACTIVE;
    }

    public static BookReservation createBookReservation(User user, BookInfo bookInfo, BookInstance bookInstance, ReservationStatus status) {
        BookReservation bookReservation = new BookReservation();
        bookReservation.userId = user;
        bookReservation.bookInfo = bookInfo;
        bookReservation.bookInstanceId = bookInstance;
        bookReservation.status = status;

        return bookReservation;
    }
}
