package org.jamup.observer;

import org.jamup.controller.NotificationController;
import org.jamup.model.Reservation;

public class NotificationObserver implements Observer {

    private final Reservation reservation;
    private final NotificationController notificationController = new NotificationController();

    public NotificationObserver(Reservation reservation) {
        this.reservation = reservation;
    }

    /**
     * Reacts to a reservation status change by creating a notification
     * for the artist informing them of the updated reservation status.
     */
    @Override
    public void update() {
        String recipientId = reservation.getArtist().getId();
        String message = "Your reservation at " + reservation.getVenue().getName()
                + " on " + reservation.getReservedSlot().getDate()
                + " at " + reservation.getReservedSlot().getTime()
                + " has been " + reservation.getStatus().name().toLowerCase() + ".";

        notificationController.createNotification(recipientId, message);
    }

}