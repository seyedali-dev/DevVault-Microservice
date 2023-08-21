package com.dev.vault.api.gateway.exceptions.handler;

import com.dev.vault.shared.lib.exceptions.DevVaultException;
import com.dev.vault.shared.lib.exceptions.response.ErrorResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GatewayExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DevVaultException.class)
    public ResponseEntity<ErrorResponse> devVaultExceptionHandler(DevVaultException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), e.getHttpStatus(), e.getStatusCode()),
                HttpStatusCode.valueOf(e.getStatusCode())
        );
    }

}
