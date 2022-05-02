package com.ktu.xsports.api.exceptions;

import java.util.function.Supplier;

public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }
}
