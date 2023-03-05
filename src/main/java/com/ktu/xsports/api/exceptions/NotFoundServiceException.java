package com.ktu.xsports.api.exceptions;

public class NotFoundServiceException extends RuntimeException {
    public NotFoundServiceException(String message) {
        super(message);
    }
}
