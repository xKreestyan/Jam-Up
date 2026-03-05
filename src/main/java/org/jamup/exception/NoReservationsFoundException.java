package org.jamup.exception;

public class NoReservationsFoundException extends RuntimeException {

    public NoReservationsFoundException() {
        super("No reservations found");
    }

}