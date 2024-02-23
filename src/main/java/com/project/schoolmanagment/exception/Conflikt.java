package com.project.schoolmanagment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class Conflikt extends RuntimeException{
    public Conflikt(String message){
        super(message);
    }
}
