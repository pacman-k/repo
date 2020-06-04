package com.epam.lab.controller;

import com.epam.lab.exception.*;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ControllerException.class)
    public ResponseEntity<ExceptionJSONInfo> handleControllerExceptions(HttpServletRequest request, ControllerException e) {
        ExceptionJSONInfo response = new ExceptionJSONInfo();
        response.setUrl(request.getRequestURL().toString());
        response.setMessage(e.getMessage());
        response.setExceptionClass(e.getClass().getSimpleName());
        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getResponseCod()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ExceptionJSONInfo> handleDataAccessExceptions(HttpServletRequest request, Exception e) {
        ExceptionJSONInfo response = new ExceptionJSONInfo();
        response.setUrl(request.getRequestURL().toString());
        response.setMessage(e.getMessage());
        response.setExceptionClass(e.getClass().getSimpleName());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionJSONInfo> handleValidationExceptions(HttpServletRequest request, Exception e) {
        ExceptionJSONInfo response = new ExceptionJSONInfo();
        response.setUrl(request.getRequestURL().toString());
        response.setMessage(e.getMessage());
        response.setExceptionClass(e.getClass().getSimpleName());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
