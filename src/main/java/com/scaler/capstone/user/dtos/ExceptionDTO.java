package com.scaler.capstone.user.dtos;

import org.springframework.http.HttpStatus;

public class ExceptionDTO {
    private HttpStatus errorCode;
    private String message;

    public ExceptionDTO(HttpStatus errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}