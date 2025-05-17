package com.ssafy.pjtaserver.exception;

public class NoAvailableBookInstancesException extends RuntimeException {
    public NoAvailableBookInstancesException(String message) {
        super(message);
    }
}
