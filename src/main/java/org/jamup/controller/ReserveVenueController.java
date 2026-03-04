package org.jamup.controller;

import org.jamup.bean.ReservationBean;
import org.jamup.bean.VenueBean;
import org.jamup.dao.interfaces.ArtistDAO;
import org.jamup.dao.interfaces.ReservationDAO;
import org.jamup.dao.interfaces.VenueDAO;
import org.jamup.exception.NoResultsFoundException;
import org.jamup.factory.DAOFactory;
import org.jamup.model.Artist;
import org.jamup.model.Notification;
import org.jamup.model.Reservation;
import org.jamup.model.Venue;
import org.jamup.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ReserveVenueController {

    /**
     * Search for venues using a {@link VenueBean} containing (optionally) values: name, genres, location, availability date
     */
    public List<VenueBean> search(VenueBean searchBean) throws NoResultsFoundException {
        VenueDAO venueDAO = DAOFactory.getInstance().createVenueDAO();

        //execute query (get list of venues)
        List<Venue> results = venueDAO.findByCriteria(
                searchBean == null ? null : searchBean.getName(),
                searchBean == null ? null : searchBean.getGenres(),
                searchBean == null ? null : searchBean.getLocation(),
                searchBean == null ? null : searchBean.getSearchDate());
        List<VenueBean> output = new ArrayList<>();

        //construction of the list of search result beans to return to the view
        for (Venue venue : results) {
            VenueBean venueBean = new VenueBean(venue.getId(), venue.getName(), venue.getDescription(),
                    venue.getLocation(), venue.getGenres(),
                    venue.getCalendar().getSlots(), venue.getManagerId());
            output.add(venueBean);
        }
        if (output.isEmpty()) {
            throw new NoResultsFoundException();
        }
        return output;
    }

    /**
     * Returns all venues
     */
    public List<VenueBean> getAllVenues() throws NoResultsFoundException {
        return search(null);
    }

    /**
     * Creation of the reservation using the {@link ReservationBean} containing the venue id and the selected slot, and any added notes
     */
    public void confirmReservation(ReservationBean bean) {
        //retrieval of the id of the artist who booked
        String reservationArtist = SessionManager.getInstance().getCurrentArtistId();
        ArtistDAO artistDAO = DAOFactory.getInstance().createArtistDAO();
        //retrieval of the artist with the previously retrieved id
        Artist artist = artistDAO.findById(reservationArtist);
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

        Notification newReservationNotification = new Notification(venue.getManagerId(), message);
    }

}