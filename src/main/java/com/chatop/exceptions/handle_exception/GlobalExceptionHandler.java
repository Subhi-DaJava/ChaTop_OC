package com.chatop.exceptions.handle_exception;

import com.chatop.exceptions.InvalidImageFormatException;
import com.chatop.exceptions.RentalNotFondException;
import com.chatop.exceptions.UnauthorizedUserException;
import com.chatop.exceptions.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<Object> handleUnauthorizedUserException(UnauthorizedUserException ex) {
        return new ResponseEntity<>("Unauthorized: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RentalNotFondException.class)
    public ResponseEntity<Object> handleRentalNotFoundException(RentalNotFondException ex) {
        return new ResponseEntity<>("Rental not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return new ResponseEntity<>("User already exists: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidImageFormatException.class)
    public ResponseEntity<Object> handleInvalidImageFormatException(InvalidImageFormatException ex) {
        return new ResponseEntity<>("Invalid image format: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
