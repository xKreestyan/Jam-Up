package org.jamup.exception;

public class NoVenuesFoundException extends Exception {

    public NoVenuesFoundException() {
        super("No venues found matching the search criteria");
    }

}