package com.dev.vault.projectservice.exceptions;

import com.dev.vault.shared.lib.exceptions.*;
import com.dev.vault.shared.lib.model.response.ApiResponse;
import com.dev.vault.shared.lib.model.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ProjectExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MissingAuthenticationHeaderException.class)
    public ResponseEntity<ErrorResponse> missingAuthenticationHeaderExceptionHandler(MissingAuthenticationHeaderException e) {
        log.error("❌👮‍♂️ MissingAuthenticationHeaderException triggered - Cause ❌👮‍♂️: {{}}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(e.getMessage())
                .httpStatus(e.getHttpStatus())
                .statusCode(e.getHttpStatus().value())
                .build();

        return new ResponseEntity<>(errorResponse, e.getHttpStatus());
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        log.error("❌🚫 ConstraintViolationException triggered - Cause ❌🚫: {{}}", ex.getMessage());
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
        log.error("❌🔀 MethodArgumentTypeMismatchException triggered - Cause ❌🔀: {{}}", ex.getMessage());
        Map<String, Object> map = new HashMap<>();
        map.put(ex.getName(), ex.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        log.error("❌🔍 ResourceNotFoundException triggered - Cause ❌🔍: {{}}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(e.getMessage())
                .httpStatus(e.getHttpStatus())
                .statusCode(e.getHttpStatus().value())
                .build();

        return new ResponseEntity<>(errorResponse, e.getHttpStatus());
    }


    @ExceptionHandler(DevVaultException.class)
    public ResponseEntity<ErrorResponse> devVaultExceptionHandler(DevVaultException e) {
        log.error("❌⭕ DevVaultException triggered - Cause ⭕❌: {{}}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(e.getMessage())
                .httpStatus(e.getHttpStatus())
                .statusCode(e.getHttpStatus().value())
                .build();

        return new ResponseEntity<>(errorResponse, e.getHttpStatus());
    }


    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> resourceAlreadyExistsExceptionHandler(ResourceAlreadyExistsException e) {
        log.error("❌🛑 ResourceAlreadyExistsException triggered - Cause ❌🛑: {{}}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(e.getMessage())
                .httpStatus(e.getHttpStatus())
                .statusCode(e.getHttpStatus().value())
                .build();

        return new ResponseEntity<>(errorResponse, e.getHttpStatus());
    }


    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedExceptionHandler(AuthenticationFailedException e) {
        log.error("❌🔒 AuthenticationFailedException triggered - Cause ❌🔒: {{}}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(e.getMessage())
                .httpStatus(e.getHttpStatus())
                .statusCode(e.getHttpStatus().value())
                .build();

        return new ResponseEntity<>(errorResponse, e.getHttpStatus());
    }


    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse> expiredJwtExceptionHandler(ExpiredJwtException e) {
        log.error("❌⌛ ExpiredJwtException triggered - Cause ❌⌛: {{}}", e.getMessage());
        return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.REQUEST_TIMEOUT);
    }


    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponse> signatureExceptionHandler(SignatureException e) {
        log.error("❌🔒 SignatureException triggered - Cause ❌🔒: {{}}", e.getMessage());
        return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.REQUEST_TIMEOUT);
    }


    @ExceptionHandler(NotLeaderOfProjectException.class)
    public ResponseEntity<ErrorResponse> notLeaderOfProjectExceptionHandler(NotLeaderOfProjectException e) {
        log.error("❌⛔ NotLeaderOfProjectException triggered - Cause ❌⛔: {{}}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(e.getMessage())
                .httpStatus(e.getHttpStatus())
                .statusCode(e.getHttpStatus().value())
                .build();

        return new ResponseEntity<>(errorResponse, e.getHttpStatus());
    }


    @ExceptionHandler(NotMemberOfProjectException.class)
    public ResponseEntity<ErrorResponse> notMemberOfProjectExceptionHandler(NotMemberOfProjectException e) {
        log.error("❌🚫 NotMemberOfProjectException triggered - Cause ❌🚫: {{}}", e.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(e.getMessage())
                .httpStatus(e.getHttpStatus())
                .statusCode(e.getHttpStatus().value())
                .build();

        return new ResponseEntity<>(errorResponse, e.getHttpStatus());
    }

}

