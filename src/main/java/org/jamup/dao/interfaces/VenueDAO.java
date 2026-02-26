package org.jamup.dao.interfaces;

import org.jamup.model.Venue;
import org.jamup.model.enums.MusicGenre;

import java.time.LocalDate;
import java.util.List;

public interface VenueDAO {

    List<Venue> findByCriteria(String searchName, List<MusicGenre> searchGenres, String searchLocation, LocalDate searchDate);
    Venue findById(String id);
    void update(Venue updatedVenue);

}