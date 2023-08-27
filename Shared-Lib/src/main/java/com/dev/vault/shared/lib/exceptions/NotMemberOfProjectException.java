package com.dev.vault.shared.lib.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class NotMemberOfProjectException extends RuntimeException {

    private HttpStatus httpStatus;
    private int status;

    public NotMemberOfProjectException() {
    }

    public NotMemberOfProjectException(String message) {
        super(message);
    }

    public NotMemberOfProjectException(String message, HttpStatus httpStatus, int status) {
        super(message);
        this.httpStatus = httpStatus;
        this.status = status;
    }

    public NotMemberOfProjectException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
