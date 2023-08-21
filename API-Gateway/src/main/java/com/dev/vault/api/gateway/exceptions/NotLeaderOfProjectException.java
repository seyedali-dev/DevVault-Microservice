package com.dev.vault.api.gateway.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotLeaderOfProjectException extends RuntimeException {

    private HttpStatus httpStatus;
    private int httpStatusCode;

    public NotLeaderOfProjectException(String message) {
        super(message);
    }

    public NotLeaderOfProjectException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public NotLeaderOfProjectException(String message, HttpStatus httpStatus, int httpStatusCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.httpStatusCode = httpStatusCode;
    }

}
