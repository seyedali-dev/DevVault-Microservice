package com.dev.vault.shared.lib.exceptions;

public class NotMemberOfProjectException extends RuntimeException {
    public NotMemberOfProjectException(String s) {
        super(s);
    }
}
