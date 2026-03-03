package org.jamup.exception;

public class InvalidFieldException extends Exception {

    public InvalidFieldException(String field, String reason) {
        super("Invalid field '" + field + "': " + reason);
    }

}