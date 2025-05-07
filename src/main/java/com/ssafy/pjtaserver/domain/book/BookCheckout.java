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
@Table(name = "book_checkout")
public class BookCheckout {

    @Id @GeneratedValue
    @Column(name = "book_checkout_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_instance_id")
    private BookInstance bookInstanceId;

    @Column(name = "checkout_date", nullable = false)
    private LocalDateTime checkoutDate;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @PrePersist
    public void prePersist() {
        checkoutDate = LocalDateTime.now();
        dueDate = LocalDateTime.now().plusDays(14);
    }
}
