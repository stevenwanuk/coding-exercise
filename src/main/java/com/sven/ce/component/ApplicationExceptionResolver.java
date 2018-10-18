package com.sven.ce.component;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.sven.ce.exception.ExclusionUserException;
import com.sven.ce.exception.UserNotFoundException;

/**
 * resolve exceptions to HttpErrors
 */
@ControllerAdvice
public class ApplicationExceptionResolver
{

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionResolver.class);

    @ExceptionHandler(ExclusionUserException.class)
    public final ResponseEntity<String> handleExclusionUserException(Exception ex, WebRequest request)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is blocked");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<String> handleUserNotFoundException(Exception ex, WebRequest request)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User is not found");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<List<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
        WebRequest request)
    {
        List<String> errors = ex.getBindingResult()
                        .getAllErrors()
                        .stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
        WebRequest request)
    {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<String> handleException(Exception ex, WebRequest request)
    {
        LOG.error("Exception is caught", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
