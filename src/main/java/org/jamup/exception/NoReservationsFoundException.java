package org.jamup.exception;

public class NoReservationsFoundException extends RuntimeException {

    public NoReservationsFoundException(String message) {
        super("No reservations found");
    }

}