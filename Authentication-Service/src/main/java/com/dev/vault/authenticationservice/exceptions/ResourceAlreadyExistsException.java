package com.dev.vault.authenticationservice.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {

    private int httpStatus;

    public ResourceAlreadyExistsException(String resource, String request, String value) {
        super(String.format("The Resource %s, with %s: '%s' already exists!", resource, request, value));
    }

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

    public ResourceAlreadyExistsException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
