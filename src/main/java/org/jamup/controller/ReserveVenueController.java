package org.jamup.controller;

import org.jamup.bean.ReservationBean;
import org.jamup.bean.VenueBean;
import org.jamup.dao.interfaces.ReservationDAO;
import org.jamup.dao.interfaces.UserDAO;
import org.jamup.dao.interfaces.VenueDAO;
import org.jamup.exception.NoVenuesFoundException;
import org.jamup.dao.factory.DAOFactory;
import org.jamup.model.Artist;
import org.jamup.model.Reservation;
import org.jamup.model.Venue;
import org.jamup.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ReserveVenueController {

    /**
     * Searches for venues based on the criteria provided in a {@link VenueBean}.
     *
     * @param searchBean a bean containing optional search filters such as name, genres, location, and date.
     * @return a list of {@link VenueBean} objects matching the search criteria.
     * @throws NoVenuesFoundException if no venues match the specified criteria.
     */
    public List<VenueBean> search(VenueBean searchBean) throws NoVenuesFoundException {
        VenueDAO venueDAO = DAOFactory.getInstance().createVenueDAO();

        //execute query (get list of venues)
        List<Venue> results = venueDAO.findByCriteria(
                searchBean == null ? null : searchBean.getName(),
                searchBean == null ? null : searchBean.getGenres(),
                searchBean == null ? null : searchBean.getSearchDate());
        return getVenueBeans(results);
    }

    private static List<VenueBean> getVenueBeans(List<Venue> results) throws NoVenuesFoundException {
        List<VenueBean> output = new ArrayList<>();

        //construction of the list of search result beans to return to the view
        for (Venue venue : results) {
            VenueBean venueBean = new VenueBean(venue.getId(), venue.getName(), venue.getDescription(),
                    venue.getLocation(), venue.getGenres(),
                    venue.getCalendar().getSlots());
            output.add(venueBean);
        }
        if (output.isEmpty()) {
            throw new NoVenuesFoundException();
        }
        return output;
    }

    /**
     * Confirms and persists a new reservation.
     * This method retrieves the current artist from the session, updates the venue's availability,
     * saves the reservation, and generates a notification for the venue manager.
     *
     * @param bean a {@link ReservationBean} containing the venue ID, selected time slot, and optional notes.
     */
    public void confirmReservation(ReservationBean bean) {
        //retrieval of the id of the artist who booked
        String reservationArtist = SessionManager.getInstance().getCurrentArtistId();
        UserDAO artistDAO = DAOFactory.getInstance().createUserDAO();
        //retrieval of the artist with the previously retrieved id
        Artist artist = artistDAO.findArtistById(reservationArtist);
        VenueDAO venueDAO = DAOFactory.getInstance().createVenueDAO();
        //retrieval of the venue with the id contained in the bean
        Venue venue = venueDAO.findById(bean.getVenueId());

        //for now with null id, it will be set by the DAO
        Reservation newReservation = new Reservation(bean.getNotes(), artist, venue, bean.getReservedSlot());

        ReservationDAO reservationDAO = DAOFactory.getInstance().createReservationDAO();
        reservationDAO.save(newReservation);

        //removal of the booked time slot from the venue calendar, and update in persistence
        venue.getCalendar().removeSlot(bean.getReservedSlot());
        venueDAO.update(venue);

        //creation of the notification for the venue manager
        String message = "New reservation request from " + artist.getName() +
                " at " + venue.getName() +
                " on " + bean.getReservedSlot().getDate() +
                " at " + bean.getReservedSlot().getTime();

        NotificationController notificationController = new NotificationController();
        notificationController.createNotification(venue.getManagerId(), message);
    }

}