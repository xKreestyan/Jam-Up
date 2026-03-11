package org.jamup.dao.memory;

import org.jamup.dao.interfaces.VenueDAO;
import org.jamup.model.Venue;
import org.jamup.model.enums.MusicGenre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class VenueDAOMemory implements VenueDAO {

    @Override
    public List<Venue> findByCriteria(String searchName, List<MusicGenre> searchGenres, LocalDate searchDate) {
        List<Venue> searchResults = new ArrayList<>();
        for (Venue venue : InMemoryStorage.getVenues()) {

            //if the entered name is NOT null and NOT empty and is NOT contained in the actual venue name, next iteration
            if (searchName != null && !searchName.isEmpty() &&
                    !venue.getName().toLowerCase().contains(searchName.toLowerCase())) {
                continue;
            }
            //if music genres have been specified, and NONE of them matches the venue, next iteration
            if (searchGenres != null && !searchGenres.isEmpty()) {
                if (!new HashSet<>(venue.getGenres()).containsAll(searchGenres)) continue;
            }
            //if a date has been specified and the venue is NOT available on that date, next iteration
            if (searchDate != null && venue.getCalendar().availableTimesForDate(searchDate).isEmpty()) {
                continue;
            }

            searchResults.add(venue);
        }

        return searchResults;
    }

    @Override
    public Venue findById(String id) {
        for (Venue venue : InMemoryStorage.getVenues()) {
            if (venue.getId().equals(id)) {
                return venue;
            }
        }
        return null;
    }

    //update of the i-th venue following a change to its availability calendar
    @Override
    public void update(Venue updatedVenue) {
        List<Venue> venues = InMemoryStorage.getVenues();
        for (int i = 0; i < venues.size(); i++) {
            if (venues.get(i).getId().equals(updatedVenue.getId())) {
                venues.set(i, updatedVenue);
                return;
            }
        }
    }

    public VenueDAOMemory() {
        System.out.println("Creato VenueDAO versione DEMO");
    }

}