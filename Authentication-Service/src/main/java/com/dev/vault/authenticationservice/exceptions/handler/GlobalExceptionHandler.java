package com.dev.vault.authenticationservice.exceptions.handler;

import com.dev.vault.authenticationservice.exceptions.AuthenticationFailedException;
import com.dev.vault.authenticationservice.exceptions.DevVaultException;
import com.dev.vault.authenticationservice.exceptions.ResourceAlreadyExistsException;
import com.dev.vault.authenticationservice.exceptions.ResourceNotFoundException;
import com.dev.vault.authenticationservice.exceptions.intercommunication.ErrorResponse;
import com.dev.vault.authenticationservice.model.response.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException mex) {
        Map<String, String> map = new HashMap<>();
        mex.getBindingResult().getAllErrors().forEach(objectError -> {
            String defaultMessage = objectError.getDefaultMessage();
            String field = ((FieldError) objectError).getField();
            map.put(field, defaultMessage);
        });
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        Map<String, Object> map = new HashMap<>();
        ex.getConstraintViolations().forEach(constraintViolation -> {
            String message = constraintViolation.getMessage();
            Object invalidValue = constraintViolation.getInvalidValue();
            map.put(message, invalidValue.toString());
        });
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> map = new HashMap<>();
        map.put(ex.getName(), ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(DevVaultException.class)
    public ResponseEntity<ErrorResponse> devVaultExceptionHandler(DevVaultException e) {
        return new ResponseEntity<>(
                new ErrorResponse(e.getMessage(), e.getErrorCode()),
                HttpStatusCode.valueOf(e.getHttpStatus())
        );
    }


    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> resourceAlreadyExistsExceptionHandler(ResourceAlreadyExistsException e) {
        return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ApiResponse> authenticationFailedExceptionHandler(AuthenticationFailedException e) {
        return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse> expiredJwtExceptionHandler(ExpiredJwtException e) {
        return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponse> signatureExceptionHandler(SignatureException e) {
        return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.REQUEST_TIMEOUT);
    }

}
