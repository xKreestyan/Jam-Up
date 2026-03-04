package org.jamup.dao.interfaces;

import org.jamup.model.Venue;
import org.jamup.model.enums.MusicGenre;

import java.time.LocalDate;
import java.util.List;

public interface VenueDAO {

    /**
     * Finds venues based on multiple search criteria.
     *
     * @param searchName     the name or partial name of the venue
     * @param searchGenres   a list of music genres associated with the venue
     * @param searchLocation the location or city of the venue
     * @param searchDate     a specific date for availability
     * @return a list of venues matching the criteria
     */
    List<Venue> findByCriteria(String searchName, List<MusicGenre> searchGenres, String searchLocation, LocalDate searchDate);

    /**
     * Retrieves a venue by its unique identifier.
     *
     * @param id the unique ID of the venue
     * @return the venue if found, or null otherwise
     */
    Venue findById(String id);

    /**
     * Updates the information of an existing venue following a change to its availability calendar.
     *
     * @param updatedVenue the venue object containing updated data
     */
    void update(Venue updatedVenue);

}