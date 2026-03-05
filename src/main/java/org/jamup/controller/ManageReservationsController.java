package org.jamup.controller;

import org.jamup.bean.ReservationBean;
import org.jamup.dao.interfaces.NotificationDAO;
import org.jamup.dao.interfaces.ReservationDAO;
import org.jamup.dao.interfaces.UserDAO;
import org.jamup.exception.NoReservationsFoundException;
import org.jamup.factory.DAOFactory;
import org.jamup.model.Notification;
import org.jamup.model.Reservation;
import org.jamup.model.VenueManager;
import org.jamup.model.enums.ReservationStatus;
import org.jamup.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ManageReservationsController {

    /**
     * Fetches all reservations associated with the venues managed by the current manager,
     * filtered by their status. If the status is null, all reservations for all venues
     * managed by the current manager will be retrieved.
     *
     * @param status the status of the reservations to retrieve, or null to retrieve all.
     * @return a list of ReservationBean objects containing reservation details.
     * @throws NoReservationsFoundException if no reservations match the criteria.
     */
    public List<ReservationBean> fetchReservations(ReservationStatus status) throws NoReservationsFoundException {
        ReservationDAO reservationDAO = DAOFactory.getInstance().createReservationDAO();
        UserDAO userDAO = DAOFactory.getInstance().createUserDAO();
        String managerId = SessionManager.getInstance().getCurrentManagerId();
        VenueManager manager = userDAO.findManagerById(managerId);

        List<Reservation> reservations = reservationDAO.findByVenues(manager.getVenueIds(), status);

        List<ReservationBean> reservationBeans = new ArrayList<>();
        for (Reservation reservation : reservations) {
            reservationBeans.add(
                    new ReservationBean(reservation.getId(), reservation.getVenue().getId(),
                            reservation.getVenue().getName(), reservation.getArtist().getId(),
                            reservation.getArtist().getName(), reservation.getArtist().getInstruments(),
                            reservation.getArtist().getGenres(), reservation.getNotes(),
                            reservation.getStatus(), reservation.getReservedSlot())
            );
        }
        if (reservationBeans.isEmpty()) {
            throw new NoReservationsFoundException();
        }
        return reservationBeans;
    }

    /**
     * Updates the status of a specific reservation to ACCEPTED and sends a notification to the artist.
     *
     * @param reservationID the unique identifier of the reservation to accept.
     */
    public void accept(String reservationID) {
        ReservationDAO reservationDAO = DAOFactory.getInstance().createReservationDAO();
        Reservation res = reservationDAO.findById(reservationID);
        res.updateStatus(ReservationStatus.ACCEPTED);
        reservationDAO.update(res);

        //creation of the notification for the artist
        String message = "Your reservation at " + res.getVenue().getName() +
                " on " + res.getReservedSlot().getDate() +
                " at " + res.getReservedSlot().getTime() +
                " has been accepted";

        Notification newReservationNotification = new Notification(res.getArtist().getId(), message);
        NotificationDAO notificationDAO = DAOFactory.getInstance().createNotificationDAO();
        notificationDAO.save(newReservationNotification);
    }

    /**
     * Updates the status of a specific reservation to REJECTED and sends a notification to the artist.
     *
     * @param reservationID the unique identifier of the reservation to reject.
     */
    public void reject(String reservationID) {
        ReservationDAO reservationDAO = DAOFactory.getInstance().createReservationDAO();
        Reservation res = reservationDAO.findById(reservationID);
        res.updateStatus(ReservationStatus.REJECTED);
        reservationDAO.update(res);

        //creation of the notification for the artist
        String message = "Your reservation at " + res.getVenue().getName() +
                " on " + res.getReservedSlot().getDate() +
                " at " + res.getReservedSlot().getTime() +
                " has been rejected";

        Notification newReservationNotification = new Notification(res.getArtist().getId(), message);
        NotificationDAO notificationDAO = DAOFactory.getInstance().createNotificationDAO();
        notificationDAO.save(newReservationNotification);
    }

}