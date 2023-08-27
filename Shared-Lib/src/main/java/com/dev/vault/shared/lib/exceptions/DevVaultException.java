package com.dev.vault.shared.lib.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class DevVaultException extends RuntimeException {

    @JsonProperty("httpStatus")
    private HttpStatus httpStatus;

    @JsonProperty("statusCode")
    private int statusCode;
    public DevVaultException() {
    }

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
