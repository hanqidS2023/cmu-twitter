package com.cmux.user.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import io.jsonwebtoken.JwtException;



@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private ResponseEntity<CustomErrorResponse> buildErrorResponse(String errorMessage, HttpStatus status) {
        CustomErrorResponse error = new CustomErrorResponse();
        error.setErrorMessage(errorMessage);
        error.setStatusCode(status.value());

        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        logger.error("Exception caught: {}", ex.getMessage(), ex);
        
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleUserNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        logger.error("User not found: {}", ex.getMessage());

        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<CustomErrorResponse> handleTokenExpired(TokenExpiredException ex) {
        logger.error("Token expired: {}", ex.getMessage());

        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<CustomErrorResponse> handleTokenInvalid(JwtException ex) {
        logger.error("Invalid token: {}", ex.getMessage());

        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleTokenNotProvided(AuthenticationCredentialsNotFoundException ex) {
        logger.error("Token not provided: {}", ex.getMessage());

        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        logger.error("Data integrity violation: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
