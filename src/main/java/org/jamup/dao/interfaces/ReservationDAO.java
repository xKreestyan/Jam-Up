package org.jamup.dao.interfaces;

import org.jamup.model.Reservation;
import org.jamup.model.enums.ReservationStatus;

import java.util.List;

public interface ReservationDAO {

    /**
     * Persists a new reservation in the data store.
     *
     * @param reservation the reservation entity to save
     */
    void save(Reservation reservation);

    /**
     * Retrieves a reservation by its unique identifier.
     *
     * @param id the unique identifier of the reservation
     * @return the reservation if found, or null otherwise
     */
    Reservation findById(String id);

    /**
     * Finds all reservations associated with a list of venue IDs that match a specific status.
     *
     * @param venueIds a list of venue identifiers to filter by
     * @param status   the status of the reservations to retrieve; if null, returns all reservations for the specified venues
     * @return a list of matching reservations
     */
    List<Reservation> findByVenues(List<String> venueIds, ReservationStatus status);

    /**
     * Updates the details of an existing reservation following a status change.
     *
     * @param reservation the reservation entity containing updated information
     */
    void update(Reservation reservation);

}