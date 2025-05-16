package com.ssafy.pjtaserver.domain.book;

import com.ssafy.pjtaserver.enums.BookCheckoutStatus;
import com.ssafy.pjtaserver.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "book_instance")
public class BookInstance {

    @Id @GeneratedValue
    @Column(name = "book_instance_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_info_id", nullable = false)
    private BookInfo bookInfo;

    @Column(name = "call_number", nullable = false, unique = true)
    private String callNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookCheckoutStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User currentUserId;

    @Column(name = "registry_date", nullable = false)
    private LocalDateTime registryDate;

    @Column(name = "checkout_start_date")
    private LocalDateTime checkoutStartDate;

    @Column(name = "checkout_end_date")
    private LocalDateTime checkoutEndDate;

    @PrePersist
    public void prePersist() {
        status = BookCheckoutStatus.AVAILABLE;
        registryDate = LocalDateTime.now();
    }

    public static BookInstance create(BookInfo bookInfo, String callNumber) {
        BookInstance bookInstance = new BookInstance();
        bookInstance.bookInfo = bookInfo;
        bookInstance.callNumber = callNumber;
        return bookInstance;
    }

    public void checkout(User user) {
        this.currentUserId = user;
        this.checkoutStartDate = LocalDateTime.now();
        this.checkoutEndDate = LocalDateTime.now().plusDays(14);
        this.status = BookCheckoutStatus.CHECKED_OUT;
    }

    public void returnBook() {
        this.currentUserId = null;
        this.checkoutStartDate = null;
        this.checkoutEndDate = null;
        this.status = BookCheckoutStatus.AVAILABLE;
    }
}
