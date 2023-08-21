package com.dev.vault.api.gateway.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class MissingAuthenticationHeaderException extends RuntimeException {

    private HttpStatus httpStatus;

    public MissingAuthenticationHeaderException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
