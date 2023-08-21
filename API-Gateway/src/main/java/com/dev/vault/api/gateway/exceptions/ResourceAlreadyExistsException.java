package com.dev.vault.api.gateway.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceAlreadyExistsException extends RuntimeException {

    private HttpStatus httpStatus;
    private int statusCode;

    public ResourceAlreadyExistsException(String resource, String request, String value) {
        super(String.format("The Resource %s, with %s: '%s' already exists!", resource, request, value));
    }

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    public ResourceAlreadyExistsException(String message, HttpStatus httpStatus, int statusCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.statusCode = statusCode;
    }

    public ResourceAlreadyExistsException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

}
