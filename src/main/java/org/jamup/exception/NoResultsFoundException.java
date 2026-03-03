package org.jamup.exception;

public class NoResultsFoundException extends Exception {

    public NoResultsFoundException() {
        super("No venues found matching the search criteria");
    }

}