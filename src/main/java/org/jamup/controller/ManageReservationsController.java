package org.jamup.controller;

import org.jamup.bean.ReservationBean;
import org.jamup.dao.interfaces.NotificationDAO;
import org.jamup.dao.interfaces.ReservationDAO;
import org.jamup.dao.interfaces.UserDAO;
import org.jamup.dao.interfaces.VenueDAO;
import org.jamup.exception.NoReservationsFoundException;
import org.jamup.factory.DAOFactory;
import org.jamup.model.Notification;
import org.jamup.model.Reservation;
import org.jamup.model.Venue;
import org.jamup.model.VenueManager;
import org.jamup.model.enums.ReservationStatus;
import org.jamup.observer.NotificationObserver;
import org.jamup.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ManageReservationsController {

    private final ReservationDAO reservationDAO = DAOFactory.getInstance().createReservationDAO();

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
     * Updates the status of a specific reservation to ACCEPTED and notifies
     * registered observers (e.g. sends a notification to the artist).
     *
     * @param reservationID the unique identifier of the reservation to accept.
     */
    public void accept(String reservationID) {
        Reservation res = reservationDAO.findById(reservationID);
        res.attach(new NotificationObserver(res));
        res.updateStatus(ReservationStatus.ACCEPTED);
        reservationDAO.update(res);
    }

    /**
     * Updates the status of a specific reservation to REJECTED, restores the
     * reserved time slot in the venue calendar, and notifies registered observers
     * (e.g. sends a notification to the artist).
     *
     * @param reservationID the unique identifier of the reservation to reject.
     */
    public void reject(String reservationID) {
        VenueDAO venueDAO = DAOFactory.getInstance().createVenueDAO();
        Reservation res = reservationDAO.findById(reservationID);
        res.attach(new NotificationObserver(res));
        res.updateStatus(ReservationStatus.REJECTED);
        reservationDAO.update(res);
        //restoring slot
        Venue venue = res.getVenue();
        venue.getCalendar().addSlot(res.getReservedSlot().getDate(), res.getReservedSlot().getTime());
        venueDAO.update(venue);
    }

}