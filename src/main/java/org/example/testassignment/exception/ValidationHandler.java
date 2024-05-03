package org.example.testassignment.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ValidationHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserWithThisAgeCanNotRegister.class)
    protected ResponseEntity<AwesomeException> userWithThisAgeCanNotRegister() {
        return new ResponseEntity<>(new AwesomeException("Only users older 18 can register"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ThereIsNoSuchUserException.class)
    protected ResponseEntity<AwesomeException> handleThereIsNoSuchUserException() {
        return new ResponseEntity<>(new AwesomeException("There is no user with this id"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BirthdayRangeValidationException.class)
    protected ResponseEntity<AwesomeException> handleTBirthdayRangeValidationException() {
        return new ResponseEntity<>(new AwesomeException("From date must be before toDate"), HttpStatus.BAD_REQUEST);
    }

    @Data
    @AllArgsConstructor
    private static class AwesomeException {
        private String message;
    }
}