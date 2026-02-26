package org.jamup.dao.csv;

import org.jamup.dao.interfaces.VenueDAO;
import org.jamup.model.Venue;
import org.jamup.model.enums.MusicGenre;

import java.time.LocalDate;
import java.util.List;

public class VenueDAOCSV implements VenueDAO {

    public VenueDAOCSV() {
        System.out.println("Creato VenueDAO versione CSV");
    }

    @Override
    public List<Venue> findByCriteria(String searchName, List<MusicGenre> searchGenres, String searchLocation, LocalDate searchDate) {
        /* TODO */
        return List.of();
    }

    @Override
    public Venue findById(String id) {
        /* TODO */
        return null;
    }

    @Override
    public void update(Venue updatedVenue) {
        /* TODO */
    }

}
