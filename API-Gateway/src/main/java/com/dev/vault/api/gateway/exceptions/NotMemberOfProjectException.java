package com.dev.vault.api.gateway.exceptions;

public class NotMemberOfProjectException extends RuntimeException {
    public NotMemberOfProjectException(String s) {
        super(s);
    }
}
