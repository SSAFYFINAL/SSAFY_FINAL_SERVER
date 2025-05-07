package com.ssafy.pjtaserver.domain.book;

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
    @JoinColumn(name = "book_instance_id", unique = true)
    private BookInstance bookInstanceId;

    @Column(name = "reservation_date", nullable = false)
    private LocalDateTime reservationDate;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReservationStatus status;

    @PrePersist
    public void prePersist() {
        reservationDate = LocalDateTime.now();
        expirationDate = LocalDateTime.now().plusDays(7);
        status = ReservationStatus.ACTIVE;
    }
}
