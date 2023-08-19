package com.dev.vault.authenticationservice.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    private int httpStatus;

    public ResourceNotFoundException(String resource, String name, String value) {
        super(String.format("%s not found with %s: %s", resource, name, value));
    }

    public ResourceNotFoundException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}