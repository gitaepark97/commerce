package com.hugo.commerce.web.controller;

import com.hugo.commerce.support.error.CoreException;
import com.hugo.commerce.support.error.ErrorType;
import com.hugo.commerce.support.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;


@Slf4j
@RestControllerAdvice
class ApiControllerAdvice {

    private record ValidationError(String field, String message) {}

    @ExceptionHandler(CoreException.class)
    ResponseEntity<ApiResponse<Object>> handleCoreException(CoreException e) {
        switch (e.getErrorType().getLogLevel()) {
            case LogLevel.ERROR -> log.error("CoreException : {}", e.getMessage(), e);
            case LogLevel.WARN -> log.warn("CoreException : {}", e.getMessage());
            default -> log.info("CoreException : {}", e.getMessage());
        }
        return errorResponse(e.getErrorType(), e.getData());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ValidationError> errors = e.getBindingResult().getFieldErrors().stream()
            .map(fe -> new ValidationError(fe.getField(), fe.getDefaultMessage()))
            .toList();
        log.warn("MethodArgumentNotValidException : {}", errors);
        return errorResponse(ErrorType.BAD_REQUEST, errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ApiResponse<Object>> handleConstraintViolationException(ConstraintViolationException e) {
        List<ValidationError> errors = e.getConstraintViolations().stream()
            .map(cv -> new ValidationError(cv.getPropertyPath().toString(), cv.getMessage()))
            .toList();
        log.warn("ConstraintViolationException : {}", errors);
        return errorResponse(ErrorType.BAD_REQUEST, errors);
    }

    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        MissingServletRequestParameterException.class,
        MethodArgumentTypeMismatchException.class
    })
    ResponseEntity<ApiResponse<Object>> handleBadRequestException(Exception e) {
        log.warn("BadRequestException [{}] : {}", e.getClass().getSimpleName(), e.getMessage());
        return errorResponse(ErrorType.BAD_REQUEST);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    ResponseEntity<ApiResponse<Object>> handleNoResourceFoundException(NoResourceFoundException e) {
        log.warn("NoResourceFoundException : {}", e.getMessage());
        return errorResponse(ErrorType.NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    ResponseEntity<ApiResponse<Object>> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("MethodNotSupportedException : {}", e.getMessage());
        return errorResponse(ErrorType.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        log.error("Exception : {}", e.getMessage(), e);
        return errorResponse(ErrorType.DEFAULT_ERROR);
    }

    private ResponseEntity<ApiResponse<Object>> errorResponse(ErrorType errorType) {
        return ResponseEntity.status(errorType.getStatus()).body(ApiResponse.error(errorType));
    }

    private ResponseEntity<ApiResponse<Object>> errorResponse(ErrorType errorType, Object data) {
        return ResponseEntity.status(errorType.getStatus()).body(ApiResponse.error(errorType, data));
    }

}
