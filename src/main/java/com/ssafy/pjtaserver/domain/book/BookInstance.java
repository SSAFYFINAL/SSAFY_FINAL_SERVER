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

    @PrePersist
    public void prePersist() {
        status = BookCheckoutStatus.AVAILABLE;
        registryDate = LocalDateTime.now();
    }
}
