package com.dan.travel_agent.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> exceptionResponse = getExceptionResponse(status.value());
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        exceptionResponse.put("errors", errors);
        return new ResponseEntity<>(exceptionResponse, headers, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, Object> exceptionResponse = getExceptionResponse(status.value());
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        exceptionResponse.put("errors", errors);
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), status);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleServerException(RuntimeException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Map<String, Object> exceptionResponse = getExceptionResponse(status.value());
        exceptionResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), status);
    }

    private static Map<String, Object> getExceptionResponse(int errorCode) {
        Map<String, Object> exceptionRespose = new LinkedHashMap<>();
        exceptionRespose.put("timestamp", new Date());
        exceptionRespose.put("status", errorCode);
        return exceptionRespose;
    }

}
