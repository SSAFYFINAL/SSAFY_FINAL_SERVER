package com.ssafy.pjtaserver.repository.book.instance;

public interface BookInstanceQueryRepository {
    boolean isBookAvailableForCheckout(Long bookInfoId);
}
