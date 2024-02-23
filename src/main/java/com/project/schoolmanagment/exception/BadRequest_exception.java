package com.project.schoolmanagment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequest_exception extends RuntimeException {
    public BadRequest_exception(String message){super(message);}
}
