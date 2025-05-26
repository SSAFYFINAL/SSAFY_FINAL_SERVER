package com.ssafy.pjtaserver.repository.book.query;

public interface BookInstanceQueryRepository {
    boolean isBookAvailableForCheckout(Long bookInfoId);
}
