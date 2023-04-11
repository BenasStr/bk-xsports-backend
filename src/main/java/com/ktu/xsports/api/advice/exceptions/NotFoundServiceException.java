package com.ktu.xsports.api.advice.exceptions;

public class NotFoundServiceException extends RuntimeException {
    public NotFoundServiceException(String message) {
        super(message);
    }
}
