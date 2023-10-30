package com.chatop.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(value = BAD_REQUEST, reason = "Fields cannot be null or empty")
public class FiledNotNullOrEmptyException extends RuntimeException {
    public FiledNotNullOrEmptyException(String message) {
        super(message);
    }
}
