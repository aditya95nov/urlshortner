package org.exception;

public class AliasAlreadyExistsException extends RuntimeException {

    public AliasAlreadyExistsException() {
        super("Alias already exists");
    }

    public AliasAlreadyExistsException(String alias) {
        super("Alias already exists: " + alias);
    }
}