package com.dev.vault.authenticationservice.exceptions;

public class AuthenticationFailedException extends RuntimeException{

    private int httpStatus;

    public AuthenticationFailedException(String message) {
        super(message);
    }

    public AuthenticationFailedException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
