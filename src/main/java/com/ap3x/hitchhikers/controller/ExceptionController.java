package com.ap3x.hitchhikers.controller;

import com.ap3x.hitchhikers.model.HitchhikersGuideError;
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
    public HitchhikersGuideError handleGenericException(final Exception ex){
        return new HitchhikersGuideError(LocalDateTime.now(), ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public HitchhikersGuideError handleNotFoundException(final NotFoundException ex){
        return new HitchhikersGuideError(LocalDateTime.now(), ex.getMessage());
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public HitchhikersGuideError handleEntityExistsException(final EntityExistsException ex){
        return new HitchhikersGuideError(LocalDateTime.now(), ex.getMessage());
    }
}
