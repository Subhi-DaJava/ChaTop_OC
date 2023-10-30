package com.chatop.exceptions.handle_exception;

import com.chatop.exceptions.*;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FiledNotNullOrEmptyException.class)
    public ProblemDetail handleFiledNotNullOrEmptyException(FiledNotNullOrEmptyException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setTitle("Fields cannot be null or empty");
        problemDetail.setType(URI.create("https://chatop.com/errors"));
        problemDetail.setProperty("error", "Fields cannot be null or empty");
        return problemDetail;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFoundException(UserNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("User not found with given userId");
        problemDetail.setType(URI.create("https://chatop.com/errors"));
        problemDetail.setProperty("error", "User not found with given userId");
        return problemDetail;
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ProblemDetail unauthorizedUserException(UnauthorizedUserException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        problemDetail.setTitle("Unauthorized User");
        problemDetail.setType(URI.create("https://chatop.com/errors"));
        problemDetail.setProperty("error", "Unauthorized User, user not logged in");
        return problemDetail;
    }
    @ExceptionHandler(RentalNotFondException.class)
    public ProblemDetail handleRentalNotFoundException(RentalNotFondException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Rental not found");
        problemDetail.setType(URI.create("https://chatop.com/errors"));
        problemDetail.setProperty("error", "Rental not found");
        return problemDetail;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        errorDetail.setTitle("User already registered");
        errorDetail.setType(URI.create("https://chatop.com/errors"));
        errorDetail.setProperty("error", "User already exists");
        return errorDetail;
    }

    @ExceptionHandler(InvalidImageFormatException.class)
    public ProblemDetail handleInvalidImageFormatException(InvalidImageFormatException ex) {
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage());
        errorDetail.setTitle("Invalid image format");
        errorDetail.setType(URI.create("https://chatop.com/errors"));
        errorDetail.setProperty("error", "Invalid image format");
        return errorDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception ex) {
        ProblemDetail errorDetail = null;
        if(ex instanceof BadCredentialsException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(401), ex.getMessage());
            errorDetail.setProperty("access_denied_reason", "Invalid_credentials");
        }
        if(ex instanceof AccessDeniedException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(403), ex.getMessage());
            errorDetail.setProperty("access_denied_reason", "Not_Authorized");
        }
        if(ex instanceof SignatureException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(403), ex.getMessage());
            errorDetail.setProperty("access_denied_reason", "Not Valid Token");
        }
        if(ex instanceof ExpiredJwtException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(403), ex.getMessage());
            errorDetail.setProperty("access_denied_reason", "Expired Token");
        }
        if(ex instanceof MalformedJwtException){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(401), ex.getMessage());
            errorDetail.setProperty("access_denied_reason", "Malformed Token");
        }
        return errorDetail;
    }

}
