package org.jamup.dao.memory;

import org.jamup.dao.VenueFilter;
import org.jamup.dao.interfaces.VenueDAO;
import org.jamup.model.Venue;
import org.jamup.model.enums.MusicGenre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VenueDAOMemory implements VenueDAO {

    @Override
    public List<Venue> findByCriteria(String searchName, List<MusicGenre> searchGenres, LocalDate searchDate) {
        List<Venue> searchResults = new ArrayList<>();
        for (Venue venue : InMemoryStorage.getVenues().values()) {

            if (!VenueFilter.matches(venue, searchName, searchGenres, searchDate)){
                continue;
            }

            searchResults.add(venue);
        }

        return searchResults;
    }

    @Override
    public Venue findById(String id) {
        return InMemoryStorage.getVenues().get(id);
    }

    //update of the i-th venue following a change to its availability calendar
    @Override
    public void update(Venue updatedVenue) {
        InMemoryStorage.getVenues().put(updatedVenue.getId(), updatedVenue);
    }

    public VenueDAOMemory() {
        System.out.println("Creato VenueDAO versione DEMO");
    }

}