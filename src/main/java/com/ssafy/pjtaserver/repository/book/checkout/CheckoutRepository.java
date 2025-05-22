package com.ssafy.pjtaserver.repository.book.checkout;

import com.ssafy.pjtaserver.domain.book.BookCheckout;
import com.ssafy.pjtaserver.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckoutRepository extends JpaRepository<BookCheckout, Long>, CheckoutQueryRepository {
    List<BookCheckout> findByUser(User user);
}
