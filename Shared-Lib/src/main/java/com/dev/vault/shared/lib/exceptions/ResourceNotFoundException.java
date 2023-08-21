package com.dev.vault.shared.lib.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceNotFoundException extends RuntimeException {

    private HttpStatus httpStatus;
    private int statusCode;

    public ResourceNotFoundException(String resource, String name, String value) {
        super(String.format("%s not found with %s: %s", resource, name, value));
    }

    public ResourceNotFoundException(String message, HttpStatus httpStatus, int statusCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.statusCode = statusCode;
    }

    public ResourceNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}