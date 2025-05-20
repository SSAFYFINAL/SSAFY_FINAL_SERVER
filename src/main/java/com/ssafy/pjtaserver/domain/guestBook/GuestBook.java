package com.ssafy.pjtaserver.domain.guestBook;

import com.ssafy.pjtaserver.domain.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "guest_book")
public class GuestBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guest_book_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "write_date")
    private LocalDateTime writeDate;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @PrePersist
    public void prePersist() {
        writeDate = LocalDateTime.now();
        isDeleted = false;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public GuestBook(User owner, User writer, String content) {
        this.owner = owner;
        this.writer = writer;
        this.content = content;
    }

    public static GuestBook createGuestBook(User ownerId, User writerId, String content) {
        return new GuestBook(ownerId, writerId, content);
    }

}