package com.ap3x.hitchhikers.controllers;

import com.ap3x.hitchhikers.models.ErrorBody;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorBody handleGenericException(Exception ex){
        return new ErrorBody(LocalDateTime.now(), ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorBody handleNotFoundException(NotFoundException ex){
        return new ErrorBody(LocalDateTime.now(), ex.getMessage());
    }
    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorBody handleEntityExistsException(EntityExistsException ex){
        return new ErrorBody(LocalDateTime.now(), ex.getMessage());
    }
}
