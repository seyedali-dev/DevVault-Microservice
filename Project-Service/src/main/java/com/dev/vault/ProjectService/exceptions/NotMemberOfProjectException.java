package com.dev.vault.ProjectService.exceptions;

public class NotMemberOfProjectException extends RuntimeException {
    public NotMemberOfProjectException(String s) {
        super(s);
    }
}
