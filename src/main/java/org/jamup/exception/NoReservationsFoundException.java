package org.jamup.exception;

public class NoReservationsFoundException extends Exception {

    public NoReservationsFoundException() {
        super("No reservations found");
    }

}