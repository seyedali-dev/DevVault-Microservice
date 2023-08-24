package com.dev.vault.shared.lib.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class DevVaultException extends RuntimeException {

    private HttpStatus httpStatus;
    private int statusCode;

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
