package com.dev.vault.authenticationservice.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DevVaultException extends RuntimeException {

    private int httpStatus;
    private String errorCode;

    public DevVaultException(String message) {
        super(message);
    }

    public DevVaultException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public DevVaultException(String message, String errorCode, int httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

}
