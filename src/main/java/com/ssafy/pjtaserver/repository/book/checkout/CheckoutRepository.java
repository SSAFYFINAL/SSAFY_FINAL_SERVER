package com.ssafy.pjtaserver.repository.book.checkout;

import com.ssafy.pjtaserver.domain.book.BookCheckout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckoutRepository extends JpaRepository<BookCheckout, Long>, CheckoutQueryRepository {
    
}
