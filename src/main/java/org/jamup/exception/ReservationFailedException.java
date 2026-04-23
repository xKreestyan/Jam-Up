package org.jamup.exception;

public class ReservationFailedException extends Exception {

    public ReservationFailedException(String reason) {
        super("Reservation failed: " + reason);
    }

}