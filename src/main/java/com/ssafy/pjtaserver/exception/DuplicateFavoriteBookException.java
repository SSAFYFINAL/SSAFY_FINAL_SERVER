package com.ssafy.pjtaserver.exception;

public class DuplicateFavoriteBookException extends RuntimeException {
    public DuplicateFavoriteBookException(String message) {
        super(message);
    }
}
