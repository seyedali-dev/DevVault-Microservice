package com.dev.vault.ProjectService.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class DevVaultException extends RuntimeException {

    private int statusCode;
    private HttpStatus httpStatus;

    public DevVaultException(String message) {
        super(message);
    }

    public DevVaultException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public DevVaultException(String message, HttpStatus httpStatus, int statusCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.statusCode = statusCode;
    }

    public DevVaultException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
